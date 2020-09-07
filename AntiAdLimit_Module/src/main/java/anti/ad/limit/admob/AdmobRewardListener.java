package anti.ad.limit.admob;

/**
 * Created by Soufiane on 07,September,2020
 * https://www.isoufiane.com
 */
public interface AdmobRewardListener {

    void onRewardAdBanned();

    void onRewardedAdLoaded();

    void onRewardedAdFailedToLoad();

    void onRewardedAdOpened();

    void onRewardedAdClosed();

    void onUserEarnedReward();

    void onRewardedAdFailedToShow();
}
