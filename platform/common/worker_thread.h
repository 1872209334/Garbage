/*
 * worker_thread.h
 *
 *  Created on: 2017Äê10ÔÂ25ÈÕ
 *      Author: 1305
 */

#ifndef WORKER_THREAD_H_
#define WORKER_THREAD_H_

#include <stddef.h>

#define WORKER_THREAD_POOL_SIZE      5

typedef struct thread_job {
    void (*process)(struct thread_job *job);
    size_t size;
    char data[0];
} thread_job_t;

int worker_thread_run(thread_job_t *job, int pool);

#endif /* WORKER_THREAD_H_ */
