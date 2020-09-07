package anti.ad.limit.fan;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdExtendedListener;

import anti.ad.limit.AdLimitUtils;
import anti.ad.limit.PrefUtils;

import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 07,September,2020
 * https://www.isoufiane.com
 */
public class FanReward {
    private Context context;
    private String unitId;
    private boolean testEnabled = false;
    private boolean isAdBanned = false;
    private boolean isAdLoaded = false;
    private FanRewardListener fanRewardListener;

    private RewardedVideoAd rewardedVideoAd;

    public FanReward(final Context context) {
        this.context = context;
    }

    public FanReward setUnitId(String unitId) {
        this.unitId = unitId;
        return this;

    }

    public FanReward enableTestAd(boolean enabled) {
        if (enabled)
            testEnabled = true;
        return this;
    }

    public void setFanRewardListener(FanRewardListener fanRewardListener) {
        this.fanRewardListener = fanRewardListener;
    }

    public void loadAd() {
        String newUnitId = unitId;
        if (testEnabled)
            newUnitId = "IMG_16_9_APP_INSTALL#" + unitId;

        rewardedVideoAd = new RewardedVideoAd(context, newUnitId);
        rewardedVideoAd.setAdListener(new RewardedVideoAdExtendedListener() {
            @Override
            public void onRewardedVideoActivityDestroyed() {
                Log.d(TAG, "Fan Reward Video Activity Destroyed");
                if (fanRewardListener != null)
                    fanRewardListener.onRewardedVideoActivityDestroyed();
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.d(TAG, "Success: Reward Video Completed");
                if (fanRewardListener != null)
                    fanRewardListener.onRewardedVideoCompleted();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Fan Reward Impression Logged");
                PrefUtils.getInstance().init(context, unitId).updateImpressionCounter();
                if (fanRewardListener != null)
                    fanRewardListener.onLoggingImpression();

            }

            @Override
            public void onRewardedVideoClosed() {
                Log.d(TAG, "Fan Reward Impression Logged");
                if (fanRewardListener != null)
                    fanRewardListener.onRewardedVideoClosed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Error: Loading Fan Reward");
                if (fanRewardListener != null)
                    fanRewardListener.onError();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Success : Fan Reward Loaded");
                isAdLoaded = true;
                if (fanRewardListener != null) {
                    fanRewardListener.onAdLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Fan Interstitial Ad Clicked : " + unitId);
                PrefUtils.getInstance().init(context, unitId).updateClicksCounter();
                if (fanRewardListener != null) {
                    fanRewardListener.onAdClicked();
                }
                // Hide Unit to prevent other clicks
                if (PrefUtils.getInstance().init(context, unitId).getHideOnClick())
                    hide();
            }
        });

        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context, unitId)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd.loadAd();
                }
            }, PrefUtils.getInstance().init(context, unitId).getDelayMs());
        } else {
            isAdBanned = true;
            if (fanRewardListener != null)
                fanRewardListener.onAdBanned();
        }
    }

    public void show() {
        if (rewardedVideoAd != null && rewardedVideoAd.isAdLoaded()) {
            rewardedVideoAd.show();
        } else
            Log.d(TAG, "Cannot Show : Fan Reward Not Loaded ");
    }

    public boolean isAdInvalidated() {
        return rewardedVideoAd != null && rewardedVideoAd.isAdInvalidated();
    }

    public boolean isAdLoaded() {
        return isAdLoaded;
    }

    public boolean isAdBanned() {
        return isAdBanned;
    }

    public void destroy() {
        if (rewardedVideoAd != null)
            rewardedVideoAd.destroy();
    }

    public void hide() {
        if (rewardedVideoAd != null)
            rewardedVideoAd.destroy();
    }

}
