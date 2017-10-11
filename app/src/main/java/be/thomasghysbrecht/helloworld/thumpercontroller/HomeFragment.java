package be.thomasghysbrecht.helloworld.thumpercontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {
    //All my own Variables and stuff
    private NeopixelsController neopixelsController;
    private String address = "";
    private String pixelId = "";
    private String port = "";
    private SeekBar redBar, greenBar, blueBar, delayBar;
    private Switch strobeToggle;
    private Button ledButton;
    private View mView;
    private TextView delayText;
    SharedPreferences sharedPreferences;

    //Random Crap
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public HomeFragment() {}

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    //Important function!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_home, container, false);
        redBar = ((SeekBar)mView.findViewById(R.id.redBar));
        greenBar = ((SeekBar)mView.findViewById(R.id.greenBar));
        blueBar = ((SeekBar)mView.findViewById(R.id.blueBar));
        delayBar = ((SeekBar)mView.findViewById(R.id.delayBar));
        strobeToggle = ((Switch)mView.findViewById(R.id.strobeToggle));
        delayText = ((TextView)mView.findViewById(R.id.delayText));

        ledButton = (Button)mView.findViewById(R.id.sendLedButton);
        ledButton.setOnClickListener(this);

        delayBar.setOnSeekBarChangeListener(this);
        blueBar.setOnSeekBarChangeListener(this);
        greenBar.setOnSeekBarChangeListener(this);
        redBar.setOnSeekBarChangeListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        address = sharedPreferences.getString((SettingsActivity.NODEJS_SERVER_IP),"10.0.0.1");
        port =  sharedPreferences.getString((SettingsActivity.NODEJS_SERVER_PORT),"3000");
        pixelId = sharedPreferences.getString((SettingsActivity.NEOPIXEL_STRING_ID),"0");
        address = "http://"+address+":"+port+"/";

        neopixelsController = new NeopixelsController(address);

        //return inflater.inflate(R.layout.fragment_home, container, false);
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
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
            case R.id.sendLedButton:
                    onBasicLedButtonClick();
                break;
        }
    }

    public void onBasicLedButtonClick(){
        int red = redBar.getProgress();
        int green = greenBar.getProgress();
        int blue = blueBar.getProgress();
        int delay = delayBar.getProgress();
        if(strobeToggle.isChecked()) neopixelsController.setStrobeColor(red, green, blue, delay, pixelId);
        else neopixelsController.setColor(red, green, blue, pixelId);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == delayBar) {
            if (progress < 50)delayBar.setProgress(50);
        }
        delayText.setText(delayBar.getProgress() + " ms");
        onBasicLedButtonClick();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar){}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar){}

}
