package project.advancedmobileapplications.trafficlightsrecognitionandroid.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.R;
import project.advancedmobileapplications.trafficlightsrecognitionandroid.interfaces.CameraViewListener;

public class TakenPhotoFragment extends Fragment {

    @BindView(R.id.taken_photo)
    ImageView takenPhoto;

    private byte[] jpeg;
    private CameraViewListener cameraViewListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taken_photo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
        takenPhoto.setImageBitmap(bitmap);
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
