/*
* server.c
*
*  Created on: 2017年8月21日
*      Author: root
*/
//#include <sys/resource.h>
//#define CURL_STATICLIB

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <ctype.h>
#include <assert.h>
#include <sysexits.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <sys/resource.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <errno.h>
#include <signal.h>
#include <fcntl.h>
#include <time.h>
#include <mysql/mysql.h>
#include <hiredis/hiredis.h>

#ifdef HAVE_GETOPT_LONG
#include <getopt.h>
#endif

#include "logger.h"
#include "server.h"

#define MAX_SOCKET_SENDBUF_SIZE   (256 * 1024 * 1024)

#define WRITE_BUFFER_SIZE      2048
#define UNIX_READ_BUFFER_SIZE  2048
#define TCP_READ_BUFFER_SIZE   2048
#define UDP_READ_BUFFER_SIZE   65536
#define MAX_READ_BUFFER_SIZE   65536

#define IS_TCP(x) (x == tcp_transport)
#define IS_UDP(x) (x == udp_transport)

enum try_read_result {
	READ_DATA_RECEIVED,
	READ_NO_DATA_RECEIVED,
	READ_ERROR,            /** an error occurred (on the socket) (or client closed connection) */
	READ_MEMORY_ERROR      /** failed to allocate more memory */
};

struct conn_item {
	int transport;
	int read_buffer_size;
	int event_flag;
	int fd;
};

struct settings {
	int maxconns;
	int port;
	int udpport;
	int java_sd;
	struct sockaddr addr;
	int java_udpport;
	char *socketpath;   /* path to unix socket if using local socket */
} settings;

conn **conns;
static int max_fds;

static struct thread_handler work_threads[5];
static pthread_cond_t init_cond;
static pthread_mutex_t init_lock;
static int init_count;


static void work_thread_event_handler(const int fd, const short which, void *arg);

//extern void analy_message(struct message_inform *inform, char *buf,  int len);
/******************************************************************************
Function      : sig_handler
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void sig_handler(int sig) {
	fprintf(stderr, "Signal handled:%d\n", sig);
	exit(EXIT_SUCCESS);
}

/******************************************************************************
Function      : ndelay_on
Description   : Turn on nonblocking I/O on a fd
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static __inline__ int ndelay_on(int fd)
{
	return evutil_make_socket_nonblocking(fd);
}

/******************************************************************************
Function      : close_on_exec_on
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static __inline__ void close_on_exec_on(int fd)
{
	evutil_make_socket_closeonexec(fd);
}

/******************************************************************************
Function      : maximize_sndbuf
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void maximize_sndbuf(int sd)
{
	socklen_t intsize = sizeof(int);
	int last_good = 0;
	int min, max, avg;
	int old_size;

	/* Start with the default size. */
	if (getsockopt(sd, SOL_SOCKET, SO_SNDBUF, &old_size, &intsize) != 0) {
		return;
	}

	/* Binary-search for the real maximum. */
	min = old_size;
	max = MAX_SOCKET_SENDBUF_SIZE;

	while (min <= max) {
		avg = ((unsigned int)(min + max)) / 2;
		if (setsockopt(sd, SOL_SOCKET, SO_SNDBUF, (void *)&avg, intsize) == 0) {
			last_good = avg;
			min = avg + 1;
		}		else {
			max = avg - 1;
		}
	}

	PLAT_TRACE("<%d send buffer was %d, now %d\n", sd, old_size, last_good);
}

