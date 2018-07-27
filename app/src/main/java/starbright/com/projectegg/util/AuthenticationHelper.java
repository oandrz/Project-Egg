package starbright.com.projectegg.util;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationHelper {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener =
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                }
            };

}
