package com.bidhee.nagariknews.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.MyCheckBox;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 4/19/16.
 */
public class SelectCategoryActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.select_category_layout)
    LinearLayout selectCategoryLayout;
    @Bind(R.id.btn_done)
    Button btnDone;
    @Bind(R.id.result)
    TextView resultView;

    ArrayList<MyCheckBox> listOfCheckedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.RepublicaTheme);
        setContentView(R.layout.select_category_layout);
        ButterKnife.bind(this);

        setUpToolBar();
        attachCheckBoxes();
    }

    @OnClick(R.id.btn_done)
    void onButtonDoneClick() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listOfCheckedItem.size(); i++) {
            MyCheckBox mcb = listOfCheckedItem.get(i);
            builder.append("\nstatus: " + mcb.getText() + " is " + mcb.getIsChecked());
        }
        resultView.setText(builder.toString());
        Intent intent = new Intent(SelectCategoryActivity.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void attachCheckBoxes() {

        listOfCheckedItem = new ArrayList<>();
        listOfCheckedItem.add(new MyCheckBox(true, "One"));
        listOfCheckedItem.add(new MyCheckBox(false, "Two"));
        listOfCheckedItem.add(new MyCheckBox(true, "Three"));
        listOfCheckedItem.add(new MyCheckBox(false, "four"));
        listOfCheckedItem.add(new MyCheckBox(false, "five"));

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < listOfCheckedItem.size(); i++) {
            final MyCheckBox myCheckBox = listOfCheckedItem.get(i);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setButtonDrawable(R.drawable.checkbox_selector_background);
            checkBox.setChecked(myCheckBox.getIsChecked());
            checkBox.setText(myCheckBox.getText());
            selectCategoryLayout.addView(checkBox, params);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    myCheckBox.setIsChecked(isChecked);

                }
            });
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        ButterKnife.unbind(this);
    }
}
