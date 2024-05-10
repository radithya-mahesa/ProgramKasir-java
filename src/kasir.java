import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class kasir {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("INPUT NILAI");
        System.out.println("1. Minyak Goreng (Harga Rp. 43.000,-)");
        System.out.println("2. Hotwheels Audi Rs6 (Harga Rp. 60.000,-)");
        System.out.println("3. Voucher Google Play (Harga Rp. 500.000,-)");
        System.out.println("4. Kinderjoy (Harga Rp. 16.000,-)");
        System.out.println("5. (Sponge Popcorn Rp. 22.000,-)");

        System.out.print("Pilih:");
        var kasir = input.next();
        int harga = 0;
        int barang = 0;

        FileWriter fileWriter = new FileWriter("cetakStruk.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        switch (kasir) {
            case "1" :
                bufferedWriter.write(" Minyak Goreng (Harga Rp. 43.000,-)");
                bufferedWriter.newLine();
                System.out.print("Masukkan Nominal : ");
                barang = input.nextInt();
                harga = 43_000;
                break;
        }

        bufferedWriter.write("Jumlah Barang : " + barang);
        bufferedWriter.newLine();
        bufferedWriter.write("Total : " + harga * barang );
        bufferedWriter.newLine();

        System.out.println("Mana duit Lo");
        int uang = input.nextInt();

        if (uang >= harga * barang) {
            int kembali = uang - (harga * barang);
            bufferedWriter.write("Uang Kembali : " + kembali);
            bufferedWriter.newLine();
        } else {
            System.out.println("Pulang lo");
        }
        bufferedWriter.close();
    }
}
