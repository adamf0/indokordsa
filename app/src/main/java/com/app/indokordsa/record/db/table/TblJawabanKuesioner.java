package com.app.indokordsa.record.db.table;

public class TblJawabanKuesioner {
    public static String tbl_jawaban_kuesioner        = "tbl_jawaban_kuesioner";
    public static String key_id_jawaban_kuesioner     = "id_jawaban_kuesioner";
    public static String key_id_kuesioner_result      = "id_kuesioner_result";
    public static String key_id_topik_kuesioner       = "id_topik_kuesioner";
    public static String key_id_pertanyaan_kuesioner  = "id_pertanyaan_kuesioner";
    public static String key_val                      = "val";
    public static String key_start                    = "start_";
    public static String key_end                      = "end_";

    public static String create_table = "CREATE TABLE "+ TblJawabanKuesioner.tbl_jawaban_kuesioner +" (" +
            TblJawabanKuesioner.key_id_jawaban_kuesioner + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblJawabanKuesioner.key_id_kuesioner_result + " INTEGER NOT NULL," +
            TblJawabanKuesioner.key_id_topik_kuesioner + " INTEGER NOT NULL," +
            TblJawabanKuesioner.key_id_pertanyaan_kuesioner + " INTEGER NOT NULL," +
            TblJawabanKuesioner.key_val + " TEXT NULL," +
            TblJawabanKuesioner.key_start + " TEXT NULL," +
            TblJawabanKuesioner.key_end + " TEXT NULL)";
}