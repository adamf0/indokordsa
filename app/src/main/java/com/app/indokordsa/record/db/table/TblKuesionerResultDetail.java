package com.app.indokordsa.record.db.table;

public class TblKuesionerResultDetail {
    public static String tbl_kuesioner_result_detail        = "tbl_kuesioner_result_detail";
    public static String key_id_kuesioner_result_detail     = "id_kuesioner_result_detail";
    public static String key_id_kuesioner_result            = "id_kuesioner_result";
    public static String key_id_kuesioner                   = "id_kuesioner";
    public static String key_jawaban                        = "jawaban";

    public static String create_table = "CREATE TABLE "+ TblKuesionerResultDetail.tbl_kuesioner_result_detail +" (" +
            TblKuesionerResultDetail.key_id_kuesioner_result_detail + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesionerResultDetail.key_id_kuesioner_result + " INTEGER NOT NULL," +
            TblKuesionerResultDetail.key_id_kuesioner + " INTEGER NOT NULL," +
            TblKuesionerResultDetail.key_jawaban + " INTEGER)";
}