package com.app.indokordsa.record.db.table;

public class TblShift {
    public static String tbl_shift 	    = "tb_shift";
    public static String key_id_shift 	= "id_shift";
    public static String key_nama 		= "nama";
    public static String key_mulai 		= "mulai";
    public static String key_selesai 	= "selesai";
    public static String key_ganti_hari	= "ganti_hari";
    public static String create_table = "CREATE TABLE "+ TblShift.tbl_shift +" (" +
            TblShift.key_id_shift + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblShift.key_nama + " TEXT NOT NULL,"+
            TblShift.key_mulai + " TEXT," +
            TblShift.key_selesai + " TEXT," +
            TblShift.key_ganti_hari + " INTEGER)";

}
