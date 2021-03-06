package project.advancedmobileapplications.trafficlightsrecognitionandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Locale;

import butterknife.ButterKnife;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.enums.LightColor;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments.CameraFragment;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments.TakenPhotoFragment;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.CameraViewListener;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.TTSListener;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.services.WristTwistGestureService;

public class MainActivity extends AppCompatActivity implements CameraViewListener, TTSListener {

    private TextToSpeech textToSpeechProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, WristTwistGestureService.class));
        ButterKnife.bind(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        startCameraFragment(false);
        ttsInit();
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String fragmentTag = fragment instanceof CameraFragment ? "Camera" : "TakenPhoto";
        transaction.replace(R.id.fragment_container, fragment, fragmentTag);
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    @Override
    public void startPhotoPreview(byte[] jpeg) {
        Fragment cameraFragment = getSupportFragmentManager().findFragmentByTag("Camera");
        LightColor selectedColorMap = ((CameraFragment)cameraFragment).selectedColorMap;
        changeFragment(TakenPhotoFragment.newInstance(jpeg, selectedColorMap), true);
    }

    @Override
    public void startCameraFragment(boolean returnFromPhotoPreview) {
        if (returnFromPhotoPreview)
            getSupportFragmentManager().popBackStack();
        else
            changeFragment(CameraFragment.newInstance(), false);
    }

    @Override
    public void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeechProvider.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeechProvider.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void ttsInit() {
        textToSpeechProvider = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeechProvider.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language is not available.");
                    }
                } else {
                    Log.e("TTS", "Initialization of Text to speach failed.");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsStop();
    }

    @Override
    public void ttsStop() {
        textToSpeechProvider.shutdown();
    }
}
