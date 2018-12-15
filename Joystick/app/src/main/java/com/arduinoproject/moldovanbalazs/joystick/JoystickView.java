package com.arduinoproject.moldovanbalazs.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;

    private JoyStickListener joyStickListenerCallback;
    public final int ratio = 5;

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setupDimensions() {
        this.centerX = getWidth()/2;
        this.centerY = 3* getHeight()/4;
        this.baseRadius = Math.min(getWidth(), getHeight())/3;
        this.hatRadius = Math.min(getWidth(), getHeight())/8;
    }

    public float getBaseRadius() {
        return baseRadius;
    }

    public void setBaseRadius(float baseRadius) {
        this.baseRadius = baseRadius;
    }

    public float getHatRadius() {
        return hatRadius;
    }

    public void setHatRadius(float hatRadius) {
        this.hatRadius = hatRadius;
    }

     public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof  JoyStickListener) {
            joyStickListenerCallback = (JoyStickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public void drawJoyStick(float newX, float newY) {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            canvas.drawColor(Color.BLACK);
            colors.setColor(Color.parseColor("#50808E"));
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setColor(Color.parseColor("#8AC4D0"));
            canvas.drawCircle(newX, newY, hatRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.setupDimensions();
        this.drawJoyStick(this.centerX, this.centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouch(View v, MotionEvent e) {

        if (v.equals(this)){
            if(e.getAction() != e.ACTION_UP){
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if( displacement < this.baseRadius ) {
                    this.drawJoyStick(e.getX(), e.getY());
                    joyStickListenerCallback.onJoyStickMoved((e.getX() - centerX)/baseRadius, (e.getY()-centerY)/baseRadius, getId());
                } else {
                    float ratio = (baseRadius  ) / displacement;
                    float constrainedX = centerX + (e.getX() - centerX  ) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY ) * ratio;
                    drawJoyStick(constrainedX, constrainedY);
                    joyStickListenerCallback.onJoyStickMoved(this.getSignedUnit((e.getX() - centerX)/baseRadius), this.getSignedUnit((e.getY()-centerY )/baseRadius), getId());
                    //joyStickListenerCallback.onJoyStickMoved((e.getX() - centerX)/baseRadius, (e.getY()-centerY )/baseRadius, getId());
                }
                //drawJoyStick(e.getX(), e.getY());
            } else {
              drawJoyStick(centerX, centerY);
              joyStickListenerCallback.onJoyStickMoved(0,0, getId());
            }
        }
        return true;
    }

    public float getSignedUnit(float coordinate) {
        if( coordinate > 1) {
            return 1;
        }

        if(coordinate < - 1) {
            return -1;
        }
        return coordinate;
    }

    public interface JoyStickListener {
        void onJoyStickMoved(float xPercent, float yPercent, int id);
    }
}
