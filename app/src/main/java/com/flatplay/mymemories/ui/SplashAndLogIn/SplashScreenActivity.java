package com.flatplay.mymemories.ui.SplashAndLogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.flatplay.mymemories.MainActivity;
import com.flatplay.mymemories.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Animation animation;
    private FirebaseAuth firebaseAuth;
    private SignInButton googlesignInBtn;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestor;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //google auth
        googlesignInBtn = findViewById(R.id.googleSignInbtn);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestor = FirebaseFirestore.getInstance();
        if (user != null) {
            googlesignInBtn.setVisibility(View.INVISIBLE);
        }


        googlesignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 4000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            user = firebaseAuth.getCurrentUser();

                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            initValues(isNew, user);
                            Intent i = new Intent(GameLogoSplashScreen.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(GameLogoSplashScreen.this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GameLogoSplashScreen.this, "failure", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void initValues(boolean isNew, FirebaseUser user) {
        if (isNew) {
        }
    }

}
