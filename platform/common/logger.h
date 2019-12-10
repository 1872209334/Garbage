/*
 * logger.h
 *
 *  Created on: 2017Äê8ÔÂ14ÈÕ
 *      Author: root
 */

#ifndef LOGGER_H_
#define LOGGER_H_

#define PLAT_TRACE(...) printf(__VA_ARGS__)
#define PLAT_INFO(...)  printf(__VA_ARGS__)
#define PLAT_ERROR(...) printf(__VA_ARGS__)
#define PLAT_WARN(...)  printf(__VA_ARGS__)

enum plat_log_level {
    PLAT_LOG_TRACE,
    PLAT_LOG_DEBUG,
    PLAT_LOG_INFO,
    PLAT_LOG_WARN,
    PLAT_LOG_ERROR,
    PLAT_LOG_FATAL
};

#endif /* LOGGER_H_ */
