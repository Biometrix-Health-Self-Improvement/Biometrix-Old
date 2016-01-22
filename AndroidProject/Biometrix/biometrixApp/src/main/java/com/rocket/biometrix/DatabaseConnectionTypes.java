package com.rocket.biometrix;

/**
 * Created by TJ on 11/1/2015.
 */
//This was the first option I toyed with. I liked it a bit less..
/*public enum DatabaseConnectionTypes
{
    LOGIN_CHECK ("Test User Login"),
    LOGIN_CREATE ("Create User Login");

    private final String text;

    private DatabaseConnectionTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}*/

public final class DatabaseConnectionTypes
{
    public static final String LOGIN_CHECK = "Test User Login";
    public static final String LOGIN_CREATE = "Create User Login";


    private DatabaseConnectionTypes() {}
}
