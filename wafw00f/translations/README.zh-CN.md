<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Cheatsheet

[wafw00f](https://github.com/EnableSecurity/wafw00f) 是一个开源命令行 Web 应用程序防火墙 (WAF) 指纹识别和检测工具。它通过发送正常和挑衅性 HTTP 请求以及分析行为响应、标头和 cookie 来识别和识别 150 多个商业和开源 WAF 安全解决方案，从而保护目标网站。

---

## 目录

- [为什么使用指纹 WAF？](#why-fingerprint-wafs)
- [安装](#安装)
- [基本和目标扫描](#basic--target-scanning)
- [详尽的多 WAF 检测 (`-a`)](#exhaustive-multi-waf-detection--a)
- [从文件批量目标扫描 (`-i`)](#bulk-target-scanning-from-file--i)
- [代理和 Tor 路由 (`-p`)](#proxy--tor-routing--p)
- [导出格式（JSON、CSV、文本）](#export-formats-json-csv-text)
- [列出支持的 WAF 签名 (`-l`)](#list-supported-waf-signatures--l)
- [Bug 赏金和渗透测试集成](#bug-bounty--penetration-testing-integration)

---

## Why Fingerprint WAFs?

在没有事先侦察的情况下对受保护目标发起主动 Web 攻击（SQL 注入、XSS、SSRF）通常会导致即时 IP 禁止或漏报漏洞结果。识别确切的 WAF 可以：
- **定制规避：** 选择特定于供应商的绕过技术（例如，ModSecurity 的 Unicode 规范化、Cloudflare 的分块传输编码）。
- **Origin IP Hunting:** If Cloudflare or Akamai is detected, pivoting to find the backend origin IP before testing.
- **Rule Threshold Awareness:** Tuning scanner concurrency and rate limits below blocking thresholds.

 - -

＃＃ 安装
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## 基本和目标扫描

### 1. 扫描单个域
```bash
wafw00f https://example.com
```
*wafw00f 发送基线 HTTP 请求，然后发送定制的恶意模式，以触发安全过滤器，识别标头、状态代码和错误页面中的签名。*

### 2. 详细输出模式 (`-v` / `-vv`)
检查特定的匹配标头、HTTP 状态代码和启发式：
```bash
wafw00f https://example.com -v
```
### 3. 禁用重定向跟随 (`-r`)
防止 wafw00f 遵循 301/302 重定向以将边缘反向代理与后端应用程序分开：
```bash
wafw00f https://example.com -r
```
---

## 详尽的多 WAF 检测 (`-a`)

默认情况下，一旦确认第一个 WAF 匹配，wafw00f 就会停止扫描。使用 `-a` 检查所有已知的供应商签名（当应用程序位于 Cloudflare CDN + AWS WAF 等多层防御后面时至关重要）：
```bash
wafw00f https://example.com -a
```
---

## 从文件批量目标扫描 (`-i`)

扫描从 `subfinder` 或 `amass` 收集的子域或目标资产的完整列表：
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## 代理和 Tor 路由 (`-p`)

通过代理路由探测流量，以防止 IP 列入黑名单或检查 Burp Suite 中的查询：

### 1.通过 Burp Suite 代理进行路由
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. 通过本地 Tor SOCKS5 代理进行路由
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## 导出格式（JSON、CSV、文本）

导出自动报告或下游侦察工具的结果：

### 1.导出为JSON格式
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. 导出到 CSV 电子表格
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## 列出支持的 WAF 签名 (`-l`)

查看所有 150 多个可检测的 WAF 产品：
```bash
wafw00f -l
```
主要支持的产品包括：
- **云提供商：** Cloudflare、AWS WAF、Akamai、Microsoft Azure Web 应用程序防火墙、Google Cloud Armor。
- **企业设备：** F5 BIG-IP ASM、Imperva SecureSphere、Barracuda、Fortinet FortiWeb、Citrix NetScaler。
- **开源/软件：** ModSecurity、NAXSI、Shadow Daemon、OpenResty。

---

## 错误赏金和渗透测试集成

将 wafw00f 集成到自动化 Web 攻击管道中：
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```