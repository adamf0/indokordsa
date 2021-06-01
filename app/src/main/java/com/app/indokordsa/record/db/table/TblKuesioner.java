package com.app.indokordsa.record.db.table;

public class TblKuesioner {
    public static String tbl_kuesioner 	    = "tb_kuesioner";
    public static String key_id 	        = "id_kuesioner";
    public static String key_nama 		    = "nama";

    public static String create_table = "CREATE TABLE "+ TblKuesioner.tbl_kuesioner +" (" +
            TblKuesioner.key_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesioner.key_nama + " TEXT NOT NULL)";
}
