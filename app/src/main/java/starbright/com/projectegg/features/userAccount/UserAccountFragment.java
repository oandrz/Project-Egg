package starbright.com.projectegg.features.userAccount;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;

public class UserAccountFragment extends Fragment implements UserAccountContract.View {

    private static final String USER_ACCOUNT_BUNDLE = "USER_ACCOUNT_BUNDLE";

    @BindView(R.id.tv_login_title)
    TextView tvLoginTitle;

    @BindView(R.id.tv_navigation)
    TextView tvNavigation;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.root_layout)
    ScrollView rootLayout;

    private UserAccountContract.Presenter mPresenter;

    public static UserAccountFragment newInstance() {
        return new UserAccountFragment();
    }

    @Override
    public void onAttach(Context context) {
        MyApp.getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        new UserAccountPresenter(this, state == null || state.getBoolean(USER_ACCOUNT_BUNDLE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(USER_ACCOUNT_BUNDLE, mPresenter.isLoginAuthentication());
    }

    @OnClick({
        R.id.tv_navigation,
        R.id.btn_confirm
    })
    void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_navigation:
                mPresenter.onNavigationTextButtonClicked();
                break;
            case R.id.btn_confirm:
                mPresenter.onAuthenticationButtonClicked(
                        etEmail.getText().toString(),
                        etPassword.getText().toString()
                );
                break;
        }
    }

    @Override
    public void setPresenter(UserAccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateView(boolean isLogin) {
        if (isLogin) {
            tvLoginTitle.setText(R.string.login_title);
            btnConfirm.setText(R.string.login_button_text);
            tvNavigation.setText(R.string.login_navigation);
        } else {
            tvLoginTitle.setText(R.string.register_title);
            btnConfirm.setText(R.string.register_button_text);
            tvNavigation.setText(R.string.register_navigation);
        }
    }

    @Override
    public void navigatePage() {
        Snackbar.make(rootLayout, "Success", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void disableConfirmButton() {
        btnConfirm.setEnabled(false);
    }

    @Override
    public void enableConfirmButton() {
        btnConfirm.setEnabled(true);
    }

    @Override
    public void showLoginErrorDialog(String errorMessage) {
        Snackbar.make(rootLayout, errorMessage, Snackbar.LENGTH_SHORT)
                .setAction(R.string.general_retry_label, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.onAuthenticationButtonClicked(etEmail.getText().toString(),
                                etPassword.getText().toString());
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.lightRed))
                .show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setupProgressBar() {
        if (MyApp.isPreLolipop()) {
            final Drawable drawableProgress = DrawableCompat.wrap(
                    progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(
                    drawableProgress,
                    ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.red)
            );
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(drawableProgress));
        } else {
            progressBar.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()),
                            R.color.red), PorterDuff.Mode.SRC_IN);
        }
    }
}
