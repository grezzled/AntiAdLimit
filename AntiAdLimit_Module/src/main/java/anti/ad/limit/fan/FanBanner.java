package anti.ad.limit.fan;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import anti.ad.limit.AdLimitUtils;
import anti.ad.limit.PrefUtils;

import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 28,August,2020
 * https://www.isoufiane.com
 */
public class FanBanner {

    private FanBannerListener fanBannerListener;
    private AdView adView;
    private Context context;
    private LinearLayout adContainer;
    private String placementId;
    boolean testEnabled = false;
    boolean isAdLoaded = false;
    AdSize adSize1 = null;
    public static final int BANNER_320_50 = 101;
    public static final int BANNER_HEIGHT_50 = 102;
    public static final int BANNER_HEIGHT_90 = 103;
    public static final int RECTANGLE_HEIGHT_250 = 104;

    public FanBanner(final Context context, int adSize, LinearLayout adContainer) {
        this.context = context;
        this.adContainer = adContainer;
        // Set banner AdSize;

        switch (adSize) {
            case BANNER_320_50:
                adSize1 = AdSize.BANNER_320_50;
                break;
            case BANNER_HEIGHT_50:
                adSize1 = AdSize.BANNER_HEIGHT_50;
                break;
            case BANNER_HEIGHT_90:
                adSize1 = AdSize.BANNER_HEIGHT_90;
                break;
            case RECTANGLE_HEIGHT_250:
                adSize1 = AdSize.RECTANGLE_HEIGHT_250;
                break;
        }
    }

    public FanBanner setUnitId(String unitId) {
        this.placementId = unitId;
        return this;
    }

    public FanBanner enableTestAd(boolean enabled) {
        if (enabled)
            testEnabled = true;
        return this;
    }

    public void setFanBannerListener(FanBannerListener fanBannerListener) {
        this.fanBannerListener = fanBannerListener;
    }


    public void loadAd() {
        // Exit if no valid banner found
        if (adSize1 == null)
            return;

        String newPlacementId = placementId;
        if (testEnabled)
            newPlacementId = "IMG_16_9_APP_INSTALL#" + placementId;

        adView = new AdView(context, newPlacementId, adSize1);
        adContainer.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Error: Loading Fan Banner");
                if (fanBannerListener != null)
                    fanBannerListener.onError();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Success: Fan Banner Loaded");
                isAdLoaded = true;
                if (fanBannerListener != null) {
                    fanBannerListener.onLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Fan Banner Ad Clicked : " + placementId);
                PrefUtils.getInstance().init(context, placementId).updateClicksCounter();
                if (fanBannerListener != null) {
                    fanBannerListener.onClicked();
                }
                // Hide Unit to prevent other clicks
                if (PrefUtils.getInstance().init(context, placementId).getHideOnClick())
                    hide();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Fan Banner Impression Logged");
                PrefUtils.getInstance().init(context, placementId).updateImpressionCounter();
                if (fanBannerListener != null)
                    fanBannerListener.onImpressionLogged();
            }
        });

        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context, placementId)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adView.loadAd();
                }
            }, PrefUtils.getInstance().init(context, placementId).getDelayMs());
        }
    }

    public boolean isAdLoaded(){
        return isAdLoaded;
    }

    public void destroy() {
        if (adView != null)
            adView.destroy();
    }

    private void hide() {
        if (adContainer != null)
            adContainer.setVisibility(View.GONE);
    }

}
