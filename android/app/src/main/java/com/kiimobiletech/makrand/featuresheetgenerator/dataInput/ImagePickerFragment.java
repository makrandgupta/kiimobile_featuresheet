package com.kiimobiletech.makrand.featuresheetgenerator.dataInput;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.kiimobiletech.makrand.featuresheetgenerator.Constants;
import com.kiimobiletech.makrand.featuresheetgenerator.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnImagePickListener} interface
 * to handle interaction events.
 * Use the {@link ImagePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagePickerFragment extends Fragment {

    private static final String TAG = "IMAGE_PICKER";
    private static Bitmap tempImagePreview;
    private OnImagePickListener mListener;
    private Integer num_images;
    Button galleryButton;
    Button cameraButton;

    DataContainer dataContainer = DataContainer.getInstance();

    public ImagePickerFragment() {}
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param num_images number of images needed by template.
     * @param imageBitmap temp parameter to pass image previews
     * @return A new instance of fragment ImagePickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagePickerFragment newInstance(Bitmap imageBitmap) {
        tempImagePreview = imageBitmap;
        Log.d(TAG, "bitmap image given");
//        Bundle args = new Bundle();
//        args.putInt(Constants.ARG_NUM_IMAGES, num_images);
//        fragment.setArguments(args);
        return new ImagePickerFragment();
    }

    public static ImagePickerFragment newInstance() {
        Log.d(TAG, "bitmap not given");
        return new ImagePickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num_images = getArguments().getInt(Constants.ARG_NUM_IMAGES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_picker, container, false);
        galleryButton = (Button) view.findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch gallery
                onButtonPressed(Constants.SOURCE_GALLERY);
            }
        });

        cameraButton = (Button) view.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch camera
                onButtonPressed(Constants.SOURCE_CAMERA);
            }
        });
        // TODO: Load selected images from dataContainer and display them
        if(dataContainer.tempImage != null) {
            Log.d("IMGLOADER", "tempImage found");
            ImageView imageView = (ImageView) view.findViewById(R.id.tempImage);
            imageView.setImageBitmap(dataContainer.tempImage);
        }
        return view;
    }


    public void onButtonPressed(String source) {
        if (mListener != null) {
            switch (source) {
                case Constants.SOURCE_GALLERY:
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    Log.d("givenRequestCode", String.valueOf(Constants.REQUEST_IMAGE_CAPTURE));
                    getActivity().startActivityForResult(photoPickerIntent, Constants.SELECT_PHOTO);
                    mListener.onImagePick();
                    break;
                case Constants.SOURCE_CAMERA:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        Log.d("givenRequestCode", String.valueOf(Constants.REQUEST_IMAGE_CAPTURE));
                        getActivity().startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
                    }
                    mListener.onImagePick();
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImagePickListener) {
            mListener = (OnImagePickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImagePickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(dataContainer.tempImage != null) {
            Log.d("IMGLOADER", "tempImage found");
            ImageView imageView = (ImageView) getView().findViewById(R.id.tempImage);
            imageView.setImageBitmap(dataContainer.tempImage);

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnImagePickListener {
        void onImagePick();
    }
}
