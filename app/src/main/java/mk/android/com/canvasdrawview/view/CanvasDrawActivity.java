package mk.android.com.canvasdrawview.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import mk.android.com.canvasdrawview.R;
import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.presenter.ShapesPresenter;

import static mk.android.com.canvasdrawview.view.CustomView.RADIUS;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class CanvasDrawActivity extends AppCompatActivity {
    private static final String TAG = "canvas123";
    private CustomView canvas = null;
    ShapesPresenter shapesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_draw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.canvas = (CustomView) this.findViewById(R.id.canvasDrawView);
        Log.d(TAG, " onCreate" + canvas);
        shapesPresenter = new ShapesPresenter(canvas);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapesPresenter.addShapeRandom(Shape.Type.CIRCLE);
            }
        });

        FloatingActionButton fabRect = (FloatingActionButton) findViewById(R.id.fabRect);
        fabRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapesPresenter.addShapeRandom(Shape.Type.RECTANGLE);
            }
        });

        FloatingActionButton fabTriangle = (FloatingActionButton) findViewById(R.id.fabTriangle);
        fabTriangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapesPresenter.addShapeRandom(Shape.Type.TRIANGLE);
            }
        });


        FloatingActionButton fabUndo = (FloatingActionButton) findViewById(R.id.fabUndo);
        fabUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapesPresenter.undo();
            }
        });

        //  maxX = Math.round(canvas.getWidth());
        //  maxY = Math.round(canvas.getHeight());
        int maxX = 1079 - RADIUS;
        int maxY = 1420;
        shapesPresenter.setMaxX(maxX);
        shapesPresenter.setMaxY(maxY);

        Log.d(TAG, " onCreate max x= " + maxX + " maxy = " + maxY);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_canvas_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
