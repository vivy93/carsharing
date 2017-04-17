package hu.uniobuda.nik.carsharing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.uniobuda.nik.carsharing.model.Advertisement;

public class AdDetailsFragment extends Fragment {

    View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_ad_details, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ///TODO
        TextView nicNameView = (TextView) rootView.findViewById(R.id.name);
        TextView rateView = (TextView) rootView.findViewById(R.id.rating);
        //TextView fromView= (TextView) rootView.findViewById(R.id.from);
        //TextView toView = (TextView) rootView.findViewById(R.id.to);
        //TextView whenView = (TextView) rootView.findViewById(R.id.when);
        //TextView freeSeatsView = (TextView) rootView.findViewById(R.id.freeSeats);


        if (getArguments()!=null)
        {
            Bundle args = getArguments();
            if (args.containsKey("selected_ad"))
            {
                Advertisement selectedAdd = args.getParcelable("selected_ad");

                if (selectedAdd!= null) {

                    nicNameView.setText("Józsi99");
                    rateView.setText("5/4.5");
                    //fromView.setText(selectedAdd.getFrom());
                    //toView.setText(selectedAdd.getTo());
                    //whenView.setText(selectedAdd.getWhen().toString());
                    //freeSeatsView.setText(selectedAdd.getFreeSeats());
                }
            }
        }

    }
}
