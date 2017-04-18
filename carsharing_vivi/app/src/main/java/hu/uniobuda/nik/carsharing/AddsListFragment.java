package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddsListFragment extends Fragment {

    View rootView;

    public static AddsListFragment newInstance()
    {
        Bundle args = new Bundle();

        AddsListFragment fragment = new AddsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_add_list,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Random random = new Random();//a hirdetéseket generálja le de ezt majd kiváltjuk egy adatbázisból leképezésre
        List<Add> addList = new ArrayList<>();
        for(int i =0; i<50; i++) {
            addList.add(new Add("From", "To", Calendar.getInstance().getTime(), random.nextInt(500)));
        }
        //vege

    final AddAdapter adapter = new AddAdapter(addList);

    ListView listView = (ListView) rootView.findViewById(R.id.add_lstview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Add selectedAdd = adapter.getItem(position);
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("selected_add", selectedAdd);
            startActivity(intent);

        }
    });
    }
}
