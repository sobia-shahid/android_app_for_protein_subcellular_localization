package com.example.proteinclassification;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class welcomscreen extends AppCompatActivity {
    Button btn;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomscreen);
        btn=findViewById(R.id.buttonw);
        txt=findViewById(R.id.textView2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lg();

            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg();
            }
        });

    }
    public void lg(){
        Intent intent=new Intent();
        startActivity(intent);
    }
    public void rg(){
        Intent intent=new Intent(this,Register.class);
        startActivity(intent);
    }






}
