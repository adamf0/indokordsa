package com.app.indokordsa.record.db.table;

public class TblUsers {
    public static String tbl_users = "users" ;
    public static String key_id 	= "id" ;
    public static String key_phone = "phone" ;
    public static String key_image = "image" ;
    public static String key_nama 	= "nama" ;
    public static String key_level = "level" ;

    public static String create_table = "CREATE TABLE "+ TblUsers.tbl_users +" (" +
            TblUsers.key_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TblUsers.key_phone + " TEXT NOT NULL," +
            TblUsers.key_image + " TEXT NOT NULL," +
            TblUsers.key_nama + " TEXT NOT NULL," +
            TblUsers.key_level + " TEXT NOT NULL)";
}
