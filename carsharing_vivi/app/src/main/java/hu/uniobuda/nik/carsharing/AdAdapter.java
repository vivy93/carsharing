package hu.uniobuda.nik.carsharing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;


public class AdAdapter extends BaseAdapter {

    private List<Advertisement> adsList;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public AdAdapter(List<Advertisement> AdsList){this.adsList=AdsList;}

    @Override
    public int getCount() {return adsList!=null ? adsList.size():0;}

    @Override
    public Advertisement getItem(int i) { return adsList!=null ? adsList.get(i): null;}

    @Override
    public long getItemId(int i) {return i;}

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
