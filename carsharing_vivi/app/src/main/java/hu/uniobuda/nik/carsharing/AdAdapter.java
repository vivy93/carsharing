package hu.uniobuda.nik.carsharing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.HandleJSON;


public class AdAdapter extends BaseAdapter {

    private HandleJSON obj;
    private boolean notLunched = true;

    public Integer jsonParser(String finalUrl) {
        obj = new HandleJSON(finalUrl);
        obj.fetchJSON();
        while (obj.parsingComplete) ;
        return obj.getDistance();
    }

    private void relevantAdsOnFoot(/*user id kell majd h a saját hirdetéseimet ne listázza,*/
                                   Date date, String fromid, List<Advertisement> adsList/*, List<Advertisement> adListFull*/) {
        List<Advertisement> adListInt = new ArrayList<>();

        for (int i = 0; i < adsList.size(); i++) {// kiválogatom azokat a hirdetéseket amik 24 órán belül indulnak

            long walkerdate = date.getTime();
            long bycarDatee=adsList.get(i).getWhen().getTime();
            if (walkerdate<=bycarDatee) {
                if ((walkerdate + 86400000)>=bycarDatee)// aznap = akkor vagy utánna-> refactorálás később
                {
                    adListInt.add(adsList.get(i));
                }
            }
        }
        String url = null;
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        String place_id = "place_id:";
        //place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ
        String urlEnd = "&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s";
        for (int i = 0; i < adListInt.size(); i++) {
            // csekkolni kell h nincs null értékű id !
            url = "";
            url = urlStart + place_id + fromid +
                    "&destinations=" + place_id + adListInt.get(i).getFromID()
                    + "|" + place_id + adListInt.get(i).getNode1ID() +
                    "|" + place_id + adListInt.get(i).getNode2ID() + urlEnd;

            adListInt.get(i).setDistance(jsonParser(url));
        }
        for (int i = 0; i < adListInt.size(); i++) {
            if (adListInt.get(i).getDistance() > 10000) {
                adListInt.remove(i);
                i = 0;
            }
        }


        Collections.sort(adListInt, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }
        });

        this.adsList.clear();
        this.adsList = adListInt;
        notLunched = false;// mert már 1szer lefutott
    }


    private List<Advertisement> adsList;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public AdAdapter(List<Advertisement> AdsList) {
        this.adsList = AdsList;
    }

    @Override
    public int getCount() {
        if (adsList.size() != 0 && notLunched == true)
            relevantAdsOnFoot(AdsListFragment.travelDate, AdsListFragment.travelFromID, adsList);

        return adsList != null ? adsList.size() : 0;
    }

    @Override
    public Advertisement getItem(int i) {
        return adsList != null ? adsList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView adView = (TextView) view;

        if (adView == null)
        {
            adView= (TextView) View.inflate(viewGroup.getContext(),R.layout.listitem_ad, null);
        }
        Advertisement ad = getItem(i);

        adView.setText("DEPARTURE: " + ad.getFrom() + ", " + sdf.format(ad.getWhen()) + "\nDESTINATION: " + ad.getTo());


        return adView;
    }
}
