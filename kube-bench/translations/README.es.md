<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hoja de referencia de kube-bench

[kube-bench](https://github.com/aquasecurity/kube-bench) es una herramienta de auditoría de seguridad basada en Go desarrollada por Aqua Security que verifica si Kubernetes está implementado de forma segura de acuerdo con las recomendaciones de Kubernetes Benchmark del Centro para la Seguridad de Internet (CIS).

---

## Tabla de contenidos

- [Descripción general de las pruebas comparativas de CIS Kubernetes] (#cis-kubernetes-benchmark-overview)
- [Installation & Deployment Methods](#installation--deployment-methods)
- [Ejecución en el plano de control (nodos maestros)](#ejecución-en-el-plano-de-control-nodos-maestros)
- [Ejecutando en nodos trabajadores](#ejecutando-en-nodos-trabajadores)
- [Kubernetes administrado (EKS, GKE, AKS)](#managed-kubernetes-eks-gke-aks)
- [Pruebas de filtrado y puntos de referencia] (#pruebas de filtrado - puntos de referencia)
- [Salida JSON y JUnit para automatización](#json--junit-output-for-automation)
- [Flujo de trabajo de corrección y refuerzo](#remediation--hardening-workflow)

---

## Descripción general del punto de referencia CIS Kubernetes

`kube-bench` ejecuta pruebas definidas en archivos YAML correspondientes a las secciones de CIS Benchmark:
1. **Componentes del plano de control:** Servidor API, Administrador de controladores, Programador.
2. **etcd:** Permisos de archivos, certificados TLS, cifrado de cliente.
3. **Configuración del plano de control:** Autenticación, autorización, RBAC, controladores de admisión.
4. **Nodos de trabajo:** Configuración de Kubelet, archivos de servicio, autenticación TLS.
5. **Políticas:** RBAC, Admisión/Estándares de Seguridad del Pod, Políticas de Red, Gestión de Secretos.

Estados devueltos por prueba:
- `[PASS]`: Cumple con los estándares CIS Benchmark.
- `[FAIL]`: Se desvía de la recomendación; requiere remediación.
- `[WARN]`: Verificar manualmente (depende de variables ambientales).
- `[INFO]`: Nota informativa.

---

## Métodos de instalación e implementación

### 1. Descargar binario precompilado
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. Ejecutar directamente dentro de un contenedor Docker
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. Implementar como un trabajo de Kubernetes
Ejecute directamente dentro del clúster utilizando el manifiesto de trabajo oficial de Aqua Security:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## Ejecutando en el plano de control (nodos maestros)

Ejecute `kube-bench` en su nodo maestro/plano de control para auditar los servicios principales:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## Ejecutándose en nodos trabajadores

Ejecute en nodos trabajadores para auditar el demonio `kubelet` y el tiempo de ejecución del contenedor:
```bash
kube-bench run --targets node
```
Los cheques verifican:
- Permisos del archivo de configuración de Kubelet (`600` o `644`).
- Autenticación anónima deshabilitada (`--anonymous-auth=false`).
- Modo de autorización establecido en `Webhook`.
- Puerto de solo lectura (`10255`) deshabilitado.

---

## Kubernetes administrado (EKS, GKE, AKS)

Los proveedores de la nube administran el plano de control maestro en EKS, GKE y AKS. Audite solo los nodos trabajadores y las configuraciones específicas de la nube:

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

## Pruebas de filtrado y puntos de referencia

Ejecute subconjuntos específicos de comprobaciones para centrar los esfuerzos de remediación:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## Salida JSON y JUnit para automatización

Integre el escaneo de cumplimiento en SIEM o paneles de control de cumplimiento:

### 1. Exportación JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. Exportación XML JUnit (canalizaciones CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## Flujo de trabajo de remediación y endurecimiento

Al final de cada ejecución, `kube-bench` proporciona comandos de corrección detallados para las pruebas fallidas.

Ejemplo de fallo:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
Consejos de remediación generados:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### Solución paso a paso:
1. Abra `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. Agregue o actualice `--anonymous-auth=false` en `spec.containers[0].command`.
3. Guarde el archivo. Kubelet reinicia automáticamente el pod estático.
4. Vuelva a ejecutar `kube-bench run --check 1.2.6` para verificar `[PASS]`.