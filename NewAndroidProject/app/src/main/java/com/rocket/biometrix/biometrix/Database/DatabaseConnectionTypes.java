package com.rocket.biometrix.biometrix.Database;

/**
 * Created by tannalynn on 1/22/2016.
 */
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

    public static final String CONNECTION_FAIL = "Unable to connect to database";
    public static final String JSON_PARSE_FAIL = "Unable to parse the input JSON";
    public static final String INCORRECT_CREDENTIALS = "Username or password was incorrect";

    private DatabaseConnectionTypes() {}
}