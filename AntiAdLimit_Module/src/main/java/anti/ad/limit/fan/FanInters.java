package anti.ad.limit.fan;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;

import anti.ad.limit.AdLimitUtils;
import anti.ad.limit.PrefUtils;


import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 01,September,2020
 * https://www.isoufiane.com
 */
public class FanInters {

    private Context context;
    private String unitId;
    private boolean testEnabled = false;
    private FanIntersLisntener fanIntersLisntener;
    boolean isAdLoaded = false;

    private InterstitialAd interstitialAd;

    public FanInters(final Context context) {
        this.context = context;
    }

    public FanInters setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }

    public FanInters enableTestAd(boolean enabled) {
        if (enabled)
            testEnabled = true;
        return this;
    }

    public void setFanIntersListener(FanIntersLisntener fanIntersListener) {
        this.fanIntersLisntener = fanIntersListener;
    }

    public void loadAd() {
        String newPlacementId = unitId;
        if (testEnabled)
            newPlacementId = "IMG_16_9_APP_INSTALL#" + unitId;

        interstitialAd = new InterstitialAd(context, newPlacementId);
        interstitialAd.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                Log.e(TAG, "Error: Loading Fan Interstitial");
                if (fanIntersLisntener != null)
                    fanIntersLisntener.onError();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Success : Fan Interstitial Loaded");
                isAdLoaded = true;
                if (fanIntersLisntener != null) {
                    fanIntersLisntener.onLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Fan Interstitial Ad Clicked : " + unitId);
                PrefUtils.getInstance().init(context, unitId).updateClicksCounter();
                if (fanIntersLisntener != null) {
                    fanIntersLisntener.onClicked();
                }
                // Hide Unit to prevent other clicks
                if (PrefUtils.getInstance().init(context, unitId).getHideOnClick())
                    hide();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Fan Interstitial Impression Logged");
                PrefUtils.getInstance().init(context, unitId).updateImpressionCounter();
                if (fanIntersLisntener != null)
                    fanIntersLisntener.onImpressionLogged();
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.d(TAG, "Fan Interstitial Displayed ");
                if (fanIntersLisntener != null)
                    fanIntersLisntener.onDisplayed();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.d(TAG, "Fan Interstitial Dismissed ");
                if (fanIntersLisntener != null)
                    fanIntersLisntener.onDismissed();
            }
        });

        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context, unitId)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    interstitialAd.loadAd();
                }
            }, PrefUtils.getInstance().init(context, unitId).getDelayMs());
        }
    }

    public void show() {
        if (interstitialAd != null && interstitialAd.isAdLoaded())
            interstitialAd.show();
        else
            Log.d(TAG, "Cannot Show : Fan Interstitial Not Loaded ");
    }

    public boolean isAdInvalidated() {
        return interstitialAd != null && interstitialAd.isAdInvalidated();
    }

    public boolean isAdLoaded() {
        return isAdLoaded;
    }

    public void destroy() {
        if (interstitialAd != null)
            interstitialAd.destroy();
    }

    private void hide() {
        if (interstitialAd != null)
            interstitialAd.destroy();
    }

}
