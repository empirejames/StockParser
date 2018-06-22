package com.james.stockparser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.james.stockparser.dataBase.TinyDB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.james.stockparser.Unit.checkDoubleClick.isFastDoubleClick;

/**
 * Created by 101716 on 2017/7/5.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private EditText emailEditText;
    private EditText passEditText;
    GoogleApiClient mGoogleApiClient;
    Button loginEmail, loginAnonymously, loginOther;
    TextView tvForgetPass;
    SignInButton loginGoogle;
    LoginButton loginFaceBook;
    CheckBox chkRemeber;
    boolean remeberMe;
    String TAG = LoginActivity.class.getSimpleName();
    RelativeLayout relativeLy;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 9001;
    CallbackManager mCallbackManager;
    TinyDB tinydb;
    boolean isTouchLG =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

//
//        new AppUpdater(this)
//                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
//                .setDisplay(Display.DIALOG)
//                .showAppUpdated(false)  // 若已是最新版本, 則 true: 仍會提示之, false: 不會提示之
//                .start();
        tinydb = new TinyDB(LoginActivity.this);
        chkRemeber = (CheckBox) findViewById(R.id.chkRemeber);
        tvForgetPass = (TextView) findViewById(R.id.tv_forgotPass);
        loginOther = (Button) findViewById(R.id.button_other);
        //loginOther.setVisibility(View.GONE);
        loginOther.setOnClickListener(this);
        loginAnonymously = (Button) findViewById(R.id.button_anonymously);
        loginAnonymously.setOnClickListener(this);
        loginEmail = (Button) findViewById(R.id.button_email);
        loginEmail.setOnClickListener(this);
        loginGoogle = (SignInButton) findViewById(R.id.button_google);
        loginGoogle.setOnClickListener(this);
        loginGoogle.setSize(SignInButton.SIZE_WIDE);
        loginFaceBook = (LoginButton) findViewById(R.id.button_facebook);
        loginFaceBook.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        tvForgetPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
        loginFaceBook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "FACEBOOK SUCCESS");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "FACEBOOK onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "FACEBOOK onError");
            }
        });
        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);

        if (tinydb.getString("account") != "") {
            emailEditText.setText(tinydb.getString("account"));
            passEditText.setText(tinydb.getString("password"));
            chkRemeber.setChecked(true);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        chkRemeber.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkRemeber.isChecked()) {
                    saveUserInfo(true, emailEditText.getText().toString(), passEditText.getText().toString());
                } else {
                    saveUserInfo(false, emailEditText.getText().toString(), passEditText.getText().toString());
                }
            }
        });
    }

    public void saveUserInfo(boolean isSave, String account, String password) {
        if (isSave) {
            tinydb.putString("account", account);
            tinydb.putString("password", password);
            remeberMe = true;
        } else {
            tinydb.putString("account", "");
            tinydb.putString("password", "");
            remeberMe = false;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "Login onStart");
        mAuth.addAuthStateListener(mAuthListener);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        Log.e(TAG, "Get Login MSG" + msg);
        if (msg!=null){
            alertDialog("推播訊息",msg);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            LoginManager.getInstance().logOut();
            signOut();
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("isVistor", "N");
                        i.putExtra("name", user.getDisplayName());
                        i.putExtra("uid", user.getUid());
                        i.putExtra("email", user.getEmail());
                        startActivity(i);
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    public void Anonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "訪客身分登入成功",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("isVistor", "Y");
                            startActivity(i);
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void checkLogin() {
        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            emailEditText.setError("Email格式錯誤");
        }

        final String password = passEditText.getText().toString();
        if (!isValidPassword(password)) {
            passEditText.setError("密碼不得為空");
        }
        if (isValidEmail(email) && isValidPassword(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!task.isSuccessful()) {
                                Log.e(TAG, task.getException().toString());
                                if (task.getException().toString().contains("The email address is already in use by another account.")) {
                                    alertDialog("系統提示","此 Email信箱已被註冊使用");
                                } else if (task.getException().toString().contains("The password is invalid or the user does not have a password")) {
                                    alertDialog("系統提示","密碼輸入錯誤");
                                } else if (task.getException().toString().contains("There is no user record corresponding to this identifier. The user may have been deleted")) {
                                    register(email, password);
                                }
                            } else {
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("isVistor", "N");
                                    i.putExtra("name", user.getDisplayName());
                                    i.putExtra("uid", user.getUid());
                                    i.putExtra("email", user.getEmail());
                                    startActivity(i);
                            }
                        }
                    });
        }

    }

    private void register(final String email, final String password) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createUser(email, password);
                            }
                        })
                .setNeutralButton("取消", null)
                .show();
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //String message = task.isSuccessful() ? "註冊成功" : "註冊失敗";
                Log.e(TAG, task.getException() + "");
                if (task.isSuccessful()) {
                    alertDialog("系統提示","註冊成功");
                } else {
                    if (task.getException().toString().contains("The email address is already in use by another account.")) {
                        alertDialog("系統提示","此 Email信箱已被註冊使用");
                    }
                }
            }
        });
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void toVisibleAnim(View view) {
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        view.startAnimation(mShowAction);
    }
    private void toGoneAnim(View view) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
        view.startAnimation(mHiddenAction);
    }
    private void alertDialog(String title , String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確認", null)
                .show();
    }

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        relativeLy = (RelativeLayout) findViewById(R.id.relative_layout_GoogleFacebook);
        if (!isFastDoubleClick()) {
            if (i == R.id.button_email) {
                checkLogin();
            } else if (i == R.id.button_google) {
                googleSignIn();
            } else if (i == R.id.button_facebook) {
            } else if (i == R.id.button_anonymously) {
                Anonymously();
            } else if (i == R.id.button_other) {
                if (loginGoogle.getVisibility() == View.VISIBLE) {
                    toGoneAnim(loginGoogle);
                    toGoneAnim(loginFaceBook);
                    toVisibleAnim(tvForgetPass);
                    loginGoogle.setVisibility(View.GONE);
                    loginFaceBook.setVisibility(View.GONE);
                    tvForgetPass.setVisibility(View.VISIBLE);
                } else {
                    toVisibleAnim(loginGoogle);
                    toVisibleAnim(loginFaceBook);
                    loginGoogle.setVisibility(View.VISIBLE);
                    loginFaceBook.setVisibility(View.VISIBLE);
                    tvForgetPass.setVisibility(View.GONE);
                }
            }
        }else{
            return;
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Log.e(TAG, "User profile : " +user.getUid() + " :: " + user.getEmail() + " :: "+ user.getDisplayName() );
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("isVistor", "N");
                            i.putExtra("name", user.getDisplayName());
                            i.putExtra("uid", user.getUid());
                            i.putExtra("email", user.getEmail());
                            startActivity(i);
                        } else {
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
