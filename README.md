# ☕ Kafe Genzigma — Sistem Kasir

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Java_Swing-GUI-4CAF50?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-Database-9C27B0?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**Aplikasi kasir desktop untuk kafe dengan koneksi database MySQL.**  
Mendukung manajemen produk, transaksi pelanggan, cetak struk, dan riwayat transaksi.

</div>

---

## 📖 Tentang Proyek

**Kafe Genzigma** adalah aplikasi Point of Sale (POS) berbasis Java Desktop yang dirancang untuk mengelola operasional kasir kafe. Sistem ini menghubungkan antarmuka Swing dengan database MySQL melalui JDBC, menerapkan arsitektur berlapis yang memisahkan logika bisnis, akses data, dan tampilan secara bersih.

---

## ✨ Fitur

| Fitur | Deskripsi |
|---|---|
| 🔐 **Login & Sign Up** | Autentikasi karyawan dengan akun terpisah per kasir |
| 🛒 **Transaksi Kasir** | Tambah item ke keranjang, hitung total, tunai, dan kembalian |
| 📦 **Manajemen Stok** | Tambah, edit, dan hapus produk beserta harga |
| 👤 **Data Pelanggan** | Catat nama pelanggan dan nomor meja |
| 📋 **Catatan Pesanan** | Tambahkan instruksi khusus per transaksi |
| 🧾 **Cetak Struk** | Simpan struk transaksi otomatis ke file `.txt` |
| 📂 **Riwayat Transaksi** | Lihat semua transaksi lengkap dengan detail pesanan |
| 🗑️ **Hapus Transaksi** | Hapus data transaksi dari database |

---

## 🏗️ Arsitektur

Proyek ini menerapkan pola **Layered Architecture** yang memisahkan tanggung jawab setiap komponen:

```
src/kasir_kafe_shop/
├── KasirKafe.java          # Entry point aplikasi
│
├── frame/                  # Lapisan UI (JFrame)
│   ├── LoginFrame.java     # Halaman login
│   ├── SignUpFrame.java    # Halaman registrasi kasir baru
│   ├── MainFrame.java      # Halaman utama transaksi kasir
│   ├── StockManagementFrame.java  # Halaman manajemen produk
│   ├── PelangganFrame.java # Dialog input data pelanggan
│   └── RiwayatFrame.java   # Halaman riwayat transaksi
│
├── panel/
│   └── BuatPanel.java      # Factory untuk komponen UI yang dapat dipakai ulang
│
├── handler/                # Lapisan event handling (listener)
│   ├── LoginFrameHandler.java
│   ├── SignUpFrameHandler.java
│   ├── BuatPanelHandler.java
│   ├── StockManagementHandler.java
│   ├── PelangganHandler.java
│   └── HoverHandler.java
│
├── dao/                    # Lapisan Data Access Object (JDBC)
│   ├── KaryawanDAO.java    # Query: login, registrasi karyawan
│   ├── ProdukDAO.java      # Query: CRUD produk
│   └── TransaksiDAO.java   # Query: simpan, lihat, cetak, hapus transaksi
│
├── model/                  # Lapisan Model / POJO
│   ├── Produk.java
│   ├── Pelanggan.java
│   └── Transaksi.java
│
└── util/
    ├── DBConnection.java           # Koneksi ke MySQL
    └── ModelTableTidakBisaDiubah.java  # Custom TableModel read-only
```

### Konsep yang Diterapkan

- **DAO Pattern** — Semua query SQL terpusat di package `dao`, bukan tersebar di UI
- **MVC-Like Separation** — Frame (View) tidak menangani logika bisnis secara langsung
- **Handler Pattern** — Event listener dipisahkan ke class Handler tersendiri
- **PreparedStatement** — Semua query menggunakan PreparedStatement untuk mencegah SQL Injection
- **Transaction dengan Commit/Rollback** — Penyimpanan transaksi menggunakan `setAutoCommit(false)` dan `commit()`

---

## 🗄️ Setup Database

### 1. Buat Database

```sql
CREATE DATABASE kasir_kafe_db;
USE kasir_kafe_db;
```

### 2. Buat Tabel

