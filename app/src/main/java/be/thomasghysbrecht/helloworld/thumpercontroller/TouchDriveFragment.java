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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TouchDriveFragment extends Fragment implements View.OnTouchListener {

    private View mView;
    private ProgressBar leftProgBar, rightProgBar;
    private ThumperController thumperController;
    private String address = "";
    private String port = "";
    SharedPreferences sharedPreferences;
    Handler handler = new Handler();

    private float X_MIN = 180;
    private float X_MAX = 580;
    private float Y_MIN = 240;
    private float Y_MAX = 640;
    private float DEADZONE = 30; //NOT YET IMPLEMENTED

    private float rightPerc, leftPerc;

    //Random Stuff
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public TouchDriveFragment() {}

    public static TouchDriveFragment newInstance(String param1, String param2) {
        TouchDriveFragment fragment = new TouchDriveFragment();
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

        mView =  inflater.inflate(R.layout.fragment_touch_drive, container, false);

        mView.setOnTouchListener(this);

        leftProgBar = mView.findViewById(R.id.leftProgBar);
        rightProgBar = mView.findViewById(R.id.rightProgBar);

        leftProgBar.setProgress(50);
        leftProgBar.setMax(100);
        leftProgBar.setIndeterminate(false);
        rightProgBar.setProgress(50);
        rightProgBar.setMax(100);
        rightProgBar.setIndeterminate(false);


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
        float currentX = event.getX();
        float currentY = event.getY();
        float percX = getPercentage(X_MIN,X_MAX,currentX);
        float percY = -1 * getPercentage(Y_MIN,Y_MAX,currentY);
        leftPerc = calcLeftTirePerc(percX, percY);
        rightPerc = calcRightTirePerc(percX,percY);

        rightProgBar.setProgress((int)(((rightPerc / 2.0) + 0.5) * 100));
        leftProgBar.setProgress((int)(((leftPerc / 2.0) + 0.5) * 100));

        Log.e("THUMP","Touched! Coordinates: X: " + currentX + " Y: " + currentY);
        Log.e("THUMP","Touched! Percentages: X: " + percX + " Y: " + percY);
        Log.e("THUMP","Touched! Tire Percen: L: " + leftPerc + " R: " + rightPerc);

        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            handler.post(periodicSend);
        } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
            rightProgBar.setProgress(50);
            leftProgBar.setProgress(50);
            handler.removeCallbacks(periodicSend);
            thumperController.stop();
        }

        return true;
    }

    private Runnable periodicSend = new Runnable() {
        @Override
        public void run() {
            thumperController.drivePercentages(leftPerc, rightPerc);
            handler.postDelayed(this, 100);
        }
    };


    public float getPercentage(float minVal, float maxVal, float curVal){
        if(curVal<minVal)curVal = minVal;
        if(curVal>maxVal)curVal = maxVal;
        return (float) ((((curVal-minVal)/(maxVal-minVal))-0.5)*2);
    }

    public float calcLeftTirePerc(float xPercentage, float yPercentage){
        if(xPercentage > 0) xPercentage = 0;
        else xPercentage = -1 * xPercentage;
        //Now we have a value between 0 and 1 that says how hard we are turning left
        xPercentage = (float)(-1 * (xPercentage - 0.5)*2.0); // 1 = no left turn, -1 = hard left turn

        return yPercentage * xPercentage;
    }

    public float calcRightTirePerc(float xPercentage, float yPercentage){
        if(xPercentage < 0) xPercentage = 0;
        else xPercentage = xPercentage;
        //Now we have a value between 0 and 1 that says how hard we are turning left
        xPercentage = (float)(-1 * (xPercentage - 0.5)*2.0); // 1 = no left turn, -1 = hard left turn

        return yPercentage * xPercentage;
    }

}