/******************************************************************************
Function      : transport_str
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static const char *transport_str(int transport)
{
	const char *ptr;

	switch (transport) {
	case local_transport:
		ptr = "unix";
		break;
	case tcp_transport:
		ptr = "tcp";
		break;
	case udp_transport:
		ptr = "udp";
		break;
	default:
		ptr = "";
		break;
	}

	return ptr;
}

/******************************************************************************
Function      : conn_init
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void conn_init(void) {
	/* We're unlikely to see an FD much higher than MAX_SETTING_CONNS. */
	int next_fd = dup(1);
	int headroom = 10;      /* account for extra unexpected open FDs */
	struct rlimit rl;

	max_fds = settings.maxconns + headroom + next_fd;

	/* But if possible, get the actual highest FD we can possibly ever see. */
	if (getrlimit(RLIMIT_NOFILE, &rl) == 0) {
		max_fds = rl.rlim_max;
	}	else {
		PLAT_ERROR("Failed to query maximum file descriptor; falling back to maxconns\n");
	}

	close(next_fd);

	if ((conns = calloc(max_fds, sizeof(conn *))) == NULL) {
		PLAT_ERROR("Failed to allocate connection structures\n");
		/* This is unrecoverable so bail out early. */
		exit(1);
	}
}

/******************************************************************************
Function      : conn_free
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void conn_free(conn *c)
{
	if (c) {
		assert(c->sd >= 0 && c->sd < max_fds);
		conns[c->sd] = NULL;
		if (c->rbuf) {
			free(c->rbuf);
		}
		if (c->wbuf) {
			free(c->wbuf);
		}
		free(c);
	}
}

/******************************************************************************
Function      : conn_new
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static conn *conn_new(const int sd, const int event_flags,
	const int read_buffer_size,
	int transport,
struct event_base *base)
{
	conn *c;

	if (sd < 0 || sd >= max_fds) {
		return NULL;
	}

	c = conns[sd];

	if (NULL == c) {
		if (!(c = (conn *)calloc(1, sizeof(conn)))) {
			PLAT_ERROR("Failed to allocate connection object\n");
			return NULL;
		}
		c->sd = sd;
		c->rsize = read_buffer_size;
		c->wsize = WRITE_BUFFER_SIZE;

		c->rbuf = (char *)malloc((size_t)c->rsize);
		c->wbuf = (char *)malloc((size_t)c->wsize);

		if (c->rbuf == 0 || c->wbuf == 0) {
			conn_free(c);
			PLAT_ERROR("Failed to allocate buffers for connection\n");
			return NULL;
		}

		conns[sd] = c;
	}

	c->transport = transport;

	c->request_addr_size = sizeof(c->request_addr);

	if (IS_TCP(transport)) {
		if (getpeername(sd, (struct sockaddr *) &c->request_addr, &c->request_addr_size)) {
			PLAT_ERROR("getpeername: %s\n", strerror(errno));
			memset(&c->request_addr, 0, sizeof(c->request_addr));
		}
	}	else {
		memset(&c->request_addr, 0, sizeof(c->request_addr));
	}

	if (IS_UDP(transport)) {
		PLAT_TRACE("<%d server listening (UDP)\n", sd);
	}	else {
		PLAT_TRACE("<%d new client connection\n", sd);
	}

	c->rbytes = c->wbytes = 0;
	c->wcurr = c->wbuf;
	c->rcurr = c->rbuf;

	event_set(&c->event, sd, event_flags, work_thread_event_handler, (void *)c);
	event_base_set(base, &c->event);

	if (event_add(&c->event, 0) == -1) {
		PLAT_ERROR("event_add fail\n");
		return NULL;
	}

	return c;
}

/******************************************************************************
Function      : conn_close
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void conn_close(conn *c)
{
	assert(c != NULL);

	/* delete the event, the socket and the conn */
	event_del(&c->event);

	PLAT_TRACE("<%d connection closed.\n", c->sd);

	close(c->sd);

	return;
}

/******************************************************************************
Function      : server_init
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void server_init(void)
{
	conn_init();
}

/******************************************************************************
Function      : work_thread_run_init
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail
Others        :
******************************************************************************/
static void work_thread_run_init(void *arg)
{
	(void)arg;

	pthread_mutex_lock(&init_lock);
	init_count++;
	pthread_cond_signal(&init_cond);
	pthread_mutex_unlock(&init_lock);
}

