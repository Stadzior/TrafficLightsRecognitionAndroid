package project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments;

import android.content.Context;
import android.content.Intent;
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

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

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

        Intent m_intent = new Intent();
        m_intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(m_intent, 0);
        textToSpeechProvider = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeechProvider.setLanguage(Locale.US);
                    // Try this someday for some interesting results.
                    // int result mTts.setLanguage(Locale.FRANCE);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language is not available.");
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeechProvider.speak("Elo mordeczki",TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textToSpeechProvider.speak("Elo mordeczki", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
        takenPhoto.setImageBitmap(bitmap);
        if (OpenCVLoader.initDebug()) {
            LightColor lightColor = ImageUtils.checkPhoto(bitmap);
            switch (lightColor) {
                case RED:
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeechProvider.speak("Elo mordeczki",TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeechProvider.speak("Elo mordeczki", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    Toast.makeText(getContext(), "CZERWONE", Toast.LENGTH_SHORT).show();
                    break;
                }
                case GREEN:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeechProvider.speak("Elo mordeczki",TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeechProvider.speak("Elo mordeczki", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    break;
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
