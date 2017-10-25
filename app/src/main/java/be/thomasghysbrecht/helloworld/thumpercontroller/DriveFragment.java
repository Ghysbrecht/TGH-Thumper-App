package be.thomasghysbrecht.helloworld.thumpercontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DriveFragment extends Fragment implements View.OnTouchListener {

    private ImageButton upButton, downButton, leftButton, rightButton, hornButton, stopButton;
    private View mView;
    private ThumperController thumperController;
    private String address = "";
    private String port = "";
    private int currentCommand;
    SharedPreferences sharedPreferences;
    Handler handler = new Handler();
    private TextView batteryText;

    //Random Stuff
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public DriveFragment() {}

    public static DriveFragment newInstance(String param1, String param2) {
        DriveFragment fragment = new DriveFragment();
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

    //Important!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_drive, container, false);

        upButton = (ImageButton)mView.findViewById(R.id.upButton);
        upButton.setOnTouchListener(this);
        downButton = (ImageButton)mView.findViewById(R.id.downButton);
        downButton.setOnTouchListener(this);
        rightButton = (ImageButton)mView.findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(this);
        leftButton = (ImageButton)mView.findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(this);
        hornButton = (ImageButton)mView.findViewById(R.id.hornButton);
        hornButton.setOnTouchListener(this);
        stopButton = (ImageButton)mView.findViewById(R.id.stopButton);
        stopButton.setOnTouchListener(this);

        batteryText = (TextView)mView.findViewById(R.id.batteryText);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        address = sharedPreferences.getString((SettingsActivity.NODEJS_SERVER_IP),"10.0.0.1");
        port =  sharedPreferences.getString((SettingsActivity.NODEJS_SERVER_PORT),"3000");
        address = "http://"+address+":"+port+"/";
        String stringMax = sharedPreferences.getString((SettingsActivity.THUMPER_MAX_SPEED),"100");
        int max = 100;
        try{
            max=Integer.parseInt(stringMax);
        }catch(NumberFormatException ex){}

        thumperController = new ThumperController(address, max);

        //return inflater.inflate(R.layout.fragment_drive, container, false);
        return mView;
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Important from this point
    @Override
    public boolean onTouch(View v, MotionEvent event){
        currentCommand = v.getId();

        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            switch(currentCommand){
                case R.id.hornButton:
                    thumperController.buzzerOn();
                    break;
                case R.id.stopButton:
                    thumperController.stop();
                    break;
                default:
                    handler.post(periodicSend);
                    break;
            }
        } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
            if(currentCommand == R.id.hornButton) thumperController.buzzerOff();
            currentCommand = R.id.stopButton;
            handler.removeCallbacks(periodicSend);
            thumperController.stop();
        }
        batteryText.setText(Float.toString(thumperController.getThumpVoltage()) + "v");
        return true;
    }

    private Runnable periodicSend = new Runnable() {
        @Override
        public void run() {
            switch(currentCommand){
                case R.id.upButton:
                    thumperController.driveForward();
                    break;
                case R.id.rightButton:
                    thumperController.goRight();
                    break;
                case R.id.leftButton:
                    thumperController.goLeft();
                    break;
                case R.id.downButton:
                    thumperController.driveBackward();
                    break;
                case R.id.stopButton:
                    thumperController.stop();
                    break;
            }
            handler.postDelayed(this, 100);
            batteryText.setText(Float.toString(thumperController.getThumpVoltage()) + "v");
        }
    };


}
