package com.app.indokordsa.record.db.table;

public class TblKuesionerResult {
    public static String tbl_kuesioner_result 	    = "tb_kuesioner_result";
    public static String key_id_kuesioner_result 	= "id_kuesioner_result";
    public static String key_id_user 	            = "id_user";
    public static String key_id_shift 		        = "id_shift";
    public static String key_id_area 		        = "id_area";
    public static String key_id_kuesioner 		    = "id_kuesioner";
    public static String key_jawaban 		        = "jawaban";
    public static String key_result 		        = "result";
    public static String key_status 		        = "status";
    public static String key_tanggal 		        = "tanggal";

    public static String create_table = "CREATE TABLE "+ TblKuesionerResult.tbl_kuesioner_result +" (" +
            TblKuesionerResult.key_id_kuesioner_result + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesionerResult.key_id_user + " INTEGER NOT NULL," +
            TblKuesionerResult.key_id_shift + " INTEGER NOT NULL," +
            TblKuesionerResult.key_id_area + " INTEGER NOT NULL," +
            TblKuesionerResult.key_id_kuesioner + " INTEGER NOT NULL," +
            TblKuesionerResult.key_jawaban + " INTEGER NOT NULL," +
            TblKuesionerResult.key_result + " LONGTEXT NOT NULL," +
            TblKuesionerResult.key_status + " INTEGER NOT NULL," +
            TblKuesionerResult.key_tanggal + " TEXT NOT NULL)";
}