/******************************************************************************
Function      : setup_work_thread
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail
Others        :
******************************************************************************/
static int setup_work_thread(void)
{
	int i;

	pthread_mutex_init(&init_lock, NULL);
	pthread_cond_init(&init_cond, NULL);
	init_count = 0;

	for (i = 0; i < sizeof(work_threads) / sizeof(work_threads[0]); i++) {
		if (thread_create(&work_threads[i], 1, work_thread_run_init, NULL) == NULL) {
			PLAT_ERROR("Can't create work thread\n");
			return -1;
		}
	}

	pthread_mutex_lock(&init_lock);
	while (init_count < sizeof(work_threads) / sizeof(work_threads[0])) {
		pthread_cond_wait(&init_cond, &init_lock);
	}
	pthread_mutex_unlock(&init_lock);

	return 0;
}

/******************************************************************************
Function      : work_thread_do_new_conn
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail
Others        :
******************************************************************************/
static int work_thread_do_new_conn(void *src_thread, void *data, size_t len)
{
	struct conn_item *item = (struct conn_item *)data;
	struct thread_handler *pthread = thread_get_self();
	conn *conn;
	(void)src_thread;
	(void)len;
	conn = conn_new(item->fd, item->event_flag,
		item->read_buffer_size, item->transport, (struct event_base *)pthread->loop);
	if (conn == NULL) {
		PLAT_ERROR("Failed to create new conn\n");
		close(item->fd);
		return -1;
	}
	return 0;
}

/******************************************************************************
Function      : dispatch_conn_new
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void dispatch_conn_new(int fd, int event_flags, int read_buffer_size, int transport)
{
	static int last_thread = -1;
	struct conn_item item;
	int tid = (last_thread + 1) % (sizeof(work_threads) / sizeof(work_threads[0]));
	struct thread_handler *dst_thread = &work_threads[tid];

	last_thread = tid;

	memset(&item, 0, sizeof(item));

	item.transport = transport;
	item.read_buffer_size = read_buffer_size;
	item.event_flag = event_flags;
	item.fd = fd;
	if (thread_msg_send(NULL, &item, sizeof(item), dst_thread, work_thread_do_new_conn) != 0) {
		PLAT_ERROR("thread message send fail\n");
		close(fd);
	}
}

/******************************************************************************
Function      : unix_server_process
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void unix_server_process(evutil_socket_t sd, short events, void *arg)
{
	struct event *ev = (struct event *)arg;
	struct sockaddr_in sa;
	socklen_t salen = sizeof(sa);
	int fd;

	printf("i'm com5");
	(void)ev;

	if (events & EV_READ) {
		fd = accept(sd, (struct sockaddr *)&sa, &salen);
		if (fd >= 0) {
			ndelay_on(fd);
			dispatch_conn_new(fd, EV_READ | EV_PERSIST, UNIX_READ_BUFFER_SIZE, local_transport);
		}
	}
}

/******************************************************************************
Function      : tcp_server_process
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void tcp_server_process(evutil_socket_t sd, short events, void *arg)
{
	struct event *ev = (struct event *)arg;
	struct sockaddr_in sa;
	socklen_t salen = sizeof(sa);
	int fd;

	(void)ev;

	printf("i'm com3");
	if (events & EV_READ) {
		fd = accept(sd, (struct sockaddr *)&sa, &salen);
		if (fd >= 0) {
			ndelay_on(fd);
			dispatch_conn_new(fd, EV_READ | EV_PERSIST, TCP_READ_BUFFER_SIZE, tcp_transport);
		}
	}
}

/******************************************************************************
Function      : try_read_network
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static int try_read_network(conn *c)
{
	int res = READ_NO_DATA_RECEIVED;
	char *new_rbuf;
	int avail, rbytes;

	if (c->rcurr != c->rbuf) {
		if (c->rbytes != 0) {
			memmove(c->rbuf, c->rcurr, c->rbytes);
		}
		c->rcurr = c->rbuf;
	}

again:
	if (c->rbytes >= c->rsize) {
		if (c->rsize < MAX_READ_BUFFER_SIZE / 2) {
			new_rbuf = realloc(c->rbuf, c->rsize * 2);
			if (new_rbuf) {
				c->rcurr = c->rbuf = new_rbuf;
				c->rsize *= 2;
			}			else {
				c->rbytes = 0;
			}
		}		else {
			//too long data received, reset it
			c->rbytes = 0;
		}
	}

	avail = c->rsize - c->rbytes;
	do {
		rbytes = read(c->sd, c->rbuf + c->rbytes, avail);
	} while (rbytes < 0 && errno == EINTR);

	if (rbytes > 0) {
		res = READ_DATA_RECEIVED;
		c->rbytes += rbytes;
		if (rbytes == avail) {
			goto again;
		}
	}	else if (rbytes == 0) {
		res = READ_ERROR;
	}	else {
		if (errno != EAGAIN && errno != EWOULDBLOCK) {
			res = READ_ERROR;
		}
	}

	return res;
}

/******************************************************************************
Function      : try_read_udp
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static int try_read_udp(conn *c)
{
	int res;

	c->request_addr_size = sizeof(c->request_addr);
	res = recvfrom(c->sd, c->rbuf, c->rsize, 0,
		(struct sockaddr *)&c->request_addr, &c->request_addr_size);
	if (res > 0) {
		c->rbytes = res;
		c->rcurr = c->rbuf;
		settings.addr = *((struct sockaddr *)&c->request_addr);
		return READ_DATA_RECEIVED;
	}
	return READ_NO_DATA_RECEIVED;
}

/******************************************************************************
Function      : try_parse_data
Description   : 数据解析
Input         :
Output        :
Return        :
Others        :
******************************************************************************/

