<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش في مقاعد البدلاء

[kube-bench](https://github.com/aquasecurity/kube-bench) هي أداة تدقيق أمني قائمة على Go تم تطويرها بواسطة Aqua Security والتي تتحقق مما إذا كان Kubernetes قد تم نشره بشكل آمن وفقًا لتوصيات Kubernetes Benchmark لمركز أمن الإنترنت (CIS).

---

## جدول المحتويات

- [نظرة عامة على معيار CIS Kubernetes](#cis-kubernetes-benchmark-overview)
- [طرق التثبيت والنشر](#installation--deployment-methods)
- [التشغيل على مستوى التحكم (العقد الرئيسية)](#العقد الرئيسية على مستوى التحكم)
- [التشغيل على العقد العاملة](#running-on-worker-nodes)
- [Kubernetes المُدارة (EKS، GKE، AKS)](#managed-kubernetes-eks-gke-aks)
- [اختبارات التصفية والمعايير المرجعية](#filtering-tests--benchmarks)
- [إخراج JSON وJUnit للأتمتة](#json--junit-output-for-automation)
- [سير عمل المعالجة والتقوية](#remediation--harding-workflow)

---

## نظرة عامة على معيار CIS Kubernetes

ينفذ `kube-bench` الاختبارات المحددة في ملفات YAML المطابقة لأقسام CIS Benchmark:
1. **مكونات مستوى التحكم:** خادم واجهة برمجة التطبيقات، ومدير وحدة التحكم، والمجدول.
2. **etcd:** أذونات الملفات، وشهادات TLS، وتشفير العميل.
3. ** تكوين مستوى التحكم: ** المصادقة، والترخيص، وRBAC، ووحدات تحكم القبول.
4. **عقد العامل:** تكوين Kubelet وملفات الخدمة ومصادقة TLS.
5. **السياسات:** RBAC، ومعايير أمان Pod/القبول، وسياسات الشبكة، وإدارة الأسرار.

الحالات التي يتم إرجاعها لكل اختبار:
- `[PASS]`: يفي بمعايير CIS المعيارية.
- `[FAIL]`: ينحرف عن التوصية؛ يتطلب العلاج.
- `[WARN]`: التحقق يدويًا (يعتمد على المتغيرات البيئية).
- `[INFO]`: مذكرة إعلامية.

---

## طرق التثبيت والنشر

### 1. قم بتنزيل الملف الثنائي المترجم مسبقًا
```bash
# Download latest release from GitHub
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Verify installation
kube-bench version
```
### 2. التشغيل مباشرة داخل حاوية Docker
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```
### 3. النشر كمهمة Kubernetes
قم بالتشغيل مباشرة داخل المجموعة باستخدام بيان الوظيفة الرسمي الخاص بـ Aqua Security:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Check job logs once completed
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```
---

## التشغيل على مستوى التحكم (العقد الرئيسية)

قم بتشغيل `kube-bench` على العقدة الرئيسية/مستوى التحكم لتدقيق الخدمات الأساسية:
```bash
# Run all control plane checks
kube-bench run --targets master

# Run specifically against the API server and etcd
kube-bench run --targets master,etcd
```
---

## يعمل على العقد العاملة

يتم التنفيذ على العقد العاملة لتدقيق البرنامج الخفي `kubelet` ووقت تشغيل الحاوية:
```bash
kube-bench run --targets node
```
التحقق من الشيكات:
- أذونات ملف تكوين Kubelet (`600` أو `644`).
- تم تعطيل المصادقة المجهولة (`--anonymous-auth=false`).
- تم ضبط وضع التفويض على `Webhook`.
- تم تعطيل منفذ القراءة فقط (`10255`).

---

## Kubernetes المُدارة (EKS، GKE، AKS)

يقوم موفرو الخدمات السحابية بإدارة مستوى التحكم الرئيسي على EKS وGKE وAKS. قم بمراجعة العقد العاملة والتكوينات الخاصة بالسحابة فقط:

### أمازون إي كي إس
```bash
kube-bench run --benchmark eks-1.4.0 --targets node
```
### جوجل جي كي إي
```bash
kube-bench run --benchmark gke-1.4.0 --targets node
```
### مايكروسوفت أكس
```bash
kube-bench run --benchmark aks-1.4.0 --targets node
```
---

## تصفية الاختبارات والمقاييس

قم بتشغيل مجموعات فرعية مستهدفة من عمليات التحقق لتركيز جهود الإصلاح:
```bash
# Run specific test check IDs (e.g. 1.2.1 and 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Run an entire section (e.g. Section 1: Control Plane)
kube-bench run --scored --targets master

# Specify exact Kubernetes version benchmark
kube-bench run --version 1.28
```
---

## إخراج JSON وJUnit للأتمتة

دمج فحص الامتثال في SIEM أو لوحات معلومات الامتثال:

### 1. تصدير JSON
```bash
kube-bench run --output json > kube-bench-report.json
```
### 2. تصدير JUnit XML (خطوط أنابيب CI/CD)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```
---

## معالجة وتصلب سير العمل

في نهاية كل عملية تشغيل، يوفر `kube-bench` أوامر معالجة مفصلة للاختبارات الفاشلة.

مثال الفشل:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```
نصيحة العلاج التي تم إنشاؤها:
```text
== Remediation 1.2.6 ==
Edit the API server pod specification file /etc/kubernetes/manifests/kube-apiserver.yaml
on the control plane node and set the below parameter:
--anonymous-auth=false
```
### الإصلاح خطوة بخطوة:
1. افتح `/etc/kubernetes/manifests/kube-apiserver.yaml`.
2. قم بإضافة أو تحديث `--anonymous-auth=false` ضمن `spec.containers[0].command`.
3. احفظ الملف. يقوم Kubelet بإعادة تشغيل الكبسولة الثابتة تلقائيًا.
4. أعد تشغيل `kube-bench run --check 1.2.6` للتحقق من `[PASS]`.