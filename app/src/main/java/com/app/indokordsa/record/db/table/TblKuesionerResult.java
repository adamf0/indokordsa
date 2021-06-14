package com.app.indokordsa.record.db.table;

public class TblKuesionerResult {
    public static String tbl_kuesioner_result       = "tbl_kuesioner_result";
    public static String key_id_kuesioner_result    = "id_kuesioner_result";
    public static String key_id_user                = "id_user";
    public static String key_id_shift               = "id_shift";
    public static String key_status                 = "status";
    public static String key_alasan                 = "alasan";
    public static String key_sync                   = "sync";
    public static String key_created_at             = "created_at";
    public static String key_updated_at             = "updated_at";
    public static String key_deleted_at             = "deleted_at";

    public static String create_table = "CREATE TABLE "+ TblKuesionerResult.tbl_kuesioner_result +" (" +
            TblKuesionerResult.key_id_kuesioner_result + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblKuesionerResult.key_id_user + " INTEGER NOT NULL," +
            TblKuesionerResult.key_id_shift + " INTEGER NOT NULL," +
            TblKuesionerResult.key_status + " INTEGER," +
            TblKuesionerResult.key_alasan + " TEXT NULL," +
            TblKuesionerResult.key_sync + " INTEGER," +
            TblKuesionerResult.key_created_at + " TEXT NULL," +
            TblKuesionerResult.key_updated_at + " TEXT NULL," +
            TblKuesionerResult.key_deleted_at + " TEXT NULL)";
}