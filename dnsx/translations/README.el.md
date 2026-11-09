<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Άδεια MIT">
</p>

<p align="center">
  <b>🌐 Ανάλυση DNS υψηλής απόδοσης, φιλτράρισμα χαρακτήρων μπαλαντέρ, αντίστροφες αναζητήσεις DNS και μηχανή εξαγωγής πολλαπλών ερωτημάτων</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Γιατί dnsx; (Χειρισμός χαρακτήρων μπαλαντέρ & ταχύτητα)](#-why-dnsx-wildcard-handling--speed)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Basic Subdomain Probing & Resolution](#-basic-subdomain-probing--resolution)
- [Φιλτράρισμα DNS με μπαλαντέρ (`-wd`)](#-μπαλαντέρ-dns-φιλτράρισμα---wd)
- [Querying Specific Record Types DNS](#-querying-specific-dns-record-types)
- [Αντίστροφες αναζητήσεις DNS (`-ptr`)](#-reverse-dns-lookups---ptr)
- [Using Custom Resolvers & Rate Limiting](#-using-custom-resolvers--rate-limiting)
- [Αυτοματισμός Pipeline (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Σχεδιασμένο στο Go από **ProjectDiscovery**, το **dnsx** είναι ένα γρήγορο και πολλαπλών χρήσεων εργαλειοθήκη DNS σχεδιασμένο για αναγνώριση, κυνήγι επικηρυγμένων σφαλμάτων και ανακάλυψη δικτύου.

Ενώ εργαλεία όπως τα `dig`, `nslookup` και `host` επιλύουν τις εγγραφές μία προς μία, το `dnsx` επεξεργάζεται χιλιάδες ερωτήματα ασύγχρονα, επαναλαμβάνει αυτόματα αποτυχημένες λύσεις επίλυσης, εξάγει καθαρές IP απόκρισης και εξαλείφει τον θόρυβο χαρακτήρων μπαλαντέρ που μολύνει την απαρίθμηση υποτομέων.

---

## ⚡ Γιατί dnsx; (Χειρισμός μπαλαντέρ και ταχύτητα)

Κατά την αναγνώριση σφαλμάτων bounty, τα παθητικά εργαλεία (`subfinder`, `amass`) συχνά επιστρέφουν χιλιάδες νεκρούς υποτομείς ή υποτομείς μπαλαντέρ. Η δοκιμή τελικών σημείων HTTP σε νεκρές καταχωρήσεις DNS σπαταλά τεράστιο εύρος ζώνης.

Το `dnsx` το λύνει με:
1. **Φιλτράρισμα χαρακτήρων μπαλαντέρ (`-wd`):** Ανιχνεύει εάν το `*.domain.com` επιλύει σε μια ολοκληρωμένη IP και φιλτράρει τους υποτομείς φανταστικούς.
2. **Ταχύτητα & Ταυτόχρονη:** Επιλύει 10.000+ τομείς ανά λεπτό σε δημόσιες και ιδιωτικές ομάδες επίλυσης.
3. **Καθαρισμός σωληνώσεων εξόδου:** Τροφοδοτείται απευθείας στο `httpx` ή στο `nmap`.

---

## 📥 Εγκατάσταση
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Βασική διερεύνηση και ανάλυση υποτομέα
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 Φιλτράρισμα DNS με χαρακτήρες μπαλαντέρ (`-wd`)

Οι χαρακτήρες μπαλαντέρ επιστρέφουν μια έγκυρη IP για ανύπαρκτους υποτομείς (`random123xyz.target.com`), σύνολα δεδομένων αναγνώρισης δηλητηρίασης.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Ερώτημα για συγκεκριμένους τύπους εγγραφής DNS

Ζητήστε συγκεκριμένους τύπους καρτελών σε χιλιάδες τομείς:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Αντίστροφες αναζητήσεις DNS (`-ptr`)

Ανακαλύψτε την ενεργή υποδομή και τους εικονικούς κεντρικούς υπολογιστές πραγματοποιώντας αναζητήσεις PTR σε μια περιοχή IP:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Χρήση προσαρμοσμένων επιλύσεων και περιορισμού τιμών

Αποφύγετε τον περιορισμό του ρυθμού από τον ISP ή το Google DNS, χρησιμοποιώντας μια ομάδα επιμελητών επιλύσεων υψηλής ταχύτητας:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Αυτοματισμός αγωγών (Subfinder + dnsx + httpx)

Η απόλυτη ροή εργασιών ανανέωσης για κυνηγούς επικηρυγμένων σφαλμάτων και δοκιμαστές διείσδυσης:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Επίλυση υποτομέων** | `dnsx -l subdomains.txt` |
| **Εξαγωγή μόνο διευθύνσεων IP** | `dnsx -l subdomains.txt -resp-only` |
| **Φιλτράρισμα τομέων μπαλαντέρ** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Εξαγωγή CNAME (Ανάληψη)** | `dnsx -l subdomains.txt -cname -resp` |
| **Ερώτημα όλων των εγγραφών (Επανάληψη)** | `dnsx -l domains.txt -recon` |
| **Αντίστροφη αναζήτηση DNS (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Custom Resolvers & Threads**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **Έξοδος JSON** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Πλημμύρα DNS:** Η ενεργοποίηση δεκάδων χιλιάδων ερωτημάτων DNS μέσω εταιρικών εσωτερικών διακομιστών DNS θα αυξήσει τα φορτία της CPU και θα ενεργοποιήσει ειδοποιήσεις SIEM για διοχέτευση σήραγγας DNS/DDoS.
2. **Δηλητηρίαση επίλυσης:** Χρησιμοποιείτε πάντα επαληθευμένες, αξιόπιστες λίστες επίλυσης για να αποτρέψετε κακόβουλους επιλύτες να υποκλέψουν ερωτήματα.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται μόνο για εξουσιοδοτημένες δοκιμές διείσδυσης και δεσμεύσεις bounty bug. Η διεξαγωγή ανιχνευτών DNS μεγάλου όγκου έναντι μη εξουσιοδοτημένης υποδομής απαγορεύεται αυστηρά.