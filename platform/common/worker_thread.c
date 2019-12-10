/*****************************************************************************
Copyright (C), LGF. Co., Ltd.
File name    £º worker_thread.c
Description  :
Author       £ºlgf
Version      £º
Date         £º2017.10.25
Others       £ºEmail:
History      £º
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
1) Date£º
   Mender£º
   Content:
2£©...

*****************************************************************************/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <pthread.h>

#include "worker_thread.h"

#define THREAD_LOG(level, fmt, args...)  printf("[%s,%d] "fmt"\n", __FUNCTION__, __LINE__, ##args)

#define THREAD_ERR(fmt, args...)         THREAD_LOG(LG_ERR, fmt,  ##args)
#define THREAD_INFO(fmt, args...)        THREAD_LOG(LG_INFO, fmt, ##args)

typedef struct worker_thread {
    struct worker_thread    *next;
    pthread_t               thread_id;
    pthread_mutexattr_t     mutex_attr;
    pthread_mutex_t         mutex;
    pthread_cond_t          cond;
    struct thread_pool      *pool;
    thread_job_t            *job;
    int                     del_self;
    int                     running;
} worker_thread_t;

typedef struct thread_pool {
    pthread_mutexattr_t  mutex_attr;
    pthread_mutex_t      mutex;
    worker_thread_t      *idle_threads;
    worker_thread_t      threads[WORKER_THREAD_POOL_SIZE];
} thread_pool_t;

/*****************************************************************************
 *
*****************************************************************************/
static pthread_once_t thread_pool_once = PTHREAD_ONCE_INIT;
static thread_pool_t  thread_pool;

#define get_thread_pool()  (&thread_pool)

/*****************************************************************************
*                        Function declare
*****************************************************************************/
static void *worker_thread_loop(void *arg);

/*****************************************************************************
Function      : worker_thread_xalloc
Description   :
Input         :
Output        :
Return        : alloc memory point
Others        :
*****************************************************************************/
static void *worker_thread_xalloc(size_t size)
{
    if (size == 0) {
        return NULL;
    }
    return malloc(size);
}

/*****************************************************************************
Function      : worker_thread_free
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void worker_thread_free(void *ptr)
{
    if (ptr) {
        free(ptr);
    }
}

/*****************************************************************************
Function      : worker_thread_lock
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static int worker_thread_lock(worker_thread_t *thread)
{
    return pthread_mutex_lock(&thread->mutex);
}

/*****************************************************************************
Function      : worker_thread_unlock
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static int worker_thread_unlock(worker_thread_t *thread)
{
    return pthread_mutex_unlock(&thread->mutex);
}

/*****************************************************************************
Function      : worker_thread_wait
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static int worker_thread_wait(worker_thread_t *thread)
{
    return pthread_cond_wait(&thread->cond, &thread->mutex);
}

/*****************************************************************************
Function      : worker_thread_signal
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static int worker_thread_signal(worker_thread_t *thread)
{
    return pthread_cond_signal(&thread->cond);
}

/*****************************************************************************
Function      : worker_thread_destory
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void worker_thread_destory(worker_thread_t *thread)
{
    pthread_cond_destroy(&thread->cond);

    pthread_mutex_destroy(&thread->mutex);

    pthread_mutexattr_destroy(&thread->mutex_attr);

    if (thread->del_self) {
        worker_thread_free(thread);
    }
}

/*****************************************************************************
Function      : worker_thread_create
Description   :
Input         :
Output        :
Return        : 0 if success, other if fail
Others        :
*****************************************************************************/
static int worker_thread_create(worker_thread_t *thread, thread_pool_t *pool, int del_self)
{
    pthread_mutexattr_init(&thread->mutex_attr);
    pthread_mutexattr_settype(&thread->mutex_attr, PTHREAD_MUTEX_RECURSIVE);

    pthread_mutex_init(&thread->mutex, &thread->mutex_attr);

    pthread_cond_init(&thread->cond, NULL );

    thread->del_self = del_self;

    thread->pool = pool;

    thread->job = NULL;

    thread->running = 0;

    return pthread_create(&thread->thread_id, NULL, worker_thread_loop, thread);
}

