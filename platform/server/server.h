/*
 * server.h
 *
 *  Created on: 2017Äê9ÔÂ1ÈÕ
 *      Author: root
 */

#ifndef SERVER_H_
#define SERVER_H_

#include "thread.h"

typedef unsigned int   u32_t;
typedef unsigned short u16_t;
typedef unsigned char  u8_t;

enum network_transport {
    local_transport, /* Unix sockets*/
    tcp_transport,
    udp_transport
};

typedef struct conn {
    int    sd;
    struct event event;

    char   *rbuf;   /** buffer to read commands into */
    char   *rcurr;  /** but if we parsed some already, this is where we stopped */
    int    rsize;   /** total allocated size of rbuf */
    int    rbytes;  /** how much data, starting from rcur, do we have unparsed */

    char   *wbuf;
    char   *wcurr;
    int    wsize;
    int    wbytes;

    struct sockaddr_in6 request_addr; /* udp: Who sent the most recent request */
    socklen_t request_addr_size;

    int transport;
    struct thread_handler *thread;
} conn;
#endif /* SERVER_H_ */

