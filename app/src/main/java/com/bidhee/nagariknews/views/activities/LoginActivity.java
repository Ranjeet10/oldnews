package com.bidhee.nagariknews.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/4/16.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    /**
     * for facebook
     */

    CallbackManager callbackManager;

    /**
     * for twitter
     */
    TwitterSession session;

    /**
     * for google plus
     */

    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
//    private ProgressDialog mProgressDialog;


    private String TAG = getClass().getSimpleName();
    private ProgressDialog dialog;


    //SessionManager
    SessionManager sessionManager;


    @Bind(R.id.btnSkip)
    TextView btnSkip;
    @Bind(R.id.btnCreateAcc)
    TextView btnCreateAccount;
    @Bind(R.id.btnBackToLogin)
    TextView btnBackToLogin;


    //=========Sign in form field============
    @Bind(R.id.login_email_field)
    AutoCompleteTextView loginEmailField;
    @Bind(R.id.login_password_password)
    EditText loginPasswordField;
    @Bind(R.id.btn_login)
    Button btnLogin;

    //==========sign up form field===========
    @Bind(R.id.signup_profile_image)
    ImageView profileImage;
    @Bind(R.id.firstName)
    AutoCompleteTextView firstNameField;
    @Bind(R.id.lastName)
    AutoCompleteTextView lastNameField;
    @Bind(R.id.signup_email)
    EditText signUpEmailField;
    @Bind(R.id.signup_password)
    EditText signUpPasswordField;
    @Bind(R.id.signup_confirm_password)
    EditText signUpConfirmPasswordField;
    @Bind(R.id.btnSignUp)
    Button btnSignUp;

    private Response.Listener<String> signUpResponse;
    private Response.ErrorListener errorListener;


    @Bind(R.id.signup_layout)
    LinearLayout signUpLayout;
    @Bind(R.id.loginLayout)
    LinearLayout loginLayout;

    @Bind(R.id.login_button)
    LoginButton btnFacebookLogin;
    @Bind(R.id.twitter_login_button)
    TwitterLoginButton btnTwitterLogin;
    @Bind(R.id.sign_in_button)
    SignInButton btnGooglePlusLogin;

    Boolean isSignUpCanceled = false;
    Boolean isLoginCanceled = false;

    View signUpFormFieldView = null;
    View loginFormFieldView = null;

    //facebook tracker object
    AccessTokenTracker accessTokenTracker;
    private String fbAccessToken;

    public static String avatarImage = "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQyK7CYeBTH0OML49nzdiFcszgtGpaj67qRVrRT3dp-sSz1k-QXGA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sessionManager = new SessionManager(this);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("signing in...");

        handleParamsResponseForLoginSignup();

        registerCallBackListenerForFacebookLoginButton();
        registerCallbackListenerForTwitterLoginButton();

        configuringGooglePlus();
        trackFacebookToken();
    }

    private void trackFacebookToken() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
    }

    private void configuringGooglePlus() {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API
        // and the options specified by gGoogleSignInOptions.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.

        btnGooglePlusLogin.setSize(SignInButton.SIZE_STANDARD);
        btnGooglePlusLogin.setScopes(gso.getScopeArray());
        // [END customize_button]
    }


    @OnClick(R.id.sign_in_button)
    void onSignINClicked() {
        signIn();
    }


    private void registerCallbackListenerForTwitterLoginButton() {
        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                dialog.show();
                WebService.authRequest(ServerConfig.AUTH_URL, getJsonBody(StaticStorage.LOGIN_TYPE_TWITTER, token, secret), signUpResponse, errorListener);

//                session = result.data;
//                final String username = session.getUserName();
//                Long userid = session.getUserId();
//
//
//                TwitterSession session = Twitter.getSessionManager().getActiveSession();
//                Twitter.getApiClient(session).getAccountService()
//                        .verifyCredentials(true, false, new Callback<User>() {
//
//
//                            @Override
//                            public void success(Result<User> userResult) {
//                                User user = userResult.data;
//                                String imageUrl = user.profileImageUrl;
//                                imageUrl = imageUrl.replace("_normal", "");
//                                String email = user.email;
//                                Log.i("imageurl", imageUrl + ":" + email);
//
//                                createSessionAndLaunchSelectCategoryActivity(StaticStorage.LOGIN_TYPE_TWITTER, username, email, imageUrl, "");
//
//                            }
//
//                            @Override
//                            public void failure(TwitterException e) {
//
//                            }
//
//                        });

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    private void registerCallBackListenerForFacebookLoginButton() {
        callbackManager = CallbackManager.Factory.create();
        btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                fbAccessToken = loginResult.getAccessToken().getToken();
                Log.i(TAG, "accesstoken : " + fbAccessToken);
                dialog.show();
                WebService.authRequest(ServerConfig.AUTH_URL, getJsonBody(StaticStorage.LOGIN_TYPE_FACEBOOK, fbAccessToken, null), signUpResponse, errorListener);

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getJsonBody(int loginType, String token, String secret) {
        JSONObject bodyObject = new JSONObject();
        String authKey = "";
        try {
            if (loginType == StaticStorage.LOGIN_TYPE_GOOGLE) {
                authKey = "auth_google";
            } else if (loginType == StaticStorage.LOGIN_TYPE_FACEBOOK) {
                authKey = "auth_facebook";
            } else if (loginType == StaticStorage.LOGIN_TYPE_TWITTER) {
                authKey = "auth_twitter";
            }

            bodyObject.put(authKey, new Boolean(true));
            bodyObject.put("access_token", token);

            if (!TextUtils.isEmpty(secret)) {
                bodyObject.put("access_token_secret", secret);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bodyObject.toString();
    }

    /**
     * create login session for the recently logged in user
     * from the class {@link SessionManager}
     */
    @OnClick(R.id.btn_login)
    void onLoginClick() {

        attemptLogin(loginEmailField.getText().toString(), loginPasswordField.getText().toString());

    }

    private void attemptLogin(String email, String password) {
        loginEmailField.setError(null);
        loginPasswordField.setError(null);

        isLoginCanceled = false;
        loginFormFieldView = null;

        if (TextUtils.isEmpty(password)) {
            loginPasswordField.setError("password required");
            requestFocusView(false, loginPasswordField);
        } else if (!BasicUtilMethods.isValidPassword(password)) {
            loginPasswordField.setError("password must be greater than 5 character");
            requestFocusView(false, loginPasswordField);
        }

        if (TextUtils.isEmpty(email)) {
            loginEmailField.setError("Email required");
            requestFocusView(false, loginEmailField);
        } else if (!BasicUtilMethods.isValidEmail(email)) {
            loginEmailField.setError("Email is not valid");
            requestFocusView(false, loginEmailField);
        }

        if (isLoginCanceled) {
            loginFormFieldView.requestFocus();
        } else {
            dialog.setMessage("Please wait...");
            dialog.show();

            HashMap<String, String> params = new HashMap<>();
            params.put("_username", email);
            params.put("_password", password);
            WebService.hitServerWithParams(ServerConfig.LOGIN_URL, params, signUpResponse, errorListener);
        }


    }

    private void createSessionAndLaunchSelectCategoryActivity(int loginType, String userName, String userEmail, String avatarImage, String token) {
        /**
         * if {@link Dashboard} was created before
         * clear the instance and proceed the further
         */
        if (Dashboard.getInstance() != null) {
            Dashboard.getInstance().finish();
        }
        sessionManager.createLoginSession(loginType, userName, userEmail, avatarImage, token);
        launchActivity(SelectCategoryActivity.class);
    }

    @OnClick(R.id.btnSkip)
    void onSkipClick() {
        launchActivity(Dashboard.class);
    }

    @OnClick(R.id.btnCreateAcc)
    void onCreateAccountClicked() {
        toggleLayout(1);
    }

    @OnClick(R.id.signup_profile_image)
    void onProfileImageCLick() {
        showFileChooser();
    }

    @OnClick(R.id.btnSignUp)
    void onSignUpCreated() {
        String fName = firstNameField.getText().toString();
        String lName = lastNameField.getText().toString();
        String email = signUpEmailField.getText().toString();
        String password = signUpPasswordField.getText().toString();
        String confirmPassword = signUpConfirmPasswordField.getText().toString();

        attemptSignUp(fName, lName, email, password, confirmPassword);
    }

    @OnClick(R.id.btnBackToLogin)
    void onBackToLoginClicked() {
        toggleLayout(2);
    }

    //====================================================================================================
    //========================================== SignUp Methods ==========================================
    //====================================================================================================

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void attemptSignUp(String fName, String lName, String email, String password, String confirmPassword) {
        firstNameField.setError(null);
        lastNameField.setError(null);
        signUpEmailField.setError(null);
        signUpPasswordField.setError(null);
        signUpConfirmPasswordField.setError(null);

        isSignUpCanceled = false;
        signUpFormFieldView = null;

        if (!BasicUtilMethods.isValidPassword(password)) {
            signUpPasswordField.setError("Password must be greater than 5 character");
            requestFocusView(true, signUpPasswordField);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            signUpConfirmPasswordField.setError("Please confirm your password");
            requestFocusView(true, signUpConfirmPasswordField);
        }

        if (TextUtils.isEmpty(password)) {
            signUpPasswordField.setError("Password required");
            requestFocusView(true, signUpPasswordField);
        }

        if (TextUtils.isEmpty(email)) {
            signUpEmailField.setError("Email required");
            requestFocusView(true, signUpEmailField);
        }

        if (!BasicUtilMethods.isValidEmail(email)) {
            signUpEmailField.setError("Email not valid");
            requestFocusView(true, signUpEmailField);
        }

        if (TextUtils.isEmpty(lName)) {
            lastNameField.setError("Last name required");
            requestFocusView(true, lastNameField);
        }

        if (TextUtils.isEmpty(fName)) {
            firstNameField.setError("First name required");
            requestFocusView(true, firstNameField);
        }

        if (!confirmPassword.equals(password)) {
            requestFocusView(true, signUpPasswordField);
            requestFocusView(true, signUpConfirmPasswordField);
            Toast.makeText(this, "Sorry Password not matched", Toast.LENGTH_SHORT).show();
        }
        if (isSignUpCanceled) {
            signUpFormFieldView.requestFocus();
        } else {
            registerUser(fName + " " + lName, email, password);
        }
    }

    private void registerUser(final String name, final String email, final String password) {
        dialog.setMessage("Please wait...");
        dialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("nagarik_consumer[name]", name);
        params.put("nagarik_consumer[email]", email);
        params.put("nagarik_consumer[plainPassword]", password);

        WebService.hitServerWithParams(ServerConfig.REGISTER_URL, params, signUpResponse, errorListener);

    }

    private void handleParamsResponseForLoginSignup() {
        signUpResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i(TAG, response);
                try {
                    JSONObject sObject = new JSONObject(response);
//                    String status = sObject.getString("status");
//                    if (status.equals("success")) {
                    JSONObject dataObject = sObject.getJSONObject("data");
                    String username = dataObject.getString("username");
                    String email = dataObject.getString("email");
                    String name = dataObject.getString("name");
                    String token = dataObject.getString("token");
                    String profile_pic = dataObject.getString("profile_picture");

                    createSessionAndLaunchSelectCategoryActivity(StaticStorage.LOGIN_TYPE_FORM, name, email, profile_pic, token);
//                    } else if (status.equals("error")) {
//                        if (sObject.has("message")) {
//                            String message = sObject.getString("message");
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        }
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        };
    }


    private void requestFocusView(Boolean isFromSignUp, View formField) {
        if (isFromSignUp) {
            isSignUpCanceled = true;
            signUpFormFieldView = formField;
        } else {
            isLoginCanceled = true;
            loginFormFieldView = formField;
        }
    }


    private void toggleLayout(int i) {
        switch (i) {
            case 1:
                loginLayout.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.VISIBLE);
                btnCreateAccount.setVisibility(View.GONE);
                break;
            case 2:
                loginLayout.setVisibility(View.VISIBLE);
                signUpLayout.setVisibility(View.GONE);
                btnCreateAccount.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void launchActivity(Class<?> activity) {
        Intent intent = new Intent(LoginActivity.this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            new RetrieveTokenTask(result).execute();

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                Log.i("facebookcode", requestCode + "\n" + resultCode);
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Log.i("twittercode", requestCode + "\n" + resultCode);
                btnTwitterLogin.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result, String token) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Log.i(TAG, "login was success");
            dialog.show();
            String jsonBody = getJsonBody(StaticStorage.LOGIN_TYPE_GOOGLE, token, null).toString();
            Log.i(TAG, jsonBody);
            WebService.authRequest(ServerConfig.AUTH_URL, jsonBody, signUpResponse, errorListener);
        } else {

        }
    }
    // [END handleSignInResult]


    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    /*
    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    */
    /*
    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

*/


    @Override
    protected void onStart() {
        super.onStart();
        accessTokenTracker.startTracking();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        ButterKnife.bind(this);
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////  BACKGROUND TASK TO GET ACCESS TOKEN FOR GOOGLE PLUS  //////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {
        GoogleSignInResult result;

        public RetrieveTokenTask(GoogleSignInResult result) {
            this.result = result;
        }

        @Override
        protected String doInBackground(String... params) {
            String token = null;
            String scopes = "oauth2:profile email";
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), Plus.AccountApi.getAccountName(mGoogleApiClient), scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), RC_SIGN_IN);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("googletoken", s + "");
            handleSignInResult(result, s);
        }
    }
}


