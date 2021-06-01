package com.app.indokordsa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.app.indokordsa.interfaces.Barcodelistener;

public class BarcodeViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public Barcodelistener Barcodelistener;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BarcodeViewModel(Barcodelistener);
    }

    public BarcodeViewModelFactory(Barcodelistener Barcodelistener){
        this.Barcodelistener = Barcodelistener;
    }
}
