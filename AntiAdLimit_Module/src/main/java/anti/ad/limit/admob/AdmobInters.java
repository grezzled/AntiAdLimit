package anti.ad.limit.admob;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import anti.ad.limit.AdLimitUtils;
import anti.ad.limit.PrefUtils;

import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 03,September,2020
 * https://www.isoufiane.com
 */
public class AdmobInters {

    private Context context;
    private String unitId;
    private String prefName;
    private boolean testEnabled = false;
    private AdmobIntersListener admobIntersListener;
    private boolean isAdLoaded = false;
    private boolean isAdBanned = false;

    private InterstitialAd interstitialAd;

    public AdmobInters(final Context context) {
        this.context = context;
    }

    public AdmobInters setUnitId(String unitId) {
        this.unitId = unitId;
        // WorkAround for creating pref xml file as it doesn't support slash symbol .. so we get the after slash only
        if (unitId.contains("/")) {
            this.prefName = unitId.substring(unitId.lastIndexOf("/") + 1);
            Log.d(TAG, unitId);
        }
        return this;
    }

    public AdmobInters enableTestAd(boolean enabled) {
        if (enabled)
            testEnabled = true;
        return this;
    }

    public void setAdmobIntersListener(AdmobIntersListener admobIntersListener) {
        this.admobIntersListener = admobIntersListener;
    }

    public void loadAd() {
        String newUnitID = unitId;
        if (testEnabled)
            newUnitID = "ca-app-pub-3940256099942544/1033173712";

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(newUnitID);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Success : Admob Interstitial Loaded");
                isAdLoaded = true;
                if (admobIntersListener != null) {
                    admobIntersListener.onAdLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Error: Loading Admob Interstitial");
                if (admobIntersListener != null)
                    admobIntersListener.onAdFailedToLoad();
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG, "Admob Interstitial Impression Logged");
                PrefUtils.getInstance().init(context, prefName).updateImpressionCounter();
                if (admobIntersListener != null) {
                    admobIntersListener.onAdOpened();

                }
            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "Admob Interstitial Ad Clicked : " + prefName);
                if (admobIntersListener != null)
                    admobIntersListener.onAdClicked();
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(TAG, "Admob Interstitial Ad Clicked and Left the app");
                PrefUtils.getInstance().init(context, prefName).updateClicksCounter();
                if (admobIntersListener != null) {
                    admobIntersListener.onAdLeftApplication();
                }
            }

            @Override
            public void onAdClosed() {
                Log.d(TAG, "Admob Interstitial Ad Closed ");
                if (admobIntersListener != null)
                    admobIntersListener.onAdClosed();
            }
        });

        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context, prefName)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }, PrefUtils.getInstance().init(context, prefName).getDelayMs());
        } else {
            isAdBanned = true;
            if (admobIntersListener != null)
                admobIntersListener.onAdBanned();
        }
    }

    public boolean isAdLoaded() {
        return isAdLoaded;
    }

    public boolean isAdBanned() {
        return isAdBanned;
    }

    public void show() {
        if (interstitialAd != null && interstitialAd.isLoaded())
            interstitialAd.show();
        else
            Log.d(TAG, "Cannot Show : Admob Interstitial Not Loaded ");
    }

}

