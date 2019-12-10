/*
 * thread.h
 *
 *  Created on: 2017Äê8ÔÂ16ÈÕ
 *      Author: root
 */

#ifndef THREAD_H_
#define THREAD_H_

#include <stddef.h>
#include <pthread.h>
//#include "../3rd_party/libevent/libevent-2.0.21/event.h"
#include "event.h"
#include "common.h"

#define thread_socket_t  int
typedef struct event thread_event_t;

enum {
    THREAD_RUN_NONE = 0,
    THREAD_RUN_LOOP,
    THREAD_RUN_COND
};

struct thread_notify_element {
    TAILQ_ENTRY(thread_notify_element) entry;
    struct thread_handler *source;
    int (*process)(void *pthread, void *data, size_t len);
    size_t bufsize;
    size_t datalen;
    char buf[0];
};

TAILQ_HEAD(thread_notify_queue_head, thread_notify_element);

struct thread_nofity_queue {
    struct thread_notify_queue_head head;
};

struct thread_handler {
    pthread_t thread_id;            /* unique ID of this thread */
    pthread_mutex_t notify_lock;
    struct thread_nofity_queue queue;
    void (*notify_process)(thread_socket_t, short, void *);
    int (*notify_fn)(struct thread_handler *);
    void *arg;
    void(*init)(void *);
    void *loop;
    union {
        thread_event_t event;
        pthread_cond_t cond;
    };
    int notify_recv_fd;             /* receiving end of notify pipe */
    int notify_send_fd;             /* sending end of notify pipe */
    int is_notify_pending;          /* True if the base already has a pending notify, and we don't need to add any more. */
    int running;
};

/******************************************************************************
******************************************************************************/
void *thread_new_buffer(size_t size);

void thread_free_buffer(void *buffer);

void *thread_create(void *pthread, int lp, void(*init)(void *), void *arg);

void *thread_get_self(void);

int thread_msg_send(void *src_thread, void *msg, int msglen, void *dst_thread, int (*process)(void *pthread, void *data, size_t len));

int thread_msg_post(void *src_thread, void *msg, int msglen, void *dst_thread, int (*process)(void *pthread, void *data, size_t len));

#endif /* THREAD_H_ */
