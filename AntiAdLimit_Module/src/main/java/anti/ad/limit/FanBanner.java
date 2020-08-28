package anti.ad.limit;

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

import java.net.FileNameMap;

import anti.ad.limit.Interface.FanBannerListener;

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

    private static final int BANNER_320_50 = 101;
    private static final int BANNER_HEIGHT_50 = 102;
    private static final int BANNER_HEIGHT_90 = 103;
    public static final int RECTANGLE_HEIGHT_250 = 104;

    public FanBanner(final Context context, String placementId, int adSize, LinearLayout adContainer) {
        this.context = context;
        this.adContainer = adContainer;
        // Set banner AdSize;
        AdSize adSize1 = null;
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

        // Exit if no valid banner found
        if (adSize1 == null)
            return;

        adView = new AdView(context, placementId, adSize1);
        adContainer.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Error: Loading Banner");
                if (fanBannerListener != null)
                    fanBannerListener.onError();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Success: Banner Loaded");
                if (fanBannerListener != null)
                    fanBannerListener.onLoaded();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Banner Ad Clicked");
                PrefUtils.getInstance().init(context).updateClicksCounter();
                if (fanBannerListener != null) {
                    fanBannerListener.onClicked();
                }
                // Hide Unit to prevent other clicks
                hide();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Impression Logged");
                PrefUtils.getInstance().init(context).updateImpressionCounter();
                if (fanBannerListener != null)
                    fanBannerListener.onImpressionLogged();
            }
        });
    }

    public void setFanBannerListener(FanBannerListener fanBannerListener) {
        this.fanBannerListener = fanBannerListener;
    }


    public void loadAd() {
        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adView.loadAd();
                }
            }, PrefUtils.getInstance().getDelayMs());
        }
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
