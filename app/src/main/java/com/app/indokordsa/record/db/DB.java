package com.app.indokordsa.record.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.indokordsa.record.db.table.TblArea;
import com.app.indokordsa.record.db.table.TblCekMesin;
import com.app.indokordsa.record.db.table.TblJawabanKuesioner;
import com.app.indokordsa.record.db.table.TblKategoriMesin;
import com.app.indokordsa.record.db.table.TblKuesioner;
import com.app.indokordsa.record.db.table.TblKuesionerResult;
import com.app.indokordsa.record.db.table.TblKuesionerResultDetail;
import com.app.indokordsa.record.db.table.TblMesin;
import com.app.indokordsa.record.db.table.TblPenugasan;
import com.app.indokordsa.record.db.table.TblPertanyaanKuesioner;
import com.app.indokordsa.record.db.table.TblShift;
import com.app.indokordsa.record.db.table.TblTopikKuesioner;
import com.app.indokordsa.record.db.table.TblTugas;
import com.app.indokordsa.record.db.table.TblUsers;
import com.app.indokordsa.view.model.Area;
import com.app.indokordsa.view.model.CheckList;
import com.app.indokordsa.view.model.JawabanKuesioner;
import com.app.indokordsa.view.model.Job;
import com.app.indokordsa.view.model.Kuesioner;
import com.app.indokordsa.view.model.KuesionerResult;
import com.app.indokordsa.view.model.KuesionerResultDetail;
import com.app.indokordsa.view.model.Machine;
import com.app.indokordsa.view.model.MachineCategory;
import com.app.indokordsa.view.model.MachineCheck;
import com.app.indokordsa.view.model.Operator;
import com.app.indokordsa.view.model.Pertanyaan;
import com.app.indokordsa.view.model.Shift;
import com.app.indokordsa.view.model.Supervisor;
import com.app.indokordsa.view.model.Topik;

import java.util.ArrayList;

