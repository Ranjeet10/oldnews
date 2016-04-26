package com.bidhee.nagariknews.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.SessionManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/4/16.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private static final int ERROR_DIALOG_REQUEST_CODE = 11;

    private ProgressDialog dialog;

    // For communicating with Google APIs
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    // contains all possible error codes for when a client fails to connect to
    // Google Play services
    private ConnectionResult mConnectionResult;

    //SessionManager
    SessionManager sessionManager;


    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.btnSkip)
    TextView btnSkip;
    @Bind(R.id.btnCreateAcc)
    TextView btnCreateAccount;
    @Bind(R.id.btnBackToLogin)
    TextView btnBackToLogin;

    @Bind(R.id.signup_layout)
    LinearLayout signUpLayout;
    @Bind(R.id.loginLayout)
    LinearLayout loginLayout;

    @Bind(R.id.login_button)
    LoginButton loginButton;
    @Bind(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;
    @Bind(R.id.sign_in_button)
    SignInButton googleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sessionManager = new SessionManager(this);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("signing in...");
        registerCallBackListenerForFacebookLoginButton();
        registerCallbackListenerForTwitterLoginButton();
        registerForGooglePlusLogin();


    }

    private void registerForGooglePlusLogin() {
        // Initializing google plus api client
        mGoogleApiClient = buildGoogleAPIClient();

        googleLoginButton.setSize(SignInButton.SIZE_STANDARD);

    }


    @OnClick(R.id.sign_in_button)
    void onSignINClicked() {
        processSignIn();
    }


    /**
     * API to handler sign in of user If error occurs while connecting process
     * it in processSignInError() api
     */
    private void processSignIn() {

        if (!mGoogleApiClient.isConnecting()) {
            processSignInError();
            mSignInClicked = true;
        }

    }

    /**
     * API to handle sign out of user
     */
    private void processSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }

    }

    /**
     * API to process sign in error Handle error based on ConnectionResult
     */
    private void processSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                dialog.show();
                mConnectionResult.startResolutionForResult(this,
                        SIGN_IN_REQUEST_CODE);
                dialog.show();
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    private void registerCallbackListenerForTwitterLoginButton() {
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {


                session = result.data;
                final String username = session.getUserName();
                Long userid = session.getUserId();


                TwitterSession session =
                        Twitter.getSessionManager().getActiveSession();
                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {


                            @Override
                            public void success(Result<User> userResult) {
                                User user = userResult.data;
                                String imageUrl = user.profileImageUrl;
                                imageUrl = imageUrl.replace("_normal", "");
                                String email = user.email;
                                Log.i("imageurl", imageUrl + ":" + email);
                                sessionManager.createLoginSession(username, "imagelink");
                                launchActivity(SelectCategoryActivity.class);

                            }

                            @Override
                            public void failure(TwitterException e) {

                            }

                        });

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    private void registerCallBackListenerForFacebookLoginButton() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String gender = object.getString("gender");
                                    String img = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    Log.d("responseebingi", email + "\n" + birthday + "\n" + name + "\n" + gender + "\n" + img);
//                                    infoTextView.setText(email + "\n" + birthday + "\n" + name + "\n" + gender + "\n" + img);

                                    sessionManager.createLoginSession("Ram Mandal", "imagelink");

                                    launchActivity(SelectCategoryActivity.class);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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

    /**
     * create login session for the recently logged in user
     * from the class {@link SessionManager}
     */
    @OnClick(R.id.btn_login)
    void onLoginClick() {
        sessionManager.createLoginSession("Ram Mandal", "imagelink");
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

    @OnClick(R.id.btnBackToLogin)
    void onBackToLoginClicked() {
        toggleLayout(2);
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
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {

                mSignInClicked = false;
            }
            dialog.dismiss();
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
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
                twitterLoginButton.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // make sure to initiate connection
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

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


    /**
     * API to return GoogleApiClient Make sure to create new after revoking
     * access or for first time sign in
     *
     * @return
     */


    private GoogleApiClient buildGoogleAPIClient() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN), new Scope(Scopes.PLUS_ME), new Scope(Scopes.PROFILE))
                .requestId()
                .build();
        googleLoginButton.setScopes(gso.getScopeArray());

        return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


    }

    /**
     * Callback for GoogleApiClient connection success
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;

        Toast.makeText(getApplicationContext(), "Signed In Successfully",
                Toast.LENGTH_LONG).show();

        processUserInfoAndUpdateUI();

    }

    /**
     * API to update signed in user information
     */
    private void processUserInfoAndUpdateUI() {
        Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        StringBuilder builder = new StringBuilder();
        if (signedInUser != null) {

            if (signedInUser.hasDisplayName()) {
                String userName = signedInUser.getDisplayName();
                builder.append("userName : " + userName + "\nid :" + signedInUser.getId());

                sessionManager.createLoginSession("Ram Mandal", "imagelink");
                launchActivity(SelectCategoryActivity.class);
            }

            if (signedInUser.hasTagline()) {
                String tagLine = signedInUser.getTagline();
                builder.append("\ntagline : " + tagLine);
            }

            if (signedInUser.hasAboutMe()) {
                String aboutMe = signedInUser.getAboutMe();
                builder.append("\nabout me : " + aboutMe);
            }

            if (signedInUser.hasBirthday()) {
                String birthday = signedInUser.getBirthday();
                builder.append("\nbirthday :" + birthday);
            }

            if (signedInUser.hasCurrentLocation()) {
                String userLocation = signedInUser.getCurrentLocation();
                builder.append("\nlocation :" + userLocation);
            }

            String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            builder.append("\nemail : " + userEmail);

            if (signedInUser.hasImage()) {
                String userProfilePicUrl = signedInUser.getImage().getUrl();
                // default size is 50x50 in pixels.changes it to desired size
                int profilePicRequestSize = 250;

                userProfilePicUrl = userProfilePicUrl.substring(0,
                        userProfilePicUrl.length() - 2) + profilePicRequestSize;
                builder.append("\nimagepath : " + userProfilePicUrl);
//                new UpdateProfilePicTask(userProfilePic)
//                        .execute(userProfilePicUrl);
            }


//            infoTextView.setText(builder.toString());
        }
    }

    /**
     * Callback for suspension of current connection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();

    }


    /**
     * Callback for GoogleApiClient connection failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {

        // Most of the time, the connection will fail with a
        // user resolvable result. We can store that in our
        // mConnectionResult property ready for to be used
        // when the user clicks the sign-in button.

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    ERROR_DIALOG_REQUEST_CODE).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                processSignInError();
            }
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
