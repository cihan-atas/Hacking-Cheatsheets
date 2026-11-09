<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```
<p align="center">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Ασφάλεια κώδικα">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 Μυστικός σαρωτής υψηλής ακρίβειας που έχει σχεδιαστεί για να αναζητά κλειδιά API, διακριτικά και ιδιωτικά πιστοποιητικά που έχουν διαρρεύσει σε Git, S3, Docker και συστήματα αρχείων με ζωντανή επικύρωση</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & The Live Verification Superpower](#-overview--the-live-verification-superpower)
- [Σύγκριση TruffleHog vs Gitleaks](#-trufflehog-vs-gitleaks-comparison)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Scanning Git Repositories](#-scanning-git-repositories)
- [Σάρωση τοπικών συστημάτων αρχείων και καταλόγων](#-scanning-local-filesystems--directories)
- [Σάρωση ολόκληρων οργανισμών GitHub](#-scanning-entire-github-organizations)
- [Σάρωση S3 Buckets & Docker Images](#-scanning-s3-buckets--docker-images)
- [Φιλτράρισμα ψευδών θετικών (`--only-verified`)](#-filtering-false-positives---only-verified)
- [Έξοδος JSON & Ενσωμάτωση CI/CD](#-json-output--cicd-integration)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & The Live Verification Superpower

Οι προγραμματιστές συχνά δεσμεύουν διαπιστευτήρια υψηλού προνομίου σε αποθετήρια Git (κλειδιά πρόσβασης AWS, ιδιωτικά κλειδιά Stripe, Slack webhooks, ιδιωτικά κλειδιά SSH, κωδικοί πρόσβασης βάσης δεδομένων). Ακόμη και όταν διαγράφονται σε επόμενες δεσμεύσεις, **αυτά τα μυστικά παραμένουν μόνιμα θαμμένα στο ιστορικό δεσμεύσεων του αποθετηρίου**.

Γράφτηκε στο Go από την **Truffle Security**, οι σαρώσεις **TruffleHog** δεσμεύουν ιστορικά, συστήματα αρχείων και αποθήκευση cloud για να αποκαλύψουν μυστικά που διέρρευσαν.

Η καθοριστική υπερδύναμή του είναι η **Ζωντανή επαλήθευση**:
Σε αντίθεση με τους παραδοσιακούς σαρωτές regex που πλημμυρίζουν ομάδες ασφαλείας με χιλιάδες ψευδώς θετικά (π.χ., παραδείγματα κλειδιών τεκμηρίωσης), το TruffleHog επικοινωνεί με τον αντίστοιχο πάροχο cloud (AWS, GitHub, Slack, OpenAI) για να **επαληθεύσει εάν το εξαγόμενο μυστικό είναι ενεργά λειτουργικό**.

---

## ⚖️ Σύγκριση TruffleHog εναντίον Gitleaks

| Χαρακτηριστικό | Τρούφα | Gitleaks |
|---------|------------|-----------|
| **Ζωντανή επαλήθευση διαπιστευτηρίων** | ✅ Ναι (750+ ανιχνευτές δοκιμάστηκαν ζωντανά)| ❌ Όχι (Regex μόνο) |
| **Πηγές δεδομένων στόχου** | Git, GitHub, S3, Docker, Σύστημα αρχείων| Git, τοπικά αρχεία |
| **Ταχύτητα σε τεράστια Repos** | Γρήγορη (Μετάβαση σε πολλαπλά νήματα) | Εξαιρετικά Γρήγορο (Ελάχιστο regex) |
| **Λάθος θετικό ποσοστό** | Κοντά στο μηδέν (με `--only-verified`) | Μέτρια (Απαιτούνται λίστες επιτρεπόμενων)|
| **Pre-Commit Hook Friendly** | ✅ Υποστηρίζεται | ✅ Εξαιρετικά βελτιστοποιημένο |

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux (via curl installer)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Container
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/target/repo
```
---

## 🚀 Σάρωση αποθετηρίων Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Σάρωση τοπικών συστημάτων αρχείων και καταλόγων

Σαρώστε τοπικούς καταλόγους, εξαγόμενα αρχεία αντιγράφων ασφαλείας ή αποευρετηριασμένες ρίζες ιστού:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Σάρωση ολόκληρων οργανισμών GitHub

Ελέγξτε όλα τα δημόσια αποθετήρια που ανήκουν σε έναν οργανισμό:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Σάρωση S3 Buckets & Docker Images
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Φιλτράρισμα ψευδών θετικών (`--only-verified`)

Καταργήστε όλες τις μη επαληθευμένες επισκέψεις regex και εμφανίστε **μόνο επιβεβαιωμένα ενεργά διαπιστευτήρια**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 Έξοδος JSON & Ενσωμάτωση CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Σάρωση Remote Git Repo** | `trufflehog git <GIT_URL>` |
| **Σάρωση μόνο επαληθευμένων μυστικών** | `trufflehog git <GIT_URL> --only-verified` |
| **Σάρωση τοπικού συστήματος αρχείων** | `trufflehog filesystem <DIRECTORY>` |
| **Σάρωση του οργανισμού GitHub** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Σάρωση εικόνας Docker** | `trufflehog docker --image=<IMAGE>` |
| **Κάδος S3 S3** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Εξαγωγή σε JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Αμυντικές Θεωρήσεις & OPSEC

1. **Εξερχόμενη επισκεψιμότητα API επαλήθευσης:** Όταν εκτελείται χωρίς `--no-verification`, το TruffleHog συνδέεται με τερματικά σημεία τρίτων (Slack, AWS, Stripe) για να δοκιμάσει τα κλειδιά. Αυτό ενεργοποιεί αρχεία καταγραφής ελέγχου ταυτότητας στον μισθωτή cloud του θύματος! Εάν το stealth είναι πρωταρχικής σημασίας, απενεργοποιήστε τη ζωντανή επικύρωση:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Ανάκληση κλειδιού και απόκριση περιστατικού:** Όταν ανακαλυφθούν έγκυρα μυστικά σε δημόσια αποθετήρια, περιστρέψτε αμέσως τα διαπιστευτήρια και ελέγξτε τα αρχεία καταγραφής CloudTrail / ελέγχου για μη εξουσιοδοτημένη πρόσβαση.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Αυτό το cheatsheet προορίζεται για εξουσιοδοτημένους ελέγχους ασφαλείας, λειτουργίες Red Team και αξιολογήσεις διαπιστευτηρίων υγιεινής. Η χρήση διαπιστευτηρίων που ανακαλύφθηκαν για πρόσβαση σε μη εξουσιοδοτημένους λογαριασμούς ή ιδιωτικά περιβάλλοντα cloud είναι αυστηρά παράνομη.