#include "analy.h"
static int try_parse_data(conn *c)
{
	struct thread_handler *pthread = thread_get_self();
	c->rbuf[c->rbytes] = '\0';

	//printf("%s[%u,%d]:  receive %d,content: %x\n", transport_str(c->transport), (unsigned int)pthread->thread_id, c->sd, c->rbytes, c->rbuf);
	

	int i;

	u16_t tp_len = 0;

	u8_t rx_buf[1024];

	u8_t ch;

	struct message_inform inform;

	inform.sd = c->sd;
	inform.addr = *((struct sockaddr_in *)(&c->request_addr));
	inet_ntop(AF_INET, &((struct sockaddr_in *) &c->request_addr)->sin_addr.s_addr, inform.ip, sizeof(inform.ip));
	inform.ip[sizeof(inform.ip) - 1] = '\0';
	inform.port = inform.addr.sin_port;
	//printf("port:%s\n", inform.ip);
	//printf("port:%d\n", inform.port);
	//printf("ntohs-port:%d\n", ntohs(inform.port));
	//printf("htons-port:%d\n", htons(inform.port));
	for (i = 0; i < c->rbytes; i++) {

		//缓冲满则清空
		if (tp_len >= sizeof(rx_buf)) {
			tp_len = 0;
		}

		ch = c->rbuf[i];

		if (ch == 0x7E) {
			if (tp_len >= 13) {
				rx_buf[tp_len++] = ch;
				//处理数据
				analy_message(&inform, rx_buf, tp_len);
			}
			tp_len = 0;
			rx_buf[tp_len++] = ch;
		}
		else {
			if (tp_len > 0) {
				rx_buf[tp_len++] = ch;
			}
		}
	}

	c->rbytes = 0;
	//sendto( c->sd, "OK\n", 2, 0, (struct sockaddr *)&c->request_addr, sizeof(c->request_addr));
	
	return 0;
}


