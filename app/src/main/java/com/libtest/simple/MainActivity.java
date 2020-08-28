package com.libtest.simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import anti.ad.limit.AntiAdLimit;
import anti.ad.limit.FanBanner;
import anti.ad.limit.Interface.FanBannerListener;

public class MainActivity extends AppCompatActivity {

    FanBanner fanBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fanBanner = new FanBanner(this, "IMG_16_9_APP_INSTALL#1564516670394005_1564551527057186", AntiAdLimit.RECTANGLE_HEIGHT_250, (LinearLayout) findViewById(R.id.fanBanner));
        fanBanner.setFanBannerListener(new FanBannerListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onLoaded() {
                Log.d("Grezz","Ad Loaded");
            }

            @Override
            public void onClicked() {

            }

            @Override
            public void onImpressionLogged() {

            }
        });
        fanBanner.loadAd();


        // TODO add hide on click option
        //  add ads activated option

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fanBanner!=null)
            fanBanner.destroy();
    }
}
