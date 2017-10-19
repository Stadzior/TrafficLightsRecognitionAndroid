package project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.R;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.enums.LightColor;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.CameraViewListener;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.utils.ImageUtils;

public class TakenPhotoFragment extends Fragment {

    @BindView(R.id.taken_photo)
    ImageView takenPhoto;

    private byte[] jpeg;
    private CameraViewListener cameraViewListener;
    private TextToSpeech textToSpeechProvider;
    private boolean TextToSpeechInitialized = false;
    public static TakenPhotoFragment newInstance(byte[] jpeg) {
        TakenPhotoFragment fragment = new TakenPhotoFragment();
        Bundle args = new Bundle();
        args.putByteArray("jpeg", jpeg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jpeg = getArguments().getByteArray("jpeg");
        }
        textToSpeechProvider = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                TextToSpeechInitialized = false;
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeechProvider.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language is not available.");
                    }
                    else
                        TextToSpeechInitialized = true;
                } else {
                    Log.e("TTS", "Initialization of Text to speach failed.");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taken_photo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        takenPhoto.setImageBitmap(ImageUtils.decodeSampledBitmapFromResource(jpeg, 512, 512));
        if (OpenCVLoader.initDebug()) {
            LightColor lightColor = ImageUtils.checkPhoto(jpeg);
            switch (lightColor) {
                case RED:
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeechProvider.speak(getString(R.string.red),TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeechProvider.speak(getString(R.string.red), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    Toast.makeText(getContext(), getString(R.string.red), Toast.LENGTH_SHORT).show();
                    break;
                }
                case GREEN:
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeechProvider.speak(getString(R.string.green),TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeechProvider.speak(getString(R.string.green), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    Toast.makeText(getContext(), getString(R.string.green), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } else {
            Log.i("OpenCV", "OpenCV initialize failed");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cameraViewListener = (CameraViewListener) context;
    }

//    @Override
//    public void onStop(){
//        super.onStop();
//        textToSpeechProvider.stop();
//        textToSpeechProvider.shutdown();
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (cameraViewListener != null) {
            cameraViewListener = null;
        }
    }

    @OnClick(R.id.taken_photo)
    public void onClickPhoto() {
        cameraViewListener.startCameraFragment(true);
    }

}
