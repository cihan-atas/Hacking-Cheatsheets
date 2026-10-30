# kube-bench Bilgi Notu (Cheatsheet)

[kube-bench](https://github.com/aquasecurity/kube-bench), Aqua Security tarafından Go diliyle geliştirilmiş; bir Kubernetes kümesinin Center for Internet Security (CIS) Kubernetes Benchmark standartlarına uygun ve güvenli şekilde yapılandırılıp yapılandırılmadığını denetleyen açık kaynaklı bir güvenlik kontrol aracıdır.

---

## İçindekiler

- [CIS Kubernetes Benchmark Genel Bakış](#cis-kubernetes-benchmark-genel-bakış)
- [Kurulum & Çalıştırma Yöntemleri](#kurulum--çalıştırma-yöntemleri)
- [Master (Control Plane) Düğümlerinde Çalıştırma](#master-control-plane-düğümlerinde-çalıştırma)
- [Worker Düğümlerinde Çalıştırma](#worker-düğümlerinde-çalıştırma)
- [Yönetilen Kubernetes (EKS, GKE, AKS)](#yönetilen-kubernetes-eks-gke-aks)
- [Test ve Kriter Filtreleme](#test-ve-kriter-filtreleme)
- [Otomasyon İçin JSON & JUnit Çıktıları](#otomasyon-için-json--junit-çıktıları)
- [İyileştirme (Remediation) ve Sertleştirme](#iyileştirme-remediation-ve-sertleştirme)

---

## CIS Kubernetes Benchmark Genel Bakış

`kube-bench`, CIS Benchmark maddelerine karşılık gelen testleri yürütür:
1. **Control Plane Bileşenleri:** API Server, Controller Manager, Scheduler.
2. **etcd:** Dosya izinleri, TLS sertifikaları, şifreli istemci iletişimi.
3. **Control Plane Yapılandırması:** Kimlik doğrulama, RBAC, Admission Controllers.
4. **Worker Düğümleri:** Kubelet yapılandırması, servis dosyaları, TLS doğrulama.
5. **Güvenlik Politikaları:** RBAC kuralları, Pod Security Standards / Admission, NetworkPolicies.

Her kontrol için dönen durumlar:
- `[PASS]`: Kriter CIS Benchmark standardına tam uyumlu.
- `[FAIL]`: Tavsiyeye uyulmamış; güvenlik sertleştirmesi gerekiyor.
- `[WARN]`: Ortama göre manuel incelenmesi gereken durumlar.
- `[INFO]`: Bilgilendirme notu.

---

## Kurulum & Çalıştırma Yöntemleri

### 1. Hazır Derlenmiş İkili Dosyayı İndirme
```bash
curl -L https://github.com/aquasecurity/kube-bench/releases/download/v0.7.3/kube-bench_0.7.3_linux_amd64.tar.gz | tar -xz
sudo mv kube-bench /usr/local/bin/

# Kurulumu doğrulayın
kube-bench version
```

### 2. Docker Konteyneri ile Çalıştırma
```bash
docker run --rm -v /etc:/etc:ro -v /var:/var:ro -t aquasec/kube-bench:latest run --targets master
```

### 3. Kubernetes Job Olarak Çalıştırma
Aqua Security'nin resmi Job manifesti ile küme içine deploy edin:
```bash
kubectl apply -f https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml

# Job tamamlandığında logları inceleyin
kubectl get pods -l app=kube-bench
kubectl logs -l app=kube-bench
```

---

## Master (Control Plane) Düğümlerinde Çalıştırma

Tüm kontrol düzlemi servislerini denetlemek için master düğüm üzerinde çalıştırın:

```bash
# Tüm master kontrollerini yürüt
kube-bench run --targets master

# Yalnızca API Server ve etcd kontrollerini çalıştır
kube-bench run --targets master,etcd
```

---

## Worker Düğümlerinde Çalıştırma

İş yüklerini çalıştıran worker makinelerinde `kubelet` ve çalışma zamanını denetleyin:

```bash
kube-bench run --targets node
```

Denetlenen temel başlıklar:
- Kubelet konfigürasyon dosyası dosya izinleri (`600` veya `644`).
- Anonim erişimin kapatılması (`--anonymous-auth=false`).
- Yetkilendirme modunun `Webhook` olarak atanması.
- Salt okunur portun (`10255`) devre dışı bırakılması.

---

## Yönetilen Kubernetes (EKS, GKE, AKS)

EKS, GKE ve AKS ortamlarında master düğümler bulut sağlayıcı tarafından yönetilir. Yalnızca worker düğümleri ve sağlayıcıya özel kuralları denetleyin:

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

## Test ve Kriter Filtreleme

Aksiyon alınacak özel maddelere odaklanmak için:

```bash
# Belirli test maddelerini çalıştırın (örn. 1.2.1 ve 1.2.2)
kube-bench run --check 1.2.1,1.2.2

# Yalnızca puanlanan (scored) kuralları çalıştırın
kube-bench run --scored --targets master

# Kubernetes sürümüne uygun benchmark seçin
kube-bench run --version 1.28
```

---

## Otomasyon İçin JSON & JUnit Çıktıları

Güvenlik panolarına veya CI/CD denetimlerine aktarmak için:

### 1. JSON Formatında Dışa Aktarma
```bash
kube-bench run --output json > kube-bench-rapor.json
```

### 2. JUnit XML Çıktısı (CI/CD Entegrasyonu)
```bash
kube-bench run --output junit > kube-bench-junit.xml
```

---

## İyileştirme (Remediation) ve Sertleştirme

Her tarama sonucunda `kube-bench`, başarısız olan testler için adım adım çözüm kılavuzu sunar.

Örnek hata:
```text
[FAIL] 1.2.6 Ensure that the --anonymous-auth argument is set to false (Automated)
```

Üretilen iyileştirme adımı:
```text
== Remediation 1.2.6 ==
Control plane düğümündeki /etc/kubernetes/manifests/kube-apiserver.yaml
dosyasını açın ve aşağıdaki parametreyi ekleyin:
--anonymous-auth=false
```

### Adım Adım Düzeltme:
1. `/etc/kubernetes/manifests/kube-apiserver.yaml` dosyasını düzenleyin.
2. `spec.containers[0].command` altına `--anonymous-auth=false` ekleyin.
3. Dosyayı kaydedin (Kubelet api-server podunu otomatik yeniden başlatır).
4. `kube-bench run --check 1.2.6` komutuyla düzeltmeyi doğrulayın (`[PASS]`).
