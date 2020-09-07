package anti.ad.limit.fan;

/**
 * Created by Soufiane on 01,September,2020
 * https://www.isoufiane.com
 */
public abstract interface FanIntersLisntener {

    void onAdBanned();

    void onError();

    void onLoaded();

    void onDisplayed();

    void onDismissed();

    void onClicked();

    void onImpressionLogged();

}
