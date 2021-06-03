package com.app.indokordsa.record.db.table;

public class TblPertanyaanKuesioner {
    public static String tbl_pertanyaan_kuesioner   = "tbl_pertanyaan_kuesioner";
    public static String key_id_pertanyaan_kuesioner= "id_pertanyaan_kuesioner";
    public static String key_id_topik_kuesioner     = "id_topik_kuesioner";
    public static String key_pertanyaan             = "pertanyaan";

    public static String create_table = "CREATE TABLE "+ TblPertanyaanKuesioner.tbl_pertanyaan_kuesioner +" (" +
            TblPertanyaanKuesioner.key_id_pertanyaan_kuesioner + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblPertanyaanKuesioner.key_id_topik_kuesioner + " INTEGER NOT NULL," +
            TblPertanyaanKuesioner.key_pertanyaan + " TEXT NOT NULL)";
}