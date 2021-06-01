package com.app.indokordsa.record.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.indokordsa.record.db.table.TblArea;
import com.app.indokordsa.record.db.table.TblCekMesin;
import com.app.indokordsa.record.db.table.TblKategoriMesin;
import com.app.indokordsa.record.db.table.TblKuesioner;
import com.app.indokordsa.record.db.table.TblKuesionerDetail1;
import com.app.indokordsa.record.db.table.TblKuesionerDetail2;
import com.app.indokordsa.record.db.table.TblKuesionerResult;
import com.app.indokordsa.record.db.table.TblMesin;
import com.app.indokordsa.record.db.table.TblPenugasan;
import com.app.indokordsa.record.db.table.TblShift;
import com.app.indokordsa.record.db.table.TblTugas;
import com.app.indokordsa.record.db.table.TblUsers;
import com.app.indokordsa.view.model.Area;
import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.Job;
import com.app.indokordsa.view.model.Kuesioner;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.view.model.KuesionerResultDetail1;
import com.app.indokordsa.view.model.KuesionerResultDetail2;
import com.app.indokordsa.view.model.Machine;
import com.app.indokordsa.view.model.MachineCategory;
import com.app.indokordsa.view.model.MachineCheck;
import com.app.indokordsa.view.model.Operator;
import com.app.indokordsa.view.model.Shift;
import com.app.indokordsa.view.model.Supervisor;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TblCekMesin.create_table);
        db.execSQL(TblKategoriMesin.create_table);
        db.execSQL(TblMesin.create_table);
        db.execSQL(TblPenugasan.create_table);
        db.execSQL(TblUsers.create_table);
        db.execSQL(TblTugas.create_table);
        db.execSQL(TblShift.create_table);
        db.execSQL(TblArea.create_table);
        db.execSQL(TblKuesioner.create_table);
        db.execSQL(TblKuesionerResult.create_table);
        db.execSQL(TblKuesionerDetail1.create_table);
        db.execSQL(TblKuesionerDetail2.create_table);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblCekMesin.tbl_cek_mesin));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKategoriMesin.tbl_kategori_mesin));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblMesin.tbl_mesin));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblPenugasan.tbl_penugasan));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblUsers.tbl_users));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblTugas.tbl_tugas));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblShift.tbl_shift));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblArea.tbl_area));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKuesioner.tbl_kuesioner));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKuesionerResult.tbl_kuesioner_result));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKuesionerDetail1.tbl_kuesioner_detail_1));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKuesionerDetail2.tbl_kuesioner_detail_2));
        onCreate(db);
    }

    public Long save_cek_mesin(String id_cek_mesin,String id_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblCekMesin.key_id_cek_mesin, id_cek_mesin);
        values.put(TblCekMesin.key_id_mesin, id_mesin);
        return db.insert(TblCekMesin.tbl_cek_mesin, null, values);
    }
    public Long save_kategori_mesin(String id_kategori_mesin,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKategoriMesin.key_id_kategori_mesin, id_kategori_mesin);
        values.put(TblKategoriMesin.key_nama, nama);
        return db.insert(TblKategoriMesin.tbl_kategori_mesin, null, values);
    }
    public Long save_mesin(String id_mesin,String kode_nfc,String nama,String id_kategori_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblMesin.key_id_mesin, id_mesin);
        values.put(TblMesin.key_kode_nfc, kode_nfc);
        values.put(TblMesin.key_nama, nama);
        values.put(TblMesin.key_id_kategori_mesin, id_kategori_mesin);
        return db.insert(TblMesin.tbl_mesin, null, values);
    }
    public Long save_penugasan(String id_penugasan,String id_supervisor,String id_operator,String id_cek_mesin,String bulan, String tahun,String status,String alasan,String sync,String created_at,String updated_at,String deleted_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblPenugasan.key_id_penugasan, id_penugasan);
        values.put(TblPenugasan.key_id_supervisor, id_supervisor);
        values.put(TblPenugasan.key_id_operator, id_operator);
        values.put(TblPenugasan.key_id_cek_mesin, id_cek_mesin);
        values.put(TblPenugasan.key_bulan, bulan);
        values.put(TblPenugasan.key_tahun, tahun);
        values.put(TblPenugasan.key_status, status);
        values.put(TblPenugasan.key_alasan, alasan);
        values.put(TblPenugasan.key_sync, sync);
        values.put(TblPenugasan.key_created_at, created_at);
        values.put(TblPenugasan.key_updated_at, updated_at);
        values.put(TblPenugasan.key_deleted_at, deleted_at);
        return db.insert(TblPenugasan.tbl_penugasan, null, values);
    }
    public Long save_tugas(String id_penugasan,String no,String cek,String kat, String val,String ket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblTugas.key_id_penugasan, id_penugasan);
        values.put(TblTugas.key_no, no);
        values.put(TblTugas.key_cek, cek);
        values.put(TblTugas.key_kat, kat);
        values.put(TblTugas.key_val, val);
        values.put(TblTugas.key_ket, ket);
        return db.insert(TblTugas.tbl_tugas, null, values);
    }
    public Long save_users(String id,String phone,String image,String nama,String level) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblUsers.key_id, id);
        values.put(TblUsers.key_phone, phone);
        values.put(TblUsers.key_image, image);
        values.put(TblUsers.key_nama, nama);
        values.put(TblUsers.key_level, level);
        return db.insert(TblUsers.tbl_users, null, values);
    }
    public Long save_shift(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblShift.key_id_shift, id);
        values.put(TblShift.key_nama, nama);
        return db.insert(TblShift.tbl_shift, null, values);
    }
    public Long save_area(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblArea.key_id_area, id);
        values.put(TblArea.key_nama, nama);
        return db.insert(TblArea.tbl_area, null, values);
    }
    public Long save_kuesioner(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesioner.key_id, id);
        values.put(TblKuesioner.key_nama, nama);
        return db.insert(TblKuesioner.tbl_kuesioner, null, values);
    }
    public Long save_kuesioner_result(String id_kuesioner_result, String id_user, String id_shift, String id_area, String id_kuesioner, String jawaban, String result, String status, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesionerResult.key_id_kuesioner_result, id_kuesioner_result);
        values.put(TblKuesionerResult.key_id_user, id_user);
        values.put(TblKuesionerResult.key_id_shift, id_shift);
        values.put(TblKuesionerResult.key_id_area, id_area);
        values.put(TblKuesionerResult.key_id_kuesioner, id_kuesioner);
        values.put(TblKuesionerResult.key_jawaban, jawaban);
        values.put(TblKuesionerResult.key_result, result);
        values.put(TblKuesionerResult.key_status, status);
        values.put(TblKuesionerResult.key_tanggal, tanggal);
        return db.insert(TblKuesionerResult.tbl_kuesioner_result, null, values);
    }
    public Long save_kuesioner_detail1(String id_kuesioner_detail_1,String id_kuesioner, String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesionerDetail1.key_id_kuesioner_detail_1, id_kuesioner_detail_1);
        values.put(TblKuesionerDetail1.key_id_kuesioner, id_kuesioner);
        values.put(TblKuesionerDetail1.key_nama, nama);
        return db.insert(TblKuesionerDetail1.tbl_kuesioner_detail_1, null, values);
    }
    public Long save_kuesioner_detail2(String id_kuesioner_detail_2,String id_kuesioner_detail_1, String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesionerDetail2.key_id_kuesioner_detail_2, id_kuesioner_detail_2);
        values.put(TblKuesionerDetail2.key_id_kuesioner_detail_1, id_kuesioner_detail_1);
        values.put(TblKuesionerDetail2.key_nama, nama);
        return db.insert(TblKuesionerDetail2.tbl_kuesioner_detail_2, null, values);
    }

    public int update_cek_mesin(String id_cek_mesin,String id_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblCekMesin.key_id_mesin, id_mesin);
        return db.update(TblCekMesin.tbl_cek_mesin,values,TblCekMesin.key_id_cek_mesin+"= ?",new String[]{id_cek_mesin});
    }
    public int update_kategori_mesin(String id_kategori_mesin,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblKategoriMesin.key_nama, nama);
        return db.update(TblKategoriMesin.tbl_kategori_mesin,values,TblKategoriMesin.key_id_kategori_mesin+"= ?",new String[]{id_kategori_mesin});
    }
    public int update_mesin(String id_mesin,String kode_nfc,String nama,String id_kategori_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblMesin.key_kode_nfc, kode_nfc);
        values.put(TblMesin.key_nama, nama);
        values.put(TblMesin.key_id_kategori_mesin, id_kategori_mesin);
        return db.update(TblMesin.tbl_mesin,values,TblMesin.key_id_mesin+"= ?",new String[]{id_mesin});
    }
    public int update_penugasan(String id_penugasan,String id_supervisor,String id_operator,String id_cek_mesin,String bulan, String tahun,String status,String sync,String alasan,String created_at,String updated_at,String deleted_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblPenugasan.key_id_supervisor, id_supervisor);
        values.put(TblPenugasan.key_id_operator, id_operator);
        values.put(TblPenugasan.key_id_cek_mesin, id_cek_mesin);
        values.put(TblPenugasan.key_bulan, bulan);
        values.put(TblPenugasan.key_tahun, tahun);
        values.put(TblPenugasan.key_status, status);
        values.put(TblPenugasan.key_alasan, alasan);
        values.put(TblPenugasan.key_sync, sync);
        values.put(TblPenugasan.key_created_at, created_at);
        values.put(TblPenugasan.key_updated_at, updated_at);
        values.put(TblPenugasan.key_deleted_at, deleted_at);
        return db.update(TblPenugasan.tbl_penugasan,values,TblPenugasan.key_id_penugasan+"= ?",new String[]{id_penugasan});
    }
    public int update_sinkron_penugasan(String id_penugasan,String sinkron) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblPenugasan.key_sync, sinkron);
        return db.update(TblPenugasan.tbl_penugasan,values,TblPenugasan.key_id_penugasan+"= ?",new String[]{id_penugasan});
    }
    public int update_tugas(String id_penugasan,String no,String cek,String kat, String val,String ket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblTugas.key_cek, cek);
        values.put(TblTugas.key_kat, kat);
        values.put(TblTugas.key_val, val);
        values.put(TblTugas.key_ket, ket);
        return db.update(TblTugas.tbl_tugas,values,TblTugas.key_id_penugasan+"= ? and "+TblTugas.key_no+"= ?",new String[]{id_penugasan,no});
    }
    public int update_users(String id,String phone,String image,String nama,String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblUsers.key_phone, phone);
        values.put(TblUsers.key_image, image);
        values.put(TblUsers.key_nama, nama);
        values.put(TblUsers.key_level, level);
        return db.update(TblUsers.tbl_users,values,TblUsers.key_id+"= ?",new String[]{id});
    }
    public int update_shift(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblShift.key_nama, nama);
        return db.update(TblShift.tbl_shift,values,TblShift.key_id_shift+"= ?",new String[]{id});
    }
    public int update_area(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblArea.key_nama, nama);
        return db.update(TblArea.tbl_area,values,TblArea.key_id_area+"= ?",new String[]{id});
    }
    public int update_kuesioner(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblKuesioner.key_nama, nama);
        return db.update(TblKuesioner.tbl_kuesioner,values,TblKuesioner.key_id+"= ?",new String[]{id});
    }
    public int update_kuesioner_result(String id_kuesioner_result, String id_user, String id_shift, String id_area, String id_kuesioner, String jawaban, String result, String status, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblKuesionerResult.key_id_user, id_user);
        values.put(TblKuesionerResult.key_id_shift, id_shift);
        values.put(TblKuesionerResult.key_id_area, id_area);
        values.put(TblKuesionerResult.key_id_kuesioner, id_kuesioner);
        values.put(TblKuesionerResult.key_jawaban, jawaban);
        values.put(TblKuesionerResult.key_result, result);
        values.put(TblKuesionerResult.key_status, status);
        values.put(TblKuesionerResult.key_tanggal, tanggal);
        return db.update(TblKuesionerResult.tbl_kuesioner_result,values,TblKuesionerResult.key_id_kuesioner_result+"= ?",new String[]{id_kuesioner_result});
    }
    public int update_kuesioner_result(String id_kuesioner_result, String jawaban, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblKuesionerResult.key_jawaban, jawaban);
        values.put(TblKuesionerResult.key_result, result);
        return db.update(TblKuesionerResult.tbl_kuesioner_result,values,TblKuesionerResult.key_id_kuesioner_result+"= ?",new String[]{id_kuesioner_result});
    }
    public int update_kuesioner_detail1(String id_kuesioner_detail_1,String id_kuesioner, String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesionerDetail1.key_id_kuesioner, id_kuesioner);
        values.put(TblKuesionerDetail1.key_nama, nama);
        return db.update(TblKuesionerDetail1.tbl_kuesioner_detail_1,values,TblKuesionerDetail1.key_id_kuesioner_detail_1+"= ?",new String[]{id_kuesioner_detail_1});
    }
    public int update_kuesioner_detail2(String id_kuesioner_detail_2,String id_kuesioner_detail_1, String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesionerDetail2.key_id_kuesioner_detail_1, id_kuesioner_detail_1);
        values.put(TblKuesionerDetail2.key_nama, nama);
        return db.update(TblKuesionerDetail2.tbl_kuesioner_detail_2,values,TblKuesionerDetail2.key_id_kuesioner_detail_2+"= ?",new String[]{id_kuesioner_detail_2});
    }

    public ArrayList<CheckList> getListCheckList(String start,String end) {
        ArrayList<CheckList> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                "tb_penugasan.id_cek_mesin," +
                "tb_mesin.id_mesin," +
                "tb_mesin.kode_nfc," +
                "tb_mesin.nama," +
                "tb_kategori_mesin.id_kategori_mesin," +
                "tb_kategori_mesin.nama as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                "tb_penugasan.created_at as penugasan_created_at," +
                "tb_penugasan.updated_at as penugasan_updated_at," +
                "tb_penugasan.deleted_at as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE tb_penugasan.created_at BETWEEN '%s' AND '%s' AND sync='0'", start,end);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cursor supervisor = getUsers(cursor.getString(cursor.getColumnIndex("id_supervisor")));
                    Cursor operator = getUsers(cursor.getString(cursor.getColumnIndex("id_operator")));

                    list_data.add(new CheckList(
                            cursor.getString(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("tanggal")),
                            (supervisor!=null?
                                    new Supervisor(
                                            supervisor.getString(supervisor.getColumnIndex("id")),
                                            supervisor.getString(supervisor.getColumnIndex("nama")),
                                            supervisor.getString(supervisor.getColumnIndex("image")),
                                            supervisor.getString(supervisor.getColumnIndex("phone")),
                                            supervisor.getString(supervisor.getColumnIndex("level"))
                                    )
                                    :
                                    null
                            ),
                            (operator!=null?
                                    new Operator(
                                            operator.getString(operator.getColumnIndex("id")),
                                            operator.getString(operator.getColumnIndex("nama")),
                                            operator.getString(operator.getColumnIndex("image")),
                                            operator.getString(operator.getColumnIndex("phone")),
                                            operator.getString(operator.getColumnIndex("level"))
                                    )
                                    :
                                    null
                            ),
                            new MachineCheck(
                                    cursor.getString(cursor.getColumnIndex("id_cek_mesin")),
                                    new Machine(
                                            cursor.getString(cursor.getColumnIndex("id_mesin")),
                                            cursor.getString(cursor.getColumnIndex("kode_nfc")),
                                            cursor.getString(cursor.getColumnIndex("nama")),
                                            new MachineCategory(
                                                    cursor.getString(cursor.getColumnIndex(("id_kategori_mesin"))),
                                                    cursor.getString(cursor.getColumnIndex("nama_kategori"))
                                            )
                                    )
                            ),
                            list_job(cursor.getString(cursor.getColumnIndex("id"))),
                            (cursor.getInt(cursor.getColumnIndex(("status")))  >= 1),
                            cursor.getString(cursor.getColumnIndex(("alasan"))),
                            cursor.getInt(cursor.getColumnIndex(("sync")))
                    ));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<CheckList> getListCheckList(String id_operator, String start,String end) {
        ArrayList<CheckList> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                "tb_penugasan.id_cek_mesin," +
                "tb_mesin.id_mesin," +
                "tb_mesin.kode_nfc," +
                "tb_mesin.nama," +
                "tb_kategori_mesin.id_kategori_mesin," +
                "tb_kategori_mesin.nama as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                "tb_penugasan.created_at as penugasan_created_at," +
                "tb_penugasan.updated_at as penugasan_updated_at," +
                "tb_penugasan.deleted_at as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE id_operator = " +
                "%s AND " +
                "tb_penugasan.created_at BETWEEN '%s' AND '%s'", id_operator,start,end);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cursor supervisor = getUsers(cursor.getString(cursor.getColumnIndex("id_supervisor")));
                    Cursor operator = getUsers(cursor.getString(cursor.getColumnIndex("id_operator")));

                    list_data.add(new CheckList(
                            cursor.getString(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("tanggal")),
                            (supervisor!=null?
                                    new Supervisor(
                                            supervisor.getString(supervisor.getColumnIndex("id")),
                                            supervisor.getString(supervisor.getColumnIndex("nama")),
                                            supervisor.getString(supervisor.getColumnIndex("image")),
                                            supervisor.getString(supervisor.getColumnIndex("phone")),
                                            supervisor.getString(supervisor.getColumnIndex("level"))
                                    )
                                    :
                                    null
                            ),
                            (operator!=null?
                                    new Operator(
                                            operator.getString(operator.getColumnIndex("id")),
                                            operator.getString(operator.getColumnIndex("nama")),
                                            operator.getString(operator.getColumnIndex("image")),
                                            operator.getString(operator.getColumnIndex("phone")),
                                            operator.getString(operator.getColumnIndex("level"))
                                    )
                                    :
                                    null
                            ),
                            new MachineCheck(
                                    cursor.getString(cursor.getColumnIndex("id_cek_mesin")),
                                    new Machine(
                                            cursor.getString(cursor.getColumnIndex("id_mesin")),
                                            cursor.getString(cursor.getColumnIndex("kode_nfc")),
                                            cursor.getString(cursor.getColumnIndex("nama")),
                                            new MachineCategory(
                                                    cursor.getString(cursor.getColumnIndex(("id_kategori_mesin"))),
                                                    cursor.getString(cursor.getColumnIndex("nama_kategori"))
                                            )
                                    )
                            ),
                            list_job(cursor.getString(cursor.getColumnIndex("id"))),
                            (cursor.getInt(cursor.getColumnIndex(("status")))  >= 1),
                            cursor.getString(cursor.getColumnIndex(("alasan"))),
                            cursor.getInt(cursor.getColumnIndex(("sync")))
                    ));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<Job> list_job(String id_penugasan){
        ArrayList<Job> list_job = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

//        String sql = String.format("Select * FROM " + TblTugas.tbl_tugas + " WHERE id_penugasan = %s ORDER BY "+TblTugas.key_no + " ASC", id_penugasan);
        String sql = String.format("Select * FROM " + TblTugas.tbl_tugas + " WHERE id_penugasan = %s", id_penugasan);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list_job.add(
                            new Job(
                                    cursor.getString(cursor.getColumnIndex("id_tugas")),
                                    cursor.getString(cursor.getColumnIndex("id_penugasan")),
                                    cursor.getString(cursor.getColumnIndex("no")),
                                    cursor.getString(cursor.getColumnIndex("cek")),
                                    cursor.getString(cursor.getColumnIndex("kat")),
                                    cursor.getString(cursor.getColumnIndex("val")),
                                    cursor.getString(cursor.getColumnIndex("ket"))
                            )
                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return list_job;
    }
    public Cursor getUsers(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.query(TblUsers.tbl_users, new String[]{TblUsers.key_id,TblUsers.key_phone,TblUsers.key_image,TblUsers.key_nama,TblUsers.key_level}, TblUsers.key_id + "=?", new String[]{id}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    public ArrayList<KuesionerResult> getListKuesionerResult(String id_user, String start, String end) {
        ArrayList<KuesionerResult> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select tb_kuesioner_result.id_kuesioner_result as id," +
                "tb_kuesioner_result.id_user," +
                "tb_shift.id_shift," +
                "tb_shift.nama as nama_shift," +
                "tb_area.id_area," +
                "tb_area.nama as nama_area," +
                "tb_kuesioner.id_kuesioner," +
                "tb_kuesioner.nama as nama_kuesioner," +
                "tb_kuesioner_result.jawaban," +
                "tb_kuesioner_result.result," +
                "tb_kuesioner_result.status," +
                "tb_kuesioner_result.tanggal " +
                "FROM tb_kuesioner_result " +
                "JOIN tb_shift ON tb_kuesioner_result.id_shift = tb_shift.id_shift " +
                "JOIN tb_area ON tb_kuesioner_result.id_area = tb_area.id_area " +
                "JOIN tb_kuesioner ON tb_kuesioner_result.id_kuesioner = tb_kuesioner.id_kuesioner " +
                "WHERE id_user = %s AND " +
                "tb_kuesioner_result.tanggal BETWEEN '%s' AND '%s'", id_user,start,end);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            int no = 1;
            if (cursor.moveToFirst()) {
                do {

                    list_data.add(new KuesionerResult(
                            String.valueOf(no),
                            cursor.getString(cursor.getColumnIndex("id")),
                            new Shift(
                                    cursor.getString(cursor.getColumnIndex("id_shift")),
                                    cursor.getString(cursor.getColumnIndex("nama_shift"))
                            ),
                            new Area(
                                    cursor.getString(cursor.getColumnIndex("id_area")),
                                    cursor.getString(cursor.getColumnIndex("nama_area"))
                            ),
                            new Kuesioner(
                                    cursor.getString(cursor.getColumnIndex("id_kuesioner")),
                                    cursor.getString(cursor.getColumnIndex("nama_kuesioner"))
                            ),
                            cursor.getString(cursor.getColumnIndex("jawaban")),
                            getListKuesionerDetail1(cursor.getString(cursor.getColumnIndex("id"))),
                            cursor.getString(cursor.getColumnIndex("result")),
                            cursor.getString(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("tanggal"))
                    ));
                    no++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<KuesionerResultDetail1> getListKuesionerDetail1(String id_kuesioner) {
        ArrayList<KuesionerResultDetail1> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select * FROM " + TblKuesionerDetail1.tbl_kuesioner_detail_1 +
                " WHERE " + TblKuesionerDetail1.key_id_kuesioner + " = %s", id_kuesioner);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int no=1;
                do {
                    list_data.add(new KuesionerResultDetail1(
                            String.valueOf(no),
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_detail_1")),
                            cursor.getString(cursor.getColumnIndex("nama")),
                            getListKuesionerDetail2(cursor.getString(cursor.getColumnIndex("id_kuesioner_detail_1")))
                    ));
                    no++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<KuesionerResultDetail2> getListKuesionerDetail2(String id_kuesioner_detail_1) {
        ArrayList<KuesionerResultDetail2> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select * FROM " + TblKuesionerDetail2.tbl_kuesioner_detail_2 +
                " WHERE " + TblKuesionerDetail2.key_id_kuesioner_detail_1 + " = %s", id_kuesioner_detail_1);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int no=1;
                do {
                    list_data.add(new KuesionerResultDetail2(
                            String.valueOf(no),
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_detail_2")),
                            cursor.getString(cursor.getColumnIndex("nama")),
                            "",
                            "",
                            "",
                            ""
                    ));
                    no++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }

    public int countListCheckList(String id_operator, String start,String end) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = String.format("Select id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                "tb_penugasan.id_cek_mesin," +
                "tb_mesin.id_mesin," +
                "tb_mesin.kode_nfc," +
                "tb_mesin.nama," +
                "tb_kategori_mesin.id_kategori_mesin," +
                "tb_kategori_mesin.nama as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "tb_penugasan.created_at as penugasan_created_at," +
                "tb_penugasan.updated_at as penugasan_updated_at," +
                "tb_penugasan.deleted_at as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE id_operator = %s AND " +
                "tb_penugasan.created_at BETWEEN '%s' AND '%s'", id_operator,start,end);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countListCheckListById(String id_operator, String id_penugasan) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                "tb_penugasan.id_cek_mesin," +
                "tb_mesin.id_mesin," +
                "tb_mesin.kode_nfc," +
                "tb_mesin.nama," +
                "tb_kategori_mesin.id_kategori_mesin," +
                "tb_kategori_mesin.nama as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "tb_penugasan.created_at as penugasan_created_at," +
                "tb_penugasan.updated_at as penugasan_updated_at," +
                "tb_penugasan.deleted_at as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE id_operator = %s AND "+
                "id_penugasan = %s", id_operator, id_penugasan), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKategoriMesinById(String id_kategori_mesin) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblKategoriMesin.tbl_kategori_mesin +
                " WHERE "+ TblKategoriMesin.key_id_kategori_mesin +" = %s", id_kategori_mesin), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countMesinById(String id_mesin) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblMesin.tbl_mesin +
                " WHERE "+ TblMesin.key_id_mesin +" = %s", id_mesin), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countCekMesinById(String id_cek_mesin) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblCekMesin.tbl_cek_mesin +
                " WHERE "+ TblCekMesin.key_id_cek_mesin +" = %s", id_cek_mesin), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countUsersById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblUsers.tbl_users +
                " WHERE "+ TblUsers.key_id +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countPenugasanById(String id_penugasan) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblPenugasan.tbl_penugasan +
                " WHERE "+ TblPenugasan.key_id_penugasan +" = %s", id_penugasan), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countTugasByIdPenugasanNo(String id_penugasan, String no) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblTugas.tbl_tugas +
                " WHERE " + TblTugas.key_id_penugasan + " = %s and " + TblTugas.key_no + " = %s ", id_penugasan, no), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countShiftById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblShift.tbl_shift +
                " WHERE "+ TblShift.key_id_shift +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countAreaById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblArea.tbl_area +
                " WHERE "+ TblArea.key_id_area +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblKuesioner.tbl_kuesioner +
                " WHERE "+ TblKuesioner.key_id +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerResultById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblKuesionerResult.tbl_kuesioner_result +
                " WHERE "+ TblKuesionerResult.key_id_kuesioner_result +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerResultByIdUser(String id_user, String start, String end) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblKuesionerResult.tbl_kuesioner_result +
                " WHERE "+ TblKuesionerResult.key_id_user +" = %s AND "+
                TblKuesionerResult.key_tanggal+" BETWEEN '%s' AND '%s'", id_user, start, end), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerDetail1ById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblKuesionerDetail1.tbl_kuesioner_detail_1 +
                " WHERE "+ TblKuesionerDetail1.key_id_kuesioner_detail_1 +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerDetail2ById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblKuesionerDetail2.tbl_kuesioner_detail_2 +
                " WHERE "+ TblKuesionerDetail2.key_id_kuesioner_detail_2 +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }

    public CheckList getCheckList(String id, String id_operator) {
        CheckList data = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                "tb_penugasan.id_cek_mesin," +
                "tb_mesin.id_mesin," +
                "tb_mesin.kode_nfc," +
                "tb_mesin.nama," +
                "tb_kategori_mesin.id_kategori_mesin," +
                "tb_kategori_mesin.nama as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                "tb_penugasan.created_at as penugasan_created_at," +
                "tb_penugasan.updated_at as penugasan_updated_at," +
                "tb_penugasan.deleted_at as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE id_penugasan = %s AND " +
                "id_operator = %s", id, id_operator);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cursor supervisor = getUsers(cursor.getString(cursor.getColumnIndex("id_supervisor")));
                    Cursor operator = getUsers(cursor.getString(cursor.getColumnIndex("id_operator")));

                    data = new CheckList(
                            cursor.getString(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("tanggal")),
                            (supervisor!=null?
                                    new Supervisor(
                                            supervisor.getString(supervisor.getColumnIndex("id")),
                                            supervisor.getString(supervisor.getColumnIndex("nama")),
                                            supervisor.getString(supervisor.getColumnIndex("image")),
                                            supervisor.getString(supervisor.getColumnIndex("phone")),
                                            supervisor.getString(supervisor.getColumnIndex("level"))
                                    )
                                    :
                                    null
                            ),
                            (operator!=null?
                                    new Operator(
                                            operator.getString(operator.getColumnIndex("id")),
                                            operator.getString(operator.getColumnIndex("nama")),
                                            operator.getString(operator.getColumnIndex("image")),
                                            operator.getString(operator.getColumnIndex("phone")),
                                            operator.getString(operator.getColumnIndex("level"))
                                    )
                                    :
                                    null
                            ),
                            new MachineCheck(
                                    cursor.getString(cursor.getColumnIndex("id_cek_mesin")),
                                    new Machine(
                                            cursor.getString(cursor.getColumnIndex("id_mesin")),
                                            cursor.getString(cursor.getColumnIndex("kode_nfc")),
                                            cursor.getString(cursor.getColumnIndex("nama")),
                                            new MachineCategory(
                                                    cursor.getString(cursor.getColumnIndex(("id_kategori_mesin"))),
                                                    cursor.getString(cursor.getColumnIndex("nama_kategori"))
                                            )
                                    )
                            ),
                            list_job(cursor.getString(cursor.getColumnIndex("id"))),
                            (cursor.getInt(cursor.getColumnIndex(("status")))  >= 1),
                            cursor.getString(cursor.getColumnIndex(("alasan"))),
                            cursor.getInt(cursor.getColumnIndex(("sync")))
                    );

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return data;
    }
    @SuppressLint("SimpleDateFormat")
    public KuesionerResult getKuesionerResult(String id_kuesioner_result) {
        KuesionerResult kuesionerResult = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select tb_kuesioner_result.id_kuesioner_result as id," +
                "tb_kuesioner_result.id_user," +
                "tb_shift.id_shift," +
                "tb_shift.nama as nama_shift," +
                "tb_area.id_area," +
                "tb_area.nama as nama_area," +
                "tb_kuesioner.id_kuesioner," +
                "tb_kuesioner.nama as nama_kuesioner," +
                "tb_kuesioner_result.jawaban," +
                "tb_kuesioner_result.result," +
                "tb_kuesioner_result.status," +
                "tb_kuesioner_result.tanggal " +
                "FROM tb_kuesioner_result " +
                "JOIN tb_shift ON tb_kuesioner_result.id_shift = tb_shift.id_shift " +
                "JOIN tb_area ON tb_kuesioner_result.id_area = tb_area.id_area " +
                "JOIN tb_kuesioner ON tb_kuesioner_result.id_kuesioner = tb_kuesioner.id_kuesioner " +
                "WHERE tb_kuesioner_result.id_kuesioner_result = %s", id_kuesioner_result);
        Log.i("app-log [query]",sql);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int no = 1;
                do {
                    kuesionerResult = new KuesionerResult(
                            String.valueOf(no),
                            cursor.getString(cursor.getColumnIndex("id")),
                            new Shift(
                                    cursor.getString(cursor.getColumnIndex("id_shift")),
                                    cursor.getString(cursor.getColumnIndex("nama_shift"))
                            ),
                            new Area(
                                    cursor.getString(cursor.getColumnIndex("id_area")),
                                    cursor.getString(cursor.getColumnIndex("nama_area"))
                            ),
                            new Kuesioner(
                                    cursor.getString(cursor.getColumnIndex("id_kuesioner")),
                                    cursor.getString(cursor.getColumnIndex("nama_kuesioner"))
                            ),
                            cursor.getString(cursor.getColumnIndex("jawaban")),
                            getListKuesionerDetail1(cursor.getString(cursor.getColumnIndex("id_kuesioner"))),
                            cursor.getString(cursor.getColumnIndex("result")),
                            cursor.getString(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("tanggal"))
                    );
                    no++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return kuesionerResult;
    }

    public int delete_tugasByIdPenugasanNo(String id_penugasan, String no) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TblTugas.tbl_tugas,TblTugas.key_id_penugasan+"= ? and "+TblTugas.key_no+"= ?",new String[]{id_penugasan,no});
    }
    public int delete_kuesionerResultById(String id_kuesioner_result) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TblKuesionerResult.tbl_kuesioner_result,TblKuesionerResult.key_id_kuesioner_result+"= ?",new String[]{id_kuesioner_result});
    }

}