/******************************************************************************
Function      : work_thread_event_handler
Description   : UNIX/TCP/UDP 数据接收处理
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void work_thread_event_handler(const int fd, short events, void *arg)
{
	conn *c = (conn *)arg;
	int res;

	assert(c != NULL);

	/* sanity */
	if (fd != c->sd) {
		PLAT_ERROR("Catastrophic: event fd doesn't match conn fd!\n");
		conn_close(c);
		return;
	}

	if (!(events & EV_READ)) {
		return;
	}

	if (IS_UDP(c->transport)) {
		res = try_read_udp(c);
	}	else {
		res = try_read_network(c);
	}

	switch (res) {
	case READ_NO_DATA_RECEIVED:
		break;
	case READ_DATA_RECEIVED:
		try_parse_data(c);
		break;
	case READ_ERROR:
		printf("socket close\n");
		conn_close(c);
		break;
	case READ_MEMORY_ERROR:
		break;
	default:
		break;
	}
}

/******************************************************************************
Function      : java_message_process
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static void java_message_process(int sd, char *buf, int len)
{
	struct sockaddr_in addr;
	u8_t ptmp[1500];
	//站点编号
	u32_t siteNo = ((buf[6] << 24) & 0xFF000000) | ((buf[5] << 16) & 0x00FF0000) | ((buf[4] << 8) & 0x0000FF00) | buf[3] & 0x000000FF;
	//设备编号
	u8_t devNo = buf[7];
	//1、命令包还原
	if (buf[0] == 0x7E) {
		len = HDP_ApaApcEscape(ptmp, OMCPA_PACKAGE_MAX_LEN, buf, len);
	}

	redisContext *rconn = redisConnect("127.0.0.1", 6379); //redis server默认端口
	if (rconn->err) {
		printf("connection to redisServer error: %s\n", rconn->errstr);
		redisFree(rconn);
	}
	u8_t ip[16];
	u16_t port;
	
	u8_t efmId[32];
	sprintf(efmId,"%08x%02x", siteNo, devNo);
	u8_t getIpStr[256];
	u8_t getPortStr[256]; 
	sprintf(getIpStr, "get ip_%s", efmId);
	sprintf(getPortStr, "get port_%s", efmId);
	printf("redis:%s",getIpStr);
	redisReply *reply = redisCommand(rconn, getIpStr);
	//key ip 不存在
	if (reply->type == REDIS_REPLY_NIL)
	{
		printf("no key ip\n");
		freeReplyObject(reply);
		redisFree(rconn);
		MYSQL mysql;
		MYSQL_RES *res;
		MYSQL_ROW row;
		u8_t query[256];
		u32_t flag, t;
		init_mysql(&mysql);
		sprintf(query, "select * from hixent_arc_efm_device where site_code = '%08x' and device_code = '%02x'", siteNo, devNo);
		flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
		if (flag) {
			printf("Query failed!\n");
			return 0;
		}
		else {
			printf("[%s] Query succeed\n", query);
		}
		res = mysql_store_result(&mysql);
		while (row = mysql_fetch_row(res)) 
		{
			sprintf(ip, "%s", row[3]);
			printf("row[4]:%s", row[4]);
			port = atoi(row[4]);
		}
		mysql_close(&mysql);
	}
	else if (reply->type != REDIS_REPLY_STRING)
	{
		printf("Failed to execute command[%s]\n", getIpStr);
		freeReplyObject(reply);
		redisFree(rconn);
		return;
	}
	//ip存在且为字符串
	else {
		printf("has key ip\n");
		sprintf(ip, "%s", reply->str);
		printf("ip:%s\n", ip);
		freeReplyObject(reply);
		printf("Succeed to execute command[%s]\n", getIpStr);
		redisReply *reply2 = redisCommand(rconn, getPortStr);
		//key port不存在
		if (reply2->type == REDIS_REPLY_NIL)
		{
			printf("no key port\n");
			freeReplyObject(reply2);
			redisFree(rconn);
			MYSQL mysql;
			MYSQL_RES *res;
			MYSQL_ROW row;
			u8_t query[256];
			u32_t flag, t;
			init_mysql(&mysql);

			sprintf(query, "select * from hixent_arc_efm_device where site_code = '%08x' and device_code = '%02x'", siteNo, devNo);

			flag = mysql_real_query(&mysql, query, (u32_t)strlen(query));
			if (flag) {
				printf("Query failed!\n");
				return 0;
			}
			else {
				printf("[%s] Query succeed\n", query);
			}
			res = mysql_store_result(&mysql);
			while (row = mysql_fetch_row(res)) {
				sprintf(ip, "%s", row[3]);
				printf("row[4]:%s", row[4]);
				port = atoi(row[4]);
			}
			mysql_close(&mysql);
		}
		else if (reply2->type != REDIS_REPLY_STRING)
		{
			printf("Failed to execute command[%s]\n", getPortStr);
			freeReplyObject(reply2);
			redisFree(rconn);
			return;
		}
		else 
		{
			printf("has key port\n");
			port = atoi(reply2->str);
			freeReplyObject(reply2);
			printf("Succeed to execute command[%s]\n", getPortStr);
		}
	}
	
	addr.sin_family = AF_INET;
	addr.sin_port = port;
	addr.sin_addr.s_addr = inet_addr(ip);
	redisFree(rconn);

	sendto(sd, buf, len, 0, (struct sockaddr *)&addr, sizeof(struct sockaddr));
	//sendto(sd, buf, len, 0, (struct sockaddr *)&settings.addr, sizeof(settings.addr));
	printf("addr1.sin_family:%d\n", addr.sin_family);
	printf("addr1.端口:%d\n", addr.sin_port);
	printf("addr1.地址:%d\n", addr.sin_addr.s_addr);
	
}

/******************************************************************************
Function      : create_unix_server
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static void java_udp_recv(evutil_socket_t sd, short events, void *arg)
{
    int tx_sd = (int)arg;
	struct sockaddr addr;
    char buf[1500];
    int res;
    socklen_t len = sizeof(addr);

	if (!(events & EV_READ)) {
		return;
	}

	len = recvfrom(sd, buf, sizeof(buf), 0, &addr, &len);
	if (len > 0) {
        java_message_process(tx_sd, buf, len);
	}
}

/******************************************************************************
Function      : create_unix_server
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
int create_unix_server(struct event_base *base, const char *path)
{
	static struct event ev;
	struct linger ling = { 0, 0 };
	struct sockaddr_un addr;
	int sd;
	int flags = 1;

	if (!path || *path == '\0') {
		return -1;
	}

	sd = socket(AF_UNIX, SOCK_STREAM, 0);
	if (sd < 0) {
		PLAT_ERROR("socket create fail\n");
		return -1;
	}

	setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, (void *)&flags, sizeof(flags));
	setsockopt(sd, SOL_SOCKET, SO_KEEPALIVE, (void *)&flags, sizeof(flags));
	setsockopt(sd, SOL_SOCKET, SO_LINGER, (void *)&ling, sizeof(ling));

	memset(&addr, 0, sizeof(addr));
	addr.sun_family = AF_UNIX;
	printf("path:%s",*path);
	strncpy(addr.sun_path, path, sizeof(addr.sun_path) - 1);
	unlink(addr.sun_path);
	if (bind(sd, (struct sockaddr *)&addr, sizeof(addr)) == -1) {
		close(sd);
		PLAT_ERROR("socket bind fail\n");
		return -1;
	}

	if (listen(sd, 1024) == -1) {
		close(sd);
		PLAT_ERROR("socket listen fail\n");
		return -1;
	}

	ndelay_on(sd);
	close_on_exec_on(sd);

	/*assign event*/
	event_assign(&ev, base, sd, EV_READ | EV_PERSIST, unix_server_process, &ev);
	if (event_add(&ev, NULL) < 0) {
		close(sd);
		PLAT_ERROR("sever event add fail\n");
		return -1;
	}

	return 0;
}

