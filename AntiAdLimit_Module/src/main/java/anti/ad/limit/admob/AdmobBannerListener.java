package anti.ad.limit.admob;

/**
 * Created by Soufiane on 01,September,2020
 * https://www.isoufiane.com
 */
public interface AdmobBannerListener {

    void onAdLoaded();

    void onAdFailedToLoad();

    void onAdOpened();

    void onAdClicked();

    void onAdLeftApplication();

    void onAdClosed();

    void onAdImpression();
}
