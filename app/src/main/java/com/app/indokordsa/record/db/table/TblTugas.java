package com.app.indokordsa.record.db.table;

public class TblTugas {
    public static String tbl_tugas          = "tb_tugas";
    public static String key_id_tugas       = "id_tugas";
    public static String key_id_penugasan   = "id_penugasan";
    public static String key_no             = "no";
    public static String key_cek            = "cek";
    public static String key_kat            = "kat";
    public static String key_val            = "val";
    public static String key_ket            = "ket";

    public static String create_table = "CREATE TABLE "+ TblTugas.tbl_tugas +" (" +
            TblTugas.key_id_tugas + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblTugas.key_id_penugasan + " INTEGER NOT NULL," +
            TblTugas.key_no + " TEXT NOT NULL," +
            TblTugas.key_cek + " TEXT NOT NULL," +
            TblTugas.key_kat + " TEXT NOT NULL," +
            TblTugas.key_val + " TEXT NOT NULL," +
            TblTugas.key_ket + " LONGTEXT NOT NULL)";
}