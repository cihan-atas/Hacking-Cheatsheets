<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 神探夏洛克备忘单

[Sherlock](https://github.com/sherlock-project/sherlock) 是一种 OSINT 侦察工具，可通过用户名同时在 400 多个社交平台、论坛、代码托管网站和在线社区中搜索社交媒体帐户。

---

## 目录

- [关键功能](#key-capability)
- [安装](#安装)
- [单个和多个用户名搜索](#single--multiple-username-search)
- [过滤网站和平台](#filtering-sites--platforms)
- [代理和 Tor 路由](#proxy--tor-routing)
- [超时和线程调整](#timeout--threading-tuning)
- [导出格式（CSV、XLSX、JSON）](#export-formats-csv-xlsx-json)
- [自动化和 OSINT 管道集成](#automation--osint-pipeline-integration)

---

## 关键能力

- **400 多个平台：** 查询 GitHub、Twitter/X、Instagram、Reddit、TikTok、LinkedIn、Telegram、Discord、Steam、Spotify、Docker Hub、Pastebin 等数百个。
- **异步并发：** 使用异步Python请求进行高速并行扫描。
- **准确检测：** 检测 HTTP 状态代码、错误消息和重定向以避免误报。
- **Tor 和 SOCKS5 支持：** 保护调查员匿名并绕过速率限制。

 - -

＃＃ 安装
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## 单个和多个用户名搜索

### 1. 基本用户名搜索
在所有 400 多个网站中搜索单个目标句柄：
```bash
sherlock target_user
```
### 2.同时搜索多个用户名
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. 仅打印找到的帐户（抑制未找到/错误）
```bash
sherlock --print-found target_user
```
---

## 过滤网站和平台

将搜索范围缩小到特定平台以节省时间或专注于特定情报类别：

### 1. 仅查询特定站点
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. 无需特定平台进行搜索（忽略误报较多的网站）
```bash
sherlock target_user --site-list targets.txt
```
---

## 代理和 Tor 路由

为了防止 IP 禁止、速率限制并维护操作安全：

### 1. 通过 Tor 网络的路由
通过本地 Tor SOCKS 代理 (`127.0.0.1:9050`) 自动路由请求：
```bash
sherlock --tor target_user
```
### 2.自定义HTTP/SOCKS5代理
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## 超时和线程调优

控制请求速度和延迟容限：
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## 导出格式（CSV、XLSX、JSON）

保存发现用于调查、证据包或报告：

### 1. 直接保存到 CSV 文件
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. 将输出保存到特定目录
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
生成包含已确认配置文件的完整 URL 的 `/tmp/recon_data/target_user.txt`。

### 3.另存为 Excel 电子表格 (XLSX)
```bash
sherlock --xlsx target_user
```
---

## 自动化和 OSINT 管道集成

将 Sherlock 链接到自动调查员脚本中：
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```