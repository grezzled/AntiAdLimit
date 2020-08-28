package anti.ad.limit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

import anti.ad.limit.Interface.FanBannerListener;

import static com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo;

/**
 * Created by Soufiane on 27,August,2020
 * https://www.isoufiane.com
 */
@SuppressLint("StaticFieldLeak")
public class AntiAdLimit {

    static final String TAG = "AntiAdLimit_TAG";
    private static final int BANNER_320_50 = 101;
    private static final int BANNER_HEIGHT_50 = 102;
    private static final int BANNER_HEIGHT_90 = 103;
    public static final int RECTANGLE_HEIGHT_250 = 104;

    private static AntiAdLimit antiAdLimit;
    private Context context;


    private AntiAdLimit() {

    }

    public static AntiAdLimit getInstance() {
        if (antiAdLimit == null)
            antiAdLimit = new AntiAdLimit();
        return antiAdLimit;
    }

    public void init(Context context, String JSONUrl) {
        this.context = context;

        // Start Pulling json Data in the background
        Intent intent = new Intent(context,JSONPullService.class);
        intent.putExtra("URL",JSONUrl);
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
                }catch (NullPointerException e){
                    Log.e(TAG, "Advertising Id: " + e.getMessage());
                }
            }
        }).start();


        // Initialize FAN
        AudienceNetworkInitializeHelper.initialize(context);

    }

}
