package kasir_kafe_shop.model;

public class Transaksi {

    private String tanggal, pelanggan, items, kasir;
    private int total;

    public Transaksi(String tanggal, String pelanggan, int total, String items, 
                     String kasir) {
        this.tanggal = tanggal;
        this.pelanggan = pelanggan;
        this.total = total;
        this.items = items;
        this.kasir = kasir;
    }

    public String getKasir() {
        return kasir;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public int getTotal() {
        return total;
    }

    public String getItems() {
        return items;
    }

    public String getTotalFormatted() {
        return String.format("Rp %,d", total).replace(',', '.');
    }
}
