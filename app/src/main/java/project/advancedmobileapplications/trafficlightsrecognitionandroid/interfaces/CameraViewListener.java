package project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces;

import android.support.v4.app.Fragment;

/**
 * Created by Patryk Rutkowski on 13.10.2017.
 */

public interface CameraViewListener {

    void changeFragment(Fragment fragment, boolean addToBackStack);
    void startPhotoPreview(byte[] jpeg);
    void startCameraFragment(boolean returnFromPhotoPreview);

}
