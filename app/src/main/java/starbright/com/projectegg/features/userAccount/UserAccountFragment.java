package starbright.com.projectegg.features.userAccount;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import starbright.com.projectegg.MyApp;
import starbright.com.projectegg.R;

public class UserAccountFragment extends Fragment implements UserAccountContract.View {

    private static final String USER_ACCOUNT_BUNDLE = "USER_ACCOUNT_BUNDLE";

    @BindView(R.id.tv_login_title)
    TextView tvLoginTitle;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @BindView(R.id.tv_navigation)
    TextView tvNavigation;

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
        outState.putBoolean(USER_ACCOUNT_BUNDLE, mPresenter.isLogin());
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
            etEmail.setHint(R.string.email_hint);
            btnConfirm.setText(R.string.login_button_text);
            tvNavigation.setText(R.string.login_navigation);
        } else {
            tvLoginTitle.setText(R.string.register_title);
            etEmail.setHint(R.string.register_email_hint);
            btnConfirm.setText(R.string.register_button_text);
            tvNavigation.setText(R.string.register_navigation);
        }
    }
}
