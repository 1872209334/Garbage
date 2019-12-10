################################################################################
# Makefile
################################################################################
export CROSS_COMPILE := 
export HOST := 
export MACHINE := 

#compiler define
PRO_DIR := ${shell pwd}/..

CC := $(CROSS_COMPILE)gcc
CFLAGS := -I$(PRO_DIR)/3rd_party/libevent/include \
          -I$(PRO_DIR)/common \
          -I$(PRO_DIR)/curl-7.62.0/include \
          -I/usr/include/mysql
          
LDFLAGS := -L$(PRO_DIR)/libs

RM := rm -rf

# All Target
all: server

# All of the sources participating in the build are defined here
-include sources.mk
-include $(PRO_DIR)/common/subdir.mk
-include subdir.mk

USER_OBJS := 

LIBS := -levent -lpthread -lcurl -lmysqlclient -lhiredis

ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs
CFLAGS += -DHAVE_GETOPT_LONG

# Add inputs and outputs from these tool invocations to the build variables 

# Tool invocations
server: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: $(CC) C Linker'
	$(CC)  -o "server" $(OBJS) $(USER_OBJS) $(LIBS) $(LDFLAGS)
	@echo 'Finished building target: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) $(OBJS)$(C_DEPS)$(EXECUTABLES) server
	-@echo ' '
	
.PHONY: all clean dependents
.SECONDARY:

-include ../makefile.targets
