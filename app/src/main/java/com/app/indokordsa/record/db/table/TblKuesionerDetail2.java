package com.app.indokordsa.record.db.table;

public class TblKuesionerDetail2 {
    public static String tbl_kuesioner_detail_2         = "tb_kuesioner_detail_2";
    public static String key_id_kuesioner_detail_2 	    = "id_kuesioner_detail_2";
    public static String key_id_kuesioner_detail_1 	    = "id_kuesioner_detail_1";
    public static String key_nama 		                = "nama";

    public static String create_table = "CREATE TABLE "+ TblKuesionerDetail2.tbl_kuesioner_detail_2 +" (" +
            TblKuesionerDetail2.key_id_kuesioner_detail_2 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesionerDetail2.key_id_kuesioner_detail_1 + " INTEGER NOT NULL," +
            TblKuesionerDetail2.key_nama + " TEXT NOT NULL)";
}
