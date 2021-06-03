package com.app.indokordsa.record.db.table;

public class TblTopikKuesioner {
    public static String tbl_topik_kuesioner        = "tbl_topik_kuesioner";
    public static String key_id_topik_kuesioner     = "id_topik_kuesioner";
    public static String key_id_kuesioner           = "id_kuesioner";
    public static String key_tentang                = "tentang";

    public static String create_table = "CREATE TABLE "+ TblTopikKuesioner.tbl_topik_kuesioner +" (" +
            TblTopikKuesioner.key_id_topik_kuesioner + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblTopikKuesioner.key_id_kuesioner + " INTEGER NOT NULL," +
            TblTopikKuesioner.key_tentang + " TEXT NOT NULL)";
}