/*****************************************************************************
Function      : thread_pool_init
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void thread_pool_init(void)
{
    thread_pool_t   *pool = get_thread_pool();
    worker_thread_t *thread;
    int i;

    THREAD_INFO("init thread pool");

    memset(pool, 0, sizeof(thread_pool_t));

    /*setup worker thread pool*/
    pthread_mutexattr_init(&pool->mutex_attr);
    pthread_mutexattr_settype(&pool->mutex_attr, PTHREAD_MUTEX_RECURSIVE);
    pthread_mutex_init(&pool->mutex, &pool->mutex_attr);

    pool->idle_threads = NULL;

    /*setup worker thread*/
    for (i = 0; i < sizeof(pool->threads)/sizeof(pool->threads[0]); i++) {
        thread = &pool->threads[i];
        if (worker_thread_create(thread, pool, 0) != 0) {
            THREAD_ERR("worker thread create fail");
            worker_thread_destory(thread);
        }
    }
}

/*****************************************************************************
Function      : thread_pool_put
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void thread_pool_put(thread_pool_t *pool, worker_thread_t *thread)
{
    if (!pool) {
        return;
    }

    pthread_mutex_lock(&pool->mutex);

    thread->next = pool->idle_threads;
    pool->idle_threads = thread;

    pthread_mutex_unlock(&pool->mutex);
}

/*****************************************************************************
Function      : get_worker_thread
Description   :
Input         :
Output        :
Return        : worker thread
Others        :
*****************************************************************************/
static worker_thread_t *get_worker_thread(void)
{
    thread_pool_t *pool = get_thread_pool();
    worker_thread_t *thread = NULL;

    pthread_once(&thread_pool_once, thread_pool_init);

    pthread_mutex_lock(&pool->mutex);

    if (pool->idle_threads) {
        thread = pool->idle_threads;
        pool->idle_threads = thread->next;
    }

    pthread_mutex_unlock(&pool->mutex);

    if (thread) {
        thread->next = NULL;
    }

    return thread;
}

/*****************************************************************************
Function      : new_worker_thread
Description   :
Input         :
Output        :
Return        : worker thread
Others        :
*****************************************************************************/
static worker_thread_t *new_worker_thread(void)
{
    worker_thread_t *thread;

    thread = (worker_thread_t *)worker_thread_xalloc(sizeof(worker_thread_t));

    if (thread) {
        memset(thread, 0, sizeof(*thread));
        if (worker_thread_create(thread, NULL, 1) != 0) {
            THREAD_ERR("worker thread create fail");
            worker_thread_destory(thread);
            thread = NULL;
        }
    }

    return thread;
}

/*****************************************************************************
Function      : worker_thread_run_job
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void worker_thread_run_job(worker_thread_t *thread, thread_job_t *job)
{
    worker_thread_lock(thread);

    thread->job = job;
    worker_thread_signal(thread);

    worker_thread_unlock(thread);
}

/*****************************************************************************
Function      : worker_thread_loop
Description   :
Input         :
Output        :
Return        :
Others        :
*****************************************************************************/
static void *worker_thread_loop(void *arg)
{
    worker_thread_t *thread = arg;

    thread->running = 1;

    while (1) {

        //append thread to idle-pool and wait for work
        thread_pool_put(thread->pool, thread);

        worker_thread_lock(thread);

        //no job, wait it
        while (thread->job == NULL) {
            worker_thread_wait(thread);
        }

        //new job, process it
        if (thread->job->process) {
            thread->job->process(thread->job);
        }

        //reset job
        thread->job = NULL;

        worker_thread_unlock(thread);

        if (!thread->pool) {
            break;
        }
    }

    thread->running = 0;

    THREAD_INFO("worker thread exit");
    worker_thread_destory(thread);

    //detach work thread
    pthread_detach(pthread_self());

    return NULL;
}

/*****************************************************************************
Function      : worker_thread_run
Description   :
Input         :
Output        :
Return        : 0 if success, -1 if fail.
Others        :
*****************************************************************************/
int worker_thread_run(thread_job_t *job, int pool)
{
    worker_thread_t *thread;

    if (job == NULL) {
        return -1;
    }

    if (pool) {
        thread = get_worker_thread();
    } else {
        thread = new_worker_thread();
    }

    if (thread) {
        worker_thread_run_job(thread, job);
        return 0;
    }

    return -1;
}
