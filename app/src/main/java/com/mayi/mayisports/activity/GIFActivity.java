package com.mayi.mayisports.activity;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.mayi.mayisports.R;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

/**
 * 测试Gif 图查看界面  暂无用
 */
public class GIFActivity extends AppCompatActivity {

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;
    private float oldDist;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();



    private ImageView imageview;
    private ImageView photoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        imageview = findViewById(R.id.imageview);
        photoview = findViewById(R.id.photoview);




        imageview.setOnTouchListener(mOnTouchListener);

        String url = "http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif";
        PictureUtils.show(url,imageview);
        PictureUtils.show(url,photoview);


        imageview.post(new Runnable() {
            @Override
            public void run() {
                int i = DisplayUtil.getScreenHeigth(GIFActivity.this) / 2 - imageview.getDrawable().getIntrinsicHeight() / 2;
                imageview.setScrollY(-i);
                int i1 = DisplayUtil.getScreenWidth(GIFActivity.this) / 2 - imageview.getDrawable().getIntrinsicWidth() / 2;
                imageview.setScrollX(-i1);
            }
        });

        String urlBase = Constant.BASE_URL+ "php/api.php?action=user&type=praise&user_id=200327&betId=1486668";
        RequestNetWorkUtils.getRequest(urlBase, new HttpParams(), new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });
    }



    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event)
        {
            ImageView view = (ImageView) v;


            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                // 多点触控
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f)
                    {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG)
                    {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    }
                    else if (mode == ZOOM)
                    {
                        float newDist = spacing(event);
                        if (newDist > 10f)
                        {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(matrix);

            return true;
        }

    };

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {

        int y1= DisplayUtil.getScreenHeigth(GIFActivity.this) / 2 ;

        int x1 = DisplayUtil.getScreenWidth(GIFActivity.this) / 2 ;

        float x = event.getX(0) + event.getX(1) - x1;
        float y = event.getY(0) + event.getY(1) - y1;
        point.set(x / 2, y / 2);
    }

}
