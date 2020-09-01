package anti.ad.limit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Soufiane on 28,August,2020
 * https://www.isoufiane.com
 */
public class AdLimitUtils {

    public static boolean isBanned(Context context, String unitId) {

        boolean limitActivated = PrefUtils.getInstance().init(context, unitId).getLimitActivated();
        boolean adActivated = PrefUtils.getInstance().init(context, unitId).getAdActivated();
        int clicksLimit = PrefUtils.getInstance().init(context, unitId).getClicksLimit();
        int impressionsLimit = PrefUtils.getInstance().init(context, unitId).getImpressionsLimit();
        int clicksCount = PrefUtils.getInstance().init(context, unitId).getClicksCount();
        int impressionsCount = PrefUtils.getInstance().init(context, unitId).getImpressionsCount();
        long timeEndBan = PrefUtils.getInstance().init(context, unitId).getTimeEndBan();

        //If Limit Activated
        if (!limitActivated)
            return false;

        // If ad deactivated
        if (!adActivated)
            return true;

        // If limit is 0 or not defined will show ads anyway
        if (clicksLimit == 0 || impressionsLimit == 0)
            return false;

        // If unit is banned from showing ad for a period of time
        if (System.currentTimeMillis() < timeEndBan)
            return true;

        boolean banned = false;

        if (clicksCount >= clicksLimit || impressionsCount >= impressionsLimit) {
            PrefUtils.getInstance().init(context, unitId).resetClicksCounter();
            PrefUtils.getInstance().init(context, unitId).resetImpressionsCounter();
            PrefUtils.getInstance().init(context, unitId).updateBanTime();
            banned = true;
        }

        return banned;

    }

}
