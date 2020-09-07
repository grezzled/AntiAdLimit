package anti.ad.limit.fan;

/**
 * Created by Soufiane on 28,August,2020
 * https://www.isoufiane.com
 */
public interface FanBannerListener {

    void onAdBanned();

    void onError();

    void onLoaded();

    void onClicked();

    void onImpressionLogged();
}