import static com.app.indokordsa.etc.Util.SC;

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
        db.execSQL(TblTopikKuesioner.create_table);
        db.execSQL(TblPertanyaanKuesioner.create_table);
        db.execSQL(TblJawabanKuesioner.create_table);
        db.execSQL(TblKuesionerResult.create_table);
        db.execSQL(TblKuesionerResultDetail.create_table);

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
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblTopikKuesioner.tbl_topik_kuesioner));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblPertanyaanKuesioner.tbl_pertanyaan_kuesioner));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblJawabanKuesioner.tbl_jawaban_kuesioner));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKuesionerResult.tbl_kuesioner_result));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TblKuesionerResultDetail.tbl_kuesioner_result_detail));
        onCreate(db);
    }

    public Long save_cek_mesin(String id_cek_mesin,String id_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblCekMesin.tbl_cek_mesin, null, _cek_mesin(id_cek_mesin,id_mesin,false));
    }
    public int update_cek_mesin(String id_cek_mesin,String id_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblCekMesin.tbl_cek_mesin,_cek_mesin(id_cek_mesin,id_mesin,true),TblCekMesin.key_id_cek_mesin+"= ?",new String[]{id_cek_mesin});
    }
    ContentValues _cek_mesin(String id_cek_mesin,String id_mesin,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblCekMesin.key_id_cek_mesin, id_cek_mesin);
        values.put(TblCekMesin.key_id_mesin, id_mesin);
        return values;
    }

    public Long save_kategori_mesin(String id_kategori_mesin,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblKategoriMesin.tbl_kategori_mesin, null, _kategori_mesin(id_kategori_mesin,nama,false));
    }
    public int update_kategori_mesin(String id_kategori_mesin,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblKategoriMesin.tbl_kategori_mesin,_kategori_mesin(id_kategori_mesin,nama,true),TblKategoriMesin.key_id_kategori_mesin+"= ?",new String[]{id_kategori_mesin});
    }
    ContentValues _kategori_mesin(String id_kategori_mesin,String nama,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblKategoriMesin.key_id_kategori_mesin, id_kategori_mesin);
        values.put(TblKategoriMesin.key_nama, nama);
        return values;
    }

    public Long save_mesin(String id_mesin,String kode_nfc,String nama,String id_kategori_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblMesin.tbl_mesin, null, _mesin(id_mesin,kode_nfc,nama,id_kategori_mesin,false));
    }
    public int update_mesin(String id_mesin,String kode_nfc,String nama,String id_kategori_mesin) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblMesin.tbl_mesin,_mesin(id_mesin,kode_nfc,nama,id_kategori_mesin,true),TblMesin.key_id_mesin+"= ?",new String[]{id_mesin});
    }
    ContentValues _mesin(String id_mesin,String kode_nfc,String nama,String id_kategori_mesin,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblMesin.key_id_mesin, id_mesin);
        values.put(TblMesin.key_kode_nfc, kode_nfc);
        values.put(TblMesin.key_nama, nama);
        values.put(TblMesin.key_id_kategori_mesin, id_kategori_mesin);
        return values;
    }

    public Long save_penugasan(String id_penugasan,String id_supervisor,String id_operator,String id_cek_mesin,String bulan, String tahun,String status,String alasan,String sync,String created_at,String updated_at,String deleted_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblPenugasan.tbl_penugasan, null, _penugasan(id_penugasan,id_supervisor,id_operator,id_cek_mesin,bulan,tahun,status,sync,alasan,created_at,updated_at,deleted_at,false));
    }
    public int update_penugasan(String id_penugasan,String id_supervisor,String id_operator,String id_cek_mesin,String bulan, String tahun,String status,String alasan,String sync,String created_at,String updated_at,String deleted_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblPenugasan.tbl_penugasan,_penugasan(id_penugasan,id_supervisor,id_operator,id_cek_mesin,bulan,tahun,status,sync,alasan,created_at,updated_at,deleted_at,true),TblPenugasan.key_id_penugasan+"= ?",new String[]{id_penugasan});
    }
    ContentValues _penugasan(String id_penugasan,String id_supervisor,String id_operator,String id_cek_mesin,String bulan, String tahun,String status,String sync,String alasan,String created_at,String updated_at,String deleted_at,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
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
        return values;
    }

    public Long save_tugas(String id_penugasan,String no,String cek,String kat, String val,String ket) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblTugas.tbl_tugas, null, _tugas(id_penugasan,no,cek,kat,val,ket,false));
    }
    public int update_tugas(String id_penugasan,String no,String cek,String kat, String val,String ket) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblTugas.tbl_tugas,_tugas(id_penugasan,no,cek,kat,val,ket,true),TblTugas.key_id_penugasan+"= ? and "+TblTugas.key_no+"= ?",new String[]{id_penugasan,no});
    }
    ContentValues _tugas(String id_penugasan,String no,String cek,String kat, String val,String ket,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate) {
            values.put(TblTugas.key_id_penugasan, id_penugasan);
            values.put(TblTugas.key_no, no);
        }
        values.put(TblTugas.key_cek, cek);
        values.put(TblTugas.key_kat, kat);
        values.put(TblTugas.key_val, val);
        values.put(TblTugas.key_ket, ket);
        return values;
    }

    public Long save_users(String id,String phone,String image,String nama,String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblUsers.tbl_users, null, _users(id,phone,image,nama,level,false));
    }
    public int update_users(String id,String phone,String image,String nama,String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblUsers.tbl_users,_users(id,phone,image,nama,level,true),TblUsers.key_id+"= ?",new String[]{id});
    }
    ContentValues _users(String id,String phone,String image,String nama,String level,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblUsers.key_id, id);
        values.put(TblUsers.key_phone, phone);
        values.put(TblUsers.key_image, image);
        values.put(TblUsers.key_nama, nama);
        values.put(TblUsers.key_level, level);
        return values;
    }

    public Long save_shift(String id,String nama,String mulai,String selesai,String ganti_hari) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblShift.tbl_shift, null, _shift(id,nama,mulai,selesai,ganti_hari,false));
    }
    public int update_shift(String id,String nama,String mulai,String selesai,String ganti_hari) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblShift.tbl_shift,_shift(id,nama,mulai,selesai,ganti_hari,true),TblShift.key_id_shift+"= ?",new String[]{id});
    }
    ContentValues _shift(String id,String nama,String mulai,String selesai,String ganti_hari,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblShift.key_id_shift, id);
        values.put(TblShift.key_nama, nama);
        values.put(TblShift.key_mulai, mulai);
        values.put(TblShift.key_selesai, selesai);
        values.put(TblShift.key_ganti_hari, ganti_hari);
        return  values;
    }

    public Long save_area(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblArea.tbl_area, null, _area(id,nama,false));
    }
    public int update_area(String id,String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblArea.tbl_area,_area(id,nama,true),TblArea.key_id_area+"= ?",new String[]{id});
    }
    ContentValues _area(String id,String nama,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblArea.key_id_area, id);
        values.put(TblArea.key_nama, nama);
        return values;
    }

    public Long save_kuesioner(String id,String id_area, String pertanyaan) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblKuesioner.tbl_kuesioner, null, _kuesioner(id,id_area,pertanyaan,false));
    }
    public int update_kuesioner(String id,String id_area, String pertanyaan) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblKuesioner.tbl_kuesioner,_kuesioner(id,id_area,pertanyaan,true),TblKuesioner.key_id_kuesioner+"= ?",new String[]{id});
    }
    ContentValues _kuesioner(String id,String id_area, String pertanyaan,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblKuesioner.key_id_kuesioner, id);
        values.put(TblKuesioner.key_id_area, id_area);
        values.put(TblKuesioner.key_pertanyaan, pertanyaan);
        return values;
    }

    public Long save_topik_kuesioner(String id,String id_kuesioner, String tentang) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblTopikKuesioner.tbl_topik_kuesioner, null, _topik_kuesioner(id,id_kuesioner,tentang,false));
    }
    public int update_topik_kuesioner(String id,String id_kuesioner, String tentang) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblTopikKuesioner.tbl_topik_kuesioner,_topik_kuesioner(id,id_kuesioner,tentang,true),TblTopikKuesioner.key_id_topik_kuesioner+"= ?",new String[]{id});
    }
    ContentValues _topik_kuesioner(String id,String id_kuesioner, String tentang,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblTopikKuesioner.key_id_topik_kuesioner, id);
        values.put(TblTopikKuesioner.key_id_kuesioner, id_kuesioner);
        values.put(TblTopikKuesioner.key_tentang, tentang);
        return values;
    }

    public Long save_pertanyaan(String id,String id_topik_kuesioner, String pertanyaan) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblPertanyaanKuesioner.tbl_pertanyaan_kuesioner, null, _pertanyaan(id,id_topik_kuesioner,pertanyaan,false));
    }
    public int update_pertanyaan(String id,String id_topik_kuesioner, String pertanyaan) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblPertanyaanKuesioner.tbl_pertanyaan_kuesioner,_pertanyaan(id,id_topik_kuesioner,pertanyaan,true),TblPertanyaanKuesioner.key_id_pertanyaan_kuesioner+"= ?",new String[]{id});
    }
    ContentValues _pertanyaan(String id,String id_topik_kuesioner, String pertanyaan,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblPertanyaanKuesioner.key_id_pertanyaan_kuesioner, id);
        values.put(TblPertanyaanKuesioner.key_id_topik_kuesioner, id_topik_kuesioner);
        values.put(TblPertanyaanKuesioner.key_pertanyaan, pertanyaan);
        return values;
    }

    public Long save_kuesioner_hasil(String id_kuesioner_result_detail,String id_topik_kuesioner, String id_pertanyaan_kuesioner, String val, String other, String start, String end, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblJawabanKuesioner.tbl_jawaban_kuesioner, null, _kuesioner_hasil(id_kuesioner_result_detail,id_topik_kuesioner,id_pertanyaan_kuesioner,val,other,start,end,remarks,false));
    }
    public int update_kuesioner_hasil(String id_kuesioner_result_detail,String id_topik_kuesioner, String id_pertanyaan_kuesioner, String val, String other, String start, String end, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(
                TblJawabanKuesioner.tbl_jawaban_kuesioner,
                _kuesioner_hasil(id_kuesioner_result_detail,id_topik_kuesioner,id_pertanyaan_kuesioner,val,other,start,end,remarks,true),
                TblJawabanKuesioner.key_id_kuesioner_result_detail+"= ? AND "+ TblJawabanKuesioner.key_id_topik_kuesioner+"= ? AND "+ TblJawabanKuesioner.key_id_pertanyaan_kuesioner+"= ?",
                new String[]{id_kuesioner_result_detail,id_topik_kuesioner,id_pertanyaan_kuesioner}
        );
    }
    ContentValues _kuesioner_hasil(String key_id_kuesioner_result_detail,String id_topik_kuesioner, String id_pertanyaan_kuesioner, String val, String other, String start, String end, String remarks,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate) {
            values.put(TblJawabanKuesioner.key_id_kuesioner_result_detail, key_id_kuesioner_result_detail);
            values.put(TblJawabanKuesioner.key_id_topik_kuesioner, id_topik_kuesioner);
            values.put(TblJawabanKuesioner.key_id_pertanyaan_kuesioner, id_pertanyaan_kuesioner);
        }
        values.put(TblJawabanKuesioner.key_val, val);
        values.put(TblJawabanKuesioner.key_other, other);
        values.put(TblJawabanKuesioner.key_start, start);
        values.put(TblJawabanKuesioner.key_end, end);
        values.put(TblJawabanKuesioner.key_remarks, remarks);
        return values;
    }

    public Long save_kuesioner_result(String id_kuesioner_result,String id_user,String id_shift,String status,String alasan,String sync,String created_at,String updated_at,String deleted_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblKuesionerResult.tbl_kuesioner_result, null, _kuesioner_result(id_kuesioner_result,id_user,id_shift,status,alasan,sync,created_at,updated_at,deleted_at,false));
    }
    public int update_kuesioner_result(String id_kuesioner_result,String id_user,String id_shift,String status,String alasan,String sync,String created_at,String updated_at,String deleted_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblKuesionerResult.tbl_kuesioner_result,_kuesioner_result(id_kuesioner_result,id_user,id_shift,status,alasan,sync,created_at,updated_at,deleted_at,true), TblKuesionerResult.key_id_kuesioner_result+"= ?",new String[]{id_kuesioner_result});
    }
    ContentValues _kuesioner_result(String id_kuesioner_result,String id_user,String id_shift,String status,String alasan,String sync,String created_at,String updated_at, String deleted_at, boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblKuesionerResult.key_id_kuesioner_result, id_kuesioner_result);
        values.put(TblKuesionerResult.key_id_user, id_user);
        values.put(TblKuesionerResult.key_id_shift, id_shift);
        values.put(TblKuesionerResult.key_status, status);
        values.put(TblKuesionerResult.key_alasan, alasan);
        values.put(TblKuesionerResult.key_sync, sync);
        values.put(TblKuesionerResult.key_created_at, created_at);
        values.put(TblKuesionerResult.key_updated_at, updated_at);
        values.put(TblKuesionerResult.key_deleted_at, deleted_at);
        return values;
    }

    public int update_sinkron_penugasan(String id_penugasan,String sinkron) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TblPenugasan.key_sync, sinkron);
        return db.update(TblPenugasan.tbl_penugasan,values,TblPenugasan.key_id_penugasan+"= ?",new String[]{id_penugasan});
    }
    public int update_sinkron_kuesioner_result(String id_kuesioner_result,String sinkron) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblKuesionerResult.key_sync, sinkron);
        return db.update(TblKuesionerResult.tbl_kuesioner_result,values, TblKuesionerResult.key_id_kuesioner_result+"= ?",new String[]{id_kuesioner_result});
    }
    public int update_kuesioner_hasil(String id,String id_kuesioner_result,String id_topik_kuesioner, String id_pertanyaan_kuesioner, String val, String other, String start, String end, String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TblJawabanKuesioner.key_id_kuesioner_result_detail, id_kuesioner_result);
        values.put(TblJawabanKuesioner.key_id_topik_kuesioner, id_topik_kuesioner);
        values.put(TblJawabanKuesioner.key_id_pertanyaan_kuesioner, id_pertanyaan_kuesioner);
        values.put(TblJawabanKuesioner.key_val, val);
        values.put(TblJawabanKuesioner.key_other, other);
        values.put(TblJawabanKuesioner.key_start, start);
        values.put(TblJawabanKuesioner.key_end, end);
        values.put(TblJawabanKuesioner.key_remarks, remarks);
        return db.update(TblJawabanKuesioner.tbl_jawaban_kuesioner,values, TblJawabanKuesioner.key_id_jawaban_kuesioner+"= ?",new String[]{id});
    }

    public Long save_kuesioner_result_detail(String id_kuesioner_result_detail,String id_kuesioner_result,String id_kuesioner,String jawaban) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TblKuesionerResultDetail.tbl_kuesioner_result_detail, null, _kuesioner_result_detail(id_kuesioner_result_detail,id_kuesioner_result,id_kuesioner,jawaban,false));
    }
    public int update_kuesioner_result_detail(String id_kuesioner_result_detail,String id_kuesioner_result,String id_kuesioner,String jawaban) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TblKuesionerResultDetail.tbl_kuesioner_result_detail,_kuesioner_result_detail(id_kuesioner_result_detail,id_kuesioner_result,id_kuesioner,jawaban,true), TblKuesionerResultDetail.key_id_kuesioner_result_detail+"= ?",new String[]{id_kuesioner_result_detail});
    }
    ContentValues _kuesioner_result_detail(String id_kuesioner_result_detail,String id_kuesioner_result,String id_kuesioner,String jawaban,boolean isUpdate){
        ContentValues values = new ContentValues();
        if(!isUpdate)
            values.put(TblKuesionerResultDetail.key_id_kuesioner_result_detail, id_kuesioner_result_detail);
        values.put(TblKuesionerResultDetail.key_id_kuesioner_result, id_kuesioner_result);
        values.put(TblKuesionerResultDetail.key_id_kuesioner, id_kuesioner);
        values.put(TblKuesionerResultDetail.key_jawaban, jawaban);
        return values;
    }

    public ArrayList<CheckList> getListCheckList(String start,String end) {
        ArrayList<CheckList> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_id_cek_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_id_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_kode_nfc)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_nama)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_id_kategori_mesin)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_nama)+" as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_created_at)+" as penugasan_created_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_updated_at)+" as penugasan_updated_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_deleted_at)+" as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE tb_penugasan.created_at BETWEEN '%s' AND '%s' AND sync='0'", start,end);
        Log.i("app-log [query]",sql);

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

        String sql = String.format("Select "+
                "id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_id_cek_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_id_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_kode_nfc)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_nama)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_id_kategori_mesin)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_nama)+" as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_created_at)+" as penugasan_created_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_updated_at)+" as penugasan_updated_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_deleted_at)+" as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE id_operator = %s AND " +
                "tb_penugasan.created_at BETWEEN '%s' AND '%s'", id_operator,start,end);
        Log.i("app-log [query]",sql);

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

        String sql = String.format("Select * FROM " + TblTugas.tbl_tugas + " WHERE id_penugasan = %s", id_penugasan);
        Log.i("app-log [query]",sql);

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

    public ArrayList<KuesionerResult> getTaskKuesioner(String start, String end) {
        ArrayList<KuesionerResult> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result," +
                "id_user," +
                "id_shift," +
                "status," +
                "alasan," +
                "sync," +
                "created_at," +
                "updated_at," +
                "deleted_at " +
                "FROM tbl_kuesioner_result " +
                "WHERE DATE(created_at) BETWEEN '%s' AND '%s' AND sync='0'", start,end);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i("app-log [query][id]",cursor.getString(cursor.getColumnIndex("id_kuesioner_result")) );
                do {
                    Cursor shift = getShift(cursor.getString(cursor.getColumnIndex("id_shift")));
                    KuesionerResult tmp_kr = new KuesionerResult(
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_result")),
                            cursor.getString(cursor.getColumnIndex("id_user")),
                            (shift!=null?
                                    new Shift(
                                            shift.getString(shift.getColumnIndex("id_shift")),
                                            shift.getString(shift.getColumnIndex("nama")),
                                            shift.getString(shift.getColumnIndex("mulai")),
                                            shift.getString(shift.getColumnIndex("selesai")),
                                            shift.getString(shift.getColumnIndex("ganti_hari"))
                                    )
                                    :
                                    null
                            ),
                            new ArrayList<>(),
                            cursor.getInt(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("alasan")),
                            cursor.getInt(cursor.getColumnIndex("sync")),
                            cursor.getString(cursor.getColumnIndex("created_at")),
                            cursor.getString(cursor.getColumnIndex("updated_at")),
                            cursor.getString(cursor.getColumnIndex("deleted_at"))
                    );
                    tmp_kr.getList_kuesioner().addAll( getListKuesioner(cursor.getString(cursor.getColumnIndex("id_kuesioner_result"))) );
                    list_data.add(tmp_kr);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<KuesionerResult> getAllTaskKuesioner(String start, String end) {
        ArrayList<KuesionerResult> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result," +
                "id_user," +
                "id_shift," +
                "status," +
                "alasan," +
                "sync," +
                "created_at," +
                "updated_at," +
                "deleted_at " +
                "FROM tbl_kuesioner_result " +
                "WHERE DATE(created_at) BETWEEN '%s' AND '%s'", start,end);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i("app-log [query][id]",cursor.getString(cursor.getColumnIndex("id_kuesioner_result")) );
                do {
                    Cursor shift = getShift(cursor.getString(cursor.getColumnIndex("id_shift")));
                    KuesionerResult tmp_kr = new KuesionerResult(
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_result")),
                            cursor.getString(cursor.getColumnIndex("id_user")),
                            (shift!=null?
                                    new Shift(
                                            shift.getString(shift.getColumnIndex("id_shift")),
                                            shift.getString(shift.getColumnIndex("nama")),
                                            shift.getString(shift.getColumnIndex("mulai")),
                                            shift.getString(shift.getColumnIndex("selesai")),
                                            shift.getString(shift.getColumnIndex("ganti_hari"))
                                    )
                                    :
                                    null
                            ),
                            new ArrayList<>(),
                            cursor.getInt(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("alasan")),
                            cursor.getInt(cursor.getColumnIndex("sync")),
                            cursor.getString(cursor.getColumnIndex("created_at")),
                            cursor.getString(cursor.getColumnIndex("updated_at")),
                            cursor.getString(cursor.getColumnIndex("deleted_at"))
                    );
                    tmp_kr.getList_kuesioner().addAll( getListKuesioner(cursor.getString(cursor.getColumnIndex("id_kuesioner_result"))) );
                    list_data.add(tmp_kr);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<KuesionerResult> getTaskKuesioner(String id_operator, String start, String end) {
        ArrayList<KuesionerResult> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result," +
                "id_user," +
                "id_shift," +
                "status," +
                "alasan," +
                "sync," +
                "created_at," +
                "updated_at," +
                "deleted_at " +
                "FROM tbl_kuesioner_result " +
                "WHERE id_user = %s AND " +
                "DATE(created_at) BETWEEN '%s' AND '%s'", id_operator,start,end);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i("app-log [query][id]",cursor.getString(cursor.getColumnIndex("id_kuesioner_result")) );
                do {
                    Cursor shift = getShift(cursor.getString(cursor.getColumnIndex("id_shift")));
                    KuesionerResult tmp_kr = new KuesionerResult(
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_result")),
                            cursor.getString(cursor.getColumnIndex("id_user")),
                            (shift!=null?
                                    new Shift(
                                            shift.getString(shift.getColumnIndex("id_shift")),
                                            shift.getString(shift.getColumnIndex("nama")),
                                            shift.getString(shift.getColumnIndex("mulai")),
                                            shift.getString(shift.getColumnIndex("selesai")),
                                            shift.getString(shift.getColumnIndex("ganti_hari"))
                                    )
                                    :
                                    null
                            ),
                            new ArrayList<>(),
                            cursor.getInt(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("alasan")),
                            cursor.getInt(cursor.getColumnIndex("sync")),
                            cursor.getString(cursor.getColumnIndex("created_at")),
                            cursor.getString(cursor.getColumnIndex("updated_at")),
                            cursor.getString(cursor.getColumnIndex("deleted_at"))
                    );
                    tmp_kr.getList_kuesioner().addAll( getListKuesioner(cursor.getString(cursor.getColumnIndex("id_kuesioner_result"))) );
                    list_data.add(tmp_kr);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<KuesionerResultDetail> getListKuesioner(String id_kuesioner_result) {
        ArrayList<KuesionerResultDetail> list_data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result_detail," +
                "id_kuesioner_result," +
                SC(TblKuesionerResultDetail.tbl_kuesioner_result_detail,TblKuesionerResultDetail.key_id_kuesioner)+"," +
                SC(TblArea.tbl_area,TblArea.key_id_area)+"," +
                SC(TblArea.tbl_area,TblArea.key_nama)+"," +
                SC(TblKuesioner.tbl_kuesioner,TblKuesioner.key_pertanyaan)+"," +
                SC(TblArea.tbl_area,TblArea.key_nama)+"," +
                "jawaban " +
                "FROM tbl_kuesioner_result_detail " +
                "LEFT JOIN tb_kuesioner ON tbl_kuesioner_result_detail.id_kuesioner = tb_kuesioner.id_kuesioner " +
                "LEFT JOIN tb_area ON tb_kuesioner.id_area = tb_area.id_area " +
                "WHERE id_kuesioner_result='%s'", id_kuesioner_result);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i("app-log [query][id]",cursor.getString(cursor.getColumnIndex("id_kuesioner_result")) );
                do {
                    KuesionerResultDetail tmp = new KuesionerResultDetail(
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_result_detail")),
                            new Kuesioner(
                                    cursor.getString(cursor.getColumnIndex("id_kuesioner")),
                                    new Area(
                                            cursor.getString(cursor.getColumnIndex("id_area")),
                                            cursor.getString(cursor.getColumnIndex("nama"))
                                    ),
                                    cursor.getString(cursor.getColumnIndex("pertanyaan"))
                            ),
                            cursor.getString(cursor.getColumnIndex("jawaban")),
                            new ArrayList<>()
                    );
                    tmp.getList_pertanyaan().addAll( getListPertanyaan(cursor.getString(cursor.getColumnIndex("id_kuesioner_result_detail"))) );
                    list_data.add(tmp);
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return list_data;
    }
    public ArrayList<JawabanKuesioner> getListPertanyaan(String id_kuesioner_result_detail) {
        ArrayList<JawabanKuesioner> list_pertanyaan = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select " +
                "id_jawaban_kuesioner AS id," +
                "id_kuesioner_result_detail," +
                "IFNULL(tbl_topik_kuesioner.id_topik_kuesioner, '') as id_topik_kuesioner," +
                "IFNULL(tbl_topik_kuesioner.tentang,'') as tentang," +
                "IFNULL(tbl_pertanyaan_kuesioner.id_pertanyaan_kuesioner,'') as id_pertanyaan_kuesioner," +
                "IFNULL(tbl_pertanyaan_kuesioner.pertanyaan,'') as pertanyaan," +
                "val," +
                "other," +
                "start_," +
                "end_," +
                "remarks " +
                "FROM tbl_jawaban_kuesioner " +
                "LEFT JOIN tbl_topik_kuesioner ON tbl_jawaban_kuesioner.id_topik_kuesioner = tbl_topik_kuesioner.id_topik_kuesioner " +
                "LEFT JOIN tbl_pertanyaan_kuesioner ON tbl_jawaban_kuesioner.id_pertanyaan_kuesioner = tbl_pertanyaan_kuesioner.id_pertanyaan_kuesioner " +
                "WHERE id_kuesioner_result_detail = %s order by tbl_pertanyaan_kuesioner.id_pertanyaan_kuesioner ASC", id_kuesioner_result_detail);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list_pertanyaan.add(new JawabanKuesioner(
                            new Topik(
                                    cursor.getString(cursor.getColumnIndex("id_topik_kuesioner")),
                                    cursor.getString(cursor.getColumnIndex("tentang"))
                            ),
                            new Pertanyaan(
                                    cursor.getString(cursor.getColumnIndex("id_pertanyaan_kuesioner")),
                                    cursor.getString(cursor.getColumnIndex("pertanyaan"))
                            ),
                            cursor.getString(cursor.getColumnIndex("val")),
                            cursor.getString(cursor.getColumnIndex("other")),
                            cursor.getString(cursor.getColumnIndex("start_")),
                            cursor.getString(cursor.getColumnIndex("end_")),
                            cursor.getString(cursor.getColumnIndex("remarks"))
                    ));
                } while (cursor.moveToNext());
            }
        }

