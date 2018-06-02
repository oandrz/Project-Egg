package starbright.com.projectegg.features.userAccount;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

class UserAccountPresenter implements UserAccountContract.Presenter{

    private final FirebaseAuth mAuth;
    private final UserAccountContract.View mView;
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
    public void onConfirmButtonClicked(String email, String password) {
        mView.showProgressDialog();
        mView.disableConfirmButton();
        mAuth
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mView.showProgressDialog();
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
                    mView.showProgressDialog();
                    mView.showLoginErrorDialog(e.getMessage());
                }
            });
    }

    @Override
    public void start() {
       mView.updateView(mIsLogin);
    }

    @Override
    public boolean isLogin() {
        return mIsLogin;
    }
}
