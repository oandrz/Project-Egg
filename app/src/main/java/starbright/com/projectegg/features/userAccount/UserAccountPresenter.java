package starbright.com.projectegg.features.userAccount;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import starbright.com.projectegg.util.Constants;

class UserAccountPresenter implements UserAccountContract.Presenter{

    private final UserAccountContract.View mView;
    private final FirebaseAuth mAuth;

    private boolean mIsLogin;

    UserAccountPresenter(UserAccountContract.View view, boolean isLogin) {
        mView = view;
        mView.setPresenter(this);
        mIsLogin = isLogin;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onNavigationTextButtonClicked() {
        mIsLogin = !mIsLogin;
        mView.updateView(mIsLogin);
    }

    @Override
    public void onAuthenticationButtonClicked(String email, String password) {
        if (email == null || email.isEmpty()) {
            if (validateEmailFormat(email)) {
                mView.showEmailFormatWrongErrorToast();
            } else {
                mView.showEmailEmptyErrorToast();
            }
            return;
        } else if (password == null || password.isEmpty()){
            mView.showPasswordErrorToast();
            return;
        }

        mView.showProgressBar();
        mView.disableConfirmButton();
        final Task<AuthResult> auth = isLoginAuthentication() ? login(email, password)
                : signup(email, password);
        auth.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mView.hideProgressBar();
                    mView.enableConfirmButton();
                    mView.navigatePage();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mView.hideProgressBar();
                    mView.enableConfirmButton();
                    mView.showLoginErrorDialog(e.getMessage());
                }
            });
    }

    @Override
    public void start() {
       mView.setupProgressBar();
       mView.updateView(mIsLogin);
    }

    @Override
    public boolean isLoginAuthentication() {
        return mIsLogin;
    }

    private Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    private Task<AuthResult> signup(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    private boolean validateEmailFormat(String email) {
        return Pattern.compile(Constants.EMAIL_FORMAT).matcher(email).matches();
    }
}
