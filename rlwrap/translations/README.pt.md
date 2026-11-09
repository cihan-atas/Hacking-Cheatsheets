<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap – Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Estabilização de Shell">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploração e projéteis">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Transforme ouvintes Netcat brutos e shells reversos burros em interfaces de linha de comando completas com teclas de seta, histórico e preenchimento automático</b>
</p>

---

## 📋 Índice

- [Introdução: O problema da "casca burra"](#-introdução-o-problema-da-casca-burra)
- [Instalação](#-instalação)
- [Uso básico](#-uso básico)
- [Capturando Shells Reversos (`rlwrap + nc`)](#-capturando-shells-reversos-rlwrap--nc)
- [Gerenciamento de histórico persistente](#-persistent-history-management)
- [Completamento automático de comando (`-f`)](#-command-auto-completion--f)
- [Fluxo de trabalho completo de atualização do TTY Shell em 5 etapas](#-full-5-step-tty-shell-upgrade-workflow)
- [Usando rlwrap com outras ferramentas Pentest](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis e Telnet](#redis--telnet)
  - [Socat e soquetes personalizados](#socat--soquetes personalizados)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Introdução: O problema da "casca burra"

Ao capturar um shell reverso usando ferramentas tradicionais como Netcat (`nc -lvnp 4444`), você obtém um **terminal burro**. Um terminal burro não possui recursos padrão de edição de linha:

- ❌ Pressionar as **teclas de seta para cima/para baixo** imprime sequências de escape como `^[[A` ou `^[[B` em vez de percorrer o histórico de comandos.
- ❌ Pressionar **Backspace** ou **Delete** imprime `^H` ou `^?` em vez de excluir caracteres.
- ❌ Pressionar **Ctrl + C** mata imediatamente o ouvinte de shell reverso, perdendo o acesso conquistado com dificuldade.
- ❌ Comandos como `su`, `sudo`, `nano`, `vim` ou `passwd` falham porque não há pseudoterminal (PTY) conectado.
- ❌ Nenhum histórico de comando persistente é retido entre as sessões.

**rlwrap** (Readline Wrapper, escrito por Hans Lub) resolve isso colocando-o entre seu terminal e o comando de destino. Ele intercepta toques no teclado, aproveita o **GNU Readline** e fornece navegação com teclas de seta, edição de linha, arquivos persistentes de histórico de comandos e preenchimento automático baseado em dicionário!

---

## 📥 Instalação

`rlwrap` é leve e está disponível nos repositórios de pacotes padrão de praticamente todas as distribuições Linux:

###Kali Linux, Debian e Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS e RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### ArchLinux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (Homebrew)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Uso básico

A sintaxe básica é simplesmente agrupar qualquer linha de comando com `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Sinalizadores Comuns

| Bandeira | Descrição |
|------|-------------|
| `-c` | Habilitar preenchimento de nome de arquivo e caminho |
| `-A` | Ignorar códigos de escape de formatação de prompt (evita falhas na linha de prompt) |
| `-r` | Lembre-se de todas as palavras inseridas na entrada e coloque-as na lista de palavras de conclusão |
| `-H <file>` | Especifique um arquivo de histórico dedicado para salvar e recuperar comandos |
| `-s <num>` | Defina o número máximo de linhas do histórico (padrão: 300) |
| `-f <file>` | Carregar um arquivo de dicionário de conclusão personalizado |
| `-b <chars>` | Definir caracteres de quebra de palavras |
| `-m` | Habilitar edição de entrada multilinha |
| `-p[color]` | Colorir o prompt de comando |

---

## 🎣 Capturando projéteis reversos (`rlwrap + nc`)

O caso de uso mais comum em segurança ofensiva é agrupar um ouvinte Netcat (consulte o [Netcat Cheatsheet](../Netcat/README.md) para variantes e sinalizadores completos do ouvinte):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Explicação dos sinalizadores `-cAr`:

1. **`-c` (Completar Arquivo):** Permite usar `<TAB>` para completar automaticamente nomes de arquivos e diretórios.
2. **`-A` (Prompts):** Evita quebras de linha estranhas e duplicação de prompt quando o servidor remoto envia strings de prompt não padronizadas (por exemplo, `root@victim:/#`).
3. **`-r` (Lembrar palavras):** Aprende palavras que você digita no shell para que você possa completá-las mais tarde.

---

## 💾 Gerenciamento de histórico persistente

Por padrão, `rlwrap` salva o histórico de comandos em `~/.<command>_history`. Você pode configurar arquivos de histórico personalizados por engajamento:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Preenchimento automático de comando (`-f`)

Você pode fornecer ao `rlwrap` um arquivo de texto contendo comandos comuns do Linux ou Windows, e ele os completará automaticamente quando você pressionar `<TAB>`:
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ Fluxo de trabalho completo de atualização do shell TTY em 5 etapas

Embora o `rlwrap` forneça navegação e histórico imediatos com teclas de seta, certos programas interativos (`su`, `sudo`, `vim`) ainda exigem um terminal TTY totalmente alocado. Aqui está o pipeline de atualização de 5 etapas padrão do setor:

### Etapa 1: iniciar o ouvinte com rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Etapa 2: Acionar o Shell reverso e gerar PTY

Depois que seu shell reverso se conectar ao seu ouvinte, gere um pseudoterminal no destino:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Etapa 3: plano de fundo do shell
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Etapa 4: Configurar o modo bruto do terminal local
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Pressione Enter duas vezes após executar `fg` para restaurar o prompt)*

### Etapa 5: redefinir e definir as dimensões do terminal
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **Resultado:** Agora você tem um shell bash interativo indestrutível e completo com preenchimento de guias, tela limpa (`Ctrl+L`), manipulação de sinal (`Ctrl+C` não desconecta) e dimensões completas do terminal!

---

## 🔧 Usando rlwrap com outras ferramentas Pentest

`rlwrap` não está limitado ao Netcat. Qualquer ferramenta CLI sem edição de linha se beneficia dela:

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis e Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat e soquetes personalizados
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Tabela de referência rápida

| Tarefa | Comando |
|------|---------|
| **Ouvinte Netcat aprimorado** | `rlwrap nc -lvnp 4444` |
| **Arquivo completo e sinalizadores de prompt** | `rlwrap -cAr nc -lvnp 4444` |
| **Arquivo de histórico dedicado** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Conclusão de guia personalizada** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Aumentar o tamanho do histórico** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Envolver Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Envolver CLI do Redis** | `rlwrap redis-cli -h 10.10.10.50` |
| **Wrap Socat Ouvinte** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Dicas e práticas recomendadas

1. **Crie um alias do Bash:** Adicione-o ao seu `~/.bashrc` ou `~/.zshrc` para nunca se esquecer de usá-lo:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Nunca perca a segurança do `Ctrl+C`:** Ao capturar shells instáveis, o `rlwrap` intercepta falhas básicas do teclado, mas lembre-se sempre de fazer a atualização do `stty raw -echo; fg` em 5 etapas se você planeja executar comandos interativos longos.
3. **Isolar históricos de clientes:** Ao testar vários clientes ou máquinas, sempre use `-H ~/.client_name.hist` para manter trilhas de auditoria e históricos de comandos separados.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** Os shells reversos e as ferramentas pós-exploração só devem ser usados em testes de penetração autorizados, compromissos de equipe vermelha e ambientes de laboratório educacional com consentimento explícito por escrito.