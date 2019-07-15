package com.example.clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2 extends Fragment {
    private static final long START_TIME_MILLIS = 600000;
    private long timeLeftInMills;
    private TextView timer_text;
    private Button reset;
    private FloatingActionButton start;
    private FloatingActionButton pause;
    private CountDownTimer countdownTimer;
    private boolean timerRunning;
    private long endTime;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_tab2, container, false);
        start = inf.findViewById(R.id.timer_start);
        pause = inf.findViewById(R.id.timer_pause);
        reset = inf.findViewById(R.id.reset_btn);
        timer_text = inf.findViewById(R.id.timer_text);

   //     start.hide();
        pause.hide();
        reset.setVisibility(View.INVISIBLE);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

       // updateTimerText();

        return inf;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void startTimer() {
        start.hide();
        pause.show();
        reset.setVisibility(View.VISIBLE);

        endTime = System.currentTimeMillis() + timeLeftInMills;
        countdownTimer = new CountDownTimer(timeLeftInMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMills = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
            }
        }.start();
        timerRunning = true;
    }

    private void pauseTimer(){
        countdownTimer.cancel();
        timerRunning = false;
        pause.hide();
        start.show();
    }

    private void resetTimer(){
        timeLeftInMills = START_TIME_MILLIS;
        countdownTimer.cancel();
        updateTimerText();
        start.show();
        pause.hide();
        reset.setVisibility(View.INVISIBLE);
    }

    private void updateTimerText(){
        int minutes = (int) timeLeftInMills / 1000 / 60;
        int seconds = (int) timeLeftInMills / 1000 % 60;
        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer_text.setText(timeLeft);
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        timeLeftInMills = prefs.getLong("millisLeft", START_TIME_MILLIS);
        timerRunning = prefs.getBoolean("timerRunning", false);
        updateTimerText();

        if (timerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMills = endTime - System.currentTimeMillis();

            if (timeLeftInMills < 0){
                timeLeftInMills = 0;
                timerRunning = false;
                updateTimerText();
            } else {
                startTimer();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", timeLeftInMills);
        editor.putBoolean("timerRunning", timerRunning);
        //editor.putLong("endTime", endTime);

        editor.apply();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
