package kasir_kafe_shop.model;

import java.util.ArrayList;

public class Produk {

    private String nama;
    private double harga;
    private String imagePath;
    private int stock;

    private static final ArrayList<Produk> makanan = new ArrayList<>();
    private static final ArrayList<Produk> minuman = new ArrayList<>();
    private static final ArrayList<Produk> cemilan = new ArrayList<>();
    
    private static boolean initialized = false;

    public Produk(String nama, double harga, String imagePath) {
        this.nama = nama;
        this.harga = harga;
        this.imagePath = imagePath;
    }


    public static ArrayList<Produk> getMakanan() {
        return new ArrayList<>(makanan);
    }

    public static ArrayList<Produk> getMinuman() {
        return new ArrayList<>(minuman);
    }

    public static ArrayList<Produk> getCemilan() {
        return new ArrayList<>(cemilan);
    }

    public static ArrayList<Produk> getSemuaProduk() {
        ArrayList<Produk> semua = new ArrayList<>();
        semua.addAll(makanan);
        semua.addAll(minuman);
        semua.addAll(cemilan);
        return semua;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void kurangiStock(int jumlah) {
        this.stock -= jumlah;
        if (this.stock < 0) {
            this.stock = 0;
        }
    }

    public void tambahStock(int jumlah) {
        this.stock += jumlah;
    }
}
