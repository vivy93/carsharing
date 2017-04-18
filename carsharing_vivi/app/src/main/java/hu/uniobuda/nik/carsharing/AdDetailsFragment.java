package hu.uniobuda.nik.carsharing;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.uniobuda.nik.carsharing.model.Advertisement;

public class AdDetailsFragment extends Fragment {
    View rootView;

    private static final String TAG = "AdDetailsFragment";

    public static AdDetailsFragment newInstance(Advertisement selectedAd) {

        Bundle args = new Bundle();
        args.putParcelable("selected_ad", selectedAd);


        return newInstance(args);
    }

    public static AdDetailsFragment newInstance(Bundle args) {

        AdDetailsFragment fragment = new AdDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ad_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TextView nicNameView = (TextView) rootView.findViewById(R.id.nickName);
        //TextView rateView = (TextView) rootView.findViewById(R.id.rate);
        TextView fromView= (TextView) rootView.findViewById(R.id.from);
        TextView toView = (TextView) rootView.findViewById(R.id.to);
        //TextView whenView = (TextView) rootView.findViewById(R.id.when);
        TextView freeSeatsView = (TextView) rootView.findViewById(R.id.freeSeats);


        if (getArguments()!=null)
        {
            Bundle args = getArguments();
            if (args.containsKey("selected_ad"))
            {
                Advertisement selectedAd = args.getParcelable("selected_ad");
                if (selectedAd!= null) {
                    //nicNameView.setText("bla bla");
                    //rateView.setText("5/4.5");
                    String temp ="From: "+selectedAd.getFrom().toString();
                    fromView.setText(temp);
                    temp ="To: "+selectedAd.getTo().toString();
                    toView.setText(temp);
                    //temp = selectedAd.getWhen().toString();
                    //whenView.setText(temp);
                    // temp="Free seats:"+ selectedAd.getSeats().toString();
                    Log.d(TAG, temp);
                    freeSeatsView.setText("55");
                }
            }
        }

    }
}
