package anti.ad.limit;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static anti.ad.limit.AntiAdLimit.TAG;

/**
 * Created by Soufiane on 28,August,2020
 * https://www.isoufiane.com
 */
public class JSONPullService extends IntentService {
    public JSONPullService(String name) {
        super(name);
    }

    public JSONPullService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Start Pulling JSON data");
        if (intent == null)
            return;
        String mUrl;
        Bundle extras = intent.getExtras();
        if (extras == null)
            return;
        mUrl = extras.getString("URL");

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(mUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            String jsonData = buffer.toString();
            Log.d(TAG, "Success : Pulling JSON data => \n" + jsonData);

            JSONObject jsonObject = new JSONObject(jsonData);
            boolean adActivated = jsonObject.getBoolean("ads_activated");
            int clicks = jsonObject.getInt("clicks");
            int impressions = jsonObject.getInt("impressions");
            int delayMs = jsonObject.getInt("delay_ms");
            int banHours = jsonObject.getInt("ban_hours");

            Log.d(TAG, "Success : " + adActivated + " | " + clicks + " | " + impressions + " | " + delayMs + " | " + banHours);

            // Update Preferences
            PrefUtils.getInstance().init(getApplicationContext()).updateJsonData(adActivated, clicks, impressions, delayMs, banHours);

        } catch (MalformedURLException e) {
            Log.e(TAG, "Error : Pulling JSON data => Malformed Url");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error : Pulling JSON data => IO Exception");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "Error : Read JSON data => Error extracting items from JSON Object");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error : Pulling JSON data => Could Not close Reader");
            }
        }
    }

    private void prefUtils() {
        SharedPreferences pref = getSharedPreferences("AntiAdLimitPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("ad_activated", false);
        editor.apply();
    }
}