//
//    public void logoutTwitter() {
//        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
//        if (twitterSession != null) {
//            ClearCookies(getApplicationContext());
//            Twitter.getSessionManager().clearActiveSession();
//            Twitter.logOut();
//        }
//    }
//
//    public static void ClearCookies(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            CookieManager.getInstance().removeAllCookies(null);
//            CookieManager.getInstance().flush();
//        } else {
//            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
//            cookieSyncMngr.startSync();
//            CookieManager cookieManager=CookieManager.getInstance();
//            cookieManager.removeAllCookie();
//            cookieManager.removeSessionCookie();
//            cookieSyncMngr.stopSync();
//            cookieSyncMngr.sync();
//        }
//    }


// App code
//                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("LoginActivity", response.getRawResponse());
//                                String _response = response.getRawResponse();
//
//
//                                // Application code
//                                try {
//                                    JSONObject jsonObject = new JSONObject(_response);
//                                    String email = jsonObject.getString("email");
//                                    String id = jsonObject.getString("id");
//                                    String name = jsonObject.getString("name");
//                                    String gender = jsonObject.getString("gender");
//                                    String img = "https://graph.facebook.com/" + id + "/picture?type=large";
//
//                                    createSessionAndLaunchSelectCategoryActivity(StaticStorage.LOGIN_TYPE_FACEBOOK, name, email, img,"");
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();

