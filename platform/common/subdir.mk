# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
$(PRO_DIR)/common/logger.c \
$(PRO_DIR)/common/thread.c 

OBJS += \
logger.o \
thread.o 

C_DEPS += \
logger.d \
thread.d 


# Each subdirectory must supply rules for building sources it contributes
logger.o: $(PRO_DIR)/common/logger.c
	@echo 'Building file: $<'
	@echo 'Invoking: $(CC) C Compiler'
	$(CC) -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<" $(CFLAGS)
	@echo 'Finished building: $<'
	@echo ' '

thread.o: $(PRO_DIR)/common/thread.c
	@echo 'Building file: $<'
	@echo 'Invoking: $(CC) C Compiler'
	$(CC) -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<" $(CFLAGS)
	@echo 'Finished building: $<'
	@echo ' '