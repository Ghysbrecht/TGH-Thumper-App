package be.thomasghysbrecht.helloworld.thumpercontroller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class DriveFragment extends Fragment implements View.OnClickListener {

    private ImageButton upButton, downButton, leftButton, rightButton, hornButton, stopButton;
    private View mView;
    private ThumperController thumperController;
    private String address = "http://10.1.25.45:3000/";

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
        upButton.setOnClickListener(this);
        downButton = (ImageButton)mView.findViewById(R.id.downButton);
        downButton.setOnClickListener(this);
        rightButton = (ImageButton)mView.findViewById(R.id.rightButton);
        rightButton.setOnClickListener(this);
        leftButton = (ImageButton)mView.findViewById(R.id.leftButton);
        leftButton.setOnClickListener(this);
        hornButton = (ImageButton)mView.findViewById(R.id.hornButton);
        hornButton.setOnClickListener(this);
        stopButton = (ImageButton)mView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);

        thumperController = new ThumperController(address);

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
    public void onClick(View v){
        switch(v.getId()){
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
            case R.id.hornButton:
                break;
        }
    }


}
