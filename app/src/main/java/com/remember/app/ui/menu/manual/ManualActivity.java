package com.remember.app.ui.menu.manual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.himanshurawat.imageworker.work.To;
import com.remember.app.R;

public class ManualActivity extends AppCompatActivity implements View.OnClickListener{

    TextView point_one;
    TextView point_two;
    TextView point_three;
    TextView point_four;
    TextView point_five;
    TextView point_six;
    TextView point_seven;
    TextView point_eight;
    ImageView firstPoint;
    ImageView secondPoint;
    ImageView thirdPoint;
    TextView fourthPoint;
    ImageView fifthPoint;
    TextView sixthPoint;
    ImageView seventhPoint;
    TextView eighthPoint;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        init();
    }

    public void init(){
        context = this;
        point_one = findViewById(R.id.first_point_one);
        point_one.setOnClickListener(this);
        point_two = findViewById(R.id.second_point_two);
        point_two.setOnClickListener(this);
        point_three = findViewById(R.id.third_point_three);
        point_three.setOnClickListener(this);
        point_four = findViewById(R.id.fourth_point_four);
        point_four.setOnClickListener(this);
        point_five = findViewById(R.id.fifth_point_five);
        point_five.setOnClickListener(this);
        point_six = findViewById(R.id.sixth_point_six);
        point_six.setOnClickListener(this);
        point_seven = findViewById(R.id.seventh_point_seven);
        point_seven.setOnClickListener(this);
        point_eight = findViewById(R.id.eighth_point_eight);
        point_eight.setOnClickListener(this);
        firstPoint = findViewById(R.id.first_per);
        secondPoint = findViewById(R.id.second_per);
        thirdPoint = findViewById(R.id.third_per);
        fourthPoint = findViewById(R.id.fourth_one_per);
        fifthPoint = findViewById(R.id.fifth_per);
        sixthPoint = findViewById(R.id.sixth_two_per);
        seventhPoint = findViewById(R.id.seventh_per);
        eighthPoint = findViewById(R.id.eighth_one_per);
    }

    @Override
    public void onClick(View v) {
        firstPoint.setFocusable(false);
        secondPoint.setFocusable(false);
        thirdPoint.setFocusable(false);
        fourthPoint.setFocusable(false);
        fifthPoint.setFocusable(false);
        sixthPoint.setFocusable(false);
        seventhPoint.setFocusable(false);
        eighthPoint.setFocusable(false);
        switch (v.getId()){
            case R.id.first_point_one:
                firstPoint.setFocusable(true);
                firstPoint.setFocusableInTouchMode(true);
                firstPoint.requestFocus();

                break;
            case R.id.second_point_two:
                secondPoint.setFocusableInTouchMode(true);
                secondPoint.setFocusable(true);
                secondPoint.requestFocus();

                break;
            case R.id.third_point_three:
                thirdPoint.setFocusableInTouchMode(true);
                thirdPoint.setFocusable(true);
                thirdPoint.requestFocus();

                break;
            case R.id.fourth_point_four:
                fourthPoint.setFocusableInTouchMode(true);
                fourthPoint.setFocusable(true);
                fourthPoint.requestFocus();

                break;
            case R.id.fifth_point_five:
                fifthPoint.setFocusableInTouchMode(true);
                fifthPoint.setFocusable(true);
                fifthPoint.requestFocus();

                break;
            case R.id.sixth_point_six:
                sixthPoint.setFocusableInTouchMode(true);
                sixthPoint.setFocusable(true);
                sixthPoint.requestFocus();

                break;
            case R.id.seventh_point_seven:
                seventhPoint.setFocusableInTouchMode(true);
                seventhPoint.setFocusable(true);
                seventhPoint.requestFocus();

                break;
            case R.id.eighth_point_eight:
                eighthPoint.setFocusableInTouchMode(true);
                eighthPoint.setFocusable(true);
                eighthPoint.requestFocus();

                break;
                default:
                    break;
        }
    }
}
