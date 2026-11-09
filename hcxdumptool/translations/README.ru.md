<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка hcxdumptool

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) — это современный высокопроизводительный инструмент для атаки Wi-Fi и захвата пакетов с использованием командной строки. В отличие от устаревших инструментов Aircrack-ng, `hcxdumptool` захватывает кадры WPA/WPA2/WPA3 PMKID (идентификатор парного главного ключа) без необходимости подключения клиентов, захватывает четырехсторонние рукопожатия EAPOL и напрямую интегрируется с `hcxtools` для взлома Hashcat (режим 22000).

---

## Содержание

- [Основные преимущества и современная архитектура](#core-advantages--modern-architecture)
- [Установка (`hcxdumptool` и `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Настройка беспроводного интерфейса](#wireless-interface-setup)
- [Захват PMKID (бесклиентская атака)](#captureing-pmkid-clientless-attack)
- [Преобразование снимков с помощью `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [Взлом Hashcat (Режим 22000)](#hashcat-cracking-mode-22000)
- [Фильтрация каналов и BSSID](#filtering-channels-and-bssids)
- [Защитные контрмеры и WPA3](#defensive-countermeasures--wpa3)

---

## Основные преимущества и современная архитектура

- **Бесклиентная атака на PMKID:** использует функцию PMKID RSN IE (надежный сетевой информационный элемент) в современных маршрутизаторах. Вам **не** нужен подключенный клиент или пакеты деаутентификации для захвата ключевого материала.
- **Унифицированный режим Hashcat 22000:** Создает собственные хэш-файлы Hashcat, поддерживающие как PMKID, так и полные рукопожатия WPA-PBKDF2-PMK.
- **Переключение каналов и активный опрос:** передает запросы целевой ассоциации для получения PMKID от прослушивающих точек доступа.

---

## Установка (`hcxdumptool` и `hcxtools`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Verify versions
hcxdumptool --version
hcxpcapngtool --version
```
---

## Настройка беспроводного интерфейса

Перед запуском `hcxdumptool` отключите фоновые службы управления сетью, которые мешают внедрению необработанных пакетов:
```bash
# 1. Terminate interfering network services (NetworkManager, wpa_supplicant)
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Put wireless card in monitor mode
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Verify monitor mode
iw dev
```
---

## Перехват PMKID (бесклиентская атака)

### 1. Общий захват PMKID на всех каналах 2,4 ГГц и 5 ГГц
Запишите все близлежащие точки доступа в файл `.pcapng`:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: интерфейс беспроводного монитора.
- `-w capture.pcapng`: Выходной файл захвата.
- `--rds=1`: передача кадров RTS для запроса ответов AP.

### 2. Захват определенной целевой точки доступа (фильтр BSSID)
Выбирайте только авторизованный BSSID, чтобы не влиять на неавторизованные сторонние маршрутизаторы:

1. Создайте список целевых фильтров (`target_bssid.txt`):
```text
001122334455
```
*(BSSID без двоеточий)*

2. Запустите целевой захват:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Блокировка инъекции и прослушивание канала 6.
- `--bpfp`: применить список BPF (фильтр пакетов Беркли) для целевых BSSID.

---

## Преобразование снимков с помощью `hcxpcapngtool`

После сбора кадров PMKID или EAPOL преобразуйте захват `.pcapng` в формат, совместимый с Hashcat:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: выходной хеш-файл в современном формате Hashcat.
- `-E essid_list.txt`: сохранять обнаруженные сетевые имена (ESSID) в словарь для целевого взлома паролей.
- Инструмент сообщает общее количество извлеченных кадров PMKID и EAPOL.

---

## Взлом Hashcat (режим 22000)

Режим `22000` — это унифицированный режим взлома Hashcat WPA-PBKDF2-PMKID + EAPOL:

### 1. Атака по словарю
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Мутационная атака на основе правил
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Маска/Атака методом перебора (8-значные цифровые пароли)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Фильтрация каналов и BSSID

Оптимизируйте скорость захвата, ограничив параметры сканирования:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Защитные меры и WPA3

| Уязвимость | Механизм атаки | Рекомендация обороны |
|---|---|---|
| **Кэширование PMKID** | Точка доступа передает PMKID в кадре EAPOL 1 без аутентификации клиента | Отключите 802.11r (Быстрый переход/Роуминг), если он не нужен; обновить прошивку роутера. |
| **Слабый общий ключ WPA2** | Офлайн-взлом словаря PBKDF2 (режим Hashcat 22000) | Введите случайные парольные фразы из 16+ символов. |
| **Устаревший протокол WPA2** | Восприимчив к PMKID и захвату рукопожатий | Обновите до **WPA3-Personal (SAE)**; WPA3 использует рукопожатие Dragonfly с прямой секретностью, благодаря чему захваченные кадры невозможно взломать в автономном режиме. |