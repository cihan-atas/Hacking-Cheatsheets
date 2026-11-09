<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Шпаргалка по kube-bench

[kube-bench](https://github.com/aquasecurity/kube-bench) — это инструмент аудита безопасности на основе Go, разработанный Aqua Security, который проверяет, безопасно ли развертывание Kubernetes в соответствии с рекомендациями Kubernetes Benchmark Центра интернет-безопасности (CIS).

---

## Содержание

- [Обзор CIS Kubernetes Benchmark](#cis-kubernetes-benchmark-overview)
- [Методы установки и развертывания](#installation--deployment-methods)
- [Работа на плоскости управления (главные узлы)](#running-on-control-plane-master-nodes)
- [Работа на рабочих узлах](#running-on-worker-nodes)
- [Управляемый Kubernetes (EKS, GKE, AKS)](#managed-kubernetes-eks-gke-aks)
- [Тесты и тесты фильтрации](#filtering-tests--benchmarks)
- [Вывод JSON и JUnit для автоматизации](#json--junit-output-for-automation)
- [Рабочий процесс исправления и усиления безопасности](#remediation--hardening-workflow)

---

## Обзор теста Kubernetes в СНГ

`kube-bench` выполняет тесты, определенные в файлах YAML, соответствующих разделам CIS Benchmark:
1. **Компоненты плоскости управления:** API-сервер, диспетчер контроллера, планировщик.
2. **etcd:** Разрешения для файлов, сертификаты TLS, шифрование клиента.
3. **Конфигурация плоскости управления:** Аутентификация, авторизация, RBAC, контроллеры доступа.
4. **Рабочие узлы:** конфигурация Kubelet, служебные файлы, аутентификация TLS.
5. **Политики:** RBAC, стандарты безопасности/допуск модулей, сетевые политики, управление секретами.

Статусы, возвращаемые за тест:
- `[PASS]`: соответствует стандартам CIS.
- `[FAIL]`: Отклонение от рекомендации; требует исправления.
- `[WARN]`: проверьте вручную (зависит от переменных среды).
- `[INFO]`: Информационное примечание.

---

## Методы установки и развертывания

### 1. Загрузите предварительно скомпилированный двоичный файл
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Запуск непосредственно внутри Docker-контейнера
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Развертывание как задание Kubernetes
Запустите непосредственно внутри кластера, используя официальный манифест задания Aqua Security:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Работа на плоскости управления (главные узлы)

Запустите `kube-bench` на главном узле/узле плоскости управления для аудита основных служб:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Запуск на рабочих узлах

Выполните на рабочих узлах для аудита демона `kubelet` и среды выполнения контейнера:
```bash
kube-bench run --targets node
```
Проверки подтверждают:
— Разрешения файла конфигурации Kubelet (`600` или `644`).
- Анонимная аутентификация отключена (`--anonymous-auth=false`).
- Режим авторизации установлен на `Webhook`.
- Порт только для чтения (`10255`) отключен.

---

## Управляемый Kubernetes (EKS, GKE, AKS)

Поставщики облачных услуг управляют главной плоскостью управления на EKS, GKE и AKS. Аудит только рабочих узлов и облачных конфигураций:

### Амазон ЭКС
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
### Google ГКЕ
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
### Microsoft АКС
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## Фильтрация тестов и тестов

Запустите целевые подмножества проверок, чтобы сосредоточить усилия по исправлению:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## Вывод JSON и JUnit для автоматизации

Интегрируйте сканирование соответствия в SIEM или информационные панели соответствия:

### 1. Экспорт JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. Экспорт XML JUnit (конвейеры CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Рабочий процесс исправления и усиления безопасности

В конце каждого запуска `kube-bench` предоставляет подробные команды исправления для неудачных тестов.

Пример неудачи:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Сгенерированные рекомендации по исправлению:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Пошаговое исправление:
1. Откройте `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Добавьте или обновите `--anonymous-auth=false` в `spec.containers[0].command`.
3. Сохраните файл. Kubelet автоматически перезапускает статический модуль.
4. Повторно запустите `kube-bench run --check 1.2.6`, чтобы проверить `[PASS]`.