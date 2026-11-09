<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Ανάλυση δικτύου">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>🕶️ Ένας διαδραστικός διακομιστής υποκλοπής HTTP με δυνατότητα SSL/TLS για δοκιμαστές διείσδυσης, ερευνητές ασφαλείας και μηχανικούς λογισμικού με πλούσια διεπαφή κονσόλας και API δέσμης ενεργειών Python</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Τα τρία δυαδικά] (#-επισκόπηση--τα-τρία-δυαδικά)
- [Εγκατάσταση & Ρύθμιση πιστοποιητικού](#-εγκατάσταση--ρύθμιση πιστοποιητικού)
  - [Εγκατάσταση σε Linux / macOS](#installing-on-linux--macos)
  - [Installing CA Certificate for SSL/TLS Interception](#installing-ca-certificate-for-ssltls-interception)
- [Τρόποι λειτουργίας: Κανονικός, Διαφανής & Αντίστροφος διακομιστής μεσολάβησης](#-λειτουργία-τρόποι-κανονικό-διαφανές--αντίστροφος διακομιστής μεσολάβησης)
- [Διαδραστική πλοήγηση TUI (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Συντομεύσεις πληκτρολογίου] (#keyboard-shortcuts)
  - [Filtering & Intercepting Flows](#filtering--intercepting-flows)
- [Μη διαδραστικός αυτοματισμός (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Εγγραφή και επανάληψη κυκλοφορίας](#recording--replaying-traffic)
- [Python Scripting & Addons](#-python-scripting--addons)
  - [Αυτοματοποιημένο σενάριο χειρισμού κεφαλίδας](#automated-header-manipulation-script)
  - [Live Response Body Tampering](#live-response-body-tampering)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Three Binaries

Σε αντίθεση με τους ογκώδεις γραφικούς διακομιστές επιτραπέζιου υπολογιστή, το **mitmproxy** παρέχει αστραπιαία, δυνατότητα εγγραφής, πλήρους πιστότητας υποκλοπή HTTP/1, HTTP/2 και WebSockets απευθείας μέσα στο τερματικό ή τη σωλήνωση αυτοματισμού σας.

Αποστέλλεται με τρία δυαδικά σημεία εισόδου:
1. **`mitmproxy`:** Η διαδραστική, βασισμένη σε ncurses Terminal User Interface (TUI).
2. **`mitmdump`:** Η μη διαδραστική έκδοση γραμμής εντολών (το ισοδύναμο `tcpdump` για το HTTP).
3. **`mitmweb`:** Μια ελαφριά γραφική διεπαφή ιστού που βασίζεται σε πρόγραμμα περιήγησης.

---

## 📥 Εγκατάσταση & Ρύθμιση πιστοποιητικού

### Εγκατάσταση σε Linux / macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Εγκατάσταση πιστοποιητικού CA για υποκλοπή SSL/TLS
Για να παρακολουθήσουν την επισκεψιμότητα HTTPS, οι στόχοι πρέπει να εμπιστεύονται τη δυναμική Αρχή έκδοσης πιστοποιητικών του mitmproxy:
1. Ξεκινήστε το mitmproxy μία φορά: `mitmproxy` (δημιουργεί πιστοποιητικά στο `~/.mitmproxy/`).
2. Στη συσκευή/πρόγραμμα προγράμματος-πελάτη, διαμορφώστε τον διακομιστή μεσολάβησης `127.0.0.1:8080`.
3. Περιηγηθείτε στο `http://mitm.it` και κατεβάστε/εγκαταστήστε το πιστοποιητικό για Android, iOS, Windows ή Linux.

---

## ⚙️ Τρόποι λειτουργίας: Κανονικός, Διαφανής & Αντίστροφος διακομιστής μεσολάβησης

### Κανονική λειτουργία διακομιστή μεσολάβησης (Προεπιλογή)
Τυπική ακρόαση μεσολάβησης προώθησης στη θύρα 8080:
```bash
mitmproxy -p 8080
```
### Αντίστροφη λειτουργία διακομιστή μεσολάβησης
Αναμεταδίδει την εισερχόμενη κίνηση σε έναν συγκεκριμένο διακομιστή προορισμού (ιδανικός για δοκιμή API):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Διαφανής λειτουργία
Αναχαιτίζει την κυκλοφορία που δρομολογείται μέσω iptables χωρίς να διαμορφώνει τις ρυθμίσεις διακομιστή μεσολάβησης πελάτη:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Διαδραστική πλοήγηση TUI (`mitmproxy`)

### Συντομεύσεις πληκτρολογίου
- `?`: Ανοίξτε την επικάλυψη βοήθειας.
- `Enter`: Επιθεωρήστε την επιλεγμένη ροή HTTP (Αίτημα / Απόκριση / Λεπτομέρεια).
- `q`: Επιστρέψτε / σταματήστε.
- `d`: Διαγραφή ροής ρεύματος.
- `r`: Επανάληψη της επιλεγμένης αίτησης στον διακομιστή.
- `e`: Επεξεργασία κεφαλίδων αιτήματος/απόκρισης, διαδρομής ή σώματος.
- `a`: Συνέχιση της ροής σε παύση (κατά την αναχαίτιση).
- `A`: Συνέχιση όλων των ροών σε παύση.

### Φιλτράρισμα και παρεμπόδιση ροών
Πατήστε `i` για να εισαγάγετε μια έκφραση φίλτρου υποκλοπής:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Μη Διαδραστικός Αυτοματισμός (`mitmdump`)

### Εγγραφή & Επανάληψη κυκλοφορίας
Καταγράψτε τη ζωντανή κίνηση απευθείας σε ένα δυαδικό αρχείο ένδειξης σφαλμάτων:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Python Scripting & Addons

Το mitmproxy διαθέτει ένα εγγενές API Python για την τροποποίηση της κυκλοφορίας μέσω προγραμματισμού:

### Αυτοματοποιημένο σενάριο χειρισμού κεφαλίδας (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Τρέξτε με:
```bash
mitmproxy -s add_headers.py
```
### Παραβίαση σώματος ζωντανής απόκρισης (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Τρέξτε με:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Έναρξη διαδραστικής TUI** | `mitmproxy -p 8080` |
| **Έναρξη Web GUI** | `mitmweb -p 8080` |
| **Αθόρυβο CLI Logger** | `mitmdump -p 8080` |
| **Αντίστροφος στόχος διακομιστή μεσολάβησης** | `mitmproxy --mode reverse:https://target.com` |
| **Καταγραφή επισκεψιμότητας στο αρχείο** | `mitmdump -w capture.mitm` |
| **Φορτώστε το σενάριο Python Addon**| `mitmproxy -s script.py` |
| **Παράβλεψη κεντρικών υπολογιστών στόχων** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Καρφίτσωμα πιστοποιητικού:** Οι εφαρμογές για κινητά που επιβάλλουν το καρφίτσωμα SSL απορρίπτουν το πιστοποιητικό CA του mitmproxy. Σύζευξη με Frida / Objection για παράκαμψη ελέγχων από την πλευρά του πελάτη.
2. **Ανίχνευση διακομιστή μεσολάβησης στη χειραψία TLS:** Οι διακομιστής μεσολάβησης εισάγουν προσαρμοσμένες σουίτες κρυπτογράφησης TLS και δακτυλικά αποτυπώματα διαπραγμάτευσης ALPN που μπορούν να ανιχνεύσουν τα WAF και οι διαχειριστές ρομπότ.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το mitmproxy παρεμποδίζει και αποκρυπτογραφεί ευαίσθητες επικοινωνίες δικτύου. Πρέπει να αναπτύσσεται αυστηρά σε συστήματα και δίκτυα όπου έχει εξασφαλιστεί ρητή εξουσιοδότηση. Η υποκλοπή επικοινωνιών χωρίς συγκατάθεση είναι παράνομη.