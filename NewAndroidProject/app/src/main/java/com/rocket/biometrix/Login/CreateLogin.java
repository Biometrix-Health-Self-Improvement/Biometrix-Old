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
 * {@link CreateLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLogin extends Fragment  implements AsyncResponse {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String returnResult;

    private String username;
    private String password;
    private String confirmedPassword;
    private String email;

    private String mParam1;
    View v;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateLogin.
     */
    public static CreateLogin newInstance(String param1, String param2) {
        CreateLogin fragment = new CreateLogin();
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
            nav.setActionBarTitleFromFragment(R.string.action_bar_title_create_login);
            //set activities active fragment to this one
            nav.activeFragment = this;
        } catch (Exception e){}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_create_login, container, false);

        return v;
    }


    public void createAccount()
    {
        //Gets username, and email passwords from the edit Text boxes
        EditText usernameEdit = (EditText) v.findViewById(R.id.usernameEditText);
        username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) v.findViewById(R.id.passwordEditText);
        password = passwordEdit.getText().toString();

        EditText passwordConfirmEdit = (EditText) v.findViewById(R.id.confirmPasswordEditText);
        confirmedPassword = passwordConfirmEdit.getText().toString();

        EditText emailEdit = (EditText) v.findViewById(R.id.emailEditText);
        email = emailEdit.getText().toString();

        //Ensures username is not blank
        if (username.equals(""))
        {
            Toast.makeText(v.getContext(), "Username cannot be blank", Toast.LENGTH_LONG).show();
        }
        //Ensures password is not blank
        else if (password.equals(""))
        {
            Toast.makeText(v.getContext(), "Password cannot be blank", Toast.LENGTH_LONG).show();
        }
        //Ensures email is not blank
        else if (email.equals(""))
        {
            Toast.makeText(v.getContext(), "Email cannot be blank", Toast.LENGTH_LONG).show();
        }
        //Ensures that the email address at least appears valid
        else if (!email.contains("@") || !email.contains(".") )
        {
            Toast.makeText(v.getContext(), "Email does not appear valid, please check it", Toast.LENGTH_LONG).show();
        }
        //Calls the database connection if the passwords match
        else if (password.equals(confirmedPassword))
        {
            new DatabaseConnect(this).execute(DatabaseConnectionTypes.LOGIN_CREATE, username, password, email);
        }
        else
        {
            Toast.makeText(v.getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
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


    /**
     * The function that retrieves the results from the webserver asynchronously
     * @param result A string that should be json encoded containing the webserver's results
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

        //If the return could not be parsed, then it was not a successful addition
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
                    Toast.makeText(v.getContext(), "Please check your email (and spam folder)", Toast.LENGTH_LONG).show();
                    getActivity().getFragmentManager().popBackStack();
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
