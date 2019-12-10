################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
$(PRO_DIR)/server/server.c 

OBJS += \
server.o analy.o

C_DEPS += \
server.d analy.d


# Each subdirectory must supply rules for building sources it contributes
server.o: $(PRO_DIR)/server/server.c
	@echo 'Building file: $<'
	@echo 'Invoking: $(CC) C Compiler'
	$(CC) -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<" $(CFLAGS)
	@echo 'Finished building: $<'
	@echo ' '
	
	
analy.o: $(PRO_DIR)/server/analy.c
	@echo 'Building file: $<'
	@echo 'Invoking: $(CC) C Compiler'
	$(CC) -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<" $(CFLAGS)
	@echo 'Finished building: $<'
	@echo ' '

