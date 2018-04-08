package starbright.com.projectegg.features.recipelist;

import android.content.Intent;
import android.provider.MediaStore;
import android.os.Bundle;

import starbright.com.projectegg.features.base.BaseActivity;
import starbright.com.projectegg.util.ClarifaiHelper;
import starbright.com.projectegg.R;

public class PhotoActivity extends BaseActivity implements ClarifaiHelper.Callback {

    private static final int CAMERA_REQUEST_CODE = 100;

    private ClarifaiHelper mClarifaiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mClarifaiHelper = new ClarifaiHelper();
        openCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            mClarifaiHelper.predict(data, this);
        }
    }

    private void openCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
    }

    @Override
    public void onPredictionCompleted(String ingredients) {
    }
}
