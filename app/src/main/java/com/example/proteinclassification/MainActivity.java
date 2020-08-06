package com.example.proteinclassification;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proteinclassification.helper.Bitmaphelper;
import com.example.proteinclassification.helper.Bitmaphelper1;

import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

   ImageButton home_upload;
   BottomNavigationView bottomNavigationView;
Bitmap btmp;
Uri img;
    private static final  int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.btm_nav);
        home_upload=findViewById(R.id.image_upload);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        //Intent here to move to another activity

                        break;


                }


                return false;
            }
        });
        home_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                    {

                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{pickIMageFromGallery();}

                }
                else {pickIMageFromGallery();}
            }
        });



    }

    public void send(){
        if(Bitmaphelper.getInstance().getBitmap()==null){
            Toast.makeText(this,"nullbitmp",Toast.LENGTH_SHORT).show();
        }else{
            Intent i=new Intent(this,imgupload.class);
            startActivity(i);
        }
    }

public void openact(){

       Intent intent=new Intent(this,Classification.class);
       startActivity(intent);
}

    //Function to select image from gallery and call another function to display it
    private void pickIMageFromGallery(){

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);

    }

    //Function to get permission to access gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickIMageFromGallery();
                }
                else {
                    Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    //Function to display the selected image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==IMAGE_PICK_CODE){
            img=data.getData();
            try {
                btmp= MediaStore.Images.Media.getBitmap(this.getContentResolver(),img);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmaphelper.getInstance().setBitmap(btmp);
            Bitmaphelper1.getInstance().setBitmap(btmp);
            send();

        }
    }

}