//        assert cursor != null;
//        cursor.close();
        return list_pertanyaan;
    }

    public Cursor getArea(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TblArea.tbl_area,
                new String[]{TblArea.key_id_area,TblArea.key_nama},
                TblArea.key_id_area + "=?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
    public Cursor getKuesioner(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TblKuesioner.tbl_kuesioner,
                new String[]{TblKuesioner.key_id_kuesioner,TblKuesioner.key_id_area,TblKuesioner.key_pertanyaan},
                TblKuesioner.key_id_kuesioner + "=?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
    public Cursor getShift(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TblShift.tbl_shift,
                new String[]{TblShift.key_id_shift,TblShift.key_nama,TblShift.key_mulai,TblShift.key_selesai,TblShift.key_ganti_hari},
                TblShift.key_id_shift + "=?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
    public Cursor getUsers(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TblUsers.tbl_users, new String[]{TblUsers.key_id,TblUsers.key_phone,TblUsers.key_image,TblUsers.key_nama,TblUsers.key_level}, TblUsers.key_id + "=?", new String[]{id}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
    public Cursor getTopik(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TblTopikKuesioner.tbl_topik_kuesioner,
                new String[]{TblTopikKuesioner.key_id_topik_kuesioner,TblTopikKuesioner.key_tentang},
                TblTopikKuesioner.key_id_topik_kuesioner + "=?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }
    public Cursor getPertanyaan(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TblPertanyaanKuesioner.tbl_pertanyaan_kuesioner,
                new String[]{TblPertanyaanKuesioner.key_id_pertanyaan_kuesioner,TblPertanyaanKuesioner.key_pertanyaan},
                TblPertanyaanKuesioner.key_id_pertanyaan_kuesioner + "=?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        return null;
    }

    public int countListCheckList(String id_operator, String start,String end) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = String.format("Select "+
                "id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_id_cek_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_id_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_kode_nfc)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_nama)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_id_kategori_mesin)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_nama)+" as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_created_at)+" as penugasan_created_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_updated_at)+" as penugasan_updated_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_deleted_at)+" as penugasan_deleted_at " +
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
        Cursor cursor = db.rawQuery(String.format("Select "+
                "id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_id_cek_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_id_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_kode_nfc)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_nama)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_id_kategori_mesin)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_nama)+" as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_created_at)+" as penugasan_created_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_updated_at)+" as penugasan_updated_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_deleted_at)+" as penugasan_deleted_at " +
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
                " WHERE "+ TblKuesioner.key_id_kuesioner +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countTopikById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblTopikKuesioner.tbl_topik_kuesioner +
                " WHERE "+ TblTopikKuesioner.key_id_topik_kuesioner +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countPertanyaanById(String id) {
        int num_row;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("Select * FROM " + TblPertanyaanKuesioner.tbl_pertanyaan_kuesioner +
                " WHERE "+ TblPertanyaanKuesioner.key_id_pertanyaan_kuesioner +" = %s", id), null);

        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerResultById(String id) {
        int num_row = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result," +
                "id_user," +
                "id_shift," +
                "status," +
                "alasan," +
                "sync," +
                "created_at," +
                "updated_at," +
                "deleted_at " +
                "FROM tbl_kuesioner_result " +
                "WHERE id_kuesioner_result = %s", id);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);
        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerResultByIdUser(String id_operator, String start, String end) {
        int num_row = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result," +
                "id_user," +
                "id_shift," +
                "status," +
                "alasan," +
                "sync," +
                "created_at," +
                "updated_at," +
                "deleted_at " +
                "FROM tbl_kuesioner_result " +
                "WHERE id_user = %s AND " +
                "DATE(created_at) BETWEEN '%s' AND '%s'", id_operator,start,end);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);
        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countJawabanKuesioner(String id_kuesioner_result_detail,String id_topik, String id_pertanyaan) {
        int num_row = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_jawaban_kuesioner AS id," +
                "id_kuesioner_result_detail," +
                "id_topik_kuesioner," +
                "id_pertanyaan_kuesioner," +
                "val," +
                "other," +
                "start_," +
                "end_, " +
                "remarks " +
                "FROM tbl_jawaban_kuesioner " +
                "WHERE id_kuesioner_result_detail = %s and id_topik_kuesioner = %s and id_pertanyaan_kuesioner = %s", id_kuesioner_result_detail, id_topik, id_pertanyaan);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);
        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }
    public int countKuesionerDetail(String id) {
        int num_row = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select * FROM tbl_kuesioner_result_detail WHERE id_kuesioner_result_detail = %s", id);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);
        num_row=cursor.getCount();
        cursor.close();
        return num_row;
    }

    public JawabanKuesioner getJawabanKuesioner(String id_kuesioner_result,String id_topik, String id_pertanyaan) {
        JawabanKuesioner data = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select " +
                        "id_jawaban_kuesioner AS id," +
                        "id_kuesioner_result," +
                        "IFNULL(tbl_topik_kuesioner.id_topik_kuesioner, '') as id_topik_kuesioner," +
                        "IFNULL(tbl_topik_kuesioner.tentang,'') as tentang," +
                        "IFNULL(tbl_pertanyaan_kuesioner.id_pertanyaan_kuesioner,'') as id_pertanyaan_kuesioner," +
                        "IFNULL(tbl_pertanyaan_kuesioner.pertanyaan,'') as pertanyaan," +
                        "val," +
                        "other," +
                        "start_," +
                        "end_, " +
                        "remarks " +
                        "FROM tbl_jawaban_kuesioner " +
                        "LEFT JOIN tbl_topik_kuesioner ON tbl_jawaban_kuesioner.id_topik_kuesioner = tbl_topik_kuesioner.id_topik_kuesioner " +
                        "LEFT JOIN tbl_pertanyaan_kuesioner ON tbl_jawaban_kuesioner.id_pertanyaan_kuesioner = tbl_pertanyaan_kuesioner.id_pertanyaan_kuesioner " +
                        "WHERE id_kuesioner_result = %s and id_topik_kuesioner = %s and id_pertanyaan_kuesioner = %s order by tbl_pertanyaan_kuesioner.id_pertanyaan_kuesioner ASC", id_kuesioner_result, id_topik, id_pertanyaan);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    data = new JawabanKuesioner(
                            new Topik(
                                    cursor.getString(cursor.getColumnIndex("id_topik_kuesioner")),
                                    cursor.getString(cursor.getColumnIndex("tentang"))
                            ),
                            new Pertanyaan(
                                    cursor.getString(cursor.getColumnIndex("id_pertanyaan_kuesioner")),
                                    cursor.getString(cursor.getColumnIndex("pertanyaan"))
                            ),
                            cursor.getString(cursor.getColumnIndex("val")),
                            cursor.getString(cursor.getColumnIndex("other")),
                            cursor.getString(cursor.getColumnIndex("start_")),
                            cursor.getString(cursor.getColumnIndex("end_")),
                            cursor.getString(cursor.getColumnIndex("remarks"))
                    );

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        assert cursor != null;
        cursor.close();
        return data;
    }
    public CheckList getCheckList(String id, String id_operator) {
        CheckList data = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_penugasan AS id," +
                "id_supervisor," +
                "id_operator," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_id_cek_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_id_mesin)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_kode_nfc)+"," +
                SC(TblMesin.tbl_mesin,TblMesin.key_nama)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_id_kategori_mesin)+"," +
                SC(TblKategoriMesin.tbl_kategori_mesin,TblKategoriMesin.key_nama)+" as nama_kategori," +
                "(tahun || '-' || bulan || '-01') as tanggal," +
