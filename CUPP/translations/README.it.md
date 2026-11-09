<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Foglio informativo CUPP (Common User Passwords Profiler).

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) è un generatore di dizionari di password specializzato progettato per la profilazione umana mirata. Invece di usare la forza bruta con dizionari generici, CUPP genera permutazioni basate su dettagli personali (nome di destinazione, partner, animali domestici, date di nascita, compagnia, hobby, variazioni di linguaggio e combinazioni di caratteri speciali).

---

## Sommario

- [Panoramica e casi d'uso](#panoramica--casi d'uso)
- [Installazione](#installazione)
- [Procedura guidata di profilazione interattiva (`-i`)](#profiling-procedura-guidata--i)
- [Configurazione e regole Leet (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Scaricamento e unione di dizionari (`-l` e `-w`)](#downloading--merging-dictionaries--l---w)
- [Convogliamento dell'output a Hashcat e Hydra](#piping-output-to-Hashcat--hydra)
- [Linee guida per gli approfondimenti difensivi e le norme sulle password](#defensive-insights--linee guida-politiche per le password)

---

## Panoramica e casi d'uso

Gli attacchi con dizionario tradizionali spesso falliscono contro password complesse che seguono modelli prevedibili dall'uomo (ad esempio, `John1984!`, `Sarah_Fluffy2022#`). CUPP affronta questo problema:
- Raccolta di informazioni personali specifiche per target.
- Combinazione di nomi, anni di nascita, date significative e nomi di animali domestici.
- Applicazione di mutazioni basate su regole (maiuscole, sostituzione di parole, caratteri speciali, aggiunta di numeri casuali).
- Produzione di un elenco di parole ottimizzato e mirato, notevolmente più piccolo di `rockyou.txt` ma con una maggiore rilevanza per il target.

---

## Installazione
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

## Procedura guidata di profilazione interattiva (`-i`)

Avvia la richiesta del questionario per creare un elenco di parole personalizzato:
```bash
python3 cupp.py -i
```
Esempio di procedura dettagliata:
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
Il file di output `alice.csv` (o il file di testo specificato) viene generato con migliaia di mutazioni personalizzate (ad esempio, `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Configurazione e regole di locazione (`cupp.cfg`)

La configurazione CUPP è controllata tramite `cupp.cfg`. Le sezioni chiave includono:
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
Modifica `cupp.cfg` per adattarlo alle impostazioni locali e alle specifiche della lingua di destinazione.

---

## Download e unione di dizionari (`-l` e `-w`)

### 1. Scarica elenchi di parole precompilate dai repository
Scarica elenchi di parole linguistici predefiniti:
```bash
python3 cupp.py -l
```
### 2. Migliora una lista di parole esistente con Leet / Regole speciali
Applica le regole di mutazione CUPP a un elenco di parole esistenti:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Invia l'output a Hashcat e Hydra

Inserisci il dizionario personalizzato generato direttamente negli strumenti a forza bruta:

### Cracking degli hash target con Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Autenticazione online Brute-Force con Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Approfondimenti difensivi e linee guida sulla politica delle password

- **Banna attributi personali:** vieta le password utente contenenti nomi, cognomi, nomi utente, nomi di marchi aziendali o date di nascita dei dipendenti.
- **Imposta la lunghezza rispetto alla complessità:** una passphrase di oltre 16 caratteri (ad esempio, quattro parole casuali del dizionario) è significativamente più resistente agli strumenti di profilazione rispetto a un modello `NameYear!` di 8 caratteri.
- **Multi-Factor Authentication (MFA):** applica token FIDO2 o TOTP hardware in modo che la sola generazione di dizionari personalizzati non possa compromettere gli account.