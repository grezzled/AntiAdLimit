package anti.ad.limit.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import anti.ad.limit.R;

/**
 * Created by Soufiane on 27,August,2020
 * https://www.isoufiane.com
 */
public class FanBannerUI extends LinearLayout {

    View bannerView ;

    public FanBannerUI(Context context) {
        super(context);
        LayoutInflater mInflater = LayoutInflater.from(context);
        bannerView = mInflater.inflate(R.layout.fan_banner,this,true);
    }

    public FanBannerUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = LayoutInflater.from(context);
        bannerView = mInflater.inflate(R.layout.fan_banner,this,true);
    }

    public FanBannerUI(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = LayoutInflater.from(context);
        bannerView = mInflater.inflate(R.layout.fan_banner,this,true);
    }



}
