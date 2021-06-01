package com.app.indokordsa.record.db.table;

public class TblCekMesin {
    public static String tbl_cek_mesin 	= "tb_cek_mesin";
    public static String key_id_cek_mesin 	= "id_cek_mesin";
    public static String key_id_mesin 		= "id_mesin";

    public static String create_table = "CREATE TABLE "+ TblCekMesin.tbl_cek_mesin +" (" +
            TblCekMesin.key_id_cek_mesin + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblCekMesin.key_id_mesin + " INTEGER NOT NULL)";
}
