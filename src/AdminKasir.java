import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class AdminKasir {
    private static final Map<String, Barang> listBaranggAdminkasir = DataBarang.getDataBarang();

    public void tampilkanMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu Admin Kasir ===");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Lihat List Barang dan Stok");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Update Stok Barang");
            System.out.println("5. Kembali ke Menu Utama");

            System.out.print("Pilihan: ");
            int pilihanAdminKasir;

            try {
                pilihanAdminKasir = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Inputan Anda tidak valid, silahkan masukkan inputan yang benar!");
                e.printStackTrace();
                scanner.next();
                continue;
            }

            scanner.nextLine();

            switch (pilihanAdminKasir) {
                case 1:
                    TambahkanBarangBaru(scanner);
                    break;
                case 2:
                    tampilkanStock();
                    break;
                case 3:
                    hapusBarang(scanner);
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

    private void TambahkanBarangBaru(Scanner scanner) {
        System.out.print("Masukkan nama barang baru: ");
        String namaBarangBaru = scanner.nextLine();

        System.out.print("Masukkan harga barang: Rp. ");
        int hargaBarangBaru = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Masukkan stok barang: ");
        int stockBarangBaru = scanner.nextInt();
        scanner.nextLine();

        Barang barangBaru = new Barang(namaBarangBaru, hargaBarangBaru, stockBarangBaru);
        String nomorUrutBarangBaru = Integer.toString(listBaranggAdminkasir.size() + 1);
        listBaranggAdminkasir.put(nomorUrutBarangBaru, barangBaru);

        DataBarang.saveDataBarang();  // Simpan data ke file

        System.out.println("Barang baru \"" + namaBarangBaru + "\" dengan stok " + stockBarangBaru + " berhasil ditambahkan ke dalam daftar Barang.");
    }

    private void hapusBarang(Scanner scanner) {
        tampilkanStock();

        System.out.print("Masukkan nomor barang yang ingin dihapus: ");
        String pilihan = scanner.nextLine();

        if (ApakahBarangValid(pilihan)) {
            Barang barang = listBaranggAdminkasir.get(pilihan);
            listBaranggAdminkasir.remove(pilihan);

            DataBarang.saveDataBarang();  // Simpan data ke file

            System.out.println(barang.getNama() + " berhasil dihapus dari daftar barang.");
        } else {
            System.out.println("Barang tidak valid.");
        }
    }

    private void perbaruiStok(Scanner scanner) {
        tampilkanStock();

        System.out.print("Masukkan nomor barang yang ingin diupdate stoknya: ");
        String pilihanUpdateBarang = scanner.nextLine();

        if (ApakahBarangValid(pilihanUpdateBarang)) {
            Barang barang = listBaranggAdminkasir.get(pilihanUpdateBarang);
            System.out.print("Masukkan jumlah stok baru: ");
            int newStock = scanner.nextInt();
            scanner.nextLine();

            barang.setStok(newStock);  // Update stok

            DataBarang.saveDataBarang();  // Simpan data ke file

            System.out.println("Stok barang berhasil diupdate.");
        } else {
            System.out.println("Barang tidak valid.");
        }
    }

    private void tampilkanStock() {
        System.out.println("\nStok Barang:");
        for (Map.Entry<String, Barang> entry : listBaranggAdminkasir.entrySet()) {
            String nomorUrut = entry.getKey();
            Barang barang = entry.getValue();
            System.out.println(nomorUrut + ". " + barang.getNama() + " - Stok: " + barang.getStok());
        }
    }

    private boolean ApakahBarangValid(String pilihan) {
        return listBaranggAdminkasir.containsKey(pilihan);
    }
}