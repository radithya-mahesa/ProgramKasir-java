import java.util.InputMismatchException; // pengecualian input tan tipe data.
import java.util.Scanner;
import java.util.*;

public class KasirApp {
    private static final Map<String, Barang> listBaranggKasirApp = DataBarang.getDataBarang();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InisialisasiStokDanListBarang();
        System.out.println("Selamat datang di Program Kasir\n");
        while (true) {
            System.out.println("Silakan Login sebagai: ");
            System.out.println("1. Kasir");
            System.out.println("2. Admin Kasir");
            System.out.println("3. Keluar");

            int pilihanMenu;
            System.out.print("Pilihan: ");

            try {
                pilihanMenu = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) { // jika anda menginput data tapi tidak sesuai apa yang di input maka akan muncul pesan berikut.
                System.out.println("Inputan anda tidak valid, silahkan masukkan inputan yang benar !!!");
                e.printStackTrace();
                scanner.next();
                continue;
            }

            switch (pilihanMenu) {
                case 1:
                    Kasir kasir = new Kasir();
                    kasir.tampilkanMenu(scanner);
                    break;
                case 2:
                    AdminKasir adminKasir = new AdminKasir();
                    adminKasir.tampilkanMenu(scanner);
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan Program Kasir. Semoga harimu menyenangkan!");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nPilihan Anda tidak valid. Silakan pilih yang benar.");
                    break;
            }
        }
    }

    private static void InisialisasiStokDanListBarang() {
        listBaranggKasirApp.put("1", new Barang("Minyak Goreng", 43000, 10));
        listBaranggKasirApp.put("2", new Barang("Hotwheels Audi Rs6", 60000, 5));
        listBaranggKasirApp.put("3", new Barang("Voucher Google Play", 500000, 3));
        listBaranggKasirApp.put("4", new Barang("Kinderjoy", 16000, 20));
        listBaranggKasirApp.put("5", new Barang("Sponge Popcorn", 22000, 15));
        listBaranggKasirApp.put("6", new Barang("Gula Merah", 25000, 20));
        listBaranggKasirApp.put("7", new Barang("Gula Halus", 25000, 20));
        listBaranggKasirApp.put("8", new Barang("Gula Pasir", 25000, 20));
    }
}
