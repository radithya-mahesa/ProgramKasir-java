import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MenyimpanDataBarang{

    private static final String DATA_FILE = "barangData.txt";

    public static Map<String, Barang> readDataFromFile() {
        Map<String, Barang> listBarangg = new HashMap<>(); // membuat hasmap baru bernama listbarang
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String nama = parts[1];
                    int harga = Integer.parseInt(parts[2]);
                    int stok = Integer.parseInt(parts[3]);
                    listBarangg.put(id, new Barang(nama, harga, stok));
                }
            }
        } catch (IOException e) {
            System.err.println("Error membaca data dari file: " + e.getMessage());
        }
        return listBarangg;
    }

    public static void writeDataToFile(Map<String, Barang> listBarangg) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, Barang> entry : listBarangg.entrySet()) {
                String id = entry.getKey();
                Barang barang = entry.getValue();
                writer.write(id + "," + barang.getNama() + "," + barang.getHarga() + "," + barang.getStok());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error menulis data ke file: " + e.getMessage());
        }
    }
}