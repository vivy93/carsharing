package hu.uniobuda.nik.carsharing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class AddAdapter extends BaseAdapter {

    private List<Add> addsList;
    public AddAdapter(List<Add> AddsList){this.addsList=AddsList;}

    @Override
    public int getCount() {return addsList!=null ? addsList.size():0;}

    @Override
    public Add getItem(int i) { return addsList!=null ? addsList.get(i): null;}

    @Override
    public long getItemId(int i) {return i;}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView addView = (TextView) view;
        if (addView == null)
        {
            addView= (TextView) View.inflate(viewGroup.getContext(),R.layout.listitem_add,null);
        }
        Add add = getItem(i);
        addView.setText(add.getFrom());

        return addView;
    }
}
