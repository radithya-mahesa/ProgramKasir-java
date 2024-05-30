import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Kasir {

    private static final String STRUK_FILE = "CetakStruk.txt"; // Mencetak Struk

    private static final Map<String, Barang> listBaranggKasir= DataBarang.getDataBarang(); //memang bernama listBarang berdasarkan class "DataBarang"

    public void tampilkanMenu(Scanner scanner) {
        System.out.println("\n=== Menu Kasir ===");
        int totalBiaya = 0;

        List<JumlahBeliBarang> barangYangDibeli = new ArrayList<>(); // membuat list baru bernama barangYangDibeli berdasarkan class JumlahBeliBarang

        while (true) {
            tampilkanBarang();
            System.out.print("Pilih barang dari nomor urut berikut atau '0' untuk selesai (Memilih Barang): ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("0")) { // keluar dari loop
                break;
            }

            if (pilihan.equals("Kembali")) { //kembali ke tampilan awal
                return;
            }

            if (!ApakahBarangValid(pilihan)) { //mengecek pilihan  barang
                System.err.println("\nInputan Tidak valid, silahkan Masukan inputan Yang Benar !!!");
                continue;
            }

            String namaBarang = listBaranggKasir.get(pilihan).getNama(); // membuat variable namaBarang berdasarkan listbarang
            int hargaBarang = listBaranggKasir.get(pilihan).getHarga(); // membuat variable hargaBarang berdasarkan listBarang

            System.out.print("Masukkan jumlah barang yang ingin dibeli: ");
            int jumlahBarang = scanner.nextInt();
            scanner.nextLine(); // membaca nilai string dari pengguna

            if (!apakahStockTersedia(pilihan, jumlahBarang)) { // ngecek barang apakah stock barang cukup
                System.out.println("Maaf, stok tidak mencukupi untuk jumlah yang diminta, Silakan memilih barang yang lain.");
                continue;
            }

            int totalBiayaPerBarang = hargaBarang * jumlahBarang;
            totalBiaya += totalBiayaPerBarang; /// total biaya semua barang, contoh nya jika beli barang 2 dan jumlah barang nya masing-masing 3 ,
            // total satu 1 barang 25.000, satu lagi 28.000, ini fungsinya ditambahin jadi 25 ditambah 28.

            barangYangDibeli.add(new JumlahBeliBarang(namaBarang, jumlahBarang)); //menambah barang.
            MengurangiStok(pilihan, jumlahBarang); // mengurangi barang.

            System.out.println("Barang berhasil ditambahkan ke dalam keranjang belanja.");
        }

        while (true) {
            if (!barangYangDibeli.isEmpty()) { // mengecek apakah barang yang dibeli itu tidak kosong,jika tidak kosong maka pembayaran dilanjutkan.
                System.out.println("===> Total biaya yang harus dibayar: Rp. " + totalBiaya + " <===");
                System.out.print("Masukkan jumlah uang yang dibayar: Rp. ");
                int jumlahUangYangHarusDibayar = scanner.nextInt();
                scanner.nextLine();

                if (jumlahUangYangHarusDibayar < totalBiaya) {
                    System.out.println("\nMaaf, uang Anda tidak mencukupi untuk pembelian ini.");
                    System.out.println("===> Apakah Anda Jadi Membeli Barang? (y/n) <===");
                    String jadiBeliBarang = scanner.nextLine();

                    if (!jadiBeliBarang.equals("y")) { //kebalikan y adalah n,jadi bakal keluar program.
                        System.out.println("Pembelian dibatalkan.");
                        System.exit(0);
                    }

                } else {
                    int kembalian = jumlahUangYangHarusDibayar - totalBiaya;
                    System.out.println("Uang kembali: Rp. " + kembalian);

                    try {
                        mencetakStruk(barangYangDibeli, totalBiaya, jumlahUangYangHarusDibayar, kembalian);
                        System.out.println("Terima kasih telah berbelanja, Semoga harimu menyenangkan");
                        System.exit(0);
                    } catch (IOException e) {
                        System.err.println("Terjadi kesalahan saat mencetak struk.");
                    }
                }
            }
        }
    }

    private void tampilkanBarang() {
        System.out.println("\nDaftar Barang:");
        for (Map.Entry<String, Barang> entry : listBaranggKasir.entrySet()) {
            String nomorUrut = entry.getKey();
            Barang barang = entry.getValue();
            System.out.println(nomorUrut + ". " + barang.getNama() + " (Rp. " + barang.getHarga() + ") - Stok: " + barang.getStok());
        }
    }

    private boolean ApakahBarangValid(String pilihan) {
        return listBaranggKasir.containsKey(pilihan);
    }

    private boolean apakahStockTersedia(String pilihan, int jumlahBarang) {
        if (listBaranggKasir.containsKey(pilihan)) {
            Barang barang = listBaranggKasir.get(pilihan);
            return barang.getStok() >= jumlahBarang;
        }
        return false;
    }

    private void MengurangiStok(String pilihan, int jumlahBarang) {
        if (listBaranggKasir.containsKey(pilihan)) {
            Barang barang = listBaranggKasir.get(pilihan);
            int stokSaatIni = barang.getStok();
            barang.setStok(stokSaatIni - jumlahBarang);
            DataBarang.saveDataBarang(); //mengsave databarang berdasarkan class data barang yang di simpan didalam class menyimpan dataBarang
        }
    }

    private void mencetakStruk(List<JumlahBeliBarang> barangYangDibeli, int totalBiaya, int jumlahUangYangHarusDibayar, int kembalian) throws IOException {// melampar io execption jadi tidak papa tanpa menggunakan catch.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STRUK_FILE, true))) {
            writer.write("\n=== Struk Pembelian ===");
            writer.newLine();

            writer.write("Barang yang dibeli:");
            writer.newLine();
            for (JumlahBeliBarang item : barangYangDibeli) {
                writer.write("- " + item.getNamaBarang() + " x " + item.getJumlahBarang());
                writer.newLine();
            }

            writer.write(String.format("Total Biaya: Rp. %d", totalBiaya)); // format digunakan agar kita bisa menggunakan placeholder d,singkatnya nilai int dijadikan string jadi satu
            writer.newLine();
            writer.write(String.format("Jumlah Uang: Rp. %d", jumlahUangYangHarusDibayar));
            writer.newLine();
            writer.write(String.format("Kembalian: Rp. %d", kembalian));
            writer.newLine();
        }
    }
}