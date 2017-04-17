package hu.uniobuda.nik.carsharing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hu.uniobuda.nik.carsharing.model.Advertisement;


public class AdAdapter extends BaseAdapter {

    private List<Advertisement> adList;

    public AdAdapter(List<Advertisement> adList){this.adList = adList;}

    @Override
    public int getCount() {return adList !=null ? adList.size():0;}

    @Override
    public Advertisement getItem(int i) { return adList !=null ? adList.get(i): null;}

    @Override
    public long getItemId(int i) {return i;}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView adView = (TextView) view;
        if (adView == null)
        {
            adView= (TextView) View.inflate(viewGroup.getContext(),R.layout.listitem_ad,null);
        }
        Advertisement ad = getItem(i);

        adView.setText(ad.getFrom());

        return adView;
    }
}
