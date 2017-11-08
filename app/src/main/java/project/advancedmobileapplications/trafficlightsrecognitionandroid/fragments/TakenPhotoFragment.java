package project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.R;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.enums.LightColor;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.CameraViewListener;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.TTSListener;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.utils.ImageUtils;

public class TakenPhotoFragment extends Fragment {

    @BindView(R.id.taken_photo)
    ImageView takenPhoto;

    private CameraViewListener cameraViewListener;
    private TTSListener ttsListener;
    private Bitmap photo;

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
            byte[] jpeg = getArguments().getByteArray("jpeg");
            photo = ImageUtils.rotateImage(ImageUtils.byteArrayToBitmap(jpeg),90);
            photo = Bitmap.createScaledBitmap(photo,400,600,false);
        }

        if (OpenCVLoader.initDebug()) {
            photo = ImageUtils.getColorBitmap(photo, LightColor.GREEN);
            LightColor lightColor = ImageUtils.checkPhoto(photo);
            switch (lightColor) {
                case RED: {
                    ttsListener.speak(getString(R.string.red));
                    Toast.makeText(getContext(), getString(R.string.red), Toast.LENGTH_SHORT).show();
                    break;
                }
                case GREEN: {
                    ttsListener.speak(getString(R.string.green));
                    Toast.makeText(getContext(), getString(R.string.green), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        takenPhoto.setImageBitmap(photo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taken_photo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cameraViewListener = (CameraViewListener) context;
        ttsListener = (TTSListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (cameraViewListener != null)
            cameraViewListener = null;
        if (ttsListener != null)
            ttsListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraViewListener.startCameraFragment(true);
    }

    @OnClick(R.id.taken_photo)
    public void onClickPhoto() {
        cameraViewListener.startCameraFragment(true);
    }

}
