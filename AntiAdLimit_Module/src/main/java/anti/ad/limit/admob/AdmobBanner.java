package anti.ad.limit.admob;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;


import anti.ad.limit.AdLimitUtils;
import anti.ad.limit.PrefUtils;

import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 01,September,2020
 * https://www.isoufiane.com
 */
public class AdmobBanner {

    private Context context;
    private LinearLayout adContainer;
    private String unitId;
    private String prefName;
    boolean testEnabled;
    boolean isAdLoaded = false;
    AdSize adSize1 = null;
    AdmobBannerListener admobBannerListener;
    AdView adView;
    public static final int BANNER_320_50 = 101;
    public static final int LARGE_BANNER_320_100 = 102;
    public static final int MEDIUM_RECTANGLE_300_250 = 103;
    public static final int FULL_BANNER_468_60 = 104;
    public static final int SMART_BANNER = 105;


    // test : ca-app-pub-3940256099942544/6300978111

    public AdmobBanner(final Context context, int adSize, LinearLayout adContainer) {
        this.context = context;
        this.adContainer = adContainer;
        switch (adSize) {
            case BANNER_320_50:
                adSize1 = AdSize.BANNER;
                break;
            case LARGE_BANNER_320_100:
                adSize1 = AdSize.LARGE_BANNER;
                break;
            case MEDIUM_RECTANGLE_300_250:
                adSize1 = AdSize.MEDIUM_RECTANGLE;
                break;
            case FULL_BANNER_468_60:
                adSize1 = AdSize.FULL_BANNER;
                break;
            case SMART_BANNER:
                adSize1 = AdSize.SMART_BANNER;
                break;
        }
    }

    public AdmobBanner setUnitId(String unitId) {
        this.unitId = unitId;
        // WorkAround for creating pref xml file as it doesn't support slash symbol .. so we get the after slash only
        if (unitId.contains("/")){
            this.prefName = unitId.substring( unitId.lastIndexOf("/")+1);
            Log.d(TAG,unitId);
        }
        return this;
    }

    public AdmobBanner enableTestAd(boolean enabled) {
        if (enabled)
            testEnabled = true;
        return this;
    }

    public void setFanBannerListener(AdmobBannerListener admobBannerListener) {
        this.admobBannerListener = admobBannerListener;
    }

    public void loadAd() {
        // Exit if no valid banner found
        if (adSize1 == null)
            return;

        String newUnitId = unitId;
        if (testEnabled)
            newUnitId = "ca-app-pub-3940256099942544/6300978111";

        adView = new AdView(context);
        adView.setAdUnitId(newUnitId);
        adContainer.addView(adView);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d(TAG, "Admob Banner Closed ");
                if (admobBannerListener != null)
                    admobBannerListener.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e(TAG, "Error: Loading Admob Banner");
                if (admobBannerListener != null)
                    admobBannerListener.onAdFailedToLoad();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.d(TAG, "Admob Banner Left Application");
                if (admobBannerListener != null)
                    admobBannerListener.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d(TAG, "Success: Admob Banner Opened");
                PrefUtils.getInstance().init(context, prefName).updateClicksCounter();
                if (admobBannerListener != null) {
                    admobBannerListener.onAdOpened();
                }
                // Hide Unit to prevent other clicks
                if (PrefUtils.getInstance().init(context, prefName).getHideOnClick())
                    hide();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "Success: Admob Banner Loaded");
                isAdLoaded = true;
                PrefUtils.getInstance().init(context, prefName).updateImpressionCounter();
                if (admobBannerListener != null)
                    admobBannerListener.onAdLoaded();

            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "Admob Banner Ad Clicked : " + prefName);
                super.onAdClicked();
                if (admobBannerListener != null) {
                    admobBannerListener.onAdClicked();
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.d(TAG, "Admob Banner Impression Logged");
                if (admobBannerListener != null)
                    admobBannerListener.onAdImpression();
            }
        });
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdSize(adSize1);



        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context, prefName)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adView.loadAd(adRequest);
                }
            }, PrefUtils.getInstance().init(context, prefName).getDelayMs());
        }
    }

    public void destroy() {
        if (adView != null)
            adView.destroy();
    }

    public boolean isAdLoaded(){
        return isAdLoaded;
    }

    private void hide() {
        if (adView != null)
            adView.setVisibility(View.GONE);
    }
}
