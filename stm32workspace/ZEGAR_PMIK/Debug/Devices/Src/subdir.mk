################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (11.3.rel1)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Devices/Src/Clocker.c \
../Devices/Src/DFPLAYER_MINI.c \
../Devices/Src/LCD1602.c \
../Devices/Src/TM1637.c 

OBJS += \
./Devices/Src/Clocker.o \
./Devices/Src/DFPLAYER_MINI.o \
./Devices/Src/LCD1602.o \
./Devices/Src/TM1637.o 

C_DEPS += \
./Devices/Src/Clocker.d \
./Devices/Src/DFPLAYER_MINI.d \
./Devices/Src/LCD1602.d \
./Devices/Src/TM1637.d 


# Each subdirectory must supply rules for building sources it contributes
Devices/Src/%.o Devices/Src/%.su Devices/Src/%.cyclo: ../Devices/Src/%.c Devices/Src/subdir.mk
	arm-none-eabi-gcc "$<" -mcpu=cortex-m0plus -std=gnu11 -g3 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L073xx -c -I../Core/Inc -I../Drivers/STM32L0xx_HAL_Driver/Inc -I../Drivers/STM32L0xx_HAL_Driver/Inc/Legacy -I../Drivers/CMSIS/Device/ST/STM32L0xx/Include -I../Drivers/CMSIS/Include -I"C:/Users/xenon/Downloads/PMiK_Projekt/pmik_projekt_ka_jj_zegar/stm32workspace/ZEGAR_PMIK/Devices/Inc" -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -fcyclomatic-complexity -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfloat-abi=soft -mthumb -o "$@"

clean: clean-Devices-2f-Src

clean-Devices-2f-Src:
	-$(RM) ./Devices/Src/Clocker.cyclo ./Devices/Src/Clocker.d ./Devices/Src/Clocker.o ./Devices/Src/Clocker.su ./Devices/Src/DFPLAYER_MINI.cyclo ./Devices/Src/DFPLAYER_MINI.d ./Devices/Src/DFPLAYER_MINI.o ./Devices/Src/DFPLAYER_MINI.su ./Devices/Src/LCD1602.cyclo ./Devices/Src/LCD1602.d ./Devices/Src/LCD1602.o ./Devices/Src/LCD1602.su ./Devices/Src/TM1637.cyclo ./Devices/Src/TM1637.d ./Devices/Src/TM1637.o ./Devices/Src/TM1637.su

.PHONY: clean-Devices-2f-Src

