# 🕸️ InQL - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Kapsamlı GraphQL Güvenlik Değerlendirme & Introspection Motoru
```

<p align="center">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Güvenliği-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Geliştirici-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Kategori-Web_Güvenliği_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/Lisans-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p align="center">
  <b>🕸️ Doyensec tarafından geliştirilen, GraphQL introspection şemalarını döken, sorgu ve mutasyon şablonları üreten ve GraphQL API güvenliğini denetleyen CLI aracı</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve GraphQL Güvenlik Temelleri](#-genel-bakış-ve-graphql-güvenlik-temelleri)
  - [Introspection (İç Gözlem) Tehlikesi](#introspection-i̇ç-gözlem-tehlikesi)
- [Kurulum](#-kurulum)
- [Introspection Dökümü ve Şema Üretimi](#-introspection-dökümü-ve-şema-üretimi)
  - [Komut Satırından Şema Çekme](#komut-satırından-şema-çekme)
  - [Kimlik Doğrulamalı Tarama](#kimlik-doğrulamalı-tarama)
  - [SDL (Schema Definition Language) Formatına Dönüştürme](#sdl-schema-definition-language-formatına-dönüştürme)
- [Hassas Sorgu ve Mutasyonları Denetleme](#-hassas-sorgu-ve-mutasyonları-denetleme)
- [Hizmet Engelleme (DoS) ve Batching Açıkları](#-hizmet-engelleme-dos-ve-batching-açıkları)
  - [Dizi Tabanlı Batching (Array Batching)](#dizi-tabanlı-batching-array-batching)
  - [Özyinelemeli / İç İçe Sorgular (Circular Queries)](#özyinelemeli--i̇ç-içe-sorgular-circular-queries)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Sıkılaştırma](#-savunma-yöntemleri-ve-sıkılaştırma)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve GraphQL Güvenlik Temelleri

Her veri kaynağı için ayrı URL'lere (`/api/users`, `/api/posts`) sahip REST API'lerin aksine, GraphQL tek bir HTTP uç noktasından (`/graphql`) istemcinin istediği veriyi esnek sorgularla döndürür.

### Introspection (İç Gözlem) Tehlikesi
GraphQL geliştiricilerin işini kolaylaştırmak için `__schema` ve `__type` adında yerleşik bir iç gözlem (introspection) sistemi sunar. Eğer bu özellik canlıda açık unutulursa:
- Saldırgan tüm backend veri şemasını tek sorguda indirir.
- Henüz yayına alınmamış gizli fonksiyonlar, yönetici mutasyonları (`deleteUser`, `updateRole`) ve gizli alanlar açığa çıkar.

**Doyensec** tarafından geliştirilen **InQL**, bu introspection verilerini otomatik çeker, şablon sorgular oluşturur ve zafiyet analizini hızlandırır.

---

## 📥 Kurulum

```bash
# Python pip ile Kurulum (Önerilen)
pip install inql

# GitHub üzerinden kurulum
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Kurulumu doğrulama
inql -h
```

---

## 🚀 Introspection Dökümü ve Şema Üretimi

### Komut Satırından Şema Çekme
Uzak GraphQL uç noktasını sorgulayıp tüm şemayı klasöre dökme:
```bash
inql -t https://hedef.com/graphql -o /tmp/graphql_rapor/
```
*Oluşan klasörün içeriği:*
- `schema.json`: Ham JSON şeması.
- `schema.graphql`: İnsan tarafından okunabilir şema tanımı (SDL).
- `queries/`: Mevcut tüm sorguların hazır `.graphql` şablonları.
- `mutations/`: Tüm veri değiştirme fonksiyonlarının hazır şablonları.

### Kimlik Doğrulamalı Tarama
Oturum token'ı veya özel başlıklar gönderme:
```bash
inql -t https://hedef.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: gizli123" \
  -o ./audit_rapor/
```

### SDL (Schema Definition Language) Formatına Dönüştürme
Mevcut JSON şemasını okunabilir SDL metnine çevirme:
```bash
inql -f schema.json --generate-sdl -o ./sdl_cikti/
```

---

## 💥 Hassas Sorgu ve Mutasyonları Denetleme

Üretilen mutasyon dosyalarını inceleyerek yetki denetimi yapın:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```

Önemli mutasyonları curl veya Burp Suite üzerinden test etme:
```bash
curl -X POST https://hedef.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```

---

## ⚡ Hizmet Engelleme (DoS) ve Batching Açıkları

### Dizi Tabanlı Batching (Array Batching)
Sunucunun tek bir HTTP isteğinde dizi olarak gönderilen çoklu sorguları çalıştırıp çalıştırmadığını test etme (oran sınırlarını baypas eder):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```

### Özyinelemeli / İç İçe Sorgular (Circular Queries)
İlişkisel modellerde sonsuz döngü ve bellek tüketimi testi:
```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```

---

## ⚡ Hızlı Referans Tablosu

| Amaç | Komut |
|------|-------|
| **Introspection Şeması Çek** | `inql -t https://hedef.com/graphql -o ./out/` |
| **Kimlik Bilgisiyle Çek** | `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **JSON Şemayı SDL'e Çevir** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **HTML Dokümanı Üret** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Savunma Yöntemleri ve Sıkılaştırma

1. **Canlı Ortamda Introspection'ı Kapatın:** Apollo, Yoga veya GraphQL-Java ayarlarında üretim ortamı için `introspection: false` uygulayın.
2. **Sorgu Derinliği ve Maliyet Analizi:** `graphql-depth-limit` kütüphanesini kullanarak iç içe geçen döngüsel sorguları engelleyin.
3. **Batching Kısıtlaması:** Çoklu sorgu paketlemeyi devre dışı bırakın veya sınırlandırın.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> InQL yalnızca yetkili güvenlik testleri ve GraphQL API denetimleri için kullanılmalıdır. İzniniz olmayan API'ler üzerinde şema çekmek ve yetki aşımı denemek yasalara aykırıdır.
