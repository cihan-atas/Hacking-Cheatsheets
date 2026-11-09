<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# dnsrecon 备忘单

[dnsrecon](https://github.com/darkoperator/dnsrecon) 是一个用 Python 编写的综合 DNS 侦察和枚举工具。它支持区域传输 (AXFR)、反向查找、域暴力破解、SRV 记录发现、通配符检测、缓存侦听和 TLD 扩展。

---

## 目录

- [核心能力](#核心能力)
- [安装](#安装)
- [标准枚举和区域传输 (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [DNS 子域暴力破解 (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [反向查找范围扫描(`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [DNS 缓存侦听 (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [定位特定名称服务器 (`-n`)](#targeting-specific-nameservers--n)
- [输出格式（CSV、JSON、SQLite、XML）](#output-formats-csv-json-sqlite-xml)
- [DNS 服务器的防御强化](#defective-hardening-for-dns-servers)

---

## 核心能力

- **区域传输测试 (AXFR)：** 自动测试所有权威名称服务器的未经身份验证的区域传输。
- **记录枚举：** 检索 SOA、NS、A、AAAA、MX、TXT、SPF 和 SRV 记录。
- **子域暴力破解：** 带通配符过滤的多线程字典查找。
- **反向 IP 范围扫描：** 跨任意 CIDR 块提取 PTR 记录。
- **缓存监听：**测试递归名称服务器是否在内存中缓存了特定的域。
- **TLD 扩展：** 发现跨国家/地区代码和通用顶级域的匹配域。

 - -

＃＃ 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## 标准枚举和区域传输 (`-d -t std / axfr`)

### 1. 标准 DNS 记录枚举
收集 SOA、NS、A、AAAA、MX 和 TXT 记录，并自动尝试针对每个权威 NS 进行区域传输：
```bash
dnsrecon -d example.com -t std
```
### 2. 仅专用区域传输 (AXFR) 测试
仅针对所有权威域名服务器测试 AXFR 漏洞：
```bash
dnsrecon -d example.com -t axfr
```
*如果成功，目标的整个 DNS 数据库将被转储，无需暴力破解。*

---

## DNS 子域暴力破解 (`-t brt`)

使用自定义或默认单词列表暴力破解子域：

### 1. 使用自定义单词列表进行子域暴力破解
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`：目标域。
- `-t brt`：暴力模式。
- `-D <file>`：子域字典文件。

### 2. 使用通配符过滤进行暴力破解
防止通配符 DNS 配置（`*.example.com` 解析为单个包罗万象的 IP）污染结果：
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`：忽略通配符解析结果。

---

## 反向查找范围扫描（`-t rvl`）

枚举 IP 块的 PTR 记录以识别内部主机、邮件网关和暂存实例：
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`：采用 CIDR 表示法（例如 `192.168.1.0/24`）或开始结束表示法（`192.168.1.1-192.168.1.254`）的 IP 范围。
- `-t rvl`：反向查找模式。

---

## DNS 缓存侦听 (`-t snoop`)

确定目标网络上的客户端最近是否通过其内部解析器解析了特定域：
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`：要查询的目标DNS服务器。
- `-D <domains_file>`：要测试的域列表（例如竞争对手、恶意 C2、内部门户）。

---

## 针对特定名称服务器 (`-n`)

直接查询内部或特定于目标的 DNS 服务器，而不是公共上游解析器：
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## 输出格式（CSV、JSON、SQLite、XML）

保存结果以便与票务、SIEM 或进一步的攻击阶段集成：

### 1. 导出为 JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2.导出到SQLite数据库
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. 导出为 CSV 和 XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## DNS 服务器的防御强化

|漏洞|剥削风险|推荐防御|
|---|---|---|
| **无限制 AXFR** |完整的内部基础设施映射 |仅将区域传输限制到授权的辅助名称服务器 IP（BIND 中的 `allow-transfer { 10.0.0.2; };`）。 |
| **打开递归解析器** | DNS 放大 DDoS 和窥探 |禁用外部递归（BIND 中的 `recursion no;`）。 |
| **可预测的主机名** |子域名暴力破解 |对敏感的暂存环境使用随机子域。 |
| **子域接管** | CNAME 指向无人认领的 AWS/GitHub/Azure 服务 |审核悬空的 DNS CNAME 记录并立即删除未使用的映射。 |