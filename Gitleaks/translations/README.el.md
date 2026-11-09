<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```
<p align="center">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Secrets">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p align="center">
  >
</p>

---

## 📋 Πίνακας περιεχομένων

- [Μηχανή Επισκόπησης & Ανίχνευσης](#-επισκόπηση--μηχανή ανίχνευσης)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Βασική μυστική σάρωση] (#-βασική-μυστική σάρωση)
  - [Scanning a Git Repository](#scanning-a-git-repository)
  - [Σάρωση μη δεσμευμένων αλλαγών](#scanning-uncommitted-changes)
  - [Σάρωση απλών αρχείων και φακέλων](#scanning-plain-files--folders)
- [Targeting Commit Ranges & History](#-targeting-commit-ranges--history)
- [Ολοκλήρωση CI/CD & Pre-commit](#-cicd--pre-commit-integration)
- [Custom Detection Rules & Allowlisting](#-custom-detection-rules--allowlisting)
- [Μορφές εξόδου και αναφορά](#-μορφές εξόδου--αναφορές)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Μηχανή επισκόπησης και ανίχνευσης

Τα σκληρά κωδικοποιημένα μυστικά (κλειδιά πρόσβασης AWS, ιδιωτικά κλειδιά SSH, διακριτικά GitHub, Slack webhook, διαπιστευτήρια βάσης δεδομένων) που δεσμεύονται ακούσια σε αποθετήρια git είναι ένα από τα πιο συνηθισμένα διανύσματα για αρχικό συμβιβασμό στο cloud.

Δημιουργήθηκε από τον **Zachary Rice (@zricethezav)**, το **Gitleaks** παρέχει εξαιρετικά γρήγορη ανάλυση εντροπίας Shannon και βασισμένη σε regex σε:
- **Πλήρης ιστορία Git:** Ελέγχει κάθε κλάδο, ετικέτα και ιστορική δέσμευση.
- **Pre-commit Hooks:** Αποκλείει τους μηχανικούς από την κατά λάθος δέσμευση διαπιστευτηρίων πριν φτάσει ο κώδικας σε απομακρυσμένους διακομιστές.
- **Σωληνώσεις CI/CD:** Αποτυγχάνει αυτόματα τα αιτήματα έλξης που περιέχουν διαρροές διαπιστευτηρίων.

---

## 📥 Εγκατάσταση
```bash
# macOS (Homebrew)
brew install gitleaks

# Go install
go install github.com/zricethezav/gitleaks/v8@latest

# Docker
docker pull zricethezav/gitleaks:latest

# Linux binary download
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```
---

## 🚀 Βασική μυστική σάρωση

### Σάρωση ενός αποθετηρίου Git
Ελέγχει το ιστορικό δεσμεύσεων του τρέχοντος αποθετηρίου:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Σάρωση μη δεσμευμένων αλλαγών
Έλεγχοι σταδιακών και μη σταδιακών αλλαγών κώδικα πριν από τη δέσμευση:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Σάρωση απλών αρχείων και φακέλων
Σάρωση καταλόγων που δεν είναι git (π.χ. εξαγόμενο υλικολογισμικό, απομεταγλωττισμένες εφαρμογές για κινητά, φάκελοι διαμόρφωσης):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Στόχευση εύρους δέσμευσης και ιστορίας
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ Ενσωμάτωση CI/CD & Pre-commit

### Διαμόρφωση πλαισίου προ-δέσμευσης (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### Ροή εργασίας GitHub Actions
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```
---

## 🧩 Προσαρμοσμένοι κανόνες ανίχνευσης και λίστα επιτρεπόμενων

Διαμορφώστε προσαρμοσμένα μοτίβα regex ή αγνοήστε τα ψευδώς θετικά χρησιμοποιώντας το `.gitleaks.toml`:
```toml
[extend]
useDefault = true

[allowlist]
description = "Ignore test fixtures and demo tokens"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Internal Company JWT Secret"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```
Εκτέλεση με προσαρμοσμένη διαμόρφωση:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Μορφές εξόδου και αναφορά
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση ολόκληρου του ιστορικού Git** | `gitleaks detect -v` |
| **Σάρωση μη δεσμευμένων αλλαγών** | `gitleaks protect -v` |
| **Σάρωση μόνο σταδιακών αλλαγών** | `gitleaks protect --staged -v` |
| **Σάρωση απλού καταλόγου (Χωρίς Git)**| `gitleaks dir /path/to/folder -v` |
| **Εξαγωγή σε JSON** | `gitleaks detect -r report.json -f json` |
| **Εξαγωγή σε SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Χρήση προσαρμοσμένων κανόνων Config** | `gitleaks detect -c .gitleaks.toml` |
| **Σάρωση συγκεκριμένου κλάδου** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Αποκατάσταση

1. **Άμεση ανάκληση:** Μόλις εντοπιστεί ένα μυστικό στο ιστορικό git, θεωρήστε το πλήρως παραβιασμένο και περιστρέψτε το/ανακαλέστε το αμέσως.
2. **Επανεγγραφή ιστορικού Git:** Απαιτείται η κατάργηση της δέσμευσης ή η χρήση του `git filter-repo` / BFG Repo-Cleaner για τον καθαρισμό τεχνουργημάτων, αλλά ποτέ μην αντικαθιστάτε τις επανεγγραφές ιστορικού για την περιστροφή διαπιστευτηρίων.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Gitleaks έχει σχεδιαστεί για εξουσιοδοτημένους εσωτερικούς ελέγχους ασφάλειας, συνεχή έλεγχο ευπάθειας ενσωμάτωσης και έλεγχο αμυντικού κώδικα. Η σάρωση μη εξουσιοδοτημένων ιδιόκτητων αποθετηρίων χωρίς άδεια απαγορεύεται αυστηρά.