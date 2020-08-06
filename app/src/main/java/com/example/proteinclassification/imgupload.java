package com.example.proteinclassification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proteinclassification.helper.Bitmaphelper;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import static com.example.proteinclassification.R.drawable.home_icon;

public class imgupload extends AppCompatActivity {
    static {
        if (!(OpenCVLoader.initDebug())) {
        } else {
        }
    }
    ImageView upload_image;
    Button btn;
    Bitmap btmp,equalizerBitmap;
    Spinner spinner;
    String spinner_pos;
    BottomNavigationView btm_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgupload);
        upload_image=findViewById(R.id.imageview_enhance);
        btn=findViewById(R.id.button3);
        spinner=findViewById(R.id.enhance_spin);
        btm_nav=findViewById(R.id.btmnav);
        upload_image.setImageBitmap(Bitmaphelper.getInstance().getBitmap());
        btmp=bitmapfromimageview();

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                send();
                return false;
            }
        });

        //Spinner Item Listener for selecting enhancement technique
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0:
                       spinner_pos="Histogram";
                       break;
                   case 1:
                       spinner_pos="Bilateral";
                       break;

               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
            spinner_pos="Histogram";
           }
       });



        //Button Listener to apply Enhancement on the Image
        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        if(spinner_pos=="Histogram"){
            display_histogram();

        }
        else{
            display_bilateral();
        }
        }
    });



    } //End of OnCreate method
    private Bitmap bitmapfromimageview(){
        return ((BitmapDrawable)upload_image.getDrawable()).getBitmap();
    }
    // Function to send enhanced image to next activity
    public void send(){
        if(Bitmaphelper.getInstance().getBitmap()==null){
            Toast.makeText(this,"nullbitmp",Toast.LENGTH_SHORT).show();
        }else{
            Intent i=new Intent(this,segmentation.class);
            startActivity(i);
        }
    }

    //Bilateral Enhancement Technique
    public void display_bilateral(){

        Mat sourcemat=new Mat();
        Utils.bitmapToMat(btmp,sourcemat);
        Mat destinationmat=new Mat(sourcemat.size(),sourcemat.type());
        Imgproc.cvtColor(sourcemat,sourcemat,Imgproc.COLOR_BGRA2BGR);
        Imgproc.bilateralFilter(sourcemat,destinationmat, 10, 250, 50);
        Imgproc.cvtColor(destinationmat, destinationmat, Imgproc.COLOR_RGB2RGBA);
        equalizerBitmap = Bitmap.createBitmap(sourcemat.cols(), sourcemat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(destinationmat, equalizerBitmap);
        upload_image.setImageBitmap(equalizerBitmap);
        Bitmaphelper.getInstance().setBitmap(equalizerBitmap);
    }

    //Histogram Enhancement Technique
    public void display_histogram(){
        Mat sourceMat = new Mat();
        Utils.bitmapToMat(btmp, sourceMat);
        Mat destinationMat = new Mat(sourceMat.size(), sourceMat.type());
        Imgproc.cvtColor(sourceMat, sourceMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(sourceMat, destinationMat);
        equalizerBitmap = Bitmap.createBitmap(sourceMat.cols(), sourceMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(destinationMat, equalizerBitmap);
        upload_image.setImageBitmap(equalizerBitmap);
        Bitmaphelper.getInstance().setBitmap(equalizerBitmap);
    }



}
