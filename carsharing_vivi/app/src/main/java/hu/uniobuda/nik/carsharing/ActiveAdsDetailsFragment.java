package hu.uniobuda.nik.carsharing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;

/**
 * Created by Vivi on 2017. 05. 14..
 */

public class ActiveAdsDetailsFragment extends Fragment implements View.OnClickListener{

    View rootView;
    private static final String TAG = "ActiveAdsDetailsFragment";
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); //igy nincs benne a GTM... az idő kiírásban
    Button acceptButton;
    Advertisement selectedAd;

    public static ActiveAdsDetailsFragment newInstance(Bundle args) {

        ActiveAdsDetailsFragment fragment = new ActiveAdsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_active_ads_details, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView nicNameView = (TextView) rootView.findViewById(R.id.name);
        TextView rateView = (TextView) rootView.findViewById(R.id.rate);
        TextView fromView= (TextView) rootView.findViewById(R.id.from);
        TextView toView = (TextView) rootView.findViewById(R.id.to);
        TextView whenView = (TextView) rootView.findViewById(R.id.when);
        TextView freeSeatsView = (TextView) rootView.findViewById(R.id.freeSeats);
        acceptButton = (Button) rootView.findViewById(R.id.acceptButton);

        if (getArguments()!=null)
        {
            Bundle args = getArguments();
            if (args.containsKey("selected_ad"))
            {
                selectedAd = args.getParcelable("selected_ad");
                if (selectedAd!= null) {
                    nicNameView.append("Test Name");
                    // nicNameView.append(String.valueOf(selectedAd.getUid()));  user id-jával kéne gettelni a DB-ből
                    rateView.append("4");
                    //rateView.append(String.valueOf(selectedAd.get)); user id-jával kéne gettelni a DB-ből, ez nem a hirdetéshez kapcsolódik
                    fromView.append(String.valueOf(selectedAd.getFrom()));
                    toView.append(String.valueOf(selectedAd.getTo()));
                    whenView.append(String.valueOf(sdf.format(selectedAd.getWhen())));

                    //Log.d(TAG, String.valueOf(selectedAd.getSeats()));
                    freeSeatsView.append(String.valueOf(selectedAd.getSeats()));
                }
            }
        }
        acceptButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==acceptButton){
            //TODO tárolni a beleegyezést
            //mock
            selectedAd.setChosenUid("Test");

        }
    }
}
