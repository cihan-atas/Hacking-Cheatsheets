<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# CUPP（通用用户密码分析器）备忘单

[CUPP](https://github.com/Mebus/cupp)（通用用户密码分析器）是一个专门的密码字典生成器，专为有针对性的人体分析而设计。 CUPP 不是使用通用词典进行暴力破解，而是根据个人详细信息（目标姓名、伴侣、宠物、出生日期、公司、爱好、利特语变体和特殊字符组合）生成排列。

---

## 目录

- [概述和用例](#overview--use-cases)
- [安装](#安装)
- [交互式分析向导 (`-i`)](#interactive-profiling-wizard--i)
- [配置和 Leet 规则 (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [下载和合并词典（`-l` 和 `-w`）](#downloading--merging-dictionaries--l---w)
- [通过管道输出到 Hashcat 和 Hydra](#piping-output-to-hashcat--Hydra)
- [防御见解和密码策略指南](#defective-insights--password-policy-guidelines)

---

## 概述和用例

传统的字典攻击通常无法针对遵循人类可预测模式的复杂密码（例如 `John1984!`、`Sarah_Fluffy2022#`）。 CUPP 通过以下方式解决这个问题：
- 收集特定目标的个人情报。
- 结合姓名、出生年份、重要日期和宠物名。
- 应用基于规则的突变（大写、leet say 替换、特殊字符、随机数附加）。
- 生成一个优化的、有针对性的单词列表，比 `rockyou.txt` 小得多，但目标相关性更高。

 - -

＃＃ 安装
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## 交互式分析向导 (`-i`)

启动问卷提示以构建个性化词汇表：
```bash
python3 cupp.py -i
```
提示演练示例：
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
输出文件 `alice.csv`（或指定的文本文件）是由数千个自定义突变（例如 `Alice1992!`、`LunaCharlie2018`、`CyberCorp@2022`）生成的。

---

## 配置和 Leet 规则 (`cupp.cfg`)

CUPP 配置通过 `cupp.cfg` 控制。关键部分包括：
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
编辑 `cupp.cfg` 以适应目标区域设置和语言细节。

---

## 下载和合并词典（`-l` 和 `-w`）

### 1.从存储库下载预编译的单词表
下载默认语言词汇表：
```bash
python3 cupp.py -l
```
### 2. 使用 Leet / 特殊规则改进现有单词表
将 CUPP 变异规则应用于现有单词列表：
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## 通过管道输出到 Hashcat 和 Hydra

将生成的自定义字典直接输入到暴力工具中：

### 使用 Hashcat 破解目标哈希值
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### 使用 Hydra 进行在线身份验证暴力破解
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## 防御见解和密码策略指南

- **禁止个人属性：** 禁止用户密码包含员工的名字、姓氏、用户名、公司品牌名称或出生日期。
- **强制长度超过复杂性：** 16 个以上字符的密码短语（例如，四个随机字典单词）比 8 个字符的 `NameYear!` 模式更能抵抗分析工具。
- **多重身份验证 (MFA)：** 强制执行硬件 FIDO2 或 TOTP 令牌，因此仅自定义字典生成不会危及帐户。