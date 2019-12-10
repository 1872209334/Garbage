/*
 * thread.c
 *
 *  Created on: 2017Äê8ÔÂ16ÈÕ
 *      Author: root
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <time.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/eventfd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/times.h>
#include <fcntl.h>

#include "thread.h"

#define THREAD_HAVE_EVENTFD

#define thread_mm_alloc(size)   malloc(size)
#define thread_mm_free(ptr)     free(ptr)

#define thread_entry(ptr, type, member)  \
        ((type *)((char *)(ptr)- (unsigned long)(&((type *)0)->member)))

static pthread_once_t thread_key_once = PTHREAD_ONCE_INIT;
static pthread_key_t  thread_key;

static void thread_notify_process(struct thread_handler *pthread);

/******************************************************************************
Function      : thread_new_buffer
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
void *thread_new_buffer(size_t size)
{
    struct thread_notify_element *elm;
    size_t to_alloc;

    size += sizeof(struct thread_notify_element);

    /* get the next largest memory that can hold the buffer */
    to_alloc = 512;
    while (to_alloc < size) {
        to_alloc <<= 1;
    }

    elm = thread_mm_alloc(to_alloc);
    if (elm == NULL) {
        return NULL;
    }
    elm->bufsize = to_alloc - sizeof(struct thread_notify_element);
    elm->datalen = 0;

    return (void *)(elm->buf);
}

/******************************************************************************
Function      : thread_free_buffer
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
void thread_free_buffer(void *buffer)
{
    struct thread_notify_element *elm;

    if (buffer) {
        elm = thread_entry(buffer, struct thread_notify_element, buf);
        thread_mm_free(elm);
    }
}

/******************************************************************************
Function      : thread_add_buffer
Description   :
Input         :
Output        :
Return        : length add to the buffer
Others        :
******************************************************************************/
size_t thread_add_buffer(void *buffer, void *data, size_t datalen)
{
    struct thread_notify_element *elm;

    if (buffer) {
        elm = thread_entry(buffer, struct thread_notify_element, buf);
        if (elm->datalen + datalen > elm->bufsize) {
            datalen = elm->bufsize - elm->datalen;
        }
        memcpy((char *)(elm->buf) + elm->datalen, data, datalen);
        elm->datalen += datalen;
    } else {
        datalen = 0;
    }

    return datalen;
}

/******************************************************************************
Function      : thread_make_socket_closeonexec
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static int thread_make_socket_closeonexec(int fd)
{
    int flags;
    if ((flags = fcntl(fd, F_GETFD, NULL)) < 0) {
        return -1;
    }
    if (fcntl(fd, F_SETFD, flags | FD_CLOEXEC) == -1) {
        return -1;
    }

    return 0;
}

/******************************************************************************
Function      : thread_make_socket_nonblocking
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static int thread_make_socket_nonblocking(int fd)
{
    int flags;
    if ((flags = fcntl(fd, F_GETFL, NULL)) < 0) {
        return -1;
    }
    if (fcntl(fd, F_SETFL, flags | O_NONBLOCK) == -1) {
        return -1;
    }

    return 0;
}

/******************************************************************************
Function      : thread_notify_drain_queue
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void thread_notify_drain_queue(thread_socket_t fd, short what, void *arg)
{
    struct thread_handler *pthread = arg;
    (void)fd;
    (void)what;

    /*process it*/
    thread_notify_process(pthread);
}

/******************************************************************************
Function      : thread_notify_base_queue
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static int thread_notify_base_queue(struct thread_handler *pthread)
{
   if (pthread_cond_signal(&pthread->cond)) {
       return -1;
   }
   return 0;
}

/******************************************************************************
Function      : thread_notify_drain_default
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void thread_notify_drain_default(thread_socket_t fd, short what, void *arg)
{
    struct thread_handler *pthread = arg;
    unsigned char buf[1024];
    (void)what;

    while (read(fd, (char*)buf, sizeof(buf)) > 0);

    /*process it*/
    thread_notify_process(pthread);
}

/******************************************************************************
Function      : thread_notify_base_default
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static int thread_notify_base_default(struct thread_handler *pthread)
{
    int r;
    char buf[1] = {'c'};

    do {
        r = write(pthread->notify_send_fd, buf, 1);
    } while (r < 0 && errno == EINTR);

    return (r <= 0) ? -1 : 0;
}

#ifdef THREAD_HAVE_EVENTFD
/******************************************************************************
Function      : thread_notify_drain_eventfd
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void thread_notify_drain_eventfd(thread_socket_t fd, short what, void *arg)
{
    struct thread_handler *pthread = arg;
    unsigned long long msg;
    ssize_t r;
    (void)what;

    r = read(fd, (void*) &msg, sizeof(msg));
    if (r < 0 && errno != EAGAIN) {
    }

    /*process it*/
    thread_notify_process(pthread);
}

/******************************************************************************
Function      : thread_notify_base_eventfd
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static int thread_notify_base_eventfd(struct thread_handler *pthread)
{
    unsigned long long msg = 1;
    int r;

    do {
        r = write(pthread->notify_recv_fd, (void*) &msg, sizeof(msg));
    } while (r < 0 && (errno == EAGAIN || errno == EINTR));

    return (r <= 0) ? -1 : 0;
}
#endif /*THREAD_HAVE_EVENTFD*/

