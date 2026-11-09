<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# kube-bench 备忘单

[kube-bench](https://github.com/aquasecurity/kube-bench) 是 Aqua Security 开发的基于 Go 的安全审计工具，用于根据互联网安全中心 (CIS) Kubernetes Benchmark 建议检查 Kubernetes 是否安全部署。

---

## 目录

- [CIS Kubernetes 基准概述](#cis-kubernetes-benchmark-overview)
- [安装和部署方法](#installation--deployment-methods)
- [在控制平面（主节点）上运行](#running-on-control-plane-master-nodes)
- [在工作节点上运行](#running-on-worker-nodes)
- [托管 Kubernetes（EKS、GKE、AKS）](#management-kubernetes-eks-gke-aks)
- [过滤测试和基准](#filtering-tests--benchmarks)
- [用于自动化的 JSON 和 JUnit 输出](#json--junit-output-for-automation)
- [修复和强化工作流程](#remediation--hardening-workflow)

---

## CIS Kubernetes 基准概述

`kube-bench` 执行与 CIS Benchmark 部分相对应的 YAML 文件中定义的测试：
1. **控制平面组件：** API 服务器、控制器管理器、调度器。
2. **etcd：** 文件权限、TLS 证书、客户端加密。
3. **控制平面配置：** 身份验证、授权、RBAC、准入控制器。
4. **工作节点：** Kubelet 配置、服务文件、TLS 身份验证。
5. **策略：** RBAC、Pod 安全标准/准入、网络策略、秘密管理。

每次测试返回的状态：
- `[PASS]`：符合 CIS 基准标准。
- `[FAIL]`：偏离建议；需要补救。
- `[WARN]`：手动检查（取决于环境变量）。
- `[INFO]`：信息说明。

---

## 安装部署方法

### 1.下载预编译的二进制文件
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. 直接在 Docker 容器内运行
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. 作为 Kubernetes 作业部署
使用 Aqua Security 的官方作业清单直接在集群内运行：
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## 在控制平面（主节点）上运行

在主/控制平面节点上运行 `kube-bench` 以审核核心服务：
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## 在工作节点上运行

在工作节点上执行以审核 `kubelet` 守护进程和容器运行时：
```bash
kube-bench run --targets node
```
检查验证：
- Kubelet 配置文件权限（`600` 或 `644`）。
- 禁用匿名身份验证 (`--anonymous-auth=false`)。
- 授权模式设置为`Webhook`。
- 只读端口 (`10255`) 已禁用。

---

## 托管 Kubernetes（EKS、GKE、AKS）

云提供商管理 EKS、GKE 和 AKS 上的主控制平面。仅审核工作节点和特定于云的配置：

### 亚马逊 EKS
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
### 谷歌 GKE
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
### 微软AKS
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## 过滤测试和基准

运行有针对性的检查子集以集中修复工作：
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## 用于自动化的 JSON 和 JUnit 输出

将合规性扫描集成到 SIEM 或合规性仪表板中：

### 1.JSON 导出
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. JUnit XML 导出（CI/CD 管道）
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## 修复和强化工作流程

每次运行结束时，`kube-bench` 都会为失败的测试提供详细的修复命令。

失败示例：
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
生成的修复建议：
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### 逐步修复：
1. 打开`/etc/kubernetes/manifests/kube-apiserver.yaml`。
2.在`spec.containers[0].command`下添加或更新`--anonymous-auth=false`。
3. 保存文件。 Kubelet 自动重启静态 Pod。
4. 重新运行`kube-bench run --check 1.2.6`以验证`[PASS]`。