package com.app.indokordsa.record.db.table;

public class TblKuesionerDetail1 {
    public static String tbl_kuesioner_detail_1         = "tb_kuesioner_detail_1";
    public static String key_id_kuesioner_detail_1 	    = "id_kuesioner_detail_1";
    public static String key_id_kuesioner 	            = "id_kuesioner";
    public static String key_nama 		                = "nama";

    public static String create_table = "CREATE TABLE "+ TblKuesionerDetail1.tbl_kuesioner_detail_1 +" (" +
            TblKuesionerDetail1.key_id_kuesioner_detail_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesionerDetail1.key_id_kuesioner + " INTEGER NOT NULL," +
            TblKuesionerDetail1.key_nama + " TEXT NOT NULL)";
}
