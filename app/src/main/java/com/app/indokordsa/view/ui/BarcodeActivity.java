package com.app.indokordsa.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.indokordsa.R;
import com.app.indokordsa.databinding.ActivityBarcodeBinding;
import com.app.indokordsa.helper.SessionManager;
import com.app.indokordsa.interfaces.Barcodelistener;
import com.app.indokordsa.viewmodel.BarcodeViewModel;
import com.app.indokordsa.viewmodel.BarcodeViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class BarcodeActivity extends AppCompatActivity implements Barcodelistener{
    BarcodeViewModel vmodel;
    ActivityBarcodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_barcode);
        vmodel = new ViewModelProvider(this,new BarcodeViewModelFactory(this)).get(BarcodeViewModel.class);
        binding.setAction(this);
    }

    public void back(){
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void onTextChange(Editable s) {
        if(s.length()>0){
            doLogin(s.toString());
        }
    }

    void doLogin(String barcode){
        binding.txtErrorBarcode.setVisibility(View.GONE);
        binding.loadingBarcode.setVisibility(View.VISIBLE);

        vmodel.doLogin(barcode);
    }

    @Override
    public void onError(String message) {
        binding.edtInputBarcode.setText("");
        binding.txtErrorBarcode.setText(message);
        binding.txtErrorBarcode.setVisibility(View.VISIBLE);
        binding.loadingBarcode.setVisibility(View.GONE);
    }

    @Override
    public void onFail(String message) {
        binding.edtInputBarcode.setText("");
        binding.txtErrorBarcode.setText(message);
        binding.txtErrorBarcode.setVisibility(View.VISIBLE);
        binding.loadingBarcode.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String message, String respon) {
        try {
            JSONObject obj = new JSONObject(respon);
            new SessionManager(this).createSession(obj.getString("id"),obj.getString("nama"));

            binding.txtErrorBarcode.setVisibility(View.GONE);
            binding.loadingBarcode.setVisibility(View.GONE);
            startActivity(new Intent(this, MainActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}