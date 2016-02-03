package com.rocket.biometrix;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;


/**
 * The activity to retrieve a user's login information (username and password) and verify it against
 * the database. It implements AsyncResponse to allow a response from an activity that is called
 * asynchronously, OnConnectionFailedListener to allow a message to be displayed if Google Services
 * cannot be contacted, and
 */
public class GetLoginActivity extends AppCompatActivity implements AsyncResponse,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
{
    //One string for each of the possible fields
    private String returnResult;
    private String username;
    private String password;
    private String email;

    //Google account variables
    //A reference to the Google API
    private GoogleApiClient googleApiClient;
    //A status bar in essence
    private ProgressDialog progressDialog;
    //A tag that is used in debug logging
    private static final String TAG = "GetLoginActivity";
    //The number that corresponds to the google API "sign-in" activity
    private static final int RC_SIGN_IN = 9001;

    @Override
    /**
     * Initializes the starting state for the activity. Also, prepares google account validation
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        //Sets the content for the page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = null;
        password = null;
        returnResult = "";


        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        //Since the action bar has the possibility of throwing nullpointer exception, catch it and
        //do nothing with it
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(java.lang.NullPointerException except)
        {
            //Do nothing
        }

        //Creates the google sign in object with requests for email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    //GOOGLE ACCOUNT SPECIFIC CODE BEGIN
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
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);

            Toast.makeText(getApplicationContext(), "Google sign in succeeded!", Toast.LENGTH_LONG).show();
        }
        else
        {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    /**
     * Makes the buttons appear or disappear as needed. If the user is signed in, the sign-in button
     * disappears. If the user is signed out it re-appears.
     * @param signedIn A boolean for whether the user is currently signed in or not
     */
    private void updateUI(boolean signedIn)
    {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        }
        else
        {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
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
     * Creates a progress dialog if it does not exist, and then shows it
     */
    private void showProgressDialog()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.login_loading_string));
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
     * actual sign in code can be private, while still allowing it to be called from a public button
     * press.
     */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.sign_in_button:
                showProgressDialog();
                signIn();
                hideProgressDialog();
                break;
            /*case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;*/
        }
    }


    //GOOGLE ACCOUNT SPECIFIC CODE END



    /**
     * Calls the database to check the login for the user
     * @param view
     */
    public void okayButtonClick(View view)
    {
        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        if (username.equals("") || password.equals("") )
        {
            Toast.makeText(getApplicationContext(), "Username or password is blank", Toast.LENGTH_LONG).show();
        }
        else
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_CHECK,username, password);
        }
    }

    /**
     * Calls the database to check the login for the user
     * @param view
     */
    public void resetButtonClick(View view)
    {
        EditText usernameEdit =  (EditText) findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText emailEdit = (EditText) findViewById(R.id.loginEnterEmailEditText);
        email = emailEdit.getText().toString();

        if (username.equals("") || email.equals("") )
        {
            Toast.makeText(getApplicationContext(), "Username or email is blank, both are required to identify you", Toast.LENGTH_LONG).show();
        }
        else
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_RESET, username, email);
        }

    }

    public void cancelButtonClick(View view)
    {
        this.onBackPressed();
    }

    @Override
    /**
     * Retrieves the results of the call to the webserver
     */
    public void processFinish(String result)
    {
        returnResult = result;

        JSONObject jsonObject;

        //Tries to parse the returned result as a json object.
        try
        {
            jsonObject = new JSONObject(returnResult);
        }
        catch (JSONException jsonExcept)
        {
            jsonObject = null;
        }

        //If the return could not be parsed, then it was not a successful login
        if (jsonObject == null)
        {
            Toast.makeText(getApplicationContext(), returnResult, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                //If the operation succeeded
                if ((Boolean)jsonObject.get("Verified") )
                {
                    //If the json object passes back an email address, that means that it was a reset, not a login
                    if ( !jsonObject.has("EmailAddress"))
                    {
                        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();

                        LocalAccount.Login(username);

                        /*//Create's an "intent" to passback user information with keys username and password.
                        Intent dataPassback = new Intent();
                        dataPassback.putExtra("username", username);
                        dataPassback.putExtra("password", password);*/

                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Check your email (and your spam folder) for your reset link", Toast.LENGTH_LONG).show();
                    }

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
}
