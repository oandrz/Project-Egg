package starbright.com.projectegg.features.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import starbright.com.projectegg.R;

public class BaseActivityWithoutToolbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_toolbar);
    }
}
