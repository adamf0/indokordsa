package com.app.indokordsa.record.db.table;

public class TblArea {
    public static String tbl_area 	    = "tb_area";
    public static String key_id_area 	= "id_area";
    public static String key_nama 		= "nama";

    public static String create_table = "CREATE TABLE "+ TblArea.tbl_area +" (" +
            TblArea.key_id_area + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblArea.key_nama + " TEXT NOT NULL)";
}
