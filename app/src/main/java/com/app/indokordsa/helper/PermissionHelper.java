package com.app.indokordsa.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionHelper {

    private final Activity mActivity;
    private final int REQUEST_PERMISSION = 99;
    private PermissionListener listener;

    public PermissionHelper(Activity activity){
        mActivity = activity;
    }

    public void permissionListener(PermissionListener permissionListener){
        listener = permissionListener;
    }


    public boolean checkAndRequestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCamera = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
            int permissionReadStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWriteStorage = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int permissionShortut = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.INSTALL_SHORTCUT);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
//            if (permissionShortut != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION);
                return false;
            }

        }

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        listener.onPermissionCheckDone();

        return true;
    }

    public void onRequestCallBack(int RequestCode, String[] permissions, int[] grantResults){//2. call this inside onRequestPermissionsResult
        if (RequestCode == REQUEST_PERMISSION) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//            perms.put(Manifest.permission.INSTALL_SHORTCUT, PackageManager.PERMISSION_GRANTED);
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                String TAG = "PermissionHelper";
                if (
                        perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                                perms.get(Manifest.permission.INSTALL_SHORTCUT) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e(TAG, "permission granted");

                    checkAndRequestPermissions();
                } else {
                    Log.e(TAG, "Some permissions are not granted ask again ");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.INSTALL_SHORTCUT)
                    ) {
                        showDialogOK("OK",
                                (dialog, which) -> {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        checkAndRequestPermissions();
                                    }
                                });
                    } else {
                        Toast.makeText(mActivity, "setting", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                        intent.setData(uri);
                        mActivity.startActivity(intent);
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    public interface PermissionListener{
        void onPermissionCheckDone();
    }

}