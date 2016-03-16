package com.example.joe.datengine;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

public class GameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        RelativeLayout rel_layout = new RelativeLayout(this);
        setContentView(rel_layout);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        rel_layout.addView(new TestView(this));
    }

}


class TestView extends View {

    private int background_dir = 10;
    private int background_color = 0;
    private int sprite_x = 0;
    private int sprite_y = 0;
    private int x_velocity = 15;
    private int y_velocity = 15;
    TestView the_view = this;
    Bitmap virtual_screen, sprite, background;
    private static int[] color_table = new int[256];
    private static byte sprite_data[] = {
            0x00, 0x00, 0x01, 0x01, 0x01, 0x01, 0x00, 0x00,
            0x00, 0x01, 0x02, 0x02, 0x02, 0x02, 0x01, 0x00,
            0x01, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01,
            0x01, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01,
            0x01, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01,
            0x01, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01,
            0x00, 0x01, 0x02, 0x02, 0x02, 0x02, 0x01, 0x00,
            0x00, 0x00, 0x01, 0x01, 0x01, 0x01, 0x00, 0x00
    };

    Handler handler = new Handler(Looper.getMainLooper());

    Runnable colorChanger = new Runnable() {
        @Override
        public void run() {

            background_color += background_dir;
            if(background_color >= 255 || background_color <= 0) {

                background_dir *= -1;
                background_color += background_dir;
            }

            the_view.postInvalidate();

            handler.postDelayed(this, 16);
        }
    };

    public TestView(Context context) {

        super(context);

        int x, y;
        color_table[0] = 0x0;
        color_table[1] = 0xFF505050;
        color_table[2] = 0xFF00FF00;
        color_table[3] = 0xFF000000;
        Paint paint = new Paint();
        virtual_screen = Bitmap.createBitmap(256, 244, Bitmap.Config.ARGB_8888);
        background = Bitmap.createBitmap(256, 244, Bitmap.Config.ARGB_8888);
        sprite = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
        Canvas sprite_canvas = new Canvas(sprite);
        Canvas background_canvas = new Canvas(background);
        paint.setStrokeWidth(1);

        for (x = 0; x < 256; x++) {

            for (y = 0; y < 244; y++) {

                paint.setColor(color_table[3]);
                background_canvas.drawPoint(x, y, paint);
            }
        }

        for (x = 0; x < 8; x++) {

            for (y = 0; y < 8; y++) {

                paint.setColor(color_table[sprite_data[y * 8 + x]]);
                sprite_canvas.drawPoint(x, y, paint);
            }
        }

        colorChanger.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        int width = the_view.getWidth();
        int height = the_view.getHeight();
        paint.setDither(false);
        float ratio = 244.0f / 256.0f;

        Canvas temp_canvas = new Canvas(virtual_screen);
        temp_canvas.drawBitmap(background, null, new Rect(0, 0, 256, 244), paint);
        temp_canvas.drawBitmap(sprite, null, new Rect(sprite_x, sprite_y, sprite_x + 8, sprite_y + 8), paint);

        canvas.drawRGB(0, 255, 255);
        Rect src = new Rect(0, 0, 256, 244);
        Rect dst = new Rect(0, 0, width, (int)(width * ratio));
        canvas.drawBitmap(virtual_screen, src, dst, paint);

        sprite_x += x_velocity;
        if(sprite_x > 248 || sprite_x < 0)  {

            x_velocity = -x_velocity;
            sprite_x += x_velocity;
        }

        sprite_y += y_velocity;
        if(sprite_y > 236 || sprite_y < 0)  {

            y_velocity = -y_velocity;
            sprite_y += y_velocity;
        }
    }

}
