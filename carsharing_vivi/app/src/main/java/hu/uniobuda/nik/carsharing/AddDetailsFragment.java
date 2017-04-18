package hu.uniobuda.nik.carsharing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AddDetailsFragment extends Fragment {
    View rootView;

    public static AddDetailsFragment newInstance(Add selectedAdd) {

        Bundle args = new Bundle();
        args.putParcelable("selected_add", selectedAdd);


        return newInstance(args);
    }

    public static AddDetailsFragment newInstance(Bundle args) {

        AddDetailsFragment fragment = new AddDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ///TODO
        TextView nicNameView = (TextView) rootView.findViewById(R.id.nickName);
        TextView rateView = (TextView) rootView.findViewById(R.id.rate);
        //TextView fromView= (TextView) rootView.findViewById(R.id.from);
        //TextView toView = (TextView) rootView.findViewById(R.id.to);
        //TextView whenView = (TextView) rootView.findViewById(R.id.when);
        //TextView freeSeatsView = (TextView) rootView.findViewById(R.id.freeSeats);


        if (getArguments()!=null)
        {
            Bundle args = getArguments();
            if (args.containsKey("selected_add"))
            {
                Add selectedAdd = args.getParcelable("selected_add");
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
