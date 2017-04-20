package hu.uniobuda.nik.carsharing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hu.uniobuda.nik.carsharing.model.Advertisement;

public class AdDetailsFragment extends Fragment {
    View rootView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");//igy nincs benne a GTM... az idő kiírásban
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
        TextView nicNameView = (TextView) rootView.findViewById(R.id.name);
        TextView rateView = (TextView) rootView.findViewById(R.id.rate);
        TextView fromView= (TextView) rootView.findViewById(R.id.from);
        TextView toView = (TextView) rootView.findViewById(R.id.to);
        TextView whenView = (TextView) rootView.findViewById(R.id.when);
        TextView freeSeatsView = (TextView) rootView.findViewById(R.id.freeSeats);
//@Patrícia : azt mondtad majd még bővíted meg formázgatod az xls-t is

        if (getArguments()!=null)
        {
            Bundle args = getArguments();
            if (args.containsKey("selected_ad"))
            {
                Advertisement selectedAd = args.getParcelable("selected_ad");
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

    }
}
