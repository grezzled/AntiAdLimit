package anti.ad.limit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Soufiane on 28,August,2020
 * https://www.isoufiane.com
 */
class AdLimitUtils {

    static boolean isBanned(Context context) {

        int clicksLimit = PrefUtils.getInstance().init(context).getClicksLimit();
        int impressionsLimit = PrefUtils.getInstance().init(context).getImpressionsLimit();
        int clicksCount = PrefUtils.getInstance().init(context).getClicksCount();
        int impressionsCount = PrefUtils.getInstance().init(context).getImpressionsCount();
        long timeEndBan = PrefUtils.getInstance().init(context).getTimeEndBan();

        // If limit is 0 or not defined will show ads anyway
        if (clicksLimit == 0 || impressionsLimit == 0)
            return false;

        if (System.currentTimeMillis() < timeEndBan)
            return true;

        boolean banned = false;

        if (clicksCount >= clicksLimit  || impressionsCount >= impressionsLimit) {
            PrefUtils.getInstance().init(context).resetClicksCounter();
            PrefUtils.getInstance().init(context).resetImpressionsCounter();
            PrefUtils.getInstance().init(context).updateBanTime();
            banned = true;
        }

        return banned;

    }

}
