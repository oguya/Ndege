package com.droid.ndege.ui.viewutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import com.droid.ndege.R;

/**
 * Created by james on 10/02/14.
 */
public class BorderDrawable extends BitmapDrawable {
    private static final int BORDER_WIDTH = 10;
    private final int[] GRADIENT_COLORS = {Color.BLUE, Color.GREEN, Color.RED};
    Paint borderPaint;
    private Bitmap bitmap;

    public BorderDrawable(Resources res, Bitmap bitmap){
        super(res, bitmap);
        this.borderPaint = new Paint();
        borderPaint.setStrokeWidth(BORDER_WIDTH);
        borderPaint.setStyle(Paint.Style.STROKE);
        this.bitmap = bitmap;

        //set border gradient
        Shader shader = new LinearGradient(0, 0, 0, getBounds().bottom, GRADIENT_COLORS,
                null, Shader.TileMode.CLAMP);
        borderPaint.setShader(shader);
//        borderPaint.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        //draw circle
//        canvas.drawRect(getBounds(), borderPaint);
    }
}
