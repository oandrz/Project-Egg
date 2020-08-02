/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

package starbright.com.projectegg.util;

import androidx.annotation.NonNull;

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
