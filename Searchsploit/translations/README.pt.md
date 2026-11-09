<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do Searchsploit

[Searchsploit](https://www.exploit-db.com/searchsploit) é o utilitário de linha de comando oficial do [Exploit-Database](https://www.exploit-db.com/). Ele permite que profissionais de segurança, testadores de penetração e pesquisadores de vulnerabilidade conduzam pesquisas off-line em dezenas de milhares de explorações, códigos de shell e white papers publicados sem enviar tráfego de rede para mecanismos de pesquisa de terceiros.

---

## Índice

- [Visão geral e arquitetura](#visão geral--arquitetura)
- [Instalação e atualizações de banco de dados local](#installation--local-database-updates)
- [Pesquisa de exploração básica e avançada](#basic--advanced-exploit-search)
- [Examinando e copiando explorações (`-m` / `-p` / `-x`)](#examinando--copiando-exploits--m---p---x)
- [Filtragem por plataforma e tipo](#filtragem por plataforma--tipo)
- [Integração de verificação Nmap XML/Grepable](#nmap-xml--grepable-scanning-integration)
- [Saída JSON e automação de script](#json-output--script-automation)
- [Melhores práticas em testes de penetração](#melhores práticas em testes de penetração)

---

## Visão geral e arquitetura

Searchsploit consulta um clone git local do Exploit-DB armazenado em `/usr/share/exploitdb/`.
- **Zero impressão digital de rede:** as pesquisas são executadas em CSV locais e estruturas de diretório.
- **Confiabilidade off-line:** Totalmente funcional em ambientes de destino isolados e redes de engajamento.
- **Espelhamento integrado:** Copia arquivos de exploração diretamente para o diretório de trabalho atual para revisão e modificação imediatas.

---

## Instalação e atualizações de banco de dados local

### Debian/Ubuntu/Kali Linux/Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Instalação do repositório Git (qualquer distribuição Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Pesquisa básica e avançada de exploits

### 1. Pesquisa simples por palavra-chave
Pesquise serviços, daemons ou software específicos:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Correspondência exata de título (`-t`)
Restrinja as consultas de pesquisa estritamente para explorar títulos e eliminar correspondências de descrições barulhentas:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Correspondência estrita de casos e termos (`-e`)
Encontre correspondências exatas de versão e padrão:
```bash
searchsploit -e "Apache Struts"
```
### 4. Pesquisa por identificador CVE
Consultar explorações mapeadas para números específicos de vulnerabilidades e exposições comuns:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Excluir resultados indesejados (`--exclude`)
Filtre a negação de serviço (DoS) ou scripts auxiliares ao buscar execução remota de código (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Examinando e copiando explorações (`-m` / `-p` / `-x`)

Nunca edite ou execute exploits diretamente no diretório `/usr/share/exploitdb/`. Use as funções de cópia e inspeção do Searchsploit.

### 1. Copiar exploração (espelho) para o diretório atual (`-m`)
Copie um arquivo PoC de exploração (Python, C, Ruby, bash) diretamente para seu diretório de trabalho local:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Inspecione os metadados de exploração e o caminho completo (`-p`)
Exiba o caminho absoluto do sistema de arquivos e o URL do Exploit-DB para um EDB-ID:
```bash
searchsploit -p 50383
```
Saída:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Leia o código de exploração no Terminal Pager (`-x`)
Abra o código de exploração diretamente no `less` para inspeção rápida:
```bash
searchsploit -x 50383
```
---

## Filtragem por plataforma e tipo

O Searchsploit indexa explorações, shellcode e artigos de pesquisa em várias arquiteturas:

### 1. Filtrar por sistema operacional de destino
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Filtro apenas para Shellcodes brutos (`-s`)
Procure cargas de shellcode específicas da arquitetura (por exemplo, execve `/bin/sh` sem bytes nulos):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Filtro para artigos e white papers de segurança (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Integração de digitalização Nmap XML/Grepable

O Searchsploit pode analisar automaticamente as saídas de varredura XML do Nmap e comparar versões de software descobertas com explorações conhecidas:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit inspeciona cada banner de porta aberta e string de versão, consultando explorações locais para triagem imediata.*

---

## Saída JSON e automação de script

Para automação de pipeline e ferramentas de reconhecimento personalizadas, a saída resulta em JSON estruturado:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Filtre os caminhos dos arquivos de exploração usando `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Melhores práticas em testes de penetração

1. **Verificar antes de executar:** Sempre inspecione o código-fonte do exploit (`searchsploit -x <id>`) antes da compilação ou execução. Verifique o shellcode codificado, direcione os endereços IP e certifique-se de que não haja backdoors ou stagers de carga útil maliciosos.
2. **Compile com sinalizadores corretos:** Muitas explorações do Exploit-DB C requerem sinalizadores GCC específicos (por exemplo, `-m32`, `-lpthread` ou `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Mantenha o banco de dados atualizado:** Execute o `searchsploit -u` regularmente para garantir que os dias zero e as provas de conceito recém-publicados sejam indexados localmente.