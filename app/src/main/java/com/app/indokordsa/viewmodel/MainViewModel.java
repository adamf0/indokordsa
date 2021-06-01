package com.app.indokordsa.viewmodel;

import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModel;
import com.app.indokordsa.R;
import com.app.indokordsa.view.model.Fitur;
import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    public ArrayList<Fitur> list_fitur = new ArrayList<>();

    @SuppressLint("UseCompatLoadingForDrawables")
    public MainViewModel(){
        list_fitur.add(new Fitur("1",R.drawable.ic_list_todo,"CheckList Machine"));
        list_fitur.add(new Fitur("2",R.drawable.ic_list_todo,"Questionnaire"));
        list_fitur.add(new Fitur("3",R.drawable.ic_profile,"Profile"));
        list_fitur.add(new Fitur("4",R.drawable.ic_arrow_back_black,"Exit Application"));

        //        list_fitur.add(new Fitur("3",R.drawable.ic_notification,"Notifikasi"));
        //        list_fitur.add(new Fitur("6",R.drawable.ic_scan_barcode,"Scan Barcode"));
    }
}