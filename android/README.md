# Hacking Cheatsheets - Android Application

Hacking Cheatsheets dokümantasyonunun tamamını (138 rehber, hem İngilizce hem Türkçe) çevrimdışı (offline) olarak sunan yerel Android mobil uygulaması.

---

## Özellikler

- **%100 Çevrimdışı Çalışma:** Tüm dokümanlar ve komutlar yerel SQLite veritabanına gömülüdür. İnternet bağlantısı gerektirmez.
- **Hızlı Arama:** Başlık, kategori, komut ve içerik üzerinde anlık tam metin arama.
- **Çift Dil Desteği (🇹🇷 TR / 🇬🇧 EN):** Herhangi bir ekranda tek dokunuşla dil değiştirme.
- **Tek Dokunuşla Komut Kopyalama:** Kod bloklarının yanındaki kopyalama butonu ile komutları panoya hızlı kopyalama.
- **Sık Kullanılanlar / Yer İmleri:** Sık kullandığınız cheatsheet'leri favorilere ekleme ve tek ekrandan erişim.
- **Pentest Yardımcı Araçları (Utilities):**
  - **Reverse Shell Generator:** IP, Port ve kabuk türü seçerek hazır reverse shell komutları üretme (Bash, Python, Netcat, PowerShell, Perl, Ruby, PHP).
  - **Encoder / Decoder:** Base64 ve URL kodlama/çözme.
  - **Hash Identifier:** MD5, SHA-1, SHA-256, NTLM, bcrypt vb. hash türlerini formatına göre tespit etme.
- **Modern Arayüz:** Jetpack Compose & Material 3 Dark / Cyberpunk teması.

---

## Ekran Görüntüleri & Mimarisi

- **Dil / Çatı:** Kotlin, Jetpack Compose, Material Design 3
- **Veri Katmanı:** Android Room Database (`cheatsheets.db`), FTS indeksleme
- **Mimari:** MVVM (Model-View-ViewModel) + StateFlow
- **Minimum SDK:** Android 8.0 (API 26)
- **Hedef SDK:** Android 15 (API 35)

---

## Derleme ve Kurulum

### Gereksinimler
- JDK 17
- Android SDK (API 35 ve Build-Tools)

### Debug APK Derleme

```bash
cd android
./gradlew assembleDebug
```

Çıktı APK dosyası:
`android/app/build/outputs/apk/debug/app-debug.apk`

### Cihaza Yükleme (ADB)

```bash
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

---

## Veritabanını Yeniden Oluşturma

Depodaki Markdown dosyaları güncellendiğinde mobil veritabanını güncellemek için:

```bash
python3 scripts/build_mobile_db.py
```

Bu script `en/` ve `tr/` klasörlerindeki tüm cheatsheet'leri tarar, hızlı komutları ayrıştırır ve `android/app/src/main/assets/cheatsheets.db` dosyasını otomatik üretir.
