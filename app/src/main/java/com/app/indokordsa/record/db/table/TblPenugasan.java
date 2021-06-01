package com.app.indokordsa.record.db.table;

public class TblPenugasan {
    public static String tbl_penugasan     = "tb_penugasan";
    public static String key_id_penugasan  = "id_penugasan";
    public static String key_id_supervisor = "id_supervisor";
    public static String key_id_operator   = "id_operator";
    public static String key_id_cek_mesin  = "id_cek_mesin";
    public static String key_bulan         = "bulan";
    public static String key_tahun         = "tahun";
//    public static String key_tugas         = "tugas";
    public static String key_status        = "status";
    public static String key_alasan        = "alasan";
    public static String key_sync          = "sync";
    public static String key_created_at    = "created_at";
    public static String key_updated_at    = "updated_at";
    public static String key_deleted_at    = "deleted_at";

    public static String create_table = "CREATE TABLE "+ TblPenugasan.tbl_penugasan +" (" +
            TblPenugasan.key_id_penugasan + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblPenugasan.key_id_supervisor + " INTEGER NOT NULL," +
            TblPenugasan.key_id_operator + " INTEGER NOT NULL," +
            TblPenugasan.key_id_cek_mesin + " INTEGER NOT NULL," +
            TblPenugasan.key_bulan + " TEXT NOT NULL," +
            TblPenugasan.key_tahun + " TEXT NOT NULL," +
//            TblPenugasan.key_tugas + " LONGTEXT NOT NULL," +
            TblPenugasan.key_status + " INTEGER," +
            TblPenugasan.key_alasan + " TEXT NULL," +
            TblPenugasan.key_sync + " INTEGER," +
            TblPenugasan.key_created_at + " TEXT NULL," +
            TblPenugasan.key_updated_at + " TEXT NULL," +
            TblPenugasan.key_deleted_at + " TEXT NULL)";
}
