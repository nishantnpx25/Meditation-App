package com.example.meditation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Yoga extends AppCompatActivity {
//    ConstraintLayout yogalayout = (ConstraintLayout)findViewById(R.id.Yogalayout);
    float x1,y1,x2,y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

    }
    public boolean onTouchEvent(MotionEvent touchevent){
        switch(touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=touchevent.getX();
                y1=touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=touchevent.getX();
                y2=touchevent.getY();
                if(x1>x2){
                    Intent i = new Intent(Yoga.this, Devotion.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
