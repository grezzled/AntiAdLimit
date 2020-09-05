package com.libtest.simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import anti.ad.limit.AntiAdLimit;
import anti.ad.limit.admob.AdmobBanner;
import anti.ad.limit.admob.AdmobBannerListener;
import anti.ad.limit.admob.AdmobInters;
import anti.ad.limit.admob.AdmobIntersListener;
import anti.ad.limit.fan.FanBanner;
import anti.ad.limit.fan.FanInters;
import anti.ad.limit.fan.FanBannerListener;
import anti.ad.limit.fan.FanIntersLisntener;

public class MainActivity extends AppCompatActivity {

    FanBanner fanBanner;
    FanInters fanInters;
    AdmobBanner admobBanner;
    AdmobInters admobInters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AntiAdLimit antiAdLimit = new AntiAdLimit(this);

        switch (antiAdLimit.getEnabledNetwork()) {
            case AntiAdLimit.NETWORK_ADMOB:
                // Admob stuff
                admobBanner = new AdmobBanner(this, AdmobBanner.LARGE_BANNER_320_100, (LinearLayout) findViewById(R.id.viewBanner));
                admobBanner.setFanBannerListener(new AdmobBannerListener() {
                    @Override
                    public void onAdLoaded() {

                    }

                    @Override
                    public void onAdFailedToLoad() {

                    }

                    @Override
                    public void onAdOpened() {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdLeftApplication() {

                    }

                    @Override
                    public void onAdClosed() {

                    }

                    @Override
                    public void onAdImpression() {

                    }
                });
                admobBanner.setUnitId("ca-app-pub-8281466180702761/6039096525").enableTestAd(true).loadAd();

                admobInters = new AdmobInters(this);
                admobInters.setAdmobIntersListener(new AdmobIntersListener() {
                    @Override
                    public void onAdLoaded() {
                        admobInters.show();
                    }

                    @Override
                    public void onAdFailedToLoad() {

                    }

                    @Override
                    public void onAdOpened() {
                            Log.d("grezz","HElloooooo!");
                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdLeftApplication() {

                    }

                    @Override
                    public void onAdClosed() {

                    }
                });
                admobInters.enableTestAd(true).setUnitId("ca-app-pub-8281466180702761/9786769849").loadAd();


                break;
            case AntiAdLimit.NETWORK_FAN:
                fanBanner = new FanBanner(this, FanBanner.RECTANGLE_HEIGHT_250, (LinearLayout) findViewById(R.id.viewBanner));
                fanBanner.setFanBannerListener(new FanBannerListener() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onLoaded() {
                        Log.d("Grezz", "Ad Loaded");
                    }

                    @Override
                    public void onClicked() {

                    }

                    @Override
                    public void onImpressionLogged() {

                    }
                });
                fanBanner.enableTestAd(true)
                        .setUnitId("1564516670394005_1564551527057186")
                        .loadAd();
                fanInters = new FanInters(this);
                fanInters.setFanIntersListener(new FanIntersLisntener() {
                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onLoaded() {
                        fanInters.show();
                    }

                    @Override
                    public void onDisplayed() {

                    }

                    @Override
                    public void onDismissed() {

                    }

                    @Override
                    public void onClicked() {

                    }

                    @Override
                    public void onImpressionLogged() {

                    }
                });
                fanInters.enableTestAd(true)
                        .setUnitId("1564516670394005_1564522647060074")
                        .loadAd();
                break;
            case AntiAdLimit.NETWORK_NONE:
                // House Ad Here
                break;
        }


        // TODO add hide on click option
        //  add ads activated option

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fanBanner != null)
            fanBanner.destroy();
        if (fanInters != null)
            fanInters.destroy();
        if (admobBanner != null)
            admobBanner.destroy();
    }
}
