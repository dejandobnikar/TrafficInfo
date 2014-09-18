package com.android.volley.toolbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.Log;

public class FadeInNetworkImageView extends NetworkImageView {
 
    private static final int FADE_IN_TIME_MS = 250;
    private int alpha = 255;
 
    public FadeInNetworkImageView(Context context) {
        super(context);
    }
 
    public FadeInNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public FadeInNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setImageViewAlpha(int alpha) {
    	this.alpha = alpha;
    }
    
 /*
    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
    public void setImageBitmap(Bitmap bm) {
    	Log.i("FadeInNetworkImageView", "alpha: " + alpha);
    	ColorDrawable cd = new ColorDrawable(android.R.color.transparent);
    	cd.setAlpha(alpha);
    	BitmapDrawable bd = new BitmapDrawable(getContext().getResources(), bm);
    	bd.setAlpha(alpha);
    	
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
        		cd,
        		bd                
        });
        td.setAlpha(alpha);
         
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			setAlpha(alpha);
		}
		else {
			setImageAlpha(alpha);
		}
        setImageDrawable(td);
        
        td.startTransition(FADE_IN_TIME_MS);
        
    }
    */
    
}
