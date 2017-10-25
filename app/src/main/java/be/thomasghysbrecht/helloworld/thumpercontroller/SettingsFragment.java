package be.thomasghysbrecht.helloworld.thumpercontroller;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button settingsButton;
    private View mView;
    private TextView settingsText;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public SettingsFragment() {}

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("THUMP", "Fragment created!");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_settings, container, false);

        settingsButton = (Button)mView.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);

        settingsText = (TextView)mView.findViewById(R.id.settingsPreviewText);
        setSettingsText(settingsText);

        return mView;
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

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.settingsButton:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
    }

    public void setSettingsText(TextView settingsTxt){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String address = sharedPreferences.getString((SettingsActivity.NODEJS_SERVER_IP),"10.0.0.1");
        String port =  sharedPreferences.getString((SettingsActivity.NODEJS_SERVER_PORT),"3000");

        StringBuffer txt = new StringBuffer("");
        txt.append("THUMPER:\n\n");
        txt.append("IP ADDRESS:\n");
        txt.append(address);
        txt.append("\n\nPORT\n");
        txt.append(port);

        settingsTxt.setText(txt);
    }

    @Override
    public void onResume() {
        super.onResume();
        setSettingsText(settingsText);
    }
}
