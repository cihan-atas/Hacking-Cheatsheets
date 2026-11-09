<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# kube-bench Jukseark

[kube-bench](https://github.com/aquasecurity/kube-bench) er et Go-basert sikkerhetsrevisjonsverktøy utviklet av Aqua Security som sjekker om Kubernetes er distribuert sikkert i henhold til anbefalinger fra Center for Internet Security (CIS) Kubernetes Benchmark.

---

## Innholdsfortegnelse

- [CIS Kubernetes Benchmark Overview](#cis-kubernetes-benchmark-overview)
- [Installasjon og distribusjonsmetoder](#installation--deployment-methods)
- [Kjører på kontrollplan (hovednoder)](#kjører-på-kontrollplan-hovednoder)
- [Kjører på arbeidernoder](#kjører-på-arbeidernoder)
- [Administrerte Kubernetes (EKS, GKE, AKS)](#managed-kubernetes-eks-gke-aks)
- [Filtreringstester og benchmarks](#filtering-tests--benchmarks)
- [JSON & JUnit Output for Automation](#json--junit-output-for-automation)
- [Remediation & Hardening Workflow](#remediation--hardening-workflow)

---

## CIS Kubernetes Benchmark Oversikt

`kube-bench` utfører tester definert i YAML-filer som tilsvarer CIS Benchmark-seksjoner:
1. **Kontrollplankomponenter:** API-server, kontrolleradministrator, planlegger.
2. **etcd:** Filtillatelser, TLS-sertifikater, klientkryptering.
3. **Konfigurasjon av kontrollplan:** Autentisering, autorisasjon, RBAC, adgangskontrollere.
4. **Arbeidernoder:** Kubelet-konfigurasjon, tjenestefiler, TLS-autentisering.
5. **Retningslinjer:** RBAC, Pod Security Standards / Admission, Network Policy, Secrets management.

Statuser returnert per test:
- `[PASS]`: Oppfyller CIS Benchmark-standarder.
- `[FAIL]`: Avviker fra anbefaling; krever utbedring.
- `[WARN]`: Kontroller manuelt (avhenger av miljøvariabler).
- `[INFO]`: Informasjonsmerknad.

---

## Installasjon og distribusjonsmetoder

### 1. Last ned forhåndskompilert binær
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Kjør direkte inne i en Docker-beholder
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Distribuer som en Kubernetes-jobb
Kjør direkte inne i klyngen ved å bruke Aqua Securitys offisielle jobbmanifest:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Kjører på kontrollplan (masternoder)

Kjør `kube-bench` på hoved-/kontrollplan-noden for å revidere kjernetjenester:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Kjører på arbeidernoder

Kjør på arbeidernoder for å revidere `kubelet`-demonen og containerkjøringen:
```bash
kube-bench run --targets node
```
Sjekker bekrefter:
- Kubelet-konfigurasjonsfiltillatelser (`600` eller `644`).
- Anonym auth deaktivert (`--anonymous-auth=false`).
- Autorisasjonsmodus satt til `Webhook`.
- Skrivebeskyttet port (`10255`) deaktivert.

---

## Administrerte Kubernetes (EKS, GKE, AKS)

Skyleverandører administrerer hovedkontrollplanet på EKS, GKE og AKS. Overvåk bare arbeidernodene og skyspesifikke konfigurasjoner:

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

## Filtreringstester og benchmarks

Kjør målrettede undersett av kontroller for å fokusere utbedringsarbeidet:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## JSON & JUnit-utgang for automatisering

Integrer samsvarsskanning i SIEM- eller samsvarskontrollpaneler:

### 1. JSON-eksport
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. JUnit XML-eksport (CI/CD-rørledninger)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Arbeidsflyt for utbedring og herding

På slutten av hver kjøring gir `kube-bench` detaljerte utbedringskommandoer for mislykkede tester.

Eksempel på feil:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Genererte utbedringsråd:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Trinn-for-trinn-fiks:
1. Åpne `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Legg til eller oppdater `--anonymous-auth=false` under `spec.containers[0].command`.
3. Lagre filen. Kubelet starter automatisk den statiske poden på nytt.
4. Kjør `kube-bench run --check 1.2.6` på nytt for å bekrefte `[PASS]`.