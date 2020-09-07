package anti.ad.limit.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import static anti.ad.limit.AntiAdLimit.TAG;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import anti.ad.limit.AdLimitUtils;
import anti.ad.limit.PrefUtils;

/**
 * Created by Soufiane on 07,September,2020
 * https://www.isoufiane.com
 */
public class AdmobReward {
    private Context context;
    private String unitId;
    private String prefName;
    private boolean testEnabled = false;
    private AdmobRewardListener admobRewardListener;
    private boolean isAdLoaded = false;
    private boolean isAdBanned = false;
    private RewardedAd rewardedAd;


    public AdmobReward(final Context context) {
        this.context = context;
    }

    public AdmobReward setUnitId(String unitId) {
        this.unitId = unitId;
        if (unitId.contains("/")) {
            this.prefName = unitId.substring(unitId.lastIndexOf("/") + 1);
            Log.d(TAG, unitId);
        }
        return this;
    }

    public AdmobReward enableTestAd(boolean enabled) {
        if (enabled)
            testEnabled = true;
        return this;
    }

    public void setAdmobRewardListener(AdmobRewardListener admobRewardListener) {
        this.admobRewardListener = admobRewardListener;
    }

    public void loadAd() {
        String newUnitId = unitId;
        if (testEnabled)
            newUnitId = "ca-app-pub-3940256099942544/5224354917";
        rewardedAd = new RewardedAd(context, newUnitId);
        final RewardedAdLoadCallback rewardedAdLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                Log.d(TAG, "Success: Admob Reward Loaded");
                isAdLoaded = true;
                if (admobRewardListener != null)
                    admobRewardListener.onRewardedAdLoaded();
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                Log.e(TAG, "Error: Admob Reward Failed to Load");
                if (admobRewardListener != null)
                    admobRewardListener.onRewardedAdFailedToLoad();
            }
        };
        // Check if Ad is Banned
        if (!AdLimitUtils.isBanned(context, prefName)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rewardedAd.loadAd(new AdRequest.Builder().build(), rewardedAdLoadCallback);
                }
            }, PrefUtils.getInstance().init(context, prefName).getDelayMs());
        } else {
            Log.d(TAG, "Admob Reward is banned");
            isAdBanned = true;
            if (admobRewardListener != null)
                admobRewardListener.onRewardAdBanned();
        }
    }

    public boolean isAdLoaded() {
        return this.isAdLoaded;
    }

    public boolean isAdBanned() {
        return this.isAdBanned;
    }

    public void show(Activity activity) {
        if (rewardedAd != null && rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                    Log.d(TAG, "Admob Reward Opened");
                    PrefUtils.getInstance().init(context, prefName).updateImpressionCounter();
                    if (admobRewardListener != null)
                        admobRewardListener.onRewardedAdOpened();

                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    Log.d(TAG, "Admob Reward Closed");

                    if (admobRewardListener != null)
                        admobRewardListener.onRewardedAdClosed();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    Log.d(TAG, "Admob Reward Earned Reward");

                    PrefUtils.getInstance().init(context, prefName).updateClicksCounter();
                    if (admobRewardListener != null)
                        admobRewardListener.onUserEarnedReward();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                    Log.e(TAG, "Admob Reward Failed to Display");
                    if (admobRewardListener != null)
                        admobRewardListener.onRewardedAdFailedToShow();
                }
            };
            rewardedAd.show(activity, adCallback);
        } else
            Log.d(TAG, "Cannot Show : Admob Reward Not Loaded ");

    }
}
