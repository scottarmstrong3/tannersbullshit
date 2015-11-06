package com.edu.elon.deeznotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;


/**
 * Created by scottarmstrong on 10/20/15.
 */
public class Note {

    private Bitmap bitmap;
    protected float width, height;
    protected float x, y;
    protected int IDNumber;
    protected boolean isSelected;
    protected boolean isDeleted;
    protected Context context;
    private Paint paint = new Paint(Color.BLACK);
    private final float ADD_BUTTON_HEIGHT = 216;
    private int screenWidth, screenHeight;
    private String title = "";
    private String text = "";

    public Note (Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.note);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        float smallestWidth = width ;
        float largestWidth = screenWidth - width;
        float smallestHeight = height;
        float largestHeight = screenHeight - (ADD_BUTTON_HEIGHT+ height);
        x = smallestWidth + (float) (Math.random() * ((largestWidth - smallestWidth) + 1));
        y = smallestHeight + (float) (Math.random() * ((largestHeight - smallestHeight) + 1));
        isDeleted = false;
    }

    public void delete() {
        isDeleted = true;
    }

    public void doDraw(Canvas canvas) {
        if (!isDeleted) {
            canvas.drawBitmap(bitmap,
                    null,
                    new Rect((int) (x - width / 2), (int) (y - height / 2),
                            (int) (x + width / 2), (int) (y + height / 2)),
                    null);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(20);

            paint.setAntiAlias(true);
            canvas.drawText(title, x, y, paint);
        }
    }

    public void doUpdate(double elapsed, float touchX, float touchY) {
        if (touchX + width < screenWidth && touchX - width / 2 > 0 && touchY + height < screenHeight && touchY - height / 2 > 0) {
            x = (float) touchX;
            y = (float) touchY;
        }
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String string){
        title = string;
    }

    public String getText(){
        return text;
    }

    public void setText(String string){
        text = string;
    }

    public Context getContext() {
        return context;
    }
}

