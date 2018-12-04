package mk.android.com.canvasdrawview.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import mk.android.com.canvasdrawview.R;
import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.presenter.CanvasPresenter;
import mk.android.com.canvasdrawview.util.Constants;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class CanvasDrawActivity extends AppCompatActivity {
    private static final String TAG = CanvasDrawActivity.class.getSimpleName();
    private CustomView canvas = null;
    CanvasPresenter canvasPresenter;
    private int maxY = 800; // average screen height
    private int maxX = 600; //average screen height

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_draw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.canvas = (CustomView) this.findViewById(R.id.canvasDrawView);

        canvasPresenter = new CanvasPresenter(canvas, this);
        setupActionButtons();
        getCanvasWidthAndHeight();
    }

    private void setupActionButtons() {
        FloatingActionButton fabCircle = (FloatingActionButton) findViewById(R.id.fabCircle);
        fabCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPresenter.addShapeRandom(Shape.Type.CIRCLE);
            }
        });

        FloatingActionButton fabRect = (FloatingActionButton) findViewById(R.id.fabRect);
        fabRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPresenter.addShapeRandom(Shape.Type.SQUARE);
            }
        });

        FloatingActionButton fabTriangle = (FloatingActionButton) findViewById(R.id.fabTriangle);
        fabTriangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPresenter.addShapeRandom(Shape.Type.TRIANGLE);
            }
        });


        FloatingActionButton fabUndo = (FloatingActionButton) findViewById(R.id.fabUndo);
        fabUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasPresenter.undo();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_canvas_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_stats) {
            startStatsView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startStatsView() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    private void getCanvasWidthAndHeight() {
        ViewTreeObserver viewTreeObserver = canvas.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    canvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    maxY = canvas.getHeight();
                    maxX = canvas.getWidth();
                    //Reduce radius so that shape isn't left incomplete at the edge
                    canvasPresenter.setMaxX(maxX - Constants.INSTANCE.getRADIUS());
                    int bottomButtonHeight = 100;
                    canvasPresenter.setMaxY(maxY - Constants.INSTANCE.getRADIUS() - bottomButtonHeight);
                    removeOnGlobalLayoutListener(canvas, this);
                    Log.d(TAG, " Screen max x= " + maxX + " maxy = " + maxY);
                }
            });
        }
    }

    /*Since global layout listener is called multiple times, remove it once we get the screen width and height
     */
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }
}
