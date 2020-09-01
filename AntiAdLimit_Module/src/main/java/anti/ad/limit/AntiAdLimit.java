package anti.ad.limit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.lang.ref.PhantomReference;

import anti.ad.limit.admob.AdmobInitializeHelper;
import anti.ad.limit.fan.AudienceNetworkInitializeHelper;

import static com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo;

/**
 * Created by Soufiane on 27,August,2020
 * https://www.isoufiane.com
 */
@SuppressLint("StaticFieldLeak")
public class AntiAdLimit {

    public static final String TAG = "AntiAdLimit_TAG";
    public static final int NETWORK_FAN = 1;
    public static final int NETWORK_ADMOB = 2;
    public static final int NETWORK_NONE = 0;

    private static AntiAdLimit antiAdLimit;
    private Context context;

    private AntiAdLimit() {
    }

    public AntiAdLimit(Context context) {
        this.context = context;
    }

    public static AntiAdLimit getInstance() {
        if (antiAdLimit == null)
            antiAdLimit = new AntiAdLimit();
        return antiAdLimit;
    }

    public void init(Context context, String JSONUrl) {
        this.context = context;

        // Start Pulling json Data in the background
        Intent intent = new Intent(context, JSONPullService.class);
        intent.putExtra("URL", JSONUrl);
        context.startService(intent);

        // Display TEST Device Id
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Advertising Id: " + getAdvertisingIdInfo(getInstance().context).getId());
                } catch (GooglePlayServicesNotAvailableException ex) {
                    Log.e(TAG, "Advertising Id: Google play services not available");
                } catch (GooglePlayServicesRepairableException ex) {
                    Log.e(TAG, "Advertising Id: Google play services repairable");
                } catch (IOException e) {
                    Log.e(TAG, "Advertising Id: " + e.getMessage());
                } catch (NullPointerException e) {
                    Log.e(TAG, "Advertising Id: " + e.getMessage());
                }
            }
        }).start();


        // Initialize FAN
        AudienceNetworkInitializeHelper.initialize(context);

        // Initialize Admob
        AdmobInitializeHelper.initialize(context);
    }

    // TODO add network weight
    public int getEnabledNetwork() {
        boolean admob = PrefUtils.getInstance().init(context, PrefUtils.PREF_NAME).getAdmobActivated();
        boolean fan = PrefUtils.getInstance().init(context, PrefUtils.PREF_NAME).getFanActivated();
        if (admob)
            return NETWORK_ADMOB;
        if (fan)
            return NETWORK_FAN;
        return NETWORK_NONE;
    }

}
