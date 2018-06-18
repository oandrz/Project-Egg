package starbright.com.projectegg.features.ingredients;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;

public class IngredientsFragment extends Fragment implements IngredientsContract.View {

    private static final int AUTOCOMPLETE_DELAY = 600;

    @BindView(R.id.et_search_ingredients)
    AutoCompleteTextView etSearchIngredients;

    @BindView(R.id.img_action_button)
    ImageView imgActionButton;

    private Timer mTimer;

    private IngredientsContract.Presenter mPresenter;

    private TextWatcher mIngredientsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTimer != null) {
                mTimer.cancel();
            }
            if (s.toString().isEmpty()) {
                imgActionButton.setImageResource(R.drawable.ic_camera);
            } else {
                imgActionButton.setImageResource(R.drawable.ic_clear);
            }
        }

        @Override
        public void afterTextChanged(final Editable s) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }, AUTOCOMPLETE_DELAY);
        }
    };

    public static IngredientsFragment newInstance() {
        return new IngredientsFragment();
    }

    @Override
    public void onAttach(Context context) {
        MyApp.getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IngredientsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @OnClick({R.id.img_action_button})
    void onClickedEvent(View view) {
        switch (view.getId()) {
            case R.id.img_action_button:
                mPresenter.onActionButtonClicked(etSearchIngredients.getText().toString());
                break;
        }
    }

    @Override
    public void setPresenter(IngredientsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupEtSearchIngredients() {
        etSearchIngredients.addTextChangedListener(mIngredientsTextWatcher);
    }

    @Override
    public void clearEtSearchQuery() {
        etSearchIngredients.setText("");
    }

    @Override
    public void openCamera() {
        // TODO: 18/06/18 Start Camera Intent
    }
}
