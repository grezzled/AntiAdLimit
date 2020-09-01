package anti.ad.limit.admob;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 01,September,2020
 * https://www.isoufiane.com
 */
public class AdmobInitializeHelper {

    public static void initialize(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG,"Admob Initialisation Completed");
            }
        });
    }
}
