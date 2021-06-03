package com.app.indokordsa.record.db.table;

public class TblKuesioner {
    public static String tbl_kuesioner      = "tb_kuesioner";
    public static String key_id_kuesioner   = "id_kuesioner";
    public static String key_id_area        = "id_area";
    public static String key_pertanyaan     = "pertanyaan";

    public static String create_table = "CREATE TABLE "+ TblKuesioner.tbl_kuesioner +" (" +
            TblKuesioner.key_id_kuesioner + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesioner.key_id_area + " INTEGER NOT NULL," +
            TblKuesioner.key_pertanyaan + " TEXT NOT NULL)";
}