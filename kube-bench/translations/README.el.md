<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# kube-bench Cheatsheet

Το [kube-bench](https://github.com/aquasecurity/kube-bench) είναι ένα εργαλείο ελέγχου ασφαλείας που βασίζεται στο Go που αναπτύχθηκε από την Aqua Security που ελέγχει εάν το Kubernetes αναπτύσσεται με ασφάλεια σύμφωνα με τις συστάσεις Kubernetes Benchmark του Κέντρου Ασφάλειας Διαδικτύου (CIS).

---

## Πίνακας περιεχομένων

- [Επισκόπηση CIS Kubernetes Benchmark](#cis-kubernetes-benchmark-overview)
- [Installation & Deployment Methods](#installation--deployment-methods)
- [Running on Control Plane (Master Nodes)] (#running-on-control-plane-master-nodes)
- [Running on Worker Nodes](#running-on-worker-nodes)
- [Διαχειριζόμενο Kubernetes (EKS, GKE, AKS)](#managed-kubernetes-eks-gke-aks)
- [Filtering Tests & Benchmarks](#filtering-tests--benchmarks)
- [JSON & JUnit Output for Automation](#json--junit-output-for-automation)
- [Ροή εργασιών Remediation & Hardening](#remediation--hardening-workflow)

---

## Επισκόπηση σημείων αναφοράς CIS Kubernetes

Το `kube-bench` εκτελεί δοκιμές που ορίζονται σε αρχεία YAML που αντιστοιχούν στις ενότητες του CIS Benchmark:
1. **Στοιχεία επιπέδου ελέγχου:** Διακομιστής API, Διαχείριση Ελεγκτή, Χρονοδιάγραμμα.
2. **κλπ:** Δικαιώματα αρχείων, πιστοποιητικά TLS, κρυπτογράφηση πελάτη.
3. **Διαμόρφωση επιπέδου ελέγχου:** Έλεγχος ταυτότητας, εξουσιοδότηση, RBAC, ελεγκτές εισαγωγής.
4. **Κόμβοι εργασίας:** Διαμόρφωση Kubelet, αρχεία υπηρεσίας, έλεγχος ταυτότητας TLS.
5. **Πολιτικές:** RBAC, Πρότυπα ασφαλείας Pod / Εισδοχή, Πολιτικές Δικτύου, Διαχείριση Μυστικών.

Επιστράφηκαν καταστάσεις ανά δοκιμή:
- `[PASS]`: Πληροί τα πρότυπα CIS Benchmark.
- `[FAIL]`: Αποκλίνει από τη σύσταση. απαιτεί αποκατάσταση.
- `[WARN]`: Ελέγξτε χειροκίνητα (εξαρτάται από περιβαλλοντικές μεταβλητές).
- `[INFO]`: Ενημερωτικό σημείωμα.

---

## Μέθοδοι εγκατάστασης και ανάπτυξης

### 1. Κατεβάστε το Pre-Compiled Binary
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Εκτελέστε απευθείας μέσα σε ένα Docker Container
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Αναπτύξτε ως εργασία Kubernetes
Εκτελέστε απευθείας μέσα στο σύμπλεγμα χρησιμοποιώντας την επίσημη δήλωση εργασιών της Aqua Security:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Εκτέλεση σε επίπεδο ελέγχου (κύριοι κόμβοι)

Εκτελέστε το `kube-bench` στον κόμβο κύριας/επιπέδου ελέγχου για να ελέγξετε τις βασικές υπηρεσίες:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Εκτέλεση σε κόμβους εργαζομένων

Εκτελέστε σε κόμβους εργαζομένων για έλεγχο του δαίμονα `kubelet` και χρόνου εκτέλεσης κοντέινερ:
```bash
kube-bench run --targets node
```
Οι έλεγχοι επαληθεύουν:
- Δικαιώματα αρχείου διαμόρφωσης Kubelet (`600` ή `644`).
- Η ανώνυμη εξουσιοδότηση είναι απενεργοποιημένη (`--anonymous-auth=false`).
- Η λειτουργία εξουσιοδότησης ορίστηκε σε `Webhook`.
- Η θύρα μόνο για ανάγνωση (`10255`) είναι απενεργοποιημένη.

---

## Διαχείριση Kubernetes (EKS, GKE, AKS)

Οι πάροχοι cloud διαχειρίζονται το κύριο επίπεδο ελέγχου σε EKS, GKE και AKS. Ελέγχετε μόνο τους κόμβους εργαζομένων και τις διαμορφώσεις ειδικά για το cloud:

### Amazon EKS
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
### Google GKE
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
### Microsoft AKS
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## Φιλτράρισμα δοκιμών και σημείων αναφοράς

Εκτελέστε στοχευμένα υποσύνολα ελέγχων για να εστιάσετε τις προσπάθειες αποκατάστασης:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## Έξοδος JSON & JUnit για αυτοματισμό

Ενσωματώστε τη σάρωση συμμόρφωσης σε πίνακες εργαλείων SIEM ή συμμόρφωσης:

### 1. Εξαγωγή JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. Εξαγωγή JUnit XML (Σωληνώσεις CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Ροή εργασιών αποκατάστασης και σκλήρυνσης

Στο τέλος κάθε εκτέλεσης, το `kube-bench` παρέχει λεπτομερείς εντολές αποκατάστασης για αποτυχημένες δοκιμές.

Παράδειγμα αποτυχίας:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Δημιουργημένες συμβουλές αποκατάστασης:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Βήμα προς βήμα Διόρθωση:
1. Ανοίξτε το `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Προσθέστε ή ενημερώστε το `--anonymous-auth=false` στο `spec.containers[0].command`.
3. Αποθηκεύστε το αρχείο. Το Kubelet επανεκκινεί αυτόματα το στατικό pod.
4. Εκτελέστε ξανά το `kube-bench run --check 1.2.6` για να επαληθεύσετε το `[PASS]`.