/******************************************************************************
Function      : create_tcp_server
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
int create_tcp_server(struct event_base *base, int port)
{
	static struct event ev;
	struct linger ling = { 0, 0 };
	struct sockaddr_in addr;
	int sd;
	int flags = 1;

	sd = socket(AF_INET, SOCK_STREAM, 0);
	if (sd < 0) {
		PLAT_ERROR("socket create fail\n");
		return -1;
	}
	setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, (void *)&flags, sizeof(flags));
	setsockopt(sd, SOL_SOCKET, SO_KEEPALIVE, (void *)&flags, sizeof(flags));
	setsockopt(sd, SOL_SOCKET, SO_LINGER, (void *)&ling, sizeof(ling));
	setsockopt(sd, IPPROTO_TCP, TCP_NODELAY, (void *)&flags, sizeof(flags));

	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr.s_addr = inet_addr("0.0.0.0");
	if (bind(sd, (struct sockaddr *)&addr, sizeof(struct sockaddr_in)) < 0) {
		close(sd);
		PLAT_ERROR("socket bind fail\n");
		return -1;
	}

	if (listen(sd, 1024) < 0) {
		close(sd);
		PLAT_ERROR("socket listen fail\n");
		return -1;
	}

	ndelay_on(sd);
	close_on_exec_on(sd);

	/*assign event*/
	event_assign(&ev, base, sd, EV_READ | EV_PERSIST, tcp_server_process, &ev);
	if (event_add(&ev, NULL) < 0) {
		close(sd);
		PLAT_ERROR("sever event add fail\n");
		return -1;
	}

	return 0;
}

