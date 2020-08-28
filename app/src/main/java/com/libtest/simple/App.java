package com.libtest.simple;

import android.app.Application;

import anti.ad.limit.AntiAdLimit;

/**
 * Created by Soufiane on 27,August,2020
 * https://www.isoufiane.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Ad Limit Protector
        AntiAdLimit.getInstance()
                .init(this, "https://api.grezz.dev/anti-ad-limit/PUBGB.json");

    }
}
