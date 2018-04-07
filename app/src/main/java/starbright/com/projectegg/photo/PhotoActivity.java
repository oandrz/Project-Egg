package starbright.com.projectegg.photo;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import starbright.com.projectegg.util.ClarifaiHelper;
import starbright.com.projectegg.R;

public class PhotoActivity extends AppCompatActivity {

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
            mClarifaiHelper.predict(data);
        }
    }

    private void openCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
    }
}
