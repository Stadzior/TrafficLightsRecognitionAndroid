package project.advancedmobileapplications.trafficlightsrecognitionandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.tbouron.shakedetector.library.ShakeDetector;

import project.advancedmobileapplications.trafficlightsrecognitionandroid.MainActivity;

/**
 * Created by patry on 26.11.2017.
 */

public class ShakeService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
        return START_STICKY;
    }
}
