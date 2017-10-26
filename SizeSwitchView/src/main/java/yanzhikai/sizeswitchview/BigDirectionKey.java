package yanzhikai.sizeswitchview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * Created by yany on 2017/7/26.
 */

public class BigDirectionKey extends RelativeLayout {
    private final String TAG = "directionkeyview";
    private ImageView upKey,downKey,leftKey,rightKey,okKey;
    private Context mContext;
    private OnKeyClickListener mOnKeyClickListener;

    public BigDirectionKey(Context context) {
        super(context);
        init(context);
        initKeys();
    }

    public BigDirectionKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initKeys();
    }

    public BigDirectionKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initKeys();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    private void init(Context context){
        mContext = context;
    }

    //初始化
    private void initKeys(){
        okKey = new ImageView(mContext);
        upKey = new ImageView(mContext);
        downKey = new ImageView(mContext);
        leftKey = new ImageView(mContext);
        rightKey = new ImageView(mContext);

        okKey.setImageResource(R.drawable.background_ok);
        upKey.setImageResource(R.drawable.background_up);
        downKey.setImageResource(R.drawable.background_down);
        leftKey.setImageResource(R.drawable.background_left);
        rightKey.setImageResource(R.drawable.background_right);

        okKey.setClickable(true);
        upKey.setClickable(true);
        downKey.setClickable(true);
        leftKey.setClickable(true);
        rightKey.setClickable(true);

        okKey.setScaleType(ImageView.ScaleType.FIT_XY);
        upKey.setScaleType(ImageView.ScaleType.FIT_XY);
        downKey.setScaleType(ImageView.ScaleType.FIT_XY);
        leftKey.setScaleType(ImageView.ScaleType.FIT_XY);
        rightKey.setScaleType(ImageView.ScaleType.FIT_XY);

        okKey.setId(generateViewId());
        upKey.setId(generateViewId());
        downKey.setId(generateViewId());
        leftKey.setId(generateViewId());
        rightKey.setId(generateViewId());

        addView(okKey);
        addView(upKey);
        addView(downKey);
        addView(leftKey);
        addView(rightKey);

        setBackgroundResource(R.drawable.button_shape);

        //设置点击监听器
        for (int i = 0; i < getChildCount(); i++){
            getChildAt(i).setOnClickListener(new MyOnClickListener(i));
        }
    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float dw = widthSize / 10;
        float dh = heightSize / 10;

        int count = getChildCount();
        if (count != 5){
            try {
                throw new Exception("方向键的View个数不是5个");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < count; i++) {
            View view = getChildAt(i);

            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (dw * 3), MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (dh * 3), MeasureSpec.EXACTLY);

            view.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        okKey.layout(centerX - okKey.getMeasuredWidth()/2,
                centerY - okKey.getMeasuredHeight()/2,
                centerX + okKey.getMeasuredWidth()/2,
                centerY + okKey.getMeasuredHeight()/2);
        upKey.layout(centerX - okKey.getMeasuredWidth()/2,
                centerY - okKey.getMeasuredHeight()/2 - upKey.getMeasuredHeight(),
                centerX + okKey.getMeasuredWidth()/2,
                centerY - okKey.getMeasuredHeight()/2);
        downKey.layout(centerX - okKey.getMeasuredWidth()/2,
                centerY + okKey.getMeasuredHeight()/2,
                centerX + okKey.getMeasuredWidth()/2,
                centerY + okKey.getMeasuredHeight()/2 + downKey.getMeasuredHeight());
        leftKey.layout(centerX - okKey.getMeasuredWidth()/2 - leftKey.getMeasuredWidth(),
                centerY - okKey.getMeasuredHeight()/2,
                centerX - okKey.getMeasuredWidth()/2,
                centerY + okKey.getMeasuredHeight()/2);
        rightKey.layout(centerX + okKey.getMeasuredWidth()/2,
                centerY - okKey.getMeasuredHeight()/2,
                centerX + okKey.getMeasuredWidth()/2 + rightKey.getMeasuredWidth(),
                centerY + okKey.getMeasuredHeight()/2);
    }

    public void setOnKeyClickListener(OnKeyClickListener mOnKeyClickListener) {
        this.mOnKeyClickListener = mOnKeyClickListener;
    }

    public ImageView getOkKey() {
        return okKey;
    }

    public ImageView getUpKey() {
        return upKey;
    }

    public ImageView getDownKey() {
        return downKey;
    }

    public ImageView getLeftKey() {
        return leftKey;
    }

    public ImageView getRightKey() {
        return rightKey;
    }


    //重写一个带索引的OnClickListener，索引用于标识5个子View
    private class MyOnClickListener implements OnClickListener {
        private int index;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (mOnKeyClickListener != null) {
                mOnKeyClickListener.onKeyClick(index);
            }
        }
    }

    //暴露给外部的点击接口
    public interface OnKeyClickListener{
        public void onKeyClick(int index);
    }
}
