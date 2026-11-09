<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# theHarvester 备忘单

[theHarvester](https://github.com/laramies/theHarvester) 是最广泛使用的开源情报 (OSINT) 工具之一，用于从不同的公共来源（搜索引擎、PGP 密钥服务器、Shodan、Censys、Certspotter 等）收集电子邮件、子域、员工姓名、虚拟主机、开放端口和员工 PGP 密钥。

---

## 目录

- [支持的数据源](#supported-data-sources)
- [安装和 API 密钥配置](#installation--api-key-configuration)
- [基本域侦察](#basic-domain-reconnaissance)
- [被动多源收获](#passive-multi-source-harvesting)
- [主动 DNS 验证和暴力破解](#active-dns-verification--brute-forcing)
- [输出格式（HTML、XML、JSON）](#output-formats-html-xml-json)
- [Pentest 工作流程集成](#pentest-workflow-integration)

---

## 支持的数据源

theHarvester 汇集了来自数十个被动和主动来源的情报：
- **搜索引擎（免费/无密钥）：** Google、Bing、DuckDuckGo、Yahoo、Baidu。
- **证书透明度：** CRT.sh、Certspotter。
- **代码和威胁存储库：** GitHub、AlienVault OTX、ThreatCrowd、Rapid7、VirusTotal。
- **侦察和 OSINT API：** Shodan、Censys、Hunter.io、SecurityTrails、Intelx、FullContact。
- **网络/DNS：** Anubis、HackerTarget、DNSDumpster。

---

## 安装和 API 密钥配置

### 1.安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2.配置API密钥(`api-keys.yaml`)
许多来源（Hunter、Shodan、Censys、Intelx、GitHub）需要免费或付费的 API 密钥。
在配置路径中创建或编辑`api-keys.yaml`：
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## 基本域侦察

### 1. 使用免费引擎收集电子邮件和子域名
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`：要侦察的目标域。
- `-l <limit>`：每个引擎查询的结果数。
- `-b <sources>`：以逗号分隔的源列表（或 `all`）。

### 2.查询所有配置的来源
```bash
theHarvester -d example.com -l 500 -b all
```
---

## 被动多源采集

查询特定威胁情报和证书透明度数据库，而不向目标发送流量：

### 1. 证书透明度和威胁英特尔
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. 针对特定源组
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## 主动 DNS 验证和暴力破解

### 1. 解析发现的子域 (`-r`)
执行主动 DNS 查找以验证收集的子域当前是否有效：
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. DNS 暴力破解 (`-c`)
直接在 theHarvester 中执行基于单词列表的 DNS 子域暴力破解：
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3.虚拟主机验证（`-v`）
通过 HTTP 请求验证虚拟主机：
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## 输出格式（HTML、XML、JSON）

导出结果以进行自动报告或下游侦察：

### 1. 导出为 HTML 和 XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
创建 `/tmp/theharvester_results.html` 和 `/tmp/theharvester_results.xml`。

### 2. 使用 `jq` 解析发现的子域
如果导出或解析 JSON：
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## 渗透测试工作流程集成

将 theHarvester 结果直接与主动漏洞扫描程序链接起来：
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```