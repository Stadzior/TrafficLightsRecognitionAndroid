package project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.SessionType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.R;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.enums.LightColor;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.CameraViewListener;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.utils.ImageUtils;

public class CameraFragment extends Fragment {

    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.take_photo_button)
    Button takePhoto;
    @BindView (R.id.toggle_map_button)
    ToggleButton toggleMapButton;
    public LightColor selectedColorMap = LightColor.GREEN;

    private CameraViewListener cameraViewListener;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraView.setSessionType(SessionType.PICTURE);
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                cameraViewListener.startPhotoPreview(ImageUtils.decodeSampledBitmapFromResource(jpeg, 512, 512));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cameraViewListener = (CameraViewListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (cameraViewListener != null)
            cameraViewListener = null;
    }

    @OnClick(R.id.take_photo_button)
    public void onClickTakePhoto() {
        cameraView.capturePicture();
    }

    @OnCheckedChanged(R.id.toggle_map_button)
    public void onCheckedChangedToggleMap() {
        selectedColorMap = toggleMapButton.isChecked() ? LightColor.RED : LightColor.GREEN;
    }
}
