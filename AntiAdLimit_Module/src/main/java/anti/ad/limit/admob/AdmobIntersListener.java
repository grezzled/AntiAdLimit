package anti.ad.limit.admob;

/**
 * Created by Soufiane on 03,September,2020
 * https://www.isoufiane.com
 */
public interface AdmobIntersListener {

    void onAdLoaded();

    void onAdFailedToLoad();

    void onAdOpened();

    void onAdClicked();

    void onAdLeftApplication();

    void onAdClosed();

}
