package com.rocket.biometrix.Login;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by TJ on 1/24/2016.
 * This class is designed to hold the shared preferences for each user account. Since each user can
 * have different preferences, the file will be named after the username.
 *
 * Since only one user can be logged in at a time, this will be a singleton.
 */
public class LocalAccount
{
    //Reference variables for information needed by shared preferences
    private static final int PREFERENCE_PRIVATE_MODE = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;
    private Context currentContext;

    //The reference back to itself
    private static LocalAccount _instance;

    //The username string is used as the filename for the shared preferences
    private static String username;

    //The username that was most recently used for a preference file
    private String preferenceUsername;

    //A google account for the current sign in. This way it can be accessed from wherever it is needed
    private static GoogleSignInAccount googleAccount;

    //A token signed by the webserver to ensure the user is currently valid
    private String webServerToken;

    /**
     * Creates the LocalAccount. Private since this is a singleton
     * @param jsonToken The token that was passed back by the webserver
     */
    private LocalAccount(String jsonToken)
    {
        webServerToken = jsonToken;
    }

    /**
     * Changes the login information to be the newly logged in user. If another user was logged in,
     * this changes to the new user. This should only be used for regular sign-in, not google sign in
     *
     * @param new_username The username of the newly logged in user.
     * @param jsonToken The token returned by the server when the login was called.
     */
    public static LocalAccount Login(String new_username, String jsonToken)
    {
        //Overrides the current username
        username = new_username;

        //Since a username was specified, this is not a google account.
        googleAccount = null;

        _instance = new LocalAccount(jsonToken);

        return _instance;
    }

    /**
     * Returns whether a google account is currently signed in or not
     * @return True if a google account is signed in, false if not.
     */
    public static boolean isGoogleAccountSignedIn()
    {
        if (googleAccount == null)
            return false;
        else
            return true;
    }

    /**
     * Logs the user in with their google account instead of with their Biometrix account
     * @param googleSignInAccount A reference to the google account that will be held
     * @return A reference to the account that was logged in
     */
    public static LocalAccount Login(GoogleSignInAccount googleSignInAccount, String jsonToken)
    {
        //If there is no google account signed in, sign the user in. If the currently logged in account
        //is the same as the one being logged in, do nothing
        if (googleAccount == null || !(googleAccount.getIdToken().equals(googleSignInAccount.getIdToken() ) ) )
        {
            //Sets the static fields before creating the login
            googleAccount = googleSignInAccount;
            username = googleSignInAccount.getId();

            _instance = new LocalAccount(jsonToken);
        }


        return _instance;
    }

    /**
     * Retrieves a reference to the currently logged in local account
     * @return A reference to the local account, throws NullPointerException if login was not called
     * first
     */
    public static LocalAccount GetInstance() throws NullPointerException
    {
        if (_instance == null)
            throw new NullPointerException("Login not called for LocalAccount");

        return _instance;
    }

    /**
     * Logs the current user out of the system. This means that login will have to be called before
     * get instance is valid again
     */
    public static void Logout()
    {
        _instance = null;

        googleAccount = null;

        username = null;
    }

    /**
     * Returns the username of the user who is currently logged in.
     *
     * @return
     */
    public String GetUsername()
    {
        return username;
    }

    /**
     * Returns the currently logged in user's token.
     * @return A string containing the user's token
     */
    public String GetToken() { return webServerToken; }

    /**
     * Sets up the shared preferences for the current user and context to allow reading or writing
     * @param context The current context
     */
    private void setupPreferences(Context context)
    {
        if (currentContext != context || !preferenceUsername.equals(username))
        {
            currentContext = context;
            sharedPreferences = currentContext.getSharedPreferences(username, PREFERENCE_PRIVATE_MODE);
            preferenceEditor = sharedPreferences.edit();
            preferenceUsername = username;
        }
    }

    /**
     * Determines if the currently logged in user has the diet module enabled
     * @param context The current context that is calling. (Application.getContext() )
     * @return Defaults to true, if user has set the dietEnabled key to false, returns false
     */
    public boolean isDietEnabled(Context context)
    {
        setupPreferences(context);
        return sharedPreferences.getBoolean("dietEnabled", true);
    }

    /**
     * Sets the shared parameter for the diet module being enabled/disabled
     * @param context The current context
     * @param enabled Whether to enable or disable the module
     */
    public void changeDietStatus(Context context, boolean enabled)
    {
        setupPreferences(context);
        preferenceEditor.putBoolean("dietEnabled", enabled).commit();
    }

    /**
     * Determines if the currently logged in user has the sleep module enabled
     * @param context The current context that is calling. (Application.getContext() )
     * @return Defaults to true, if user has set the sleepEnabled key to false, returns false
     */
    public boolean isSleepEnabled(Context context)
    {
        setupPreferences(context);
        return sharedPreferences.getBoolean("sleepEnabled", true);
    }

    /**
     * Sets the shared parameter for the sleep module being enabled/disabled
     * @param context The current context
     * @param enabled Whether to enable or disable the module
     */
    public void changeSleepStatus(Context context, boolean enabled)
    {
        setupPreferences(context);
        preferenceEditor.putBoolean("sleepEnabled", enabled).commit();
    }

    /**
     * Determines if the currently logged in user has the medication module enabled
     * @param context The current context that is calling. (Application.getContext() )
     * @return Defaults to true, if user has set the medicationEnabled key to false, returns false
     */
    public boolean isMedicationEnabled(Context context)
    {
        setupPreferences(context);
        return sharedPreferences.getBoolean("medicationEnabled", true);
    }

    /**
     * Sets the shared parameter for the medication module being enabled/disabled
     * @param context The current context
     * @param enabled Whether to enable or disable the module
     */
    public void changeMedicationStatus(Context context, boolean enabled)
    {
        setupPreferences(context);
        preferenceEditor.putBoolean("medicationEnabled", enabled).commit();
    }

    /**
     * Determines if the currently logged in user has the exercise module enabled
     * @param context The current context that is calling. (Application.getContext() )
     * @return Defaults to true, if user has set the exerciseEnabled key to false, returns false
     */
    public boolean isExerciseEnabled(Context context)
    {
        setupPreferences(context);
        return sharedPreferences.getBoolean("exerciseEnabled", true);
    }

    /**
     * Sets the shared parameter for the exercise module being enabled/disabled
     * @param context The current context
     * @param enabled Whether to enable or disable the module
     */
    public void changeExerciseStatus(Context context, boolean enabled)
    {
        setupPreferences(context);
        preferenceEditor.putBoolean("exerciseEnabled", enabled).commit();
    }


}
