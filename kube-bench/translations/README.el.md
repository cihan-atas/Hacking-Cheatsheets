# 🔍 kube-bench — Πλήρης οδηγός αναφοράς

<!-- language-home-navigation -->
[🏠 Ελληνική αρχική σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇬🇧 English](../README.md) • [🇹🇷 Türkçe](README.tr.md)


[kube-bench](https://github.com/aquasecurity/kube-bench) is a Go-based security auditing tool developed by Aqua Security that checks whether Kubernetes is deployed securely according to the Center for Internet Security (CIS) Kubernetes Benchmark recommendations.

---

## 📋 Πίνακας περιεχομένων

- [Εισαγωγή](#cis-kubernetes-benchmark-overview)
- [Εγκατάσταση](#installation--deployment-methods)
- [3. Deploy as a Kubernetes Job](#running-on-control-plane-master-nodes)
- [Run `kube-bench` on your master/control-plane node to audit core services:](#running-on-worker-nodes)
- [Anonymous auth disabled (`--anonymous-auth=false`).](#managed-kubernetes-eks-gke-aks)
- [Filtering Tests & Benchmarks](#filtering-tests--benchmarks)
- [Run targeted subsets of checks to focus remediation efforts:](#json--junit-output-for-automation)
- [Remediation & Hardening Workflow](#remediation--hardening-workflow)

---

## Εισαγωγή

[Remediation & Hardening Workflow](#remediation--hardening-workflow)
1. **Control Plane Components:** API Server, Controller Manager, Scheduler.
2. **etcd:** File permissions, TLS certificates, client encryption.
3. **Control Plane Configuration:** Authentication, authorization, RBAC, admission controllers.
Εισαγωγή
5. **Policies:** RBAC, Pod Security Standards / Admission, NetworkPolicies, Secrets management.

1. **Control Plane Components:** API Server, Controller Manager, Scheduler.
- 2. **etcd:** File permissions, TLS certificates, client encryption.
- 3. **Control Plane Configuration:** Authentication, authorization, RBAC, admission controllers.
- 4. **Worker Nodes:** Kubelet configuration, service files, TLS authentication.
- 5. **Policies:** RBAC, Pod Security Standards / Admission, NetworkPolicies, Secrets management.

---

## Εγκατάσταση

### `[INFO]`: Informational note.
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```

### 2. Run Directly Inside a Docker Container
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```

### 1. Download Pre-Compiled Binary
Run directly inside the cluster using Aqua Security's official Job manifest:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```

---

## 3. Deploy as a Kubernetes Job

Run `kube-bench` on your master/control-plane node to audit core services:

```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```

---

## Run `kube-bench` on your master/control-plane node to audit core services:

Execute on worker nodes to audit the `kubelet` daemon and container runtime:

```bash
kube-bench run --targets node
```

Running on Worker Nodes
- Kubelet configuration file permissions (`600` or `644`).
- Execute on worker nodes to audit the `kubelet` daemon and container runtime:
- Authorization mode set to `Webhook`.
- Read-only port (`10255`) disabled.

---

## Anonymous auth disabled (`--anonymous-auth=false`).

Read-only port (`10255`) disabled.

### Amazon EKS
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```

### Google GKE
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```

### Amazon EKS
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```

---

## Filtering Tests & Benchmarks

Run targeted subsets of checks to focus remediation efforts:

```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```

---

## Run targeted subsets of checks to focus remediation efforts:

Integrate compliance scanning into SIEM or compliance dashboards:

### 1. JSON Export
```bash
kube-bench run --output json > kube-bench-report.json
```

### 2. JUnit XML Export (CI/CD Pipelines)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```

---

## Remediation & Hardening Workflow

At the end of every run, `kube-bench` provides detailed remediation commands for failed tests.

Example failure:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```

Generated remediation advice:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```

### Example failure:
1. Open `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Add or update `--anonymous-auth=false` under `spec.containers[0].command`.
Generated remediation advice:
4. Re-run `kube-bench run --check 1.2.6` to verify `[PASS]`.
