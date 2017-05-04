package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class AdsListFragment extends Fragment {
    private static final String TAG = "AdListFragment";

    View rootView;

    public static AdsListFragment newInstance()
    {
        Bundle args = new Bundle();

        AdsListFragment fragment = new AdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_ads_list,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Advertisement> adList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateInString = "30-04-2017 10:20:00";
        Date date;

        try {
            date = sdf.parse(dateInString);
            Log.d(TAG, "trying to parse date");
            Random random = new Random();

            for(int i =0; i<10; i++) {// Teszt adatok -> van megjelenítés
                Log.d(TAG, "add element to advertisement list");
                adList.add(new Advertisement(/*"1",*/ TravelMode.ON_FOOT, date, "Debrecen", "Budapest", "Debrecen", "Budapest", random.nextInt(9) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.ON_FOOT, date, "Budapest, Csontváry utca", "Jászfelsőszentgyörgy", "Debrecen", "Budapest", random.nextInt(9) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.ON_FOOT, date, "Budapest, Kosztka Tivadar utca", "Jászfelsőszentgyörgy", "Debrecen", "Budapest", random.nextInt(9) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Budaörs, Szabadság út", "Bakony",  "Debrecen", "Budapest",random.nextInt(9) ));
            }

            final AdAdapter adapter = new AdAdapter(adList);
            ListView listView = (ListView) rootView.findViewById(R.id.ads_lstview);
            listView.setAdapter(adapter);

            // klikk egy listaelemre: új DetailsActivity()
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Advertisement selectedAd = adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("selected_ad", selectedAd);
                    startActivity(intent);
                }
            });

        } catch (ParseException e) {
            // a catch blokk az  sdf.parse miatt kell, de ez majd úgyis eltűnik ha valós adatokkal dolgozunk a DB-ből
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }

        //vege
    }
}
