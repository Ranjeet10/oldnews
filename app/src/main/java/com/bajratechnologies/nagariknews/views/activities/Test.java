package com.bajratechnologies.nagariknews.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;

/**
 * Created by ronem on 5/5/16.
 */
public class Test extends AppCompatActivity {
    protected EditText input;
    protected TextView result;
    protected Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        input = (EditText) findViewById(R.id.inputdate);
        result = (TextView) findViewById(R.id.result);
        calculate = (Button) findViewById(R.id.calculate);


        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputDate = input.getText().toString();
                if (!TextUtils.isEmpty(inputDate)) {
                    String resultDate = BasicUtilMethods.getTimeAgo(inputDate);
                    result.setText(resultDate);
                }
            }
        });
    }
}
