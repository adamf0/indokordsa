package com.app.indokordsa.record.db.table;

public class TblKategoriMesin {
    public static String tbl_kategori_mesin 	= "tb_kategori_mesin";
    public static String key_id_kategori_mesin = "id_kategori_mesin";
    public static String key_nama              = "nama";

    public static String create_table = "CREATE TABLE "+ TblKategoriMesin.tbl_kategori_mesin +" (" +
            TblKategoriMesin.key_id_kategori_mesin + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKategoriMesin.key_nama + " TEXT NOT NULL)";
}
