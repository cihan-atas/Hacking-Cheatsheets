<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# kube-bench Cheatsheet

[kube-bench](https://github.com/aquasecurity/kube-bench) ist ein Go-basiertes Sicherheitsüberwachungstool, das von Aqua Security entwickelt wurde und prüft, ob Kubernetes gemäß den Kubernetes-Benchmark-Empfehlungen des Center for Internet Security (CIS) sicher bereitgestellt wird.

---

## Inhaltsverzeichnis

- [CIS Kubernetes Benchmark-Übersicht](#cis-kubernetes-benchmark-overview)
- [Installations- und Bereitstellungsmethoden](#installation--deployment-methods)
- [Laufen auf der Kontrollebene (Masterknoten)](#running-on-control-plane-master-nodes)
- [Läuft auf Worker-Knoten](#running-on-worker-nodes)
– [Verwaltetes Kubernetes (EKS, GKE, AKS)](#managed-kubernetes-eks-gke-aks)
- [Filtertests und Benchmarks](#filtering-tests--benchmarks)
- [JSON- und JUnit-Ausgabe für die Automatisierung](#json--junit-output-for-automation)
- [Remediation & Hardening Workflow](#remediation--hardening-workflow)

---

## CIS Kubernetes Benchmark-Übersicht

`kube-bench` führt Tests aus, die in YAML-Dateien definiert sind, die den CIS-Benchmark-Abschnitten entsprechen:
1. **Komponenten der Steuerungsebene:** API-Server, Controller-Manager, Scheduler.
2. **etcd:** Dateiberechtigungen, TLS-Zertifikate, Client-Verschlüsselung.
3. **Kontrollebenenkonfiguration:** Authentifizierung, Autorisierung, RBAC, Zulassungscontroller.
4. **Worker-Knoten:** Kubelet-Konfiguration, Servicedateien, TLS-Authentifizierung.
5. **Richtlinien:** RBAC, Pod-Sicherheitsstandards/Zulassung, Netzwerkrichtlinien, Secrets-Management.

Pro Test zurückgegebene Status:
- `[PASS]`: Erfüllt die CIS-Benchmark-Standards.
- `[FAIL]`: Weicht von der Empfehlung ab; erfordert eine Sanierung.
- `[WARN]`: Manuell prüfen (abhängig von Umgebungsvariablen).
- `[INFO]`: Informationshinweis.

---

## Installations- und Bereitstellungsmethoden

### 1. Vorkompilierte Binärdatei herunterladen
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Direkt in einem Docker-Container ausführen
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Als Kubernetes-Job bereitstellen
Führen Sie es mithilfe des offiziellen Job-Manifests von Aqua Security direkt im Cluster aus:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Läuft auf der Steuerungsebene (Masterknoten)

Führen Sie `kube-bench` auf Ihrem Master-/Steuerungsebenenknoten aus, um Kerndienste zu prüfen:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Läuft auf Worker-Knoten

Auf Worker-Knoten ausführen, um den `kubelet`-Daemon und die Containerlaufzeit zu überwachen:
```bash
kube-bench run --targets node
```
Schecks verifizieren:
- Berechtigungen für die Kubelet-Konfigurationsdatei (`600` oder `644`).
- Anonyme Authentifizierung deaktiviert (`--anonymous-auth=false`).
- Autorisierungsmodus auf `Webhook` eingestellt.
– Schreibgeschützter Port (`10255`) deaktiviert.

---

## Verwaltetes Kubernetes (EKS, GKE, AKS)

Cloud-Anbieter verwalten die Master-Steuerungsebene auf EKS, GKE und AKS. Überwachen Sie nur die Worker-Knoten und cloudspezifischen Konfigurationen:

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

## Filtertests und Benchmarks

Führen Sie gezielte Teilmengen von Prüfungen durch, um die Abhilfemaßnahmen zu fokussieren:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## JSON- und JUnit-Ausgabe für die Automatisierung

Integrieren Sie Compliance-Scans in SIEM- oder Compliance-Dashboards:

### 1. JSON-Export
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. JUnit-XML-Export (CI/CD-Pipelines)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Sanierungs- und Härtungsworkflow

Am Ende jedes Laufs stellt `kube-bench` detaillierte Korrekturbefehle für fehlgeschlagene Tests bereit.

Beispielfehler:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Generierte Sanierungshinweise:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Schritt-für-Schritt-Lösung:
1. Öffnen Sie `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Fügen Sie `--anonymous-auth=false` unter `spec.containers[0].command` hinzu oder aktualisieren Sie es.
3. Speichern Sie die Datei. Kubelet startet den statischen Pod automatisch neu.
4. Führen Sie `kube-bench run --check 1.2.6` erneut aus, um `[PASS]` zu überprüfen.