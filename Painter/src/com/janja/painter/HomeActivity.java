package com.janja.painter;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends ActionBarActivity {

    private ArrayList<Button> colorButtons;
    private DrawBoard drawBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {
        colorButtons = new ArrayList<Button>();
        LinearLayout colorBoardTop = (LinearLayout) findViewById(R.id.color_board_top);
        LinearLayout colorBoardBottom = (LinearLayout) findViewById(R.id.color_board_bottom);

        findColorButtons(colorBoardTop);
        findColorButtons(colorBoardBottom);
        setColorButtonsClickListener();

        drawBoard = (DrawBoard) findViewById(R.id.draw_board);
    }

    private void findColorButtons(LinearLayout layout) {

        int layoutSize = layout.getChildCount();
        for (int i = 0; i < layoutSize; i++) {
            Button button = (Button) layout.getChildAt(i);
            colorButtons.add(button);
        }
    }

    private void setColorButtonsClickListener() {
        for (int i = 0; i < colorButtons.size(); i++) {
            Button button = colorButtons.get(i);
            button.setOnClickListener(colorButtonsClickListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reset_draw_board) {
            drawBoard.reset();
            return true;
        } else if (id == R.id.save_draw_board_png) {
            saveImage(Bitmap.CompressFormat.PNG);
            return true;
        } else if (id == R.id.save_draw_board_jpeg) {
            saveImage(Bitmap.CompressFormat.JPEG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener colorButtonsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int color = ((ColorDrawable) view.getBackground()).getColor();
            drawBoard.setDrawPaintColor(color);
        }
    };

    private void saveImage(Bitmap.CompressFormat format) {
        long time = System.currentTimeMillis();
        String fileFormat = (format == Bitmap.CompressFormat.JPEG) ? ".JPEG"
                : ".PNG";
        String fileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                Locale.getDefault()).format(new Date(time));
        String filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File file = new File(filePath, fileName + fileFormat);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(drawBoard.getDrawFile(format, 50));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
