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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proteinclassification.helper.Bitmaphelper;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.dnn.Importer;
import org.opencv.imgproc.Imgproc;

public class segmentation extends AppCompatActivity {
ImageView imgseg;
Spinner segspinner;
Button apply_segment;
String spinner_pos;
Bitmap btmp;
BottomNavigationView btm_nav_nextback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segmentation);
        imgseg=findViewById(R.id.imageview_segment);
        segspinner=findViewById(R.id.segment_spin);
        apply_segment=findViewById(R.id.segment);
        btm_nav_nextback=findViewById(R.id.btmnavnext);

        imgseg.setImageBitmap(Bitmaphelper.getInstance().getBitmap());
        btmp=bitmapfromimageview();

        btm_nav_nextback.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //intent here
                send();

                return false;
            }
        });

      segspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        spinner_pos="GrabCut";
                        break;
                    case 1:
                        spinner_pos="Watershed";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_pos="GrabCut";
            }
        });
      apply_segment.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(spinner_pos=="GrabCut"){
                  apply_grabcut();

              }
              else{
                  apply_watershed();
              }


          }
      });


    }

    public void back(){
        Intent intent=new Intent(this,imgupload.class);
        startActivity(intent);

    }

    public void apply_watershed(){


        Mat sourcemat=new Mat();
        Utils.bitmapToMat(btmp,sourcemat);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize=4;

        int width , height ;
        width  = sourcemat.width();
        height = sourcemat.height();



        Mat rgba = new Mat();
        Mat gray_mat= new Mat();
        Mat threeChannel = new Mat();



        Utils.bitmapToMat(btmp,gray_mat);

        Imgproc.cvtColor(gray_mat,rgba , Imgproc.COLOR_RGBA2RGB);


        Imgproc.cvtColor(rgba, threeChannel, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(threeChannel, threeChannel, 100, 255, Imgproc.THRESH_OTSU);


        Mat fg = new Mat(rgba.size(), CvType.CV_8U);
        Imgproc.erode(threeChannel,fg,new Mat(),new Point(-1,-1),2);

        Mat bg = new Mat(rgba.size(),CvType.CV_8U);
        Imgproc.dilate(threeChannel,bg,new Mat(),new Point(-1,-1),3);
        Imgproc.threshold(bg,bg,1, 128,Imgproc.THRESH_BINARY_INV);



        Mat markers = new Mat(rgba.size(),CvType.CV_8U, new Scalar(0));
        Core.add(fg, bg, markers);

        // Start the WaterShed Segmentation :


        Mat marker_tempo = new Mat();
        markers.convertTo(marker_tempo, CvType.CV_32S);


        Imgproc.watershed(rgba, marker_tempo);
        marker_tempo.convertTo(markers,CvType.CV_8U);

        Bitmap result_Bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

        Imgproc.applyColorMap( markers, markers,4 );
        Utils.matToBitmap( markers,result_Bitmap);


        imgseg.setImageBitmap(result_Bitmap);
        Bitmaphelper.getInstance().setBitmap(result_Bitmap);


    }

    public void apply_grabcut(){
        Bitmap b=btmp.copy(Bitmap.Config.ARGB_8888,true);
        Point tl=new Point();
        Point br=new Point();
        //GrabCut part
        Mat img = new Mat();
        Utils.bitmapToMat(b, img);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);

        int r = img.rows();
        int c = img.cols();
        Point p1 = new Point(c / 100, r / 100);
        Point p2 = new Point(c - c / 100, r - r / 100);
        Rect rect = new Rect(p1, p2);
        Mat background = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        Mat firstMask = new Mat();
        Mat bgModel = new Mat();
        bgModel.setTo(new Scalar(255,255,255));

        Mat fgModel = new Mat();
        fgModel.setTo(new Scalar(255,255,255));

        Mat mask;
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
        Mat dst = new Mat();


        Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel, 5, Imgproc.GC_INIT_WITH_RECT);

        Core.compare(firstMask, source, firstMask, Core.CMP_EQ);
          firstMask.convertTo(source,CvType.CV_8U);


        Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        img.copyTo(foreground, firstMask);
        Scalar color = new Scalar(255, 0, 0, 255);
        Imgproc.rectangle(img, tl, br, color);

        Mat tmp = new Mat();
        Imgproc.resize(background, tmp, img.size());
        background = tmp;
        mask = new Mat(foreground.size(), CvType.CV_8UC1,new Scalar(255, 255, 255));

        Imgproc.cvtColor(foreground, mask, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mask, mask, 254, 255, Imgproc.THRESH_BINARY_INV);
        Mat vals = new Mat(1, 1, CvType.CV_8UC3, new Scalar(0.0));
        background.copyTo(dst);

        background.setTo(vals, mask);

        Core.add(background, foreground, dst, mask);
        Bitmap grabCutImage = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Bitmap processedImage = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(dst, grabCutImage);
       // dst.copyTo(sampleImage);
        imgseg.setImageBitmap(grabCutImage);
        Bitmaphelper.getInstance().setBitmap(grabCutImage);
        firstMask.release();
        source.release();
        bgModel.release();
        fgModel.release();
    }

    // Function to send image to next activity
    public void send(){
        if(Bitmaphelper.getInstance().getBitmap()==null){
            Toast.makeText(this,"nullbitmp",Toast.LENGTH_SHORT).show();
        }else{
            Intent i=new Intent(this,feature_extraction.class);
            startActivity(i);
        }
    }

    private Bitmap bitmapfromimageview(){
        return ((BitmapDrawable)imgseg.getDrawable()).getBitmap();
    }
}
