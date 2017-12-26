package project.advancedmobileapplications.trafficlightsrecognitionandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.WristTwistDetector;

import project.advancedmobileapplications.trafficlightsrecognitionandroid.MainActivity;

/**
 * Created by Patryk Rutkowski on 26.11.2017.
 */

public class WristTwistGestureService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Sensey.getInstance().init(getBaseContext());
        Sensey.getInstance().startWristTwistDetection(new WristTwistDetector.WristTwistListener() {
            @Override public void onWristTwist() {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });

        return START_STICKY;
    }
}
