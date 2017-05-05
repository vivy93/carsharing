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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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

    public static AdsListFragment newInstance(/*user id vagy dupla konstruktor újrafelhasználtó legyen*/)
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

    private List<Advertisement> relevantAdsOnFoot(/*user id kell majd h a saját hirdetéseimet ne listázza TravelMode travelMode,*/
                                                    Date date, String fromid, List<Advertisement> adListFull)
    {
        List<Advertisement> adList = new ArrayList<>();

        for(int i =0; i<adListFull.size(); i++) {// kiválogatom a kocsis hirdetéseket akik aznap indulna mint én
            if ( adListFull.get(i).getMode().equals( TravelMode.BY_CAR))
                if(adListFull.get(i).getWhen().equals(date) || adListFull.get(i).getWhen().after(date))// aznap = akkor vagy utánna-> refactorálás később
                {
                    adList.add(adListFull.get(i));
                }

        }
//praser kell!
        for(int i = 0; i<adList.size();i++)
        {


        }



        return  adList;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Advertisement> adList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateInString = "06-05-2017 10:20:00";
        String dateInString2 = "10-05-2017 10:20:00";
        Date date;
        Date date2;

        try {
            date = sdf.parse(dateInString);
            date2 = sdf.parse(dateInString2);
            Log.d(TAG, "trying to parse date");
            Random random = new Random();

            //for(int i =0; i<10; i++) {// Teszt adatok -> van megjelenítés
                Log.d(TAG, "add element to advertisement list");
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Lurdy Ház", "ChIJDS0Ugd7cQUcRf2iJF_ktiA0", "Debrecen", "Népliget","ChIJ5T0Xg97cQUcRGeP7pI9T0nU","Nagyvárad tér","ChIJ1WyAm-bcQUcRVMj1mLIjBXo", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date2, "Lurdy Ház", "ChIJDS0Ugd7cQUcRf2iJF_ktiA0", "Debrecen", "Népliget","ChIJ5T0Xg97cQUcRGeP7pI9T0nU","Nagyvárad tér","ChIJ1WyAm-bcQUcRVMj1mLIjBXo", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Nyugati pu", "ChIJU05ZAQ3cQUcRf5IgNd3ONqk", "Hernád", "Oktogon","ChIJiXy4eG7cQUcRQOdbiM3lrfQ","Corvin Plaza","ChIJDbyKvffcQUcRCxObD4qECw4", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Budapest Bécsi út 96", "ChIJ8bksZFrZQUcRDF9qdcEH2hI", "Miskolc", "Budapest, Kolosy tér","ChIJc5DTZlbZQUcRipml-U4xDEI","Margit híd, budai hídfő","ChIJYR_27QPcQUcRkT1s8EXCBnU", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Debrecen", "ChIJgyte_ioMR0cRcBEeDCnEAAQ", "Bocskaikert", "node1","node1id","node2","node2id", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.ON_FOOT, date, "Népliget", "ChIJ5T0Xg97cQUcRGeP7pI9T0nU", null,  null, null,null,null,null ));
            //}

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
