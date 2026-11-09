<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📱 Frida e Objeção - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗██████╗ ██╗██████╗  █████╗      ██████╗ ██████╗      ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗
  ██╔════╝██╔══██╗██║██╔══██╗██╔══██╗    ██╔═══██╗██╔══██╗     ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║
  █████╗  ██████╔╝██║██║  ██║███████║    ██║   ██║██████╔╝     ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║
  ██╔══╝  ██╔══██╗██║██║  ██║██╔══██║    ██║   ██║██╔══██╗██   ██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║
  ██║     ██║  ██║██║██████╔╝██║  ██║    ╚██████╔╝██████╔╝╚█████╔╝███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═════╝ ╚═╝  ╚═╝     ╚═════╝ ╚═════╝  ╚════╝ ╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
           Dynamic Binary Instrumentation & Mobile Security Assessment
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Frida-Runtime_Hooking-blue?style=for-the-badge" alt="Frida">
  <img src="https://img.shields.io/badge/Objection-Mobile_Exploration-orange?style=for-the-badge" alt="Objection">
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_iOS-green?style=for-the-badge" alt="Android | iOS">
  <img src="https://img.shields.io/badge/Category-Mobile_Security-red?style=for-the-badge" alt="Segurança móvel">
</p>

<p alinhar="centro">
  <b>📱 Kit de ferramentas de instrumentação binária dinâmica padrão do setor para conexão de método de tempo de execução Android e iOS, desvio de fixação SSL, evasão de root/jailbreak e análise de memória</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação e configuração do dispositivo](#-instalação--configuração do dispositivo)
  - [Instalação de ferramentas de host](#host-tools-installation)
  - [Configuração do dispositivo Android (`frida-server`)](#android-device-setup-frida-server)
  - [Configuração do dispositivo iOS](#ios-device-setup)
- [Fundamentos do Frida CLI](#-frida-cli-fundamentals)
  - [Listando dispositivos e processos em execução](#listing-devices-and-running-processes)
  - [Anexar vs Gerar](#attaching-vs-spawning)
- [Scripts de conexão JavaScript](#-javascript-hooking-scripts)
  - [Ignorando a fixação SSL do Android](#bypassing-android-ssl-pinning)
  - [Ignorando detecção de raiz](#bypassing-root-detection)
  - [Interceptação de método Java e adulteração de argumentos](#java-method-interception--argument-tampering)
- [Kit de ferramentas de tempo de execução de objeção](#-objection-runtime-toolkit)
  - [Iniciando uma Sessão de Objeção](#iniciando-uma-sessão-de-objeção)
  - [Fixação SSL e desvio de raiz com um comando](#one-command-ssl-pinning--root-bypass)
  - [Explorando Keystore, SQLite e preferências compartilhadas](#exploring-keystore-sqlite-and-shared-preferences)
  - [Aulas de Hooking e Atividades de Listagem](#hooking-classes-and-listing-activities)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Anti-Frida](#-considerações-defensivas--anti-frida)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

**Frida** injeta o tempo de execução JavaScript V8 do Google em processos de destino no Android, iOS, Windows, macOS e Linux. Ele capacita os pesquisadores de segurança a:
- Conecte funções nativas arbitrárias (`libc`, OpenSSL) e gerenciadas (`Java`, `Objective-C`, `Swift`).
- Inspecione e modifique parâmetros de memória e função em tempo real.
- Rastreie APIs criptográficas e descriptografe o tráfego criptografado na memória.

**Objection** é uma estrutura de avaliação de tempo de execução de segurança móvel desenvolvida pela Frida que automatiza tarefas comuns de pentesting móvel (desativar fixação de certificado, despejar memória, ignorar autenticação biométrica) sem exigir código JavaScript manual.

---

## 📥 Instalação e configuração do dispositivo

### Instalação de ferramentas de host
```bash
pip install frida frida-tools objection
```
### Configuração do dispositivo Android (`frida-server`)
Baixe o `frida-server` correspondente à versão do host Frida e à arquitetura da CPU do dispositivo (x86_64, arm64, etc.):
```bash
# Verify architecture
adb shell getprop ro.product.cpu.abi

# Push and run frida-server on rooted Android device
unxz frida-server-*-android-arm64.xz
adb root
adb push frida-server-*-android-arm64 /data/local/tmp/frida-server
adb shell "chmod 755 /data/local/tmp/frida-server"
adb shell "/data/local/tmp/frida-server &"
```
### Configuração do dispositivo iOS
Em um dispositivo iOS com jailbreak, adicione o repositório Frida no Cydia/Sileo (`https://build.frida.re`) e instale o pacote `Frida`.

---

## 🚀 Fundamentos da Frida CLI

### Listando dispositivos e processos em execução
```bash
# List all connected USB devices
frida-ls-devices

# List all running applications on USB-connected Android/iOS
frida-ps -Uai

# List all system processes on USB device
frida-ps -U
```
### Anexando vs Gerando
- **Anexar (`-n` ou `-p`):** Anexa a um aplicativo já em execução.
- **Spawn (`-f`):** Inicia o aplicativo desde a inicialização inicial, pausando a execução antes da execução dos construtores iniciais:
```bash
# Attach to running process
frida -U -n com.example.app -l hook.js

# Spawn process with early instrumentation
frida -U -f com.example.app -l hook.js
```
---

## 💻 Scripts de conexão JavaScript

### Ignorando a fixação SSL do Android
Script de desvio do Universal TrustManager (`ssl_bypass.js`):
```javascript
Java.perform(function() {
    var X509TrustManager = Java.use('javax.net.ssl.X509TrustManager');
    var SSLContext = Java.use('javax.net.ssl.SSLContext');

    var TrustManager = Java.registerClass({
        name: 'com.custom.TrustManager',
        implements: [X509TrustManager],
        methods: {
            checkClientTrusted: function(chain, authType) {},
            checkServerTrusted: function(chain, authType) {},
            getAcceptedIssuers: function() { return []; }
        }
    });

    var TrustManagers = [TrustManager.$new()];
    var SSLContext_init = SSLContext.init.overload(
        '[Ljavax.net.ssl.KeyManager;', '[Ljavax.net.ssl.TrustManager;', 'java.security.SecureRandom');
    
    SSLContext_init.implementation = function(km, tm, random) {
        console.log('[+] Bypassing SSLContext.init() with custom TrustManager');
        SSLContext_init.call(this, km, TrustManagers, random);
    };
});
```
Corra com Frida:
```bash
frida -U -f com.example.app -l ssl_bypass.js
```
### Ignorando a detecção de raiz
Hook RootBeer padrão / verificações comuns:
```javascript
Java.perform(function() {
    var RootBeer = Java.use("com.scottyab.rootbeer.RootBeer");
    RootBeer.isRooted.implementation = function() {
        console.log("[+] Overriding RootBeer.isRooted() -> false");
        return false;
    };
});
```
### Interceptação de método Java e adulteração de argumentos
```javascript
Java.perform(function() {
    var LoginClass = Java.use("com.example.app.AuthManager");
    LoginClass.verifyToken.implementation = function(token) {
        console.log("[*] Original Token: " + token);
        // Force method to return true regardless of server response
        return true;
    };
});
```
---

## 🛠️ Kit de ferramentas de tempo de execução de objeções

Objection permite testes móveis sem esforço por meio de um shell REPL interativo:

### Iniciando uma sessão de objeção
```bash
objection -g com.example.app explore
```
### Fixação SSL e desvio de raiz com um comando
Dentro do shell de objeção:
```text
# Disable SSL Pinning immediately
android sslpinning disable

# Disable Root Detection checks
android root disable

# iOS equivalents
ios sslpinning disable
ios jailbreak disable
```
### Explorando Keystore, SQLite e preferências compartilhadas
```text
# Dump Keystore certificates & keys
android keystore list

# List application SQLite databases
sqlite sync
sqlite execute schema

# Inspect Android SharedPreferences XML files
env
android hooking list activities
```
### Aulas de engate e atividades de listagem
```text
# Search classes matching keyword
android hooking search classes auth

# Watch method invocations in real time
android hooking watch class_method com.example.app.Auth.login --dump-args --dump-return
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando Frida | Comando de objeção |
|-----------|---------------|-------------------|
| **Listar aplicativos USB** | `frida-ps -Uai` | `frida-ps -Uai` |
| **Aplicativo Spawn com Gancho**| `frida -U -f <PKG> -l hook.js` | `objection -g <PKG> explore` |
| **Ignorar fixação SSL** | `frida -U -f <PKG> -l ssl_bypass.js` | `android sslpinning disable` |
| **Ignorar verificação de raiz** | Script de gancho Java personalizado | `android root disable` |
| **Despejar strings de memória**| `frida-dump` | `memory dump all strings.txt` |
| **Listar atividades** | `frida-ps` | `android hooking list activities` |
| **Métodos de classe de rastreamento**| `frida-trace -U -j "com.pkg.Class!*"` | `android hooking watch class ...` |

---

## 🛡️ Considerações defensivas e anti-Frida

1. **Verificação de portas para Frida:** Verifique se há porta localhost aberta `27042` ou examine `/proc/self/maps` para `frida-agent.so`.
2. **Proteção Ptrace nativa:** Chame `ptrace(PTRACE_TRACEME, 0, 1, 0)` em bibliotecas C/JNI nativas para impedir a conexão de depuradores e mecanismos de instrumentação.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Frida e Objection são ferramentas especializadas de engenharia reversa e testes de penetração projetadas estritamente para avaliações de segurança autorizadas. Instrumentar aplicativos móveis de terceiros sem permissão viola os termos de serviço e as leis de propriedade intelectual.