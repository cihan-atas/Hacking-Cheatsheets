<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet di kube-bench

[kube-bench](https://github.com/aquasecurity/kube-bench) è uno strumento di controllo della sicurezza basato su Go sviluppato da Aqua Security che controlla se Kubernetes è distribuito in modo sicuro secondo le raccomandazioni del Kubernetes Benchmark del Center for Internet Security (CIS).

---

## Sommario

- [Panoramica sul benchmark CIS Kubernetes](#cis-kubernetes-benchmark-overview)
- [Metodi di installazione e distribuzione](#installazione--metodi-di-distribuzione)
- [In esecuzione sul piano di controllo (nodi master)](#in esecuzione-sul-piano-di-controllo-nodi-master)
- [In esecuzione sui nodi di lavoro](#in esecuzione-sui-nodi di lavoro)
- [Kubernetes gestito (EKS, GKE, AKS)](#kubernetes-gestito-eks-gke-aks)
- [Filtro di test e benchmark](#filtro-test--benchmark)
- [Output JSON e JUnit per l'automazione](#json--junit-output-for-automation)
- [Flusso di lavoro di riparazione e rafforzamento](#risanamento--flusso di lavoro di rafforzamento)

---

## Panoramica del benchmark CIS Kubernetes

`kube-bench` esegue i test definiti nei file YAML corrispondenti alle sezioni CIS Benchmark:
1. **Componenti del piano di controllo:** Server API, Gestione controller, Pianificatore.
2. **etcd:** Autorizzazioni file, certificati TLS, crittografia client.
3. **Configurazione del piano di controllo:** Autenticazione, autorizzazione, RBAC, controller di ammissione.
4. **Nodi di lavoro:** configurazione Kubelet, file di servizio, autenticazione TLS.
5. **Politiche:** RBAC, standard/ammissione di sicurezza dei pod, politiche di rete, gestione dei segreti.

Stati restituiti per test:
- `[PASS]`: soddisfa gli standard di riferimento CIS.
- `[FAIL]`: devia dalla raccomandazione; richiede una bonifica.
- `[WARN]`: controllo manuale (dipende dalle variabili ambientali).
- `[INFO]`: nota informativa.

---

## Metodi di installazione e distribuzione

### 1. Scarica il file binario precompilato
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Esegui direttamente all'interno di un contenitore Docker
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Distribuisci come lavoro Kubernetes
Esegui direttamente all'interno del cluster utilizzando il Job manifest ufficiale di Aqua Security:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## In esecuzione sul piano di controllo (nodi master)

Esegui `kube-bench` sul tuo nodo master/piano di controllo per controllare i servizi principali:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## In esecuzione sui nodi di lavoro

Esegui sui nodi di lavoro per controllare il daemon `kubelet` e il runtime del contenitore:
```bash
kube-bench run --targets node
```
I controlli verificano:
- Autorizzazioni del file di configurazione Kubelet (`600` o `644`).
- Autenticazione anonima disabilitata (`--anonymous-auth=false`).
- Modalità di autorizzazione impostata su `Webhook`.
- Porta di sola lettura (`10255`) disabilitata.

---

## Kubernetes gestito (EKS, GKE, AKS)

I provider cloud gestiscono il piano di controllo principale su EKS, GKE e AKS. Controlla solo i nodi di lavoro e le configurazioni specifiche del cloud:

###Amazon EKS
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
###Google GKE
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
### AKS di Microsoft
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## Test di filtraggio e benchmark

Esegui sottoinsiemi mirati di controlli per concentrare gli sforzi di correzione:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## Output JSON e JUnit per l'automazione

Integra la scansione di conformità nel SIEM o nei dashboard di conformità:

### 1. Esportazione JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. Esportazione XML JUnit (pipeline CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Flusso di lavoro di bonifica e rafforzamento

Alla fine di ogni esecuzione, `kube-bench` fornisce comandi di riparazione dettagliati per i test non riusciti.

Esempio di fallimento:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Avviso di riparazione generato:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Correzione dettagliata:
1. Apri `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Aggiungi o aggiorna `--anonymous-auth=false` sotto `spec.containers[0].command`.
3. Salvare il file. Kubelet riavvia automaticamente il pod statico.
4. Eseguire nuovamente `kube-bench run --check 1.2.6` per verificare `[PASS]`.