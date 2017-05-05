package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class PostChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton editRadioByCar;
    private Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_choose);

        nextButton = (Button) findViewById(R.id.nextButton);
        editRadioByCar=(RadioButton) findViewById(R.id.editRadioByCar);

        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==nextButton && editRadioByCar.isChecked()==true)
        {
            finish();
            startActivity(new Intent(this, PostCarActivity.class));
        }
        if (v==nextButton && editRadioByCar.isChecked()==false)
        {
            finish();
            startActivity(new Intent(this, PostFootActivity.class));
        }
    }
}
