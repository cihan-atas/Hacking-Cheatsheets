<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Trivy Cheatsheet

Το [Trivy](https://github.com/aquasecurity/trivy) της Aqua Security είναι ένας ολοκληρωμένος σαρωτής ασφαλείας πολλαπλών χρήσεων για κοντέινερ, συστήματα αρχείων, αποθετήρια Git, εικόνες εικονικής μηχανής, συμπλέγματα Kubernetes και διαμορφώσεις Infrastructure as Code (IaC). Ανιχνεύει ευπάθειες λειτουργικού συστήματος και γλώσσας (CVE), μυστικά που διέρρευσαν, εσφαλμένες διαμορφώσεις και δημιουργεί Λογισμικό Bill of Materials (SBOM).

---

## Πίνακας περιεχομένων

- [Core Capabilities](#core-capabilities)
- [Εγκατάσταση](#installation)
- [Σάρωση εικόνας κοντέινερ (`trivy image`)](#container-image-scanning-trivy-image)
- [Filesystem & Repository Scanning (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Secret & Credential Detection (`--scanners secret`)](#secret--credential-detection---scanners-secret)
- [Kubernetes Cluster Auditing (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Σάρωση εσφαλμένης διαμόρφωσης υποδομής ως κώδικα (IaC)](#infrastructure-as-code-iac-misconfiguration-scanning)
- [CI/CD Integration & Exit Codes](#cicd-integration--exit-codes)
- [SBOM Generation (CycloneDX & SPDX)](#sbom-generation-cyclonedx--spdx)

---

## Βασικές Δυνατότητες

- **Ευπάθειες:** Ανιχνεύει CVE σε πακέτα Alpine, Debian, RHEL, Ubuntu, Amazon Linux και γλώσσας (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Λανθασμένες ρυθμίσεις παραμέτρων:** Ελέγχει τα γραφήματα Dockerfiles, δηλώσεων Kubernetes, Terraform, CloudFormation και Helm σε σχέση με δείκτες αναφοράς CIS.
- **Μυστική σάρωση:** Επισημαίνει εκτεθειμένα κλειδιά API, ιδιωτικά πιστοποιητικά, διακριτικά JWT και κωδικούς πρόσβασης.
- **Σάρωση συμπλέγματος:** Ελέγχει τους φόρτους εργασίας του Kubernetes και τα επίπεδα ελέγχου ζωντανά.
- **Γρήγορη και ελαφριά:** Η τοπική βάση δεδομένων SQLite ενημερώθηκε με διαφάνεια χωρίς απαιτήσεις εξωτερικού δαίμονα.

---

## Εγκατάσταση
```bash
# Debian / Ubuntu
sudo apt-get install wget apt-transport-https gnupg
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | gpg --dearmor | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null
echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb generic main" | sudo tee -a /etc/apt/sources.list.d/trivy.list
sudo apt-get update && sudo apt-get install -y trivy

# Arch Linux
sudo pacman -S trivy

# Homebrew (macOS / Linux)
brew install trivy

# Docker container
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image alpine:latest
```
---

## Σάρωση εικόνων κοντέινερ (`trivy image`)

### 1. Βασική σάρωση εικόνας
Σαρώστε μια τοπική ή απομακρυσμένη εικόνα Docker:
```bash
trivy image nginx:latest
```
### 2. Φιλτράρισμα κατά επίπεδο σοβαρότητας
Εστίαση μόνο σε κρίσιμα και υψηλά τρωτά σημεία με δυνατότητα δράσης:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Εμφάνιση μόνο ευπαθών σημείων που μπορούν να διορθωθούν
Φιλτράρετε μη επιδιορθωμένα ζητήματα μηδενικών ημερών ή μη επιδιορθώσιμα ανοδικά ζητήματα:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Σάρωση αποθηκευμένου αρχείου πίσσας
Σάρωση εικόνων κοντέινερ εκτός σύνδεσης που εξάγονται μέσω `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Σάρωση συστήματος αρχείων και αποθετηρίου (`trivy fs / repo`)

Σάρωση δέντρων πηγαίου κώδικα, αρχεία κλειδώματος εξάρτησης (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Σάρωση τοπικού καταλόγου / έργου
```bash
trivy fs /path/to/project
```
### 2. Σαρώστε απευθείας το Remote Git Repository
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Ανίχνευση μυστικού και διαπιστευτηρίων (`--scanners secret`)

Εντοπίστε κλειδιά API με σκληρό κώδικα, διακριτικά και ιδιωτικά κλειδιά ενσωματωμένα σε επίπεδα κώδικα ή κοντέινερ:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Έλεγχος συμπλέγματος Kubernetes (`trivy k8s`)

Ελέγξτε τους ενεργούς φόρτους εργασίας και τις ρυθμίσεις παραμέτρων σε ένα ζωντανό σύμπλεγμα Kubernetes χρησιμοποιώντας το ενεργό περιβάλλον `kubectl`:

### 1. Έλεγχος ολόκληρου του φόρτου εργασίας συμπλέγματος
```bash
trivy k8s --report summary cluster
```
### 2. Ελέγξτε έναν συγκεκριμένο χώρο ονομάτων
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. Συμμόρφωση σημείων αναφοράς CIS Kubernetes
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Σάρωση εσφαλμένης διαμόρφωσης υποδομής ως κώδικα (IaC).

Εντοπίστε επικίνδυνες διαμορφώσεις στα Dockerfiles, Terraform και Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## Κωδικοί ενσωμάτωσης & εξόδου CI/CD

Αυτοματοποιήστε τις πύλες αγωγών για να εμποδίσετε την αποστολή ευάλωτων κατασκευών:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## SBOM Generation (CycloneDX & SPDX)

Δημιουργία Λογισμικού Bill of Materials (SBOM) για έλεγχο συμμόρφωσης και εφοδιαστικής αλυσίδας:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```