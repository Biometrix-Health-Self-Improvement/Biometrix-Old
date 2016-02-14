package com.rocket.biometrix.biometrix.Login;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TJ on 1/24/2016.
 * This class is designed to hold the shared preferences for each user account. Since each user can
 * have different preferences, the file will be named after the username.
 *
 * Since only one user can be logged in at a time, this will be a singleton.
 */
public class LocalAccount
{
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

    /**
     * Creates the LocalAccount. Private since this is a singleton
     */
    private LocalAccount()
    {     }

    /**
     * Changes the login information to be the newly logged in user. If another user was logged in,
     * this changes to the new user
     *
     * @param new_username The username of the newly logged in user.
     */
    public static LocalAccount Login(String new_username)
    {
        //Updates the shared preferences and username if the new username is not the same one
        username = new_username;


        if (_instance == null) {
            _instance = new LocalAccount();
        }

        return _instance;
    }

    /**
     * Retrieves a reference to the currently logged in local account
     *
     * @return A reference to the local account, throws null if login was not called first
     */
    public static LocalAccount GetInstance() throws NullPointerException
    {
        if (_instance == null)
            throw new NullPointerException("Login not called for LocalAccount");

        return _instance;
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