```sql
CREATE TABLE karyawan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nama VARCHAR(100)
);

CREATE TABLE produk (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    harga INT NOT NULL,
    stok INT DEFAULT 0
);

CREATE TABLE transaksi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pelanggan VARCHAR(100),
    catatan TEXT,
    total INT,
    tunai INT,
    kembalian INT,
    kasir VARCHAR(50),
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transaksi_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaksi_id INT,
    produk_nama VARCHAR(100),
    qty INT,
    harga_satuan INT,
    subtotal INT,
    FOREIGN KEY (transaksi_id) REFERENCES transaksi(id) ON DELETE CASCADE
);
```

### 3. Konfigurasi Koneksi

Edit file `src/kasir_kafe_shop/util/DBConnection.java`:

```java
String host = "localhost";
String port = "3306";
String usr  = "root";       // sesuaikan username MySQL kamu
String pwd  = "";           // sesuaikan password MySQL kamu
```

---

## 🚀 Cara Menjalankan

### Prasyarat

- **Java JDK 8+**
- **MySQL Server** (local)
- **MySQL Connector/J** (JDBC Driver) — tambahkan ke classpath/library project
- **NetBeans IDE** (direkomendasikan)

### Langkah

```bash
# 1. Clone repository
git clone https://github.com/putra-as-kyuutora/kasir-kafe-shop.git
cd kasir-kafe-shop

# 2. Setup database (jalankan SQL di atas di MySQL Workbench atau terminal MySQL)

# 3. Pastikan MySQL Connector/J sudah ditambahkan ke project library
```

**Via NetBeans:**
1. Buka NetBeans → **File → Open Project**
2. Pilih folder hasil clone
3. Klik kanan project → **Properties → Libraries** → tambahkan `mysql-connector-java.jar`
4. Tekan **F6** untuk menjalankan

---

## 💻 Contoh Struk Transaksi

```
════════════════════════════════════
       KAFE GENZIGMA
════════════════════════════════════

ID Transaksi   : 24
Tanggal        : 30-11-2025 22:25:13
Kasir          : admin
Pelanggan      : Jamal (Meja 1)
Catatan        : Tidak pake sambel

------------------------------------
          DAFTAR PESANAN
------------------------------------
  Mie Goreng           1 x Rp   17.500 = Rp     17.500
  Ayam Katsu           1 x Rp   18.000 = Rp     18.000
------------------------------------
TOTAL          : Rp       35.500
TUNAI          : Rp       50.000
KEMBALIAN      : Rp       14.500

     Terima kasih atas kunjungannya!
        Selamat datang kembali!
════════════════════════════════════
```

Struk disimpan otomatis di folder `riwayat_transaksi/` dengan format nama:
`struk_[ID]_[TANGGAL]_[NAMA_PELANGGAN].txt`

---

## 🎯 Konsep yang Dipelajari

- ✅ **JDBC & MySQL** — Koneksi database, PreparedStatement, ResultSet
- ✅ **DAO Pattern** — Pemisahan logika akses data dari UI
- ✅ **Database Transaction** — `setAutoCommit(false)`, `commit()`, `rollback()`
- ✅ **Foreign Key & Cascade Delete** — Relasi tabel transaksi dan transaksi_detail
- ✅ **Java Swing GUI** — JFrame, JTable, JDialog, event handling
- ✅ **Handler Pattern** — Event listener dipisah dari kode UI
- ✅ **File I/O** — Ekspor struk transaksi ke file `.txt`
- ✅ **SQL Injection Prevention** — Semua query pakai PreparedStatement

---

## 👤 Developer

**Eka Alssah Putra** — Mahasiswa Informatika

[![GitHub](https://img.shields.io/badge/GitHub-putra--as--kyuutora-181717?style=flat-square&logo=github)](https://github.com/putra-as-kyuutora)
[![Email](https://img.shields.io/badge/Email-putra.dvpr@gmail.com-EA4335?style=flat-square&logo=gmail)](mailto:putra.dvpr@gmail.com)

---

## 📄 Lisensi

Proyek ini dilisensikan di bawah [MIT License](LICENSE).
