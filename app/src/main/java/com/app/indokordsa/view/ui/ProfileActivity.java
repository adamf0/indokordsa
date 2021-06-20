package com.app.indokordsa.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.etc.Util;
import com.app.indokordsa.databinding.ActivityProfileBinding;
import com.app.indokordsa.helper.PermissionHelper;
import com.app.indokordsa.helper.RealPathUtil;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Profilelistener;
import com.app.indokordsa.viewmodel.ProfileViewModel;
import com.app.indokordsa.viewmodel.ProfileViewModelFactory;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity implements Profilelistener, PermissionHelper.PermissionListener {
    ProfileViewModel vmodel;
    ActivityProfileBinding binding;
    PermissionHelper permissionHelper;
    SessionManager session;

    int FILE = 1;
    Uri Uri_foto;
    String file_foto;
    HashMap<String, String> data_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        vmodel = new ViewModelProvider(this,new ProfileViewModelFactory(this,new SessionManager(this))).get(ProfileViewModel.class);

        binding.setModel(vmodel.model);
        binding.setAction(this);

        loadData();
        session = new SessionManager(this);
        data_session = session.getSession();

        permissionHelper = new PermissionHelper(this);
        permissionHelper.permissionListener(this);

//        SpannableString ss = new SpannableString(binding.txtWarningProfile.getText().toString());
//        String spannableString = ss.toString();
//        int start = spannableString.indexOf("Admin");
//        int end = start + "Admin".length();
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        binding.txtWarningProfile.setText(ss);
//        binding.txtWarningProfile.setMovementMethod(LinkMovementMethod.getInstance());
//        binding.txtWarningProfile.setHighlightColor(Color.TRANSPARENT);
    }

    public void back(){
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void select_picture(){
        permissionHelper.checkAndRequestPermissions();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
    }

    public void loadData(){
        binding.scrollProfile.setVisibility(View.GONE);
        binding.layoutHeaderProfile.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        vmodel.loadData();
    }

    public void update(){
        binding.scrollProfile.setVisibility(View.GONE);
        binding.layoutHeaderProfile.setVisibility(View.GONE);
        binding.loader.layoutLoading.setVisibility(View.VISIBLE);
        vmodel.updateProfile();
    }

    void close_dialog(String message){
        new Handler().postDelayed(() -> {
            binding.scrollProfile.setVisibility(View.VISIBLE);
            binding.layoutHeaderProfile.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);
            Snackbar.make(binding.layoutProfile,message,Snackbar.LENGTH_LONG).show();
        }, 1000);
    }

    @Override
    public void onFail(String message) {
        close_dialog(message);
    }

    @Override
    public void onError(String message) {
        close_dialog(message);
    }

    @Override
    public void onSuccess(String message) {
        HashMap<String, String> data = session.getSession();
        new SessionManager(this).createSession(data.get(SessionManager.KEY_ID_USER),vmodel.model.getNama());
        close_dialog(message);
    }

    @Override
    public void onFailGet(String message) {
        close_dialog(message);
    }

    @Override
    public void onSuccessGet(String response) {
        new Handler().postDelayed(() -> {
            binding.scrollProfile.setVisibility(View.VISIBLE);
            binding.layoutHeaderProfile.setVisibility(View.VISIBLE);
            binding.loader.layoutLoading.setVisibility(View.GONE);

            try {
                JSONObject obj = new JSONObject(response);
                binding.getModel().setNama(obj.getString("nama"));
                binding.getModel().setPhone(obj.getString("phone"));
                binding.getModel().setUsername(obj.getString("username"));
                binding.getModel().setEmail(obj.getString("email"));
                binding.getModel().setPassword("");
                binding.getModel().setImage(obj.getString("image"));

                if(!obj.isNull("image")){
                    Glide.with(this).load(String.format("http://indokordsa.media-phonix.co.id/assets/user/image/%s",obj.getString("image"))).into(binding.imgUserProfile);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    @Override
    public void onPermissionCheckDone() {
        Log.i("app-log","akses permission berhasil");

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("xapp-log", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE && data != null && data.getData() != null) {
                Uri_foto = data.getData();
                try {
                    Log.i("app-log uri",Uri_foto.toString());
                    file_foto = RealPathUtil.getRealPath(this,Uri_foto);
                    Log.i("app-log path:", (file_foto==null? "null":file_foto));
                    if(file_foto==null){
                        InputStream iStream = getContentResolver().openInputStream(Uri_foto);
                        byte[] inputData = Util.getBytes(iStream);

                        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Indokorsda");
                        if (!mediaStorageDir.exists()) {
                            if (!mediaStorageDir.mkdirs()) {
                                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                            }
                        }

                        String timeStamp = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(new Date());
                        File file_pdf_copy = new File(mediaStorageDir.getPath(), String.format("%s_%s.jpg",data_session.get(SessionManager.KEY_ID_USER),timeStamp));
                        OutputStream op = new FileOutputStream(file_pdf_copy);
                        op.write(inputData);
                        file_foto = file_pdf_copy.getPath();
                        Log.i("app-log path:", (file_foto==null? "null":file_foto));
                    }

                    vmodel.model.setImage_file(new File(file_foto));
                    Glide.with(this).load(Uri_foto).into(binding.imgUserProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}