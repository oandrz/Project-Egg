package starbright.com.projectegg.features.ingredients;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

public class IngredientsFragment extends Fragment implements IngredientsContract.View {

    private static final int AUTOCOMPLETE_DELAY = 600;

    @Inject
    AppRepository repository;

    @Inject
    BaseSchedulerProvider schedulerProvider;

    @BindView(R.id.et_search_ingredients)
    EditText etSearchIngredients;

    @BindView(R.id.img_action_button)
    ImageView imgActionButton;

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;

    @BindView(R.id.suggestion_progress_bar)
    ProgressBar suggestionProgressBar;

    private Timer mTimer;

    private IngredientsAdapter mAdapter;
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
           mPresenter.handleOnSuggestionTextChanged(s.toString());
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
                            mPresenter.searchIngredient(s.toString());
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
        new IngredientsPresenter(repository, this, schedulerProvider);
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
                mPresenter.handleActionButtonClicked(etSearchIngredients.getText().toString());
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
    public void setupRvIngredientSuggestion() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvIngredients.setLayoutManager(layoutManager);

        mAdapter = new IngredientsAdapter(getActivity());
        rvIngredients.setAdapter(mAdapter);
    }

    @Override
    public void clearEtSearchQuery() {
        etSearchIngredients.setText("");
    }

    @Override
    public void openCamera() {
        // TODO: 18/06/18 Start Camera Intent
    }

    @Override
    public void showSearchSuggestion(List<Ingredient> ingredients) {
        rvIngredients.setVisibility(View.VISIBLE);
        mAdapter.setIngredients(ingredients);
    }

    @Override
    public void showActionCamera() {
        imgActionButton.setVisibility(View.VISIBLE);
        imgActionButton.setImageResource(R.drawable.ic_camera);
    }

    @Override
    public void showActionClear() {
        imgActionButton.setVisibility(View.VISIBLE);
        imgActionButton.setImageResource(R.drawable.ic_clear);
    }

    @Override
    public void hideActionButton() {
        imgActionButton.setVisibility(View.GONE);
    }

    @Override
    public void showSuggestionProgressBar() {
        suggestionProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSuggestionProgressBar() {
        suggestionProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showItemEmptyToast() {
        Toast.makeText(getActivity(), getString(R.string.ingredients_search_empty),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideSearchSuggestion() {
        rvIngredients.setVisibility(View.GONE);
    }
}
