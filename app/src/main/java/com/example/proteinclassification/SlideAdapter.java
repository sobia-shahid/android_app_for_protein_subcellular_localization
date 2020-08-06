package com.example.proteinclassification;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    //list of side images
    public int[] lst_images={
            R.drawable.upload,
            R.drawable.enhancement,
            R.drawable.segmentation,
            R.drawable.images,
            R.drawable.classify
    };

    //list of tiles
    public String[] lst_tile={
            "Step 1: Upload Image",
            "Step 2: Enhancement",
            "Step 3: Segmentation",
            "Step 4: Feature Extraction",
            "Step 5: Classification"

    };

    //list of description
    public  String[] lst_description={
            "just tap on upload image and select an image from the gallery",
            "choose the technique and apply enhancement to the picture",
            "choose the technique and apply segmentation to the picture",
            "choose the technique and apply extraction to the picture",
            "choose the technique and classify the picture to get the final result"
    };
//list of background color
    public int[] lst_backgroundcolor={
        Color.rgb(255,255,255),
        Color.rgb(255,255,255),
        Color.rgb(255,255,255),
        Color.rgb(255,255,255),
        Color.rgb(0,0,0)

};
    public SlideAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return lst_tile.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(LinearLayout)o);
    }
    @Override
    public  Object instantiateItem(ViewGroup container, int position){
        inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.slide,container,false);
        LinearLayout layoutslide=view.findViewById(R.id.slidelinearlayout);
        //ImageView imgslide= (ImageView) view.findViewById(R.id.slideimg);
        TextView txttile=(TextView) view.findViewById(R.id.txttitle);
        TextView description=(TextView) view.findViewById(R.id.textdecription);
        //layoutslide.setBackgroundColor(lst_backgroundcolor[position]);
       // imgslide.setImageResource(lst_images[position]);
        txttile.setText(lst_tile[position]);
        description.setText(lst_description[position]);
        container.addView(view);
        return  view;
    }
    @Override
    public void destroyItem (ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}
