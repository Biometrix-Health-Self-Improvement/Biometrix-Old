package com.rocket.biometrix.Login;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.rocket.biometrix.Database.AsyncResponse;
import com.rocket.biometrix.Database.DatabaseConnect;
import com.rocket.biometrix.Database.DatabaseConnectionTypes;
import com.rocket.biometrix.NavigationDrawerActivity;
import com.rocket.biometrix.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GetLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GetLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetLogin extends Fragment implements AsyncResponse {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    private String returnResult;

    private String username;
    private String password;
    private String email;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GetLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetLogin.
     */
    public static GetLogin newInstance(String param1, String param2) {
        GetLogin fragment = new GetLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try{
            NavigationDrawerActivity nav = (NavigationDrawerActivity) getActivity();
            //Change the title of the action bar to reflect the current fragment
            nav.setActionBarTitleFromFragment(R.string.action_bar_title_login);
            //set activities active fragment to this one
            nav.activeFragment = this;
        } catch (Exception e){}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_get_login, container, false);


        return v;
    }




    /**
     * Calls the database to check the login for the user
     * @param view
     */
    public void okayButtonClick(View view)
    {
        EditText usernameEdit =  (EditText) v.findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) v.findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        if (username.equals("") || password.equals("") )
        {
            Toast.makeText(v.getContext(), "Username or password is blank", Toast.LENGTH_LONG);
        }
        else
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_CHECK,username, password);
        }
    }


    public void resetPasswordClick()
    {
        EditText usernameEdit =  (EditText) v.findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText emailEdit = (EditText) v.findViewById(R.id.loginEnterEmailEditText);
        email = emailEdit.getText().toString();

        if (username.equals("") || email.equals("") )
        {
            Toast.makeText(v.getContext(), "Username or email is blank, both are required to identify you", Toast.LENGTH_LONG).show();
        }
        else
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_RESET, username, email);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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
            Toast.makeText(v.getContext(), returnResult, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                if (jsonObject.has("Error"))
                {
                    Toast.makeText(v.getContext(), jsonObject.getString("Error"), Toast.LENGTH_LONG).show();
                }
                //If the operation succeeded
                else if ((Boolean)jsonObject.get("Verified") )
                {
                    //If the json object passes back a token then it was a login
                    if ( jsonObject.has("Token"))
                    {
                        Toast.makeText(v.getContext(), "Login Successful!", Toast.LENGTH_LONG).show();

                        //Logs the user in with their login token.
                        LocalAccount.Login(username, jsonObject.getString("Token"));

                        getActivity().getFragmentManager().popBackStack();
                    }
                    else
                    //Assume it was a password reset
                    {
                        Toast.makeText(v.getContext(), "Check your email (and your spam folder) for your reset link", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(v.getContext(), "Login failed", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException jsonExcept)
            {
                Toast.makeText(v.getContext(), "Something went wrong with the server's return", Toast.LENGTH_LONG).show();
            }
        }

    }
}
