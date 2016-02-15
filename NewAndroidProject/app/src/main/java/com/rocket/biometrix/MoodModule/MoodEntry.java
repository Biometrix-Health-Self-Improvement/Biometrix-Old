package com.rocket.biometrix.MoodModule;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rocket.biometrix.Common.DateTimeSelectorPopulateTextView;
import com.rocket.biometrix.NavigationDrawerActivity;
import com.rocket.biometrix.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoodEntry.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoodEntry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodEntry extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MoodEntry() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoodEntry.
     */
    public static MoodEntry newInstance(String param1, String param2) {
        MoodEntry fragment = new MoodEntry();
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
            nav.setActionBarTitleFromFragment(R.string.action_bar_title_mood_entry);
            //set activities active fragment to this one
            nav.activeFragment = this;
        } catch (Exception e){}



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_entry, container, false);

        setRatingBarListener(view);

        DateTimeSelectorPopulateTextView popDateTime = new DateTimeSelectorPopulateTextView
                (getActivity(), view, R.id.moodCreateEntryDateSelect, R.id.moodCreateEntryTimeSelect);
        popDateTime.Populate();

        return view;
    }


    /**************************************************************************
     * Sets on rating bar listener to change the description text for the rating bar
     * @param view the view the rating bar is in
     **************************************************************************/
    private void setRatingBarListener(final View view) {
        //Depression
        SeekBar rating = (SeekBar)view.findViewById(R.id.moodDepressedRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) view.findViewById(R.id.moodDepressedDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

        //Elevated
        rating = (SeekBar)view.findViewById(R.id.moodElevatedRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) view.findViewById(R.id.moodElevatedDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

        //Irritability
        rating = (SeekBar) view.findViewById(R.id.moodIrritabilityRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) view.findViewById(R.id.moodIrritabilityDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

        //Anxiety
        rating = (SeekBar) view.findViewById(R.id.moodAnxietyRating);//the rating bar
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView desc = (TextView) view.findViewById(R.id.moodAnxietyDesc);//description of rating
                setRatingLabel(desc, progress);
            }
        });

    }

    private void setRatingLabel(TextView desc, int prog){
        String str = null;
        switch (prog) { //get string based on rating
            case 0: //none
                str = getResources().getString(R.string.mood_rating_none);
                break;
            case 1: //mild
                str = getResources().getString(R.string.mood_rating_mild);
                break;
            case 2: //moderate
                str = getResources().getString(R.string.mood_rating_mod);
                break;
            case 3: //severe
                str = getResources().getString(R.string.mood_rating_sev);
                break;
            case 4: //very severe
                str = getResources().getString(R.string.mood_rating_vsev);
                break;
        }
        desc.setText(str);
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



}
