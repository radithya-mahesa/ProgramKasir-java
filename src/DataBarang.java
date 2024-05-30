import java.util.Map;

public class DataBarang {
    private static final Map<String, Barang> listBarangg; // membuat hasmap bernama listBarangg

    static {
        listBarangg = MenyimpanDataBarang.readDataFromFile();
    }

    public static Map<String, Barang> getDataBarang() {
        return listBarangg;
    }

    public static void saveDataBarang() {
        MenyimpanDataBarang.writeDataToFile(listBarangg); // menulis data barang ke file berdasarkan class "MenyimpanDataBarang"
    }
}
