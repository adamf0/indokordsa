package com.app.indokordsa.viewmodel;

import android.os.Handler;

import androidx.lifecycle.ViewModel;

import com.app.indokordsa.interfaces.Splashlistener;

public class SplashViewModel extends ViewModel {

    public void loading(final Splashlistener splashlistener){
        new Handler().postDelayed(() -> splashlistener.waitingFinish(), 3000L);
    }
}
