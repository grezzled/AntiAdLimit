package anti.ad.limit.fan;

/**
 * Created by Soufiane on 07,September,2020
 * https://www.isoufiane.com
 */
public interface FanRewardListener {

    void onAdBanned();

    void onRewardedVideoActivityDestroyed();

    void onRewardedVideoCompleted();

    void onLoggingImpression();

    void onRewardedVideoClosed();

    void onError();

    void onAdLoaded();

    void onAdClicked();

}
