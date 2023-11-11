################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (11.3.rel1)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Devices/Src/TM1637.c \
../Devices/Src/clock.c 

OBJS += \
./Devices/Src/TM1637.o \
./Devices/Src/clock.o 

C_DEPS += \
./Devices/Src/TM1637.d \
./Devices/Src/clock.d 


# Each subdirectory must supply rules for building sources it contributes
Devices/Src/%.o Devices/Src/%.su Devices/Src/%.cyclo: ../Devices/Src/%.c Devices/Src/subdir.mk
	arm-none-eabi-gcc "$<" -mcpu=cortex-m0plus -std=gnu11 -g3 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L073xx -c -I../Core/Inc -I../Drivers/STM32L0xx_HAL_Driver/Inc -I../Drivers/STM32L0xx_HAL_Driver/Inc/Legacy -I../Drivers/CMSIS/Device/ST/STM32L0xx/Include -I../Drivers/CMSIS/Include -I"/home/zero-jedynkowy/Pulpit/PMIK/pmik_projekt_ka_jj_zegar/stm32workspace/ZEGAR_PMIK/Devices/Inc" -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -fcyclomatic-complexity -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfloat-abi=soft -mthumb -o "$@"

clean: clean-Devices-2f-Src

clean-Devices-2f-Src:
	-$(RM) ./Devices/Src/TM1637.cyclo ./Devices/Src/TM1637.d ./Devices/Src/TM1637.o ./Devices/Src/TM1637.su ./Devices/Src/clock.cyclo ./Devices/Src/clock.d ./Devices/Src/clock.o ./Devices/Src/clock.su

.PHONY: clean-Devices-2f-Src

