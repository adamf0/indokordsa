package com.app.indokordsa.record.db.table;

public class TblMesin {
    public static String tbl_mesin 	            = "tb_mesin";
    public static String key_id_mesin          = "id_mesin";
    public static String key_kode_nfc          = "kode_nfc";
    public static String key_nama              = "nama";
    public static String key_id_kategori_mesin = "id_kategori_mesin";

    public static String create_table = "CREATE TABLE "+ TblMesin.tbl_mesin +" (" +
            TblMesin.key_id_mesin + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblMesin.key_id_kategori_mesin + " INTEGER NOT NULL," +
            TblMesin.key_nama + " TEXT NOT NULL," +
            TblMesin.key_kode_nfc + " TEXT NOT NULL)";
}
