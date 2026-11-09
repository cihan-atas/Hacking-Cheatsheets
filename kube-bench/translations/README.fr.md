<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire kube-bench

[kube-bench](https://github.com/aquasecurity/kube-bench) est un outil d'audit de sécurité basé sur Go développé par Aqua Security qui vérifie si Kubernetes est déployé en toute sécurité conformément aux recommandations Kubernetes Benchmark du Center for Internet Security (CIS).

---

## Table des matières

- [Présentation du benchmark CIS Kubernetes](#cis-kubernetes-benchmark-overview)
- [Méthodes d'installation et de déploiement](#installation--deployment-methods)
-[Exécution sur le plan de contrôle (nœuds maîtres)](#running-on-control-plane-master-nodes)
- [Exécution sur des nœuds de travail](#running-on-worker-nodes)
- [Kubernetes géré (EKS, GKE, AKS)](#managed-kubernetes-eks-gke-aks)
- [Tests de filtrage et benchmarks](#filtering-tests--benchmarks)
- [Sortie JSON et JUnit pour l'automatisation](#json--junit-output-for-automation)
- [Workflow de remédiation et de durcissement](#remediation--hardening-workflow)

---

## Présentation de l'analyse comparative Kubernetes CIS

`kube-bench` exécute les tests définis dans les fichiers YAML correspondant aux sections CIS Benchmark :
1. **Composants du plan de contrôle :** Serveur API, gestionnaire de contrôleur, planificateur.
2. **etcd :** Autorisations de fichiers, certificats TLS, cryptage client.
3. **Configuration du plan de contrôle :** Authentification, autorisation, RBAC, contrôleurs d'admission.
4. **Nœuds de travail :** configuration Kubelet, fichiers de service, authentification TLS.
5. **Politiques :** RBAC, normes de sécurité des pods/admission, politiques réseau, gestion des secrets.

Statuts renvoyés par test :
- `[PASS]` : répond aux normes de référence CIS.
- `[FAIL]` : s'écarte de la recommandation ; nécessite une réhabilitation.
- `[WARN]` : vérifier manuellement (dépend des variables environnementales).
- `[INFO]` : Note informative.

---

## Méthodes d'installation et de déploiement

### 1. Téléchargez le binaire précompilé
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Exécuter directement dans un conteneur Docker
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Déployer en tant que tâche Kubernetes
Exécutez directement dans le cluster à l'aide du manifeste de travail officiel d'Aqua Security :
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Exécution sur le plan de contrôle (nœuds maîtres)

Exécutez `kube-bench` sur votre nœud maître/plan de contrôle pour auditer les services principaux :
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Exécuté sur des nœuds de travail

Exécutez sur les nœuds de travail pour auditer le démon `kubelet` et l'exécution du conteneur :
```bash
kube-bench run --targets node
```
Les chèques vérifient :
- Autorisations du fichier de configuration Kubelet (`600` ou `644`).
- Authentification anonyme désactivée (`--anonymous-auth=false`).
- Mode d'autorisation défini sur `Webhook`.
- Port en lecture seule (`10255`) désactivé.

---

## Kubernetes gérés (EKS, GKE, AKS)

Les fournisseurs de cloud gèrent le plan de contrôle principal sur EKS, GKE et AKS. Auditez uniquement les nœuds de travail et les configurations spécifiques au cloud :

###Amazon EKS
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
### Google GKE
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
### Microsoft AKS
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## Tests et benchmarks de filtrage

Exécutez des sous-ensembles ciblés de vérifications pour concentrer les efforts de remédiation :
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## Sortie JSON et JUnit pour l'automatisation

Intégrez l’analyse de conformité dans le SIEM ou les tableaux de bord de conformité :

### 1. Exportation JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. Exportation XML JUnit (pipelines CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Workflow de remédiation et de renforcement

À la fin de chaque exécution, `kube-bench` fournit des commandes de correction détaillées pour les tests ayant échoué.

Exemple d'échec :
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Conseils de correction générés :
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Correction étape par étape :
1. Ouvrez `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Ajoutez ou mettez à jour `--anonymous-auth=false` sous `spec.containers[0].command`.
3. Enregistrez le fichier. Kubelet redémarre automatiquement le pod statique.
4. Réexécutez `kube-bench run --check 1.2.6` pour vérifier `[PASS]`.