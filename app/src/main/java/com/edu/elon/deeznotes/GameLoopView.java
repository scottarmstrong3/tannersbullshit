package com.edu.elon.deeznotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by scottarmstrong on 10/21/15.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback {

    public final static int STRING_DIALOG = 1;
    private Intent intent;
    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;

    private float downTouchX, downTouchY;
    private float upTouchX, upTouchY;
    private float moveTouchX;
    private float moveTouchY;
    private boolean moved;
    private Delete deleteButton;
    private int whichNote = -1;
    int highestSelected = -1;
    boolean wasTouched = false;
    protected long downTouch;
    protected long upTouch;
    protected boolean startActivity;
    //DeezNotesActivity activity = new DeezNotesActivity();

    //private Notes notes;
    private ArrayList<Note> noteArray;
    public ArrayList<String> title;
    public ArrayList<String> text;

    public GameLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        thread = new GameLoopThread();
    }

    public void setNotes(ArrayList<Note> noteArray) {
        this.noteArray = noteArray;
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(thread.getState() == Thread.State.TERMINATED){
            thread = new GameLoopThread();
        }


        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void setText(String string) { noteArray.get(whichNote).setText(string);
    }

    public String getText() {
        return noteArray.get(whichNote).getText();
    }

    public void setTitle(String string){
        noteArray.get(whichNote).setTitle(string);
    }

    public String getTitle() {
        return noteArray.get(whichNote).getTitle();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // remember the last touch point

        int sizeOfArray = noteArray.size();
        int selectedToss = -1;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downTouch = System.currentTimeMillis();
            downTouchX = event.getX();
            downTouchY = event.getY();
            if (sizeOfArray > 0) {
                for (int i = 0; i < noteArray.size(); i++) {
                    if (downTouchX <= noteArray.get(i).x + (noteArray.get(i).width / 2) && downTouchX >= noteArray.get(i).x - (noteArray.get(i).width / 2) && downTouchY <= noteArray.get(i).y + (noteArray.get(i).height / 2) && downTouchY >= noteArray.get(i).y - (noteArray.get(i).height / 2)) {
                        highestSelected = i;
                        selectedToss = i;
                        moveTouchX = downTouchX;
                        moveTouchY = downTouchY;
                        whichNote = i;
                        noteArray.get(i).isSelected = true;
                        wasTouched = true;
                    }
                }
            }
            moved = false;
            downTouch = System.currentTimeMillis();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (wasTouched) {
                //System.out.println("MOVING");
                moveTouchX = event.getX();
                moveTouchY = event.getY();
            }
            moved = true;
        }

        if(event.getAction()== MotionEvent.ACTION_UP){
            upTouch = System.currentTimeMillis();
            upTouchX = event.getX();
            upTouchY = event.getY();
            wasTouched = false;



            if (highestSelected >= 0) {
                noteArray.get(highestSelected).isSelected = false;
            }


            if (upTouchX > deleteButton.x - deleteButton.width/2
                    && upTouchX < deleteButton.x + deleteButton.width/2
                    && upTouchY > deleteButton.y - deleteButton.height/2
                    && upTouchY < deleteButton.y + deleteButton.height/2) {
                //System.out.println("SHOULD DELETE " + whichNote);

                // want to delete something?
                if (whichNote >= 0) {
                    noteArray.get(whichNote).delete();
                    whichNote = -1;
                }
            }
        }

        if(downTouch - upTouch < 200 && selectedToss >= 0){
            intent = new Intent(context, NoteViewActivity.class);
            intent.putExtra("text", getText());
            intent.putExtra("title", getTitle());
            context.startActivity(intent);
            //System.out.println("viewIntent");
            selectedToss = -1;
           startActivity = true;
        }
        return true;

    }
    protected void onActivityResult(int requestInt, int finishInt, Intent returnIntent){
        if(requestInt == STRING_DIALOG){
            if(finishInt == Activity.RESULT_OK){
                String returnText = returnIntent.getStringExtra("returntext");
                String returnTitle = returnIntent.getStringExtra("returntitle");
                setText(returnText);
                setTitle(returnTitle);
            }

        }
    }


    private class GameLoopThread extends Thread{

        private boolean isRunning = false;
        private long lastTime;

        public GameLoopThread() {
            deleteButton = new Delete(context);
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {

            lastTime = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();
                    double elapsed = (now - lastTime) / 1000.0;
                    lastTime = now;

                    // update/draw
                    doUpdate(elapsed);
                    doDraw(canvas);

                    //updateFPS(now);
                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        // move all objects in the game
        private void doUpdate(double elapsed) {

            if (highestSelected >= 0 && moveTouchX != 0 && moveTouchY != 0) {
                //System.out.println(moveTouchX + "    " + moveTouchY);
                noteArray.get(highestSelected).doUpdate(elapsed, moveTouchX, moveTouchY);
                //System.out.println("Highest +" + highestSelected);
            }

            if (highestSelected >= 0 && noteArray.get(highestSelected).isSelected == false) {
                highestSelected = -1;
            }

            }

            // draw all objects in the game
        private void doDraw(Canvas canvas) {

            // draw the background
            canvas.drawColor(Color.argb(255, 126, 192, 238));
            deleteButton.doDraw(canvas);

            if (noteArray.size() > 0) {
                for (int i = 0; i < noteArray.size(); i++) {
                    noteArray.get(i).doDraw(canvas);
                }
            }
        }

    }


}
