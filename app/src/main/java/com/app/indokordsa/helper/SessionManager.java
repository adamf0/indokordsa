package com.app.indokordsa.helper;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.app.indokordsa.view.ui.LoginActivity;

public class SessionManager {
    public SharedPreferences pref;
    public Editor editor;
    public Context _context;

    public static final String PREF_NAME = "Session";
    public static final String KEY_IS_LOGIN = "islogin";
    public static final String KEY_ID_USER = "id_user";
    public static final String KEY_Name = "name";
    public static final String KEY_ID_CHECKLIST = "id_checklist";
    public static final String KEY_NFC = "nfc";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSession(String id_user,String name){
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_ID_USER, id_user);
        editor.putString(KEY_Name, name);
        editor.commit();
    }
    public void SessionTapNfc(String id_checklist,String nfc){
        editor.putString(KEY_ID_CHECKLIST, id_checklist);
        editor.putString(KEY_NFC, nfc);
        editor.commit();
    }

//    public void checkLogin(){
//        if(!this.isLoggedIn()){
//            Intent i = new Intent(_context, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            _context.startActivity(i);
//        }
//    }

    public HashMap<String, String> getSession(){
        HashMap<String, String> data = new HashMap<>();
        data.put(KEY_ID_USER, pref.getString(KEY_ID_USER, null));
        data.put(KEY_Name, pref.getString(KEY_Name, null));
        return data;
    }

//    public void logout(){
//        editor.clear();
//        editor.commit();
//
//        Intent i = new Intent(_context, LoginActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        _context.startActivity(i);
//    }
//
//    private boolean isLoggedIn(){
//        return pref.getBoolean(KEY_IS_LOGIN, false);
//    }
}