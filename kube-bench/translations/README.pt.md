<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de referências do kube-bench

[kube-bench](https://github.com/aquasecurity/kube-bench) é uma ferramenta de auditoria de segurança baseada em Go desenvolvida pela Aqua Security que verifica se o Kubernetes está implantado com segurança de acordo com as recomendações do Kubernetes Benchmark do Center for Internet Security (CIS).

---

## Índice

- [Visão geral do benchmark CIS Kubernetes](#cis-kubernetes-benchmark-overview)
- [Métodos de instalação e implantação](#installation--deployment-methods)
- [Executando no plano de controle (nós mestres)](#running-on-control-plane-master-nodes)
- [Executando em nós de trabalho](#running-on-worker-nodes)
- [Kubernetes gerenciados (EKS, GKE, AKS)](#kubernetes gerenciados-eks-gke-aks)
- [Testes de filtragem e benchmarks](#filtering-tests--benchmarks)
- [Saída JSON e JUnit para automação](#json--junit-output-for-automation)
- [Fluxo de trabalho de remediação e proteção](#remediation--hardening-workflow)

---

## Visão geral do benchmark CIS Kubernetes

`kube-bench` executa testes definidos em arquivos YAML correspondentes às seções CIS Benchmark:
1. **Componentes do Plano de Controle:** Servidor API, Gerenciador de Controlador, Agendador.
2. **etcd:** Permissões de arquivo, certificados TLS, criptografia de cliente.
3. **Configuração do Plano de Controle:** Autenticação, autorização, RBAC, controladores de admissão.
4. **Nós de trabalho:** configuração do Kubelet, arquivos de serviço, autenticação TLS.
5. **Políticas:** RBAC, padrões/admissão de segurança de pod, políticas de rede, gerenciamento de segredos.

Status retornados por teste:
- `[PASS]`: Atende aos padrões CIS Benchmark.
- `[FAIL]`: Desvia da recomendação; requer remediação.
- `[WARN]`: Verifique manualmente (depende das variáveis ​​ambientais).
- `[INFO]`: Nota informativa.

---

## Métodos de instalação e implantação

### 1. Baixe o binário pré-compilado
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Execute diretamente dentro de um contêiner Docker
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Implantar como um trabalho do Kubernetes
Execute diretamente dentro do cluster usando o manifesto de trabalho oficial do Aqua Security:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Executando no plano de controle (nós mestres)

Execute `kube-bench` em seu nó mestre/plano de controle para auditar os serviços principais:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Executando em nós de trabalho

Execute em nós de trabalho para auditar o daemon `kubelet` e o tempo de execução do contêiner:
```bash
kube-bench run --targets node
```
Verificações verificam:
- Permissões do arquivo de configuração do Kubelet (`600` ou `644`).
- Autenticação anônima desativada (`--anonymous-auth=false`).
- Modo de autorização definido como `Webhook`.
- Porta somente leitura (`10255`) desativada.

---

## Kubernetes gerenciados (EKS, GKE, AKS)

Os provedores de nuvem gerenciam o plano de controle mestre no EKS, GKE e AKS. Audite apenas os nós do trabalhador e as configurações específicas da nuvem:

###Amazon EKS
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
### Google GKE
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
###Microsoft AKS
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## Filtragem de testes e benchmarks

Execute subconjuntos direcionados de verificações para concentrar os esforços de remediação:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## Saída JSON e JUnit para automação

Integre a verificação de conformidade ao SIEM ou aos painéis de conformidade:

### 1. Exportação JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. Exportação XML JUnit (pipelines CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Fluxo de trabalho de remediação e proteção

No final de cada execução, o `kube-bench` fornece comandos de correção detalhados para testes com falha.

Exemplo de falha:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Conselhos de remediação gerados:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Correção passo a passo:
1. Abra `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Adicione ou atualize `--anonymous-auth=false` em `spec.containers[0].command`.
3. Salve o arquivo. Kubelet reinicia automaticamente o pod estático.
4. Execute novamente o `kube-bench run --check 1.2.6` para verificar o `[PASS]`.