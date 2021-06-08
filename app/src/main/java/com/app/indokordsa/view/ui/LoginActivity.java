package com.app.indokordsa.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityLoginBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Loginlistener;
import com.app.indokordsa.viewmodel.LoginViewModel;
import com.app.indokordsa.viewmodel.LoginViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Loginlistener {
    LoginViewModel vmodel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        vmodel = new ViewModelProvider(this, new LoginViewModelFactory(this)).get(LoginViewModel.class);
        binding.setModel(vmodel.model);
        binding.setAction(this);

        //gagal ambil payload
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            String payload = bundle.getString("payload","-");
//            Log.i("app-log login",payload);
//        }

//        FirebaseMessaging fcm = FirebaseMessaging.getInstance();
//        fcm.subscribeToTopic("pengumuman");
//        fcm.getToken().addOnSuccessListener(s -> {
//            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
//            Log.i("app-log token",s);
//        });

//        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
//        SpannableString ss = new SpannableString(binding.txtBarcodeLogin.getText().toString());
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                startActivity(new Intent(LoginActivity.this,BarcodeActivity.class));
//            }
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//                ds.setColor(Color.BLACK);
//            }
//        };
//
//        String spannableString = ss.toString();
//        int start = spannableString.indexOf("here");
//        int end = start + "here".length();
////        ss.setSpan(bold, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////        ss.setSpan(new CustomTypefaceSpan(getResources().getFont(R.font.helvetica_neue_condensed_bold)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        binding.txtBarcodeLogin.setText(ss);
//        binding.txtBarcodeLogin.setMovementMethod(LinkMovementMethod.getInstance());
//        binding.txtBarcodeLogin.setHighlightColor(Color.TRANSPARENT);
    }

    public void loginScanBarcode(){
        startActivity(new Intent(LoginActivity.this,BarcodeActivity.class));
    }

    public void doLogin(){
        binding.btnMasukLogin.setVisibility(View.GONE);
        binding.btnMasukLoadingLogin.setVisibility(View.VISIBLE);
        vmodel.doLogin();
    }

    void closeLoading(String message){
        binding.btnMasukLogin.setVisibility(View.VISIBLE);
        binding.btnMasukLoadingLogin.setVisibility(View.GONE);
        Snackbar.make(binding.layoutLogin,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onErrorValidation(String message) {
        closeLoading(message);
    }

    @Override
    public void onFailAuth(String message) {
        closeLoading(message);
    }

    @Override
    public void onSuccessAuth(String message, String respon) {
        try {
            JSONObject obj = new JSONObject(respon);
            new SessionManager(this).createSession(obj.getString("id"),obj.getString("nama"));

            closeLoading(message);
            Snackbar.make(binding.layoutLogin,message,Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(() -> startActivity(new Intent(LoginActivity.this, MainActivity.class)), 1000L);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}