/******************************************************************************
Function      : create_udp_server
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
int create_udp_server(struct event_base *base, int port)
{
	struct sockaddr_in addr;
	int c;
	int sd, thread_fd;
	int flags = 1;

	if (port == 0) {
		return -1;
	}

	sd = socket(AF_INET, SOCK_DGRAM, 0);
	if (sd < 0) {
		PLAT_ERROR("socket create fail\n");
		return -1;
	}
	setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &flags, sizeof(flags));
	maximize_sndbuf(sd);

	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr.s_addr = inet_addr("0.0.0.0");
	if (bind(sd, (struct sockaddr *)&addr, sizeof(struct sockaddr_in)) < 0) {
		close(sd);
		PLAT_ERROR("socket bind fail\n");
		return -1;
	}

	ndelay_on(sd);
	close_on_exec_on(sd);

	printf("i'm com%d", port);
	/*dispatch*/
	for (c = 1; c < sizeof(work_threads) / sizeof(work_threads[0]); c++) {
		/* Allocate one UDP file descriptor per work thread;
		* this allows "stats conns" to separately list multiple
		* parallel UDP requests in progress.
		* The dispatch code round-robins new connection requests
		* among threads, so this is guaranteed to assign one
		* FD to each thread.
		*/
		thread_fd = dup(sd);
		if (thread_fd >= 0) {
			ndelay_on(thread_fd);
			dispatch_conn_new(thread_fd, EV_READ | EV_PERSIST, UDP_READ_BUFFER_SIZE, udp_transport);
		}
	}
	dispatch_conn_new(sd, EV_READ | EV_PERSIST, UDP_READ_BUFFER_SIZE, udp_transport);

	sd = dup(sd);
	if (sd < 0) {
	    sd = -1;
	}

	return sd;
}

