package project.advancedmobileapplications.trafficlightsrecognitionandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments.CameraFragment;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments.TakenPhotoFragment;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.CameraViewListener;

public class MainActivity extends AppCompatActivity implements CameraViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        startCameraFragment(false);
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void startPhotoPreview(byte[] jpeg) {
        changeFragment(TakenPhotoFragment.newInstance(jpeg), true);
    }

    @Override
    public void startCameraFragment(boolean returnFromPhotoPreview) {
        if (returnFromPhotoPreview)
            getSupportFragmentManager().popBackStack();
        else
            changeFragment(CameraFragment.newInstance(), false);
    }

}
