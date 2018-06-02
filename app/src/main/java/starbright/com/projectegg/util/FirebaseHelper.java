package starbright.com.projectegg.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {
    private FirebaseAuth mAuth;

    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signIn(String email, String password, SignInListener listener) {
        mAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }

    public interface SignInListener {
        void success();
        void failed(String errorMessage);
    }
}
