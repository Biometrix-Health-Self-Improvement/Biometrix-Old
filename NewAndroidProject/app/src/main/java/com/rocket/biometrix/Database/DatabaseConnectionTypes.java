package com.rocket.biometrix.Database;

/**
 * Created by Troy Riblett, troy.riblett@oit.edu
 * DatabaseConnectionTypes
 * This class is a container class to test which type of operation should be performed by the
 * database
 */
public final class DatabaseConnectionTypes
{
    //References to different operations for the database
    public static final String LOGIN_CHECK = "Test User Login";
    public static final String LOGIN_CREATE = "Create User Login";
    public static final String LOGIN_DELETE = "Delete User Login";
    public static final String LOGIN_RESET = "Reset User Password";
    public static final String GOOGLE_TOKEN = "Verify Google Token";

    public static final String CONNECTION_FAIL = "Unable to connect to database";


    private DatabaseConnectionTypes() {}
}
