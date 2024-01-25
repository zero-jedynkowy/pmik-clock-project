/**
 * @file Clocker.h
 * @brief Plik nagłówkowy biblioteki Clocker; zawiera wszystkie nagłówki funkcji
 *
 * Detailed description of the file goes here.
 *
 * @author Karol Ambroziński, Jakub Jastrzębski
 * @date   January 25, 2024
 */

#ifndef __CLOCKER_H
#define __CLOCKER_H

#include <stdint.h>
#include "DFPLAYER_MINI.h"
#include "TM1637.h"
#include "LCD1602.h"
#include "stm32l0xx_hal_rtc.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <cJSON.h>


/**
 * @brief Struktura zarządzająca całym urządzeniem
 *
 * Zawiera wszystkie zmienne i flagi wykorzystywane przy zmianach ekranu LCD,
 * połączeniu bluetooth, wifi itp.
 */
struct
{
	uint8_t maxScreen; /**< Maksymalna liczba ekranów LCD */
	uint8_t currentScreen; /**< Obecnie wybrany ekran/informacja wyświetlana na LCD */
	uint8_t screenTimeChanging; /**< Czas zmiany ekranu LCD w sekundach; ustawiany przed użytkownika */
	char tableOfScreens[10][2][16]; /**< Tabela zawartośći ekranu LCD */
	RTC_TimeTypeDef * sTime; /**< Struktura przechowująca czas RTC */
	RTC_DateTypeDef * sDate; /**< Struktura przechowująca date RTC */
	RTC_AlarmTypeDef * sAlarm; /**< Struktura przechowująca alarm RTC */
	RTC_HandleTypeDef * rtcHandle; /**< Wskażnik na strukture RTC */
	uint8_t screenTimer; /**< Zmienna określająca kiedy ekran LCD się zmieni */
	uint8_t alarmTimer; /**< Zmienna określająca kiedy ekran LCD się zmieni */
	uint8_t alarmTimerOn; /**< Zmienna określająca kiedy ekran LCD się zmieni */
	uint8_t alarm; /**< Zmienna określająca kiedy ekran LCD się zmieni */
	uint8_t wifi; /**< Flaga decydująca czy wifi jest włączone */
	uint8_t weather; /**< Flaga decydująca czy pogoda jest włączona */
	uint8_t dateTime; /**< Flaga decydująca czy data ma być ustawiana po wifi; 1 - wifi, 0 - uzytkownik */
	char login[250]; /**< Zmienna przechowująca login do wifi */
	char password[250]; /**< Zmienna przechowująca hasło do wifi */
	char city[250]; /**< Zmienna przechowująca nazwe miasta */
	char contentOfScreens[10][16]; /**< Zmienna pomocnicza przechowująca zawartość ekranu LCD*/
} typedef Clocker;

/** @brief Inicjalizuje strukturę zegara czyli godzinę,
 * 		   datę, pogodę, ekrany itp.
 *
 *  @param myClocker Struktura Clocker która ma byc zainicjalizowana
 *
 *  @param rtcHandle Handler od RTC
 *
 *  @param timSegment Handler od Timera 1 (ekran segmentowy)
 *
 *  @param timScreen Handler od Timera 2 (LCD)
 *
 *  @return Void.
 */
void Clocker_Init(Clocker * myClocker, RTC_HandleTypeDef * rtcHandle, TIM_HandleTypeDef * timSegment, TIM_HandleTypeDef * timScreen);


/** @brief Ustawia początkowe wartości ekranów LCD
 *
 *
 *  @param myClocker Struktura Clocker która ma byc edytowana
 *
 *  @return Void.
 */
void Clocker_Set_Screens(Clocker * myClocker);





/** @brief Ustawia czasa od którego zacznie odliczać czas.
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @param newHours Godzina która ma być ustawiona w RTC
 *
 *  @param newMinutes Minuty które mają być ustawiona w RTC
 *
 *  @param newSeconds Sekundy które mają być ustawiona w RTC
 *
 *  @return Void.
 */
void Clocker_Set_Time(Clocker * myClocker, uint8_t newHours, uint8_t newMinutes, uint8_t newSeconds);


/** @brief Ustawia godzinę i date alarmu
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @param alarmHours Ustawia godzinę alarmu
 *
 *  @param alarmMinutes Ustawia minuty alarmu
 *
 *  @return Void.
 */
void Clocker_Set_Alarm(Clocker * myClocker, uint8_t alarmHours, uint8_t alarmMinutes);

/** @brief Aktualizuje czas na ekranie segmentowym
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @return Void.
 */
void Clocker_Segment_Update(Clocker * myClocker);

/** @brief Aktualizuje ekran LCD
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @return Void.
 */
void Clocker_Change_Screen(Clocker * myClocker);

/** @brief Aktualizuje czas alarmu; sprawdza czy mineła minuta od odpalenia alarmu
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @return Void.
 */
void Clocker_Alarm_Update(Clocker * myClocker);

/** @brief Aktualizuje alarm
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @param hrtc Struktura RTC
 *
 *  @return Void.
 */
void Clocker_Run_Alarm(Clocker * myClocker, RTC_HandleTypeDef * hrtc);

/** @brief Aktualizuje timery ekranu LCD i alarmu; ich czasy trwania
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @return Void.
 */
void Clocker_Update_Timers(Clocker * myClocker);

/** @brief Zamienia liczbę na dane, którą ekran segmentowy jest w stanie odczytać
 *
 *  @param c Liczba int do zamiany
 *
 *  @return Void.
 */
uint8_t Clocker_Convert_Int_to_Segment(uint8_t c);

/** @brief Odpowiada za przetworzenie danych odebranych z telefonu z bluetooth
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @param table Tablica znaków char w której są dane z bluetooth
 *
 *  @return Void.
 */
void Clocker_Bluetooth(Clocker * myClocker, char * table);

/** @brief Odpowiada za przetowrzenie danychy z wifi
 *
 *  @param myClocker Struktura Clocker która ma być edytowana
 *
 *  @param table Tablica znaków char w której są dane z wifi
 *
 *  @return Void.
 */
void Clocker_Wifi_Receive(Clocker * myClocker, char * table);

#endif
