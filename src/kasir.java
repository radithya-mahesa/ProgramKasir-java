import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class kasir {
    private static final String STRUK_FILE = "CetakStruk.txt"; // ubah ke struk kamu dit,aku ngk muncul wae
    private static final Map<String, Integer> stock = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        initializeStock();

        System.out.println("Selamat datang di Program Kasir\n");

        while (true) {
            System.out.println("Silakan login sebagai:");
            System.out.println("1. Kasir");
            System.out.println("2. Admin Kasir");
            System.out.println("3. Keluar");

            System.out.print("Pilihan: ");
            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (menuChoice) {
                case 1:
                    kasirMenu(scanner);
                    break;
                case 2:
                    adminKasirMenu(scanner);
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan Program Kasir.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih menu yang benar.");
                    break;
            }
        }
    }

    private static void kasirMenu(Scanner scanner) {
        System.out.println("\n=== Menu Kasir ===");
        int totalBiaya = 0;
        List<String> purchasedItems = new ArrayList<>();

        while (true) {
            displayItems();
            System.out.print("Pilih barang (1-5) atau '0' untuk selesai: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }

            if (!isValidItem(choice)) {
                System.out.println("Barang tidak valid. Silakan pilih kembali.");
                continue;
            }

            String itemName = getItemName(choice);
            int itemPrice = getItemPrice(choice);

            System.out.print("Masukkan jumlah barang yang ingin dibeli: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (!isStockAvailable(choice, quantity)) {
                System.out.println("Maaf, stok tidak mencukupi untuk jumlah yang diminta.");
                continue;
            }

            int itemTotalCost = itemPrice * quantity;
            totalBiaya += itemTotalCost;

            // Mengurangi stok hanya jika transaksi berhasil
            reduceStock(choice, quantity);
            purchasedItems.add(itemName); // Tambahkan nama barang yang dibeli ke daftar

            System.out.println("Barang berhasil ditambahkan ke keranjang.");
        }

        System.out.println("\nTotal biaya semua barang: Rp. " + totalBiaya);

        System.out.print("Masukkan jumlah uang: Rp. ");
        int amountPaid = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (amountPaid < totalBiaya) {
            System.out.println("Maaf, uang Anda tidak mencukupi untuk pembelian ini.");
        } else {
            int change = amountPaid - totalBiaya;
            System.out.println("Uang kembali: Rp. " + change);

            try {
                // Print struk dengan informasi pembelian
                printReceipt(purchasedItems, totalBiaya, amountPaid, change);
                System.out.println("Terima kasih telah berbelanja!");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat mencetak struk.");
                e.printStackTrace();
            }
        }
    }

    private static void adminKasirMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu Admin Kasir ===");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Lihat List Barang dan Stok");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Update Stok Barang");
            System.out.println("5. Kembali ke Menu Utama");

            System.out.print("Pilihan: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addNewItem(scanner);
                    break;
                case 2:
                    displayStock();
                    break;
                case 3:
                    removeItem(scanner);
                    break;
                case 4:
                    updateStock(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    private static void addNewItem(Scanner scanner) {
        System.out.print("Masukkan nama barang baru: ");
        String newItemName = scanner.nextLine();

        System.out.print("Masukkan harga barang: Rp. ");
        int newItemPrice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Masukkan stok barang: ");
        int newItemStock = scanner.nextInt();
        scanner.nextLine(); // consume newline

        stock.put(Integer.toString(stock.size() + 1), newItemStock);
        System.out.println("Barang baru berhasil ditambahkan ke dalam stok.");
    }

    private static void removeItem(Scanner scanner) {
        displayItems();

        System.out.print("Masukkan nomor barang yang ingin dihapus: ");
        String choice = scanner.nextLine();

        if (isValidItem(choice)) {
            String itemName = getItemName(choice);
            stock.remove(choice);
            System.out.println(itemName + " berhasil dihapus dari stok.");
        } else {
            System.out.println("Barang tidak valid.");
        }
    }

    private static void updateStock(Scanner scanner) {
        displayItems();

        System.out.print("Masukkan nomor barang yang ingin diupdate stoknya: ");
        String choice = scanner.nextLine();

        if (isValidItem(choice)) {
            System.out.print("Masukkan jumlah stok baru: ");
            int newStock = scanner.nextInt();
            scanner.nextLine(); // consume newline

            stock.put(choice, newStock);
            System.out.println("Stok barang berhasil diupdate.");
        } else {
            System.out.println("Barang tidak valid.");
        }
    }

    private static void initializeStock() {
        stock.put("1", 10); // Minyak Goreng
        stock.put("2", 5);  // Hotwheels Audi Rs6
        stock.put("3", 3);  // Voucher Google Play
        stock.put("4", 20); // Kinderjoy
        stock.put("5", 15); // Sponge Popcorn
    }

    private static void displayItems() {
        System.out.println("\nDaftar Barang:");
        System.out.println("1. Minyak Goreng (Rp. 43.000,-)");
        System.out.println("2. Hotwheels Audi Rs6 (Rp. 60.000,-)");
        System.out.println("3. Voucher Google Play (Rp. 500.000,-)");
        System.out.println("4. Kinderjoy (Rp. 16.000,-)");
        System.out.println("5. Sponge Popcorn (Rp. 22.000,-)");
    }

    private static void displayStock() {
        System.out.println("\nStok Barang:");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            String itemName = getItemName(entry.getKey());
            int itemStock = entry.getValue();
            System.out.println(itemName + " - Stok: " + itemStock);
        }
    }

    private static boolean isValidItem(String choice) {
        return stock.containsKey(choice);
    }

    private static String getItemName(String choice) {
        switch (choice) {
            case "1":
                return "Minyak Goreng";
            case "2":
                return "Hotwheels Audi Rs6";
            case "3":
                return "Voucher Google Play";
            case "4":
                return "Kinderjoy";
            case "5":
                return "Sponge Popcorn";
            default:
                return "";
        }
    }

    private static int getItemPrice(String choice) {
        switch (choice) {
            case "1":
                return 43000;
            case "2":
                return 60000;
            case "3":
                return 500000;
            case "4":
                return 16000;
            case "5":
                return 22000;
            default:
                return 0;
        }
    }

    private static boolean isStockAvailable(String choice, int quantity) {
        if (stock.containsKey(choice)) {
            return stock.get(choice) >= quantity;
        }
        return false;
    }

    private static void reduceStock(String choice, int quantity) {
        if (stock.containsKey(choice)) {
            int currentStock = stock.get(choice);
            stock.put(choice, currentStock - quantity);
        }
    }

    private static void printReceipt(List<String> purchasedItems, int totalCost, int amountPaid, int change) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STRUK_FILE, true))) {
            writer.write("\n=== Struk Pembelian ===");
            writer.newLine();

            // Cetak nama barang yang dibeli
            writer.write("Barang yang dibeli:");
            writer.newLine();
            for (String item : purchasedItems) {
                writer.write("- " + item);
                writer.newLine();
            }

            writer.write("Total Biaya: Rp. " + totalCost);
            writer.newLine();
            writer.write("Jumlah Uang: Rp. " + amountPaid);
            writer.newLine();
            writer.write("Kembalian: Rp. " + change);
            writer.newLine();
        }
    }
}