//                "tb_penugasan.tugas," +
                "status," +
                "alasan," +
                "sync," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_created_at)+" as penugasan_created_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_updated_at)+" as penugasan_updated_at," +
                SC(TblPenugasan.tbl_penugasan,TblPenugasan.key_deleted_at)+" as penugasan_deleted_at " +
                "FROM tb_penugasan " +
                "JOIN tb_cek_mesin ON tb_penugasan.id_cek_mesin = tb_cek_mesin.id_cek_mesin " +
                "JOIN tb_mesin ON tb_cek_mesin.id_mesin = tb_mesin.id_mesin " +
                "JOIN tb_kategori_mesin ON tb_mesin.id_kategori_mesin = tb_kategori_mesin.id_kategori_mesin " +
                "WHERE id_penugasan = %s AND " +
                "id_operator = %s", id, id_operator);
        Log.i("app-log [query]",sql);

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
    public KuesionerResult getKuesionerResult(String id, String id_operator) {
        KuesionerResult data = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = String.format("Select "+
                "id_kuesioner_result," +
                "id_user," +
                "id_shift," +
                "status," +
                "alasan," +
                "sync," +
                "created_at," +
                "updated_at," +
                "deleted_at " +
                "FROM tbl_kuesioner_result " +
                "WHERE id_kuesioner_result = %s AND id_user = %s", id, id_operator);
        Log.i("app-log [query]",sql);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Cursor shift = getShift(cursor.getString(cursor.getColumnIndex("id_shift")));
                    KuesionerResult tmp_kr = new KuesionerResult(
                            cursor.getString(cursor.getColumnIndex("id_kuesioner_result")),
                            cursor.getString(cursor.getColumnIndex("id_user")),
                            (shift!=null?
                                    new Shift(
                                            shift.getString(shift.getColumnIndex("id_shift")),
                                            shift.getString(shift.getColumnIndex("nama")),
                                            shift.getString(shift.getColumnIndex("mulai")),
                                            shift.getString(shift.getColumnIndex("selesai")),
                                            shift.getString(shift.getColumnIndex("ganti_hari"))
                                    )
                                    :
                                    null
                            ),
                            new ArrayList<>(),
                            cursor.getInt(cursor.getColumnIndex("status")),
                            cursor.getString(cursor.getColumnIndex("alasan")),
                            cursor.getInt(cursor.getColumnIndex("sync")),
                            cursor.getString(cursor.getColumnIndex("created_at")),
                            cursor.getString(cursor.getColumnIndex("updated_at")),
                            cursor.getString(cursor.getColumnIndex("deleted_at"))
                    );
                    tmp_kr.getList_kuesioner().addAll( getListKuesioner(cursor.getString(cursor.getColumnIndex("id_kuesioner_result"))) );
                    return tmp_kr;
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return data;
    }

    public int delete_tugasByIdPenugasanNo(String id_penugasan, String no) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TblTugas.tbl_tugas,TblTugas.key_id_penugasan+"= ? and "+TblTugas.key_no+"= ?",new String[]{id_penugasan,no});
    }
    public int delete_ShiftById(String id_shift) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TblShift.tbl_shift,TblShift.key_id_shift+"= ?",new String[]{id_shift});
    }
    public int delete_KuesionerById(String id_kuesioner) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TblKuesioner.tbl_kuesioner,TblKuesioner.key_id_kuesioner+"= ?",new String[]{id_kuesioner});
    }
    public int delete_TopikById(String id_topik) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TblTopikKuesioner.tbl_topik_kuesioner,TblTopikKuesioner.key_id_topik_kuesioner+"= ?",new String[]{id_topik});
    }
    public void delete_KuesionerHasilById(String id_kuesioner_result_detail, String id_topik_kuesioner, String id_pertanyaan_kuesioner) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TblJawabanKuesioner.tbl_jawaban_kuesioner,
                TblJawabanKuesioner.key_id_kuesioner_result_detail + "= ? and " +
                        TblJawabanKuesioner.key_id_topik_kuesioner + "= ? and " +
                        TblJawabanKuesioner.key_id_pertanyaan_kuesioner + "= ?",
                new String[]{id_kuesioner_result_detail, id_topik_kuesioner, id_pertanyaan_kuesioner});
        db.close();
    }
    public void delete_KuesionerHasilByIdKuesionerResultDetail(String id_kuesioner_result_detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TblJawabanKuesioner.tbl_jawaban_kuesioner,
                TblJawabanKuesioner.key_id_kuesioner_result_detail + "= ?",
                new String[]{id_kuesioner_result_detail});
        db.close();
    }
    public void delete_KuesionerResultDetailById(String id_kuesioner_result_detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TblKuesionerResultDetail.tbl_kuesioner_result_detail,
                TblKuesionerResultDetail.key_id_kuesioner_result_detail + "= ?",
                new String[]{id_kuesioner_result_detail}
        );
        db.close();
    }
}