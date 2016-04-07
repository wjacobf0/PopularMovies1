package com.example.jake.popularmovies;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;


public class PicassoTransformation implements Transformation {

    // The source Imageview object
    private  ImageView imageView;

    public PicassoTransformation(ImageView imageView)
    {
        this.imageView = imageView;
    }

    @Override
    public Bitmap transform(Bitmap source)
    {
        int dstWidth = imageView.getWidth();
        double scale = (double)source.getHeight()/source.getWidth();
        int dstHeight = (int)(scale * dstWidth);
        Bitmap result = Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override public String key()
    {
        return "scale()";
    }

}
