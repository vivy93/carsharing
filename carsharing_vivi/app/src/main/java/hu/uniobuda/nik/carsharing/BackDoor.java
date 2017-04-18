package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by igyartoimre on 2017. 04. 17..
 */

public class BackDoor extends AppCompatActivity implements View.OnClickListener{

    Button buttonAddList;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.backdoor);

        buttonAddList =(Button) findViewById(R.id.RAL);
        buttonAddList.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
    if (v==buttonAddList)
        startActivity(new Intent(this,ListActivity.class));

    }
}