/*****************************************************************************
Function      : thread_key_init
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void thread_key_init(void)
{
    pthread_key_create(&thread_key, NULL);
}

/******************************************************************************
Function      : thread_set
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void thread_set(struct thread_handler *pthread)
{
    pthread_once(&thread_key_once, thread_key_init);
    pthread_setspecific(thread_key, (void *)pthread);
}

/******************************************************************************
Function      : thread_get_self
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
void *thread_get_self(void)
{
    pthread_once(&thread_key_once, thread_key_init);
    return pthread_getspecific(thread_key);
}

/******************************************************************************
Function      : thread_run_loop
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void *thread_run_loop(void *arg)
{
    struct thread_handler *pthread = arg;

    //set thread
    thread_set(pthread);

    pthread->running = THREAD_RUN_LOOP;

    if (pthread->init) {
        pthread->init(pthread->arg);
    }

    event_base_loop((struct event_base *)pthread->loop, 0);

    /*some error happen*/
    pthread->running = THREAD_RUN_NONE;

    return NULL;
}

/******************************************************************************
Function      : thread_run_cond
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void *thread_run_cond(void *arg)
{
    struct thread_handler *pthread = arg;
    struct thread_notify_element *elm;

    //set thread
    thread_set(pthread);

    pthread->running = THREAD_RUN_COND;

    if (pthread->init) {
        pthread->init(pthread->arg);
    }

    while (1) {
        pthread_mutex_lock(&pthread->notify_lock);
        while ((elm = TAILQ_FIRST(&pthread->queue.head)) == NULL) {
            pthread->is_notify_pending = 0;
            pthread_cond_wait(&pthread->cond, &pthread->notify_lock);
        }
        pthread_mutex_unlock(&pthread->notify_lock);

        if (elm->process) {
            elm->process(elm->source, elm->buf, elm->datalen);
        }

        /*free*/
        thread_free_buffer(elm->buf);
    }

    /*some error happen*/
    pthread->running = THREAD_RUN_NONE;

    return NULL;
}

/******************************************************************************
Function      : thread_notify_process
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void thread_notify_process(struct thread_handler *pthread)
{
    struct thread_notify_element *elm;

again:
    /*get inform*/
    pthread_mutex_lock(&pthread->notify_lock);
    if ((elm = TAILQ_FIRST(&pthread->queue.head))) {
        TAILQ_REMOVE(&pthread->queue.head, elm, entry);
    } else {
        pthread->is_notify_pending = 0;
    }
    pthread_mutex_unlock(&pthread->notify_lock);

    if (!elm) {
        return;
    }

    if (elm->process) {
        elm->process(elm->source, elm->buf, elm->datalen);
    }

    /*free*/
    thread_free_buffer(elm->buf);

    /*schedule next*/
    if (pthread->loop) {
        event_active(&pthread->event, EV_READ, 1);
    } else {
        goto again;
    }
}

/******************************************************************************
Function      : thread_notify
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        : the buffer will be eat
******************************************************************************/
static int thread_notify(void *src_thread, void *buffer, void *dst_thread,
                         int (*process)(void *pthread, void *data, size_t len))
{
    struct thread_handler *pthread = dst_thread;
    struct thread_notify_element *elm;

    if (pthread == NULL || pthread->notify_fn == NULL || pthread->running == THREAD_RUN_NONE) {
        thread_free_buffer(buffer);
        return -1;
    }
    elm = thread_entry(buffer, struct thread_notify_element, buf);
    elm->source = (struct thread_handler *)src_thread;
    elm->process = process;

    pthread_mutex_lock(&pthread->notify_lock);
    TAILQ_INSERT_TAIL(&pthread->queue.head, elm, entry);
    if (pthread->is_notify_pending == 0) {
        if (pthread->notify_fn(pthread) == 0) {
            pthread->is_notify_pending = 1;
        }
    }
    pthread_mutex_unlock(&pthread->notify_lock);

    return 0;
}

