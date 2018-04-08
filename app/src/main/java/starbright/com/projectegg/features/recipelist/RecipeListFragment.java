package starbright.com.projectegg.features.recipelist;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import starbright.com.projectegg.R;
import starbright.com.projectegg.util.ClarifaiHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Andreas on 4/8/2018.
 */

@RuntimePermissions
public class RecipeListFragment extends Fragment implements ClarifaiHelper.Callback {

    private static final int CAMERA_REQUEST_CODE = 100;

    private ClarifaiHelper mClarifaiHelper;

    public static RecipeListFragment newInstance() {
        Bundle args = new Bundle();
        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClarifaiHelper = new ClarifaiHelper();
        RecipeListFragmentPermissionsDispatcher.openCameraWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RecipeListFragmentPermissionsDispatcher
                .onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            mClarifaiHelper.predict(data, this);
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void openCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
    }

    @Override
    public void onPredictionCompleted(String ingredients) {

    }
}
