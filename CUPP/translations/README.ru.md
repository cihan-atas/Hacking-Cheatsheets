<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# CUPP (Профилировщик паролей обычных пользователей) Шпаргалка

[CUPP](https://github.com/Mebus/cupp) (Профилировщик паролей обычных пользователей) — это специализированный генератор словаря паролей, предназначенный для целевого профилирования людей. Вместо того, чтобы грубо использовать общие словари, CUPP генерирует перестановки на основе личных данных (имя цели, партнер, домашние животные, даты рождения, компания, хобби, варианты скороговорки и специальные комбинации символов).

---

## Содержание

- [Обзор и варианты использования](#overview--use-cases)
- [Установка](#установка)
- [Мастер интерактивного профилирования (`-i`)](#interactive-profiling-wizard--i)
- [Конфигурация и правила Leet (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Загрузка и объединение словарей (`-l` и `-w`)](#downloading--merging-dictionaries--l---w)
- [Вывод по конвейеру в Hashcat и Hydra](#piping-output-to-hashcat--hydra)
- [Рекомендации по защитной аналитике и политике паролей](#defensive-insights--password-policy-guidelines)

---

## Обзор и примеры использования

Традиционные атаки по словарю часто терпят неудачу против сложных паролей, которые следуют шаблонам, предсказуемым для человека (например, `John1984!`, `Sarah_Fluffy2022#`). CUPP решает эту проблему следующим образом:
- Сбор целевой личной разведывательной информации.
- Объединение имен, лет рождения, значимых дат и имен домашних животных.
- Применение мутаций на основе правил (заглавные буквы, замена буквенного алфавита, специальные символы, добавление случайных чисел).
- Создание оптимизированного целевого списка слов, значительно меньшего, чем `rockyou.txt`, но с более высокой целевой релевантностью.

---

## Установка
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

## Мастер интерактивного профилирования (`-i`)

Запустите анкету для создания персонализированного списка слов:
```bash
python3 cupp.py -i
```
Пример подсказки:
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
Выходной файл `alice.csv` (или указанный текстовый файл) генерируется с тысячами настраиваемых мутаций (например, `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Правила конфигурации и обучения (`cupp.cfg`)

Конфигурация CUPP контролируется через `cupp.cfg`. Ключевые разделы включают в себя:
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
Отредактируйте `cupp.cfg`, чтобы настроить его в соответствии с целевой локалью и языковыми особенностями.

---

## Загрузка и объединение словарей (`-l` и `-w`)

### 1. Загрузите предварительно скомпилированные списки слов из репозиториев.
Загрузите лингвистические списки слов по умолчанию:
```bash
python3 cupp.py -l
```
### 2. Улучшите существующий список слов с помощью Leet/специальных правил
Примените правила мутации CUPP к списку существующих слов:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Передача вывода в Hashcat и Hydra

Передайте сгенерированный настроенный словарь непосредственно в инструменты грубой силы:

### Взлом целевых хешей с помощью Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Брутфорс онлайн-аутентификации с помощью Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Защитная информация и рекомендации по политике паролей

- **Запретить личные атрибуты.** Запретить использование паролей пользователей, содержащих имена, фамилии, имена пользователей, названия компаний или даты рождения.
- **Длина важнее сложности.** Парольная фраза длиной более 16 символов (например, четыре случайных словарных слова) значительно более устойчива к инструментам профилирования, чем 8-значный шаблон `NameYear!`.
- **Многофакторная аутентификация (MFA):** применяйте аппаратные токены FIDO2 или TOTP, чтобы создание индивидуального словаря не могло поставить под угрозу учетные записи.