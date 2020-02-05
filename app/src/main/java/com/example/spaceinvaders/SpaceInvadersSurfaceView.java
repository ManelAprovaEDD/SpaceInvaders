package com.example.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class SpaceInvadersSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MyAnimationThread animThread = null;
    private MyAliensThread aliensThread = null;
    private MyShootThread shootThread = null;

    Bitmap background;
    Bitmap bitmap_nau;
    Bitmap bitmap_alien;
    Bitmap bitmap_alien2;
    Bitmap bitmap_alien3;
    Bitmap bitmap_dispar;

    ArrayList<Punt> aliens;
    ArrayList<Punt> aliens2;
    ArrayList<Punt> aliens3;

    Punt nau;
    Punt alien;
    Punt bala;

    Rect rectBackground;
    float x_bala;
    float x, y;
    Boolean lr = true;
    int score, lives;
    Paint text;
    Paint gameOver;

    private boolean dispar = true;

    // The constructor
    public SpaceInvadersSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        bitmap_nau = BitmapFactory.decodeResource(getResources(), R.drawable.nau);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background2);
        bitmap_alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);
        bitmap_alien2 = BitmapFactory.decodeResource(getResources(), R.drawable.alien2);
        bitmap_alien3 = BitmapFactory.decodeResource(getResources(), R.drawable.alien3);
        bitmap_dispar = BitmapFactory.decodeResource(getResources(), R.drawable.dispar);
        rectBackground = new Rect(0, 0, getWidth(), getHeight());

        nau = new Punt(getWidth() / 2 - 60, getHeight() - 160, 0);
        bala = new Punt(0, 0, 0);
        x_bala = 0;

        aliens = new ArrayList();
        aliens2 = new ArrayList();
        aliens3 = new ArrayList();

        //primera fila aliens
        for (int i = 1; i < 7; i++) {
            alien = new Punt(getWidth() / 7 * i - 50, 50, i);
            aliens.add(alien);
        }

        //segona fila aliens
        for (int i = 1; i < 7; i++) {
            alien = new Punt(getWidth() / 7 * i - 50, 130, i);
            aliens2.add(alien);
        }

        //tercera fila aliens
        for (int i = 1; i < 7; i++) {
            alien = new Punt(getWidth() / 7 * i - 55, 210, i);
            aliens3.add(alien);
        }

        if (animThread != null) return;
        animThread = new MyAnimationThread(getHolder());
        animThread.start(); // To run the animation

        if (aliensThread != null) return;
        aliensThread = new MyAliensThread();
        aliensThread.start();

        if (shootThread != null) return;
        shootThread = new MyShootThread();
        shootThread.start();

        score = 0;
        lives = 3;

        text = new Paint();
        text.setTextSize(22);
        text.setColor(Color.WHITE);

        gameOver = new Paint();
        gameOver.setTextSize(60);
        gameOver.setColor(Color.RED);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopThread();
    }

    public void stopThread() {
        animThread.stop = true;
        aliensThread.stop = true;
        shootThread.stop = true;
    }


    public void newDraw(Canvas canvas) {
        //fons
        canvas.drawBitmap(background, null, rectBackground, null);
        //nau
        canvas.drawBitmap(bitmap_nau, nau.getX(), nau.getY(), null);
        //vides
        canvas.drawText("Score: " + score, 20, 30, text);
        //score
        canvas.drawText("Lives: " + lives, 20, 60, text);

        //primera fila aliens
        for (int i = 1; i <= aliens.size(); i++) {
            canvas.drawBitmap(bitmap_alien, aliens.get(i - 1).getX(), aliens.get(i - 1).getY(), null);
        }
        //segona fila aliens
        for (int i = 1; i <= aliens2.size(); i++) {
            canvas.drawBitmap(bitmap_alien2, aliens2.get(i - 1).getX(), aliens2.get(i - 1).getY(), null);
        }
        //tercera fila aliens
        for (int i = 1; i <= aliens3.size(); i++) {
            canvas.drawBitmap(bitmap_alien3, aliens3.get(i - 1).getX(), aliens3.get(i - 1).getY(), null);
        }

        if (lives <= 0) {
            canvas.drawText("GAME\nOVER", getWidth() / 2, getHeight() / 2, gameOver);
        }

        canvas.drawBitmap(bitmap_dispar,bala.getX(),bala.getY(),null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                // Getting the position of the click to move the ship to the point
                x = (int) event.getX();

                nau.setX(x);
                break;
            }
        }
        return true;
    }

    // The inner thread class
    private class MyAnimationThread extends Thread {
        public boolean stop = false;
        private SurfaceHolder surfaceHolder;

        // Constructor
        public MyAnimationThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void run() { // Thread operations }
            while (!stop) {
                Canvas c = null;
                try { // Getting the canvas for drawing
                    c = surfaceHolder.lockCanvas(null);

                    // Drawing into the canvas
                    synchronized (surfaceHolder) {
                        newDraw(c);
                    }

                    for (int i = 0; i < aliens.size(); i++) {
                        if (aliens.get(i).getY() >= getHeight() - 80 || aliens2.get(i).getY() >= getHeight() - 80 || aliens3.get(i).getY() >= getHeight() - 80) {
                            lives--;
                        }
                    }

                    if (lives <= 0) {
                        stopThread();
                    }
                } finally { // Showing the canvas on the screen
                    if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private class MyAliensThread extends Thread {
        public boolean stop = false;

        // Constructor
        public MyAliensThread() {
        }

        public void run() { // Thread operations }
            while (!stop) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (lr) {
                    lr = false;
                } else {
                    lr = true;
                }
                for (int i = 0; i < aliens.size(); i++) {
                    alien_move(aliens.get(i), lr);
                    alien_move(aliens2.get(i), lr);
                    alien_move(aliens3.get(i), lr);
                }
            }
        }
    }

    private class MyShootThread extends Thread {
        public boolean stop = false;

        // Constructor
        public MyShootThread() {
        }

        public void run() { // Thread operations

            while (!stop) {
                if (dispar == false) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bala.setY(bala.getY() - 1);

                    if(bala.getY() <= 0){
                        dispar = true;
                    }

                } else if (dispar == true){
                    bala.setX(nau.getX()+58);
                    bala.setY(nau.getY());
                    dispar = false;

                    SpaceInvadersActivity.soDispar.start();
                }

                //3a fila aliens
                for (int i = 0; i < aliens3.size(); i++) {
                    if(bala.getY()<aliens3.get(i).getY() && bala.getY() > aliens3.get(i).getY()-60){
                        if(bala.getX() > aliens3.get(i).getX() && bala.getX()<aliens3.get(i).getX()+65){
                            dispar = true;
                            aliens3.get(i).setX(-200);
                            aliens3.get(i).setY(-200);

                            score += 10;
                        }
                    }
                }

                //2a fila aliens
                for (int i = 0; i < aliens2.size(); i++) {
                    if(bala.getY()<aliens2.get(i).getY() && bala.getY() > aliens2.get(i).getY()-50){
                        if(bala.getX() > aliens2.get(i).getX() && bala.getX()<aliens2.get(i).getX()+55){
                            dispar = true;
                            aliens2.get(i).setX(-200);
                            aliens2.get(i).setY(-200);

                            score += 10;;
                        }
                    }
                }

                for (int i = 0; i < aliens.size(); i++) {
                    if(bala.getY()<aliens.get(i).getY() && bala.getY() > aliens.get(i).getY()-38){
                        if(bala.getX() > aliens.get(i).getX() && bala.getX()<aliens.get(i).getX()+55){
                            dispar = true;
                            aliens.get(i).setX(-200);
                            aliens.get(i).setY(-200);

                            score += 10;
                        }
                    }
                }
            }
        }
    }

    private void alien_move(Punt punt, boolean lr) {
        float x = punt.getX();
        float y = punt.getY();

        punt.setY(y + 30);
        if (lr) {
            punt.setX(x + 30);
        } else {
            punt.setX(x - 30);
        }
    }

    public Boolean getDispar() {
        return dispar;
    }
}