/******************************************************************************
Function      : create_java_udp_server
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
int create_java_udp_server(struct event_base *base, int tx_sd, int port)
{

	static struct event event;
	struct sockaddr_in addr;
	int c;
	int sd, thread_fd;
	int flags = 1;

	if (port == 0) {
		return -1;
	}

	sd = socket(AF_INET, SOCK_DGRAM, 0);
	if (sd < 0) {
		PLAT_ERROR("socket create fail\n");
		return -1;
	}
	setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &flags, sizeof(flags));
	maximize_sndbuf(sd);

	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr.s_addr = inet_addr("0.0.0.0");
	if (bind(sd, (struct sockaddr *)&addr, sizeof(struct sockaddr_in)) < 0) {
		close(sd);
		PLAT_ERROR("socket bind fail\n");
		return -1;
	}

	ndelay_on(sd);
	close_on_exec_on(sd);

	PLAT_INFO("create java udp\n");

    event_assign(&event, base, sd,  EV_READ | EV_PERSIST, java_udp_recv, tx_sd);
    event_add(&event, 0);

	return 0;
}

/******************************************************************************
Function      : create_server
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail
Others        :
******************************************************************************/
int create_server(struct event_base *base)
{
	create_unix_server(base, settings.socketpath);
	create_tcp_server(base, settings.port);

    settings.java_sd = create_udp_server(base, settings.udpport);

    create_java_udp_server(base, settings.java_sd, settings.java_udpport);

	return 0;
}

int get_java_sd(void)
{
    return settings.java_sd;
}

/******************************************************************************
Function      : help
Description   :
Input         :
Output        :
Return        :
Others        : U:p:s:c:h
******************************************************************************/
static void help(FILE *stream)
{
	fprintf(stream, " GD platform sever, version "PLAT_VERSION"\n\n");
	fprintf(stream,
		"-U, --udp-port=<num>      UDP port to listen on (default: 8090, 0 is off)\n"
		"-p, --port=<num>          TCP port to listen on (default: 8090)\n"
		"-s, --unix-socket=<file>  UNIX socket to listen on (disables network support)\n"
		"-c, --conn-limit=<num>    max simultaneous connections (default: 1024)\n"
		"-h, --help                print this help and exit\n");
}

/******************************************************************************
Function      : settings_init
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void settings_init(void)
{
	settings.port = 8090;
	settings.udpport = 8090;
	settings.socketpath = NULL;  /* by default, not using a unix socket */
	settings.maxconns = 1024;
	settings.java_udpport = 9002;
}

/******************************************************************************
Function      : main
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
int main(int argc, char *argv[])
{
	struct event_base *main_base;
	int c;

	/* handle SIGINT and SIGTERM */
	signal(SIGINT, sig_handler);
	signal(SIGTERM, sig_handler);

	/*ignore pipe*/
	signal(SIGPIPE, SIG_IGN);

	/* init settings */
	settings_init();

	const char *shortopts = "U:j:p:s:c:h";

#if HAVE_GETOPT_LONG
	const struct option longopts[] = {
		{ "udp-port", required_argument, 0, 'U' },
		{ "java-port", required_argument, 0, 'j' },
		{ "port", required_argument, 0, 'p' },
		{ "unix-socket", required_argument, 0, 's' },
		{ "conn-limit", required_argument, 0, 'c' },
		{ "help", no_argument, 0, 'h' },
		{ 0, 0, 0, 0 }
	};
	int optindex;
	while ((c = getopt_long(argc, argv, shortopts, longopts, &optindex)) != -1) {
#else
	while ((c = getopt(argc, argv, shortopts)) != -1) {
#endif
		switch (c) {
		case 'U':
			settings.udpport = atoi(optarg);
			break;
		case 'j':
			settings.java_udpport = atoi(optarg);
			break;
		case 'p':
			settings.port = atoi(optarg);
			break;
		case 's':
			settings.socketpath = optarg;
			break;
		case 'c':
			settings.maxconns = atoi(optarg);
			if (settings.maxconns <= 0) {
				fprintf(stderr, "Maximum connections must be greater than 0\n");
				return 1;
			}
			break;
		case 'h':
		default:
			help(stdout);
			exit(EXIT_SUCCESS);
		}
	}
	server_init();

	/*start work thread*/
	if (setup_work_thread() != 0) {
		PLAT_ERROR("Fail to setup work thread\n");
		exit(1);
	}

	/*create server*/
	main_base = event_init();
	if (create_server(main_base) != 0) {
		PLAT_ERROR("Fail to create server\n");
		exit(1);
	}

	event_base_dispatch(main_base);

	return 0;
	}

