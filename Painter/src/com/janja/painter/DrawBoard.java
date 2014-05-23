package com.janja.painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class DrawBoard extends View {

    private final static int DEFAULT_DRAW_PAINT_WIDTH = 8;
    private final static int DEFAULT_DRAW_PAINT_COLOR = Color.BLACK;

    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    private Path drawPath;
    private Paint drawPaint;
    private float lastPositionX;
    private float lastPositionY;

    public DrawBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawBoard(Context context) {
        super(context);
        init();
    }

    private void init() {
        drawPath = new Path();
        drawPath.reset();

        drawPaint = new Paint();
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(DEFAULT_DRAW_PAINT_COLOR);
        drawPaint.setStrokeWidth(DEFAULT_DRAW_PAINT_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawCanvas == null) {
            drawBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(drawBitmap);
            drawCanvas.drawColor(Color.WHITE);
        }

        canvas.drawBitmap(drawBitmap, 0, 0, null);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawStart(event);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                drawMove(event);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                drawUp(event);
                invalidate();
                return true;
        }

        return false;
    }

    private void drawStart(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        lastPositionX = x;
        lastPositionY = y;
        drawPath.moveTo(x, y);
    }

    private void drawMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        final float lastX = lastPositionX;
        final float lastY = lastPositionY;

        float currentX = (x + lastX) / 2;
        float currentY = (y + lastY) / 2;
        drawPath.quadTo(lastX, lastY, currentX, currentY);

        lastPositionX = x;
        lastPositionY = y;
    }

    private void drawUp(MotionEvent event) {
        drawCanvas.drawPath(drawPath, drawPaint);
        drawPath.reset();
    }

    public void setDrawPaintColor(int alpha, int red, int green, int blue) {
        drawPaint.setColor(Color.argb(alpha, red, green, blue));
    }

    public void setDrawPaintColor(int color) {
        drawPaint.setColor(color);
    }

    public void setDrawPaintStrokeWidth(float strokeWidth) {
        drawPaint.setStrokeWidth(strokeWidth);
    }

    public Bitmap getDrawBitmap() {
        return drawBitmap;
    }

    public byte[] getDrawFile(Bitmap.CompressFormat format, int quality) {
        return getDrawBytes(format, quality);
    }

    private byte[] getDrawBytes(Bitmap.CompressFormat format, int quality) {

        Bitmap bitmap = getDrawBitmap();

        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(format, quality, stream);
            return stream.toByteArray();
        }
    }

    public void reset() {
        drawBitmap = null;
        drawCanvas = null;
        drawPath.reset();
        invalidate();
    }
}