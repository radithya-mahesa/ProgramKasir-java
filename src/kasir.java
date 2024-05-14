import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class kasir {
    private static final String STRUK_FILE = "CetakStruk.txt";/*
    Code Yang Ada Kata Kunci Final nya Benilai Konstanta (Tidak Bisa diubah),
    Di Code INI Bernama Struk_fie Yang Menyimpan nilai CetakStruk,
    yang dimana Struck akan di cetak, dengan kode ini juga dapat membantu dalam menghindari kesalahan penulisan nama file
    dan memudahkan pengelolaan perubahan nama file jika diperlukan.
    */
    private static final Map<String, Barang> stock = new HashMap<>();/*
    Code ini merepresentasikan sebuah kumpulan pasangan key-value (pasangan kunci dan nilai). Di dalam Map,
    yang dimana KEY nya itu "String" Dan VALUE Nya Itu "int", Contohnya "Minyak Goreng" itu KEY dan "Rp.43000" Itu VALUE
    Fungsinya Yakni Untuk Mempermudah Menambah,MengUpdate,Menyimpan,Menghapus Data.
    */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        InisialisasiStokBarang();// Memberi nilai awal Stock barang

        System.out.println("Selamat datang di Program Kasir\n");

        while (true) {
            System.out.println("Silakan login sebagai:");
            System.out.println("1. Kasir");
            System.out.println("2. Admin Kasir");
            System.out.println("3. Keluar");

            System.out.print("Pilihan: ");
            int pilihanMenu = scanner.nextInt();
            scanner.nextLine();

            switch (pilihanMenu) {
                case 1:
                    MenuKasir(scanner);
                    break;
                case 2:
                    MenuAdminKasir(scanner);
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan Program Kasir. Semoga harimu menyenangkan!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih menu yang benar.");
                    break;
            }
        }
    }

    private static void MenuKasir(Scanner scanner) {
        System.out.println("\n=== Menu Kasir ===");
        int totalBiaya = 0;
        List<String> barangYangDibeli = new ArrayList<>();

        while (true) {
            tampilkanBarang();
            System.out.print("Pilih barang dengan No.Urut atau '0' untuk selesai: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("0")) {
                break;
            }

            if (!ApakahBarangValid(pilihan)) {
                System.out.println("Barang tidak valid. Silakan pilih kembali.");
                continue;
            }

            String namaBarang = stock.get(pilihan).getNama();
            int hargaBarang = stock.get(pilihan).getHarga();

            System.out.print("Masukkan jumlah barang yang ingin dibeli: ");
            int jumlahBarang = scanner.nextInt();
            scanner.nextLine();

            if (!apakahStockTersedia(pilihan, jumlahBarang)) {
                System.out.println("Maaf, stok tidak mencukupi untuk jumlah yang diminta.");
                continue;
            }

            int totalBiayaPerBarang = hargaBarang * jumlahBarang;
            totalBiaya += totalBiayaPerBarang;

            // Mengurangi Stok saat transaksi berhasil
            MengurangiStok(pilihan, jumlahBarang);
            barangYangDibeli.add(namaBarang);

            System.out.println("Barang berhasil ditambahkan ke keranjang.");
        }

        System.out.println("\nTotal biaya semua barang: Rp. " + totalBiaya);

        System.out.print("Masukkan jumlah uang: Rp. ");
        int jumlahUangYangHarusDibayar = scanner.nextInt();
        scanner.nextLine(); //

        if (jumlahUangYangHarusDibayar < totalBiaya) {
            System.out.println("Maaf, uang Anda tidak mencukupi untuk pembelian ini.");
        } else {
            int kembalian = jumlahUangYangHarusDibayar - totalBiaya;
            System.out.println("Uang kembali: Rp. " + kembalian);

            try {
                mencetakStruk(barangYangDibeli, totalBiaya, jumlahUangYangHarusDibayar, kembalian);
                System.out.println("Terima kasih telah berbelanja!");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat mencetak struk.");
                e.printStackTrace();
            }
        }
    }

    private static void MenuAdminKasir(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu Admin Kasir ===");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Lihat List Barang dan Stok");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Update Stok Barang");
            System.out.println("5. Kembali ke Menu Utama");

            System.out.print("Pilihan: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    TambahkanBarangBaru(scanner);
                    break;
                case 2:
                    tampilkanStock();
                    break;
                case 3:
                    removeItem(scanner);
                    break;
                case 4:
                    perbaruiStok(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    private static void TambahkanBarangBaru(Scanner scanner) {
        System.out.print("Masukkan nama barang baru: ");
        String namaBarangBaru = scanner.nextLine();

        System.out.print("Masukkan harga barang: Rp. ");
        int hargaBarangBaru = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Masukkan stok barang: ");
        int stockBarangBaru = scanner.nextInt();
        scanner.nextLine();

        Barang barangBaru = new Barang(namaBarangBaru, hargaBarangBaru, stockBarangBaru);
        String nomorUrutBarangBaru = Integer.toString(stock.size() + 1);
        stock.put(nomorUrutBarangBaru, barangBaru);

        System.out.println("Barang baru \"" + namaBarangBaru + "\" dengan stok " + stockBarangBaru + " berhasil ditambahkan ke dalam stok.");
    }

    private static void removeItem(Scanner scanner) {
        tampilkanStock();

        System.out.print("Masukkan nomor barang yang ingin dihapus: ");
        String pilihan = scanner.nextLine();

        if (ApakahBarangValid(pilihan)) {
            Barang barang = stock.get(pilihan);
            stock.remove(pilihan);
            System.out.println(barang.getNama() + " berhasil dihapus dari stok.");
        } else {
            System.out.println("Barang tidak valid.");
        }
    }

    private static void perbaruiStok(Scanner scanner) {
        tampilkanStock();

        System.out.print("Masukkan nomor barang yang ingin diupdate stoknya: ");
        String pilihan = scanner.nextLine();

        if (ApakahBarangValid(pilihan)) {
            Barang barang = stock.get(pilihan);
            System.out.print("Masukkan jumlah stok baru: ");
            int newStock = scanner.nextInt();
            scanner.nextLine();

            barang.setStok(newStock);
            System.out.println("Stok barang berhasil diupdate.");
        } else {
            System.out.println("Barang tidak valid.");
        }
    }

    private static void InisialisasiStokBarang() {
        stock.put("1", new Barang("Minyak Goreng", 43000, 10));
        stock.put("2", new Barang("Hotwheels Audi Rs6", 60000, 5));
        stock.put("3", new Barang("Voucher Google Play", 500000, 3));
        stock.put("4", new Barang("Kinderjoy", 16000, 20));
        stock.put("5", new Barang("Sponge Popcorn", 22000, 15));
    }

    private static void tampilkanBarang() {
        System.out.println("\nDaftar Barang:");
        for (Map.Entry<String, Barang> entry : stock.entrySet()) {
            String nomorUrut = entry.getKey();
            Barang barang = entry.getValue();
            System.out.println(nomorUrut + ". " + barang.getNama() + " (Rp. " + barang.getHarga() + ") - Stok: " + barang.getStok());
        }
    }

    private static void tampilkanStock() {
        System.out.println("\nStok Barang:");
        for (Map.Entry<String, Barang> entry : stock.entrySet()) {
            Barang barang = entry.getValue();
            System.out.println(barang.getNama() + " - Stok: " + barang.getStok());
        }
    }

    private static boolean ApakahBarangValid(String pilihan) {
        return stock.containsKey(pilihan);
    }

    private static boolean apakahStockTersedia(String pilihan, int jumlahBarang) {
        if (stock.containsKey(pilihan)) {
            Barang barang = stock.get(pilihan);
            return barang.getStok() >= jumlahBarang;
        }
        return false;
    }

    private static void MengurangiStok(String pilihan, int jumlahBarang) {
        if (stock.containsKey(pilihan)) {
            Barang barang = stock.get(pilihan);
            int stokSaatIni = barang.getStok();
            barang.setStok(stokSaatIni - jumlahBarang);
        }
    }

    private static void mencetakStruk(List<String> barangYangDibeli, int totalBiaya, int jumlahUangYangHarusDibayar, int kembalian) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STRUK_FILE, true))) {
            writer.write("\n=== Struk Pembelian ===");
            writer.newLine();

            writer.write("Barang yang dibeli:");
            writer.newLine();
            for (String item : barangYangDibeli) {
                writer.write("- " + item);
                writer.newLine();
            }

            writer.write(String.format("Total Biaya: Rp. %d", totalBiaya));
            writer.newLine();
            writer.write(String.format("Jumlah Uang: Rp. %d", jumlahUangYangHarusDibayar));
            writer.newLine();
            writer.write(String.format("Kembalian: Rp. %d", kembalian));
            writer.newLine();
        }
    }
}

class Barang {
    private String nama;
    private int harga;
    private int stok;

    public Barang(String nama, int harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}