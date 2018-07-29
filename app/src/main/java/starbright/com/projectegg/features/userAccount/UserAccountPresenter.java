package starbright.com.projectegg.features.userAccount;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import starbright.com.projectegg.util.Constants;

class UserAccountPresenter implements UserAccountContract.Presenter{

    private final UserAccountContract.View mView;
    private final FirebaseAuth mAuth;

    private boolean mIsLoginAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener =
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (isUserEmailVerified(firebaseAuth)) {
                        mView.navigateToSearchRecipePage();
                    }
                }
            };

    UserAccountPresenter(UserAccountContract.View view, boolean isLogin) {
        mView = view;
        mView.setPresenter(this);
        mIsLoginAuth = isLogin;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void handleNavigationTextButtonClicked() {
        mIsLoginAuth = !mIsLoginAuth;
        mView.updateView(mIsLoginAuth);
    }

    @Override
    public void handleAuthenticationButtonClicked(String email, String password) {
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
        mView.disableView();
        final Task<AuthResult> auth = isLoginAuthentication() ? login(email, password)
                : signup(email, password);
        auth
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (!isLoginAuthentication()) {
                        sendUserVerificationEmail(authResult.getUser());
                    } else {
                        mView.hideProgressBar();
                        mView.enableView();
                        if (!authResult.getUser().isEmailVerified()) {
                            mView.showVerificationEmailSentDialog();
                        }
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mView.hideProgressBar();
                    mView.enableView();
                    mView.showLoginErrorToast(e.getMessage());
                }
            });
    }

    @Override
    public void onDestroy() {
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void start() {
        mAuth.addAuthStateListener(mAuthStateListener);
       mView.setupProgressBar();
        mView.updateView(mIsLoginAuth);
    }

    @Override
    public boolean isLoginAuthentication() {
        return mIsLoginAuth;
    }

    private Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    private Task<AuthResult> signup(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    private void sendUserVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mView.hideProgressBar();
                        mView.enableView();
                        mView.showVerificationEmailSentDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.hideProgressBar();
                        mView.enableView();
                        mView.showLoginErrorToast(e.getMessage());
                    }
                });
    }

    private boolean isUserEmailVerified(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().isEmailVerified();
        } else {
            return false;
        }
    }

    private boolean validateEmailFormat(String email) {
        return Pattern.compile(Constants.EMAIL_FORMAT).matcher(email).matches();
    }
}
