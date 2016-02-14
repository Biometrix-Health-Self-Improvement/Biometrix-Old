package com.rocket.biometrix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A separate activity for Google login since there are specific operations that can be done on it
 *
 * Created 2/2/2016 by Troy Riblett
 */
public class GoogleLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, AsyncResponse
{
    //Google account variables
    //A reference to the Google API
    private GoogleApiClient googleApiClient;
    //A loading symbol
    private ProgressDialog progressDialog;
    //A tag that is used in debug logging
    private static final String TAG = "GoogleLoginActivity";
    //The number that corresponds to the google API "sign-in" activity
    private static final int RC_SIGN_IN = 9001;
    //A reference to the user's google account
    private GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Button listeners
        findViewById(R.id.googleSignInButton).setOnClickListener(this);
        findViewById(R.id.logoutButton).setOnClickListener(this);
        findViewById(R.id.revokeButton).setOnClickListener(this);

        //Creates the google sign in object with requests for email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("212262655567-0na15tnrbol7g1ukjhqh98vit1je64a4.apps.googleusercontent.com")
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //This code is lightly refactored from the android example given by Android here
    //https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java#L59-L64

    /**
     * Attempts to sign the user in using silent sign in. If that fails, it is still possible to
     * attempt login via the login button
     */
    @Override
    public void onStart()
    {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (opr.isDone())
        {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.

            //Logs that the current activity performed a sign-in via cached credentials.
            Log.d(TAG, "Got cached sign-in");

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
        {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            //Shows the progress of sign in
            showProgressDialog();

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>()
            {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult)
                {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        googleApiClient.disconnect();
    }

    /**
     * Retrieves the result of the sign-in intent
     * @param requestCode The type of google API call used
     * @param resultCode A result code
     * @param data The data from the sign-in packaged in an intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * This function is called when the google sign in activity is finished (see resultcallback in
     * on start)
     * @param result The result of the attempt to sign in using Google APIs
     */
    private void handleSignInResult(GoogleSignInResult result)
    {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);

            String token = acct.getIdToken();

            new DatabaseConnect(this).execute(DatabaseConnectionTypes.GOOGLE_TOKEN, token);
            showBiometrixProgressDialog();

        }
        else
        {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    /**
     * Retrieves the results of calling the webserver.
     */
    public void processFinish(String result)
    {
        hideProgressDialog();

        JSONObject jsonObject;

        //Tries to parse the returned result as a json object.
        try
        {
            jsonObject = new JSONObject(result);
        }
        catch (JSONException jsonExcept)
        {
            jsonObject = null;
        }

        //If the return could not be parsed, then it was not a successful addition
        if (jsonObject == null)
        {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                if (jsonObject.has("Error"))
                {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                }
                //If the operation succeeded
                else if ((Boolean)jsonObject.get("Verified") )
                {
                    //Logins the google account user with their id as their "username"
                    LocalAccount.Login(acct.getId(), jsonObject.getString("Token"));

                    Toast.makeText(getApplicationContext(), "Google sign in succeeded!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException jsonExcept)
            {
                Toast.makeText(getApplicationContext(), "Something went wrong with the server's return", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Makes the buttons appear or disappear as needed. If the user is signed in, the sign-in button
     * disappears. If the user is signed out it re-appears.
     * @param signedIn A boolean for whether the user is currently signed in or not
     */
    private void updateUI(boolean signedIn)
    {
        hideProgressDialog();

        //Toggles buttons on or off depending on whether the user is now signed in or not
        if (signedIn)
        {
            findViewById(R.id.googleSignInButton).setVisibility(View.GONE);
            findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
            findViewById(R.id.revokeButton).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.googleSignInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.logoutButton).setVisibility(View.GONE);
            findViewById(R.id.revokeButton).setVisibility(View.GONE);;
        }
    }


    /**
     * Starts the google authentication activity from a button click.
     */
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Starts the google authentication activity for logging out purposes.
     */
    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback
                (new ResultCallback<Status>() {
                    //Creates the function that is called when the sign out finishes executing
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                            updateUI(false);

                            LocalAccount.Logout();
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not logout", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    /**
     * Revokes Google sign-in from having access to the application
     */
    private void revokeAccess()
    {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback
                (new ResultCallback<Status>()
                {
                    //Creates the function that is called when revoke access is finished executing
                    @Override
                    public void onResult(Status status)
                    {
                        Toast.makeText(getApplicationContext(), "This app has been removed from your google account", Toast.LENGTH_LONG).show();
                        LocalAccount.Logout();
                        updateUI(false);
                    }
                });
    }


    /**
     * Creates a progress dialog if it does not exist, and then shows it
     */
    private void showProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.login_loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    /**
     * Creates a progress dialog if it does not exist, and then shows it
     */
    private void showBiometrixProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.login_biometrix_loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    /**
     * Hides the progress dialog if it currently exists
     */
    private void hideProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.hide();
        }
    }

    /**
     * A call to this function means that the connection to Google APIs failed, and so Google Account
     * cannot be used
     * @param connectionResult This paramater is ignored
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Toast.makeText(getApplicationContext(), "Unable to access Google Services", Toast.LENGTH_LONG).show();
    }

    @Override
    /**
     * //TODO discuss the following, we may want to go to this type of execution in other areas
     * An interesting way of doing things from the Android documentation. This means that the
     * actual sign in call can be private, while still allowing it to be called from a public button
     * press.
     */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.googleSignInButton:
                signIn();
                break;
            case R.id.logoutButton:
                signOut();
                break;
            case R.id.revokeButton:
                revokeAccess();
                break;
        }
    }
}
