package kasir_kafe_shop.model;

import java.util.ArrayList;

public class Pelanggan {

    private String nama;
    private String nomorMeja;
    private String catatan;
    private static String lastCatatan = "";
    private static ArrayList<Pelanggan> daftarPelanggan = new ArrayList<>();
    private static Pelanggan currentPelanggan = null;

    public Pelanggan(String nama, String nomorMeja, String catatan) {
        this.nama = nama;
        this.nomorMeja = nomorMeja;
        this.catatan = catatan;
    }

    public static void tambahPelanggan(Pelanggan pelanggan) {
        daftarPelanggan.add(pelanggan);
        currentPelanggan = pelanggan;
    }

    public static Pelanggan getCurrentPelanggan() {
        return currentPelanggan;
    }

    public static void resetCurrentPelanggan() {
        currentPelanggan = null;
    }
    
    public String getNama() {
        return nama;
    }

    public String getNomorMeja() {
        return nomorMeja;
    }

    public String getCatatan() {
        return catatan;
    }

    public static String getLastCatatan() {
        return lastCatatan;
    }

    public static void setLastCatatan(String catatan) {
        lastCatatan = catatan;
    }
}