/******************************************************************************
Function      : thread_msg_send
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
int thread_msg_send(void *src_thread, void *msg, int msglen, void *dst_thread, int (*process)(void *pthread, void *data, size_t len))
{
    char *buffer;

    buffer = (char *)thread_new_buffer(msglen);
    if (buffer == NULL) {
        return -1;
    }
    thread_add_buffer(buffer, msg, msglen);

    return thread_notify(src_thread, buffer, dst_thread, process);
}

/******************************************************************************
Function      : thread_msg_post
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
int thread_msg_post(void *src_thread, void *msg, int msglen, void *dst_thread, int (*process)(void *pthread, void *data, size_t len))
{
    struct thread_notify_element *elm;

    if (msg == NULL) {
        return -1;
    }

    elm = thread_entry(msg, struct thread_notify_element, buf);
    if (msglen > elm->bufsize) {
        msglen = elm->bufsize;
    }
    elm->datalen = msglen;

    return thread_notify(src_thread, msg, dst_thread, process);
}

/******************************************************************************
Function      : thread_init
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
******************************************************************************/
static int thread_init(struct thread_handler *pthread, void *loop)
{
    void (*process)(thread_socket_t, short, void *) = thread_notify_drain_default;
    int (*notify)(struct thread_handler *) = thread_notify_base_default;
    int fd[2];

    pthread->notify_process = NULL;
    pthread->notify_fn = NULL;
    pthread->notify_recv_fd = -1;
    pthread->notify_send_fd = -1;
    pthread->running = THREAD_RUN_NONE;

    /*make thread*/
    if (!loop) {
        process = thread_notify_drain_queue;
        notify = thread_notify_base_queue;
        pthread_cond_init(&pthread->cond, NULL);
    } else {
        /*notify pair fd*/
        fd[0] = fd[1] = -1;
        #ifdef THREAD_HAVE_EVENTFD
        #ifndef EFD_CLOEXEC
        #define EFD_CLOEXEC 0
        #endif
        fd[0] = eventfd(0, EFD_CLOEXEC);
        if (fd[0] >= 0) {
            process = thread_notify_drain_eventfd;
            notify = thread_notify_base_eventfd;
        }
        #endif
        if (fd[0] < 0) {
            if (pipe(fd) < 0) {
                fd[0] = fd[1] = -1;
            }
        }
        if (fd[0] < 0) {
            if (socketpair(AF_UNIX, SOCK_STREAM, 0, fd) == -1) {
                return -1;
            }
        }
        thread_make_socket_closeonexec(fd[0]);
        thread_make_socket_nonblocking(fd[0]);
        if (fd[1] >= 0) {
            thread_make_socket_closeonexec(fd[1]);
            thread_make_socket_nonblocking(fd[1]);
        }
        pthread->notify_recv_fd = fd[0];
        pthread->notify_send_fd = fd[1];

        /*assign event*/
        event_assign(&pthread->event, (struct event_base *)loop,
                      pthread->notify_recv_fd,  EV_READ | EV_PERSIST, process, pthread);
        if (event_add(&pthread->event, 0) < 0) {
            close(pthread->notify_recv_fd);
            if (pthread->notify_send_fd >= 0) {
                close(pthread->notify_send_fd);
            }
            pthread->notify_recv_fd = -1;
            pthread->notify_send_fd = -1;
            return -1;
        }
    }

    /*set loop*/
    pthread->loop = loop;

    /*init thread event lock and condition*/
    pthread_mutex_init(&pthread->notify_lock, NULL);

    /*pthread queue init*/
    pthread->is_notify_pending = 0;
    TAILQ_INIT(&pthread->queue.head);

    /*set notify process function*/
    pthread->notify_process = process;
    /*Now set notify function*/
    pthread->notify_fn = notify;

    return 0;
}

/******************************************************************************
Function      : thread_setup
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
static void *thread_setup(struct thread_handler *pthread, void *loop, int create, void(*init)(void *), void *arg)
{
    int alloc = 0;

    if (!pthread) {
        /*create new */
        pthread = thread_mm_alloc(sizeof(struct thread_handler));
        if (!pthread) {
            return NULL;
        }
        alloc = 1;
    }
    memset(pthread, 0, sizeof(struct thread_handler));

    if (thread_init(pthread, loop) != 0) {
        if (alloc) {
            thread_mm_free(pthread);
        }
        return NULL;
    }

    pthread->arg = arg;
    pthread->init = init;

    if (!create) {
        pthread->thread_id = pthread_self();
        thread_set(pthread);
        if (pthread->loop) {
            pthread->running = THREAD_RUN_LOOP;
        } else {
            pthread->running = THREAD_RUN_COND;
        }
        return (void *)pthread;
    }

    if (pthread->loop) {
        if (pthread_create(&pthread->thread_id, NULL, thread_run_loop, pthread) != 0) {
            pthread_mutex_destroy(&pthread->notify_lock);
            event_del(&pthread->event);
            close(pthread->notify_recv_fd);
            if (pthread->notify_send_fd >= 0) {
                close(pthread->notify_send_fd);
            }
            if (alloc) {
                thread_mm_free(pthread);
            }
            return NULL;
        }
    } else {
        if (pthread_create(&pthread->thread_id, NULL, thread_run_cond, pthread) != 0) {
            pthread_mutex_destroy(&pthread->notify_lock);
            pthread_cond_destroy(&pthread->cond);
            if (alloc) {
                thread_mm_free(pthread);
            }
            return NULL;
        }
    }

    return (void *)pthread;
}

/******************************************************************************
Function      : thread_create
Description   :
Input         :
Output        :
Return        :
Others        :
******************************************************************************/
void *thread_create(void *pthread, int lp, void(*init)(void *), void *arg)
{
    void *loop;

    if (lp) {
        loop = (void *)event_base_new();
        if (!loop) {
            return NULL;
        }
    } else {
        loop = NULL;
    }

    pthread = thread_setup((struct thread_handler *)pthread, loop, 1, init, arg);
    if (!pthread && loop) {
        event_base_free(loop);
    }

    return pthread;
}

