package com.example.proteinclassification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proteinclassification.helper.Bitmaphelper;
import com.example.proteinclassification.helper.Bitmaphelper1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class feature_extraction extends AppCompatActivity {
    Bitmap btmp;
    ImageView feature_image;
    BottomNavigationView btm_nav_nextback;
    TextView extractresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_extraction);

        extractresult=findViewById(R.id.extractresult);
        Button feature_extract= findViewById(R.id.feature);
        feature_image=findViewById(R.id.imageview_feature);
        btm_nav_nextback=findViewById(R.id.btmnavnext);

        feature_image.setImageBitmap(Bitmaphelper.getInstance().getBitmap());

        btmp=(Bitmaphelper1.getInstance().getBitmap());
        btm_nav_nextback.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //intent here
                send();

                return false;
            }
        });

        feature_extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectServer(v);
            }
        });
    }

    void send(){
        Intent i=new Intent(this,Classification.class);
        startActivity(i);
    }
    void connectServer(View v){

        String ipv4Address = "192.168.1.2";
        String portNumber ="5000";

        String postUrl= "http://"+ipv4Address+":"+portNumber+"/";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        btmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "testimage.png", RequestBody.create(MediaType.parse("image/*png"), byteArray))
                .build();

      /*  String postBodyText="Hello";
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        RequestBody postBody = RequestBody.create(mediaType, postBodyText);*/

        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(feature_extraction.this,"Connection to server failed",Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg;

                        try {
                            msg=response.body().string();

                            extractresult.setText(msg);
                            Bitmaphelper1.getInstance().savemsg(msg);
                           // Toast.makeText(feature_extraction.this, msg, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

        }


