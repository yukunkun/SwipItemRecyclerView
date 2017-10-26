package yanzhikai.sizeswitchview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by yany on 2017/7/26.
 */

public class SizeSwitchView extends RelativeLayout implements View.OnClickListener{
    private final String TAG = "SizeSwitchView";
    private Context mContext;
    //是否为小形态
    private boolean isSmallMode = true;

    private BigDirectionKey mBigDirectionKey;
    private ImageView mSmallKey;
    //大小形态宽高
    private int mSmallWidth = 60,mSmallHeight = 60;
    private int mBigWidth = 300,mBigHeight = 300;

    private float lastX = 0, lastY = 0;

    //当前状态能否被拖动
    private boolean isDraggable = true;

    //能否被拖动
    private boolean canDrag = true;

    //最小拖动量
    private float clickOffset = 3;

    //放大缩小动画
    private Animation smallShrinkAnimation,bigLargenAnimation,bigShrinkAnimation,smallLargenAnimation;

    public SizeSwitchView(Context context) {
        super(context);
        init(context);
    }

    public SizeSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SizeSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.layout_size_switch_view,this);
        mBigDirectionKey = (BigDirectionKey) findViewById(R.id.big_dk);
        mSmallKey = (ImageView) findViewById(R.id.small_dk);

//        setKeysVisibility();

        //要等到宽高初始化完，才能获取宽高，进行
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "onGlobalLayout: ");
                setMode(isSmallMode);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        initAnim();
        setSmallKeyClick();
//        setGesture();
    }

    //初始化动画
    private void initAnim(){
        smallShrinkAnimation = AnimationUtils.loadAnimation(mContext,R.anim.shrink);
        bigLargenAnimation = AnimationUtils.loadAnimation(mContext,R.anim.largen);
        smallShrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setKeysClickable(false);
                isDraggable = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMode(false);
                startAnimation(bigLargenAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bigLargenAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setKeysClickable(true);
                isDraggable = true;
                setSmallKeyClick();
                checkBoundary();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bigShrinkAnimation = AnimationUtils.loadAnimation(mContext,R.anim.shrink);
        smallLargenAnimation = AnimationUtils.loadAnimation(mContext,R.anim.largen);
        bigShrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setKeysClickable(false);
                isDraggable = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setMode(true);
                startAnimation(smallLargenAnimation);
                setSmallKeyClick();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        smallLargenAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setKeysClickable(true);
                isDraggable = true;
                checkBoundary();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void setKeysVisibility(){
        if (isSmallMode){
            mSmallKey.setVisibility(VISIBLE);
            mBigDirectionKey.setVisibility(GONE);
        }else {
            mSmallKey.setVisibility(GONE);
            mBigDirectionKey.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        if (isSmallMode){
            if (widthSize > 0){
                mSmallWidth = widthSize;
            }
            if (heightSize > 0){
                mSmallHeight = heightSize;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMode(boolean isSmallMode){
        this.isSmallMode = isSmallMode;
        Log.d(TAG, "setMode: ");
        //设置大小形态的宽高和位置
        if (isSmallMode){
            LayoutParams smallParams = (LayoutParams) getLayoutParams();
            smallParams.width = mSmallWidth;
            smallParams.height = mSmallHeight;
//            smallParams.leftMargin += (getWidth() - mSmallWidth)/2;
//            smallParams.bottomMargin += (getHeight() - mSmallHeight)/2;
//            Log.d(TAG, "setMode: smallParams" + smallParams.bottomMargin);
//            smallParams.topMargin += (getHeight() - mSmallHeight)/2;
//            smallParams.rightMargin += (getWidth() - mSmallWidth)/2;
            setLayoutParams(smallParams);
        }else {
            LayoutParams bigParams = (LayoutParams) getLayoutParams();
            bigParams.width = mBigWidth;
            bigParams.height = mBigHeight;
//            bigParams.leftMargin -= (mBigWidth - mSmallWidth)/2;
//            bigParams.bottomMargin -= (mBigHeight - mSmallHeight)/2;
//            Log.d(TAG, "setMode: bigParams" + bigParams.bottomMargin);
//            bigParams.topMargin -= (mBigHeight - mSmallHeight)/2;
//            bigParams.rightMargin -= (mBigHeight - mSmallHeight)/2;
            setLayoutParams(bigParams);
//            requestLayout();
        }
        setKeysVisibility();
        isDraggable = true;
    }

    //小形态点击
    private void setSmallKeyClick(){
        mSmallKey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "small onClick: ");
                toBigMode();
            }
        });
    }

    //变大
    public void toBigMode(){
        startAnimation(smallShrinkAnimation);
    }

    //变小
    public void toSmallMode(){
        startAnimation(bigShrinkAnimation);
    }

//    public void setKeysOnClickListener(OnClickListener onClickListener){
//        mBigDirectionKey.getOkKey().setOnClickListener(onClickListener);
//        mBigDirectionKey.getUpKey().setOnClickListener(onClickListener);
//        mBigDirectionKey.getDownKey().setOnClickListener(onClickListener);
//        mBigDirectionKey.getLeftKey().setOnClickListener(onClickListener);
//        mBigDirectionKey.getRightKey().setOnClickListener(onClickListener);
//    }

    @Override
    public void onClick(View v) {

    }

    public void setKeysClickable(boolean clickable){
        mSmallKey.setClickable(clickable);
        mBigDirectionKey.getOkKey().setClickable(clickable);
        mBigDirectionKey.getUpKey().setClickable(clickable);
        mBigDirectionKey.getDownKey().setClickable(clickable);
        mBigDirectionKey.getLeftKey().setClickable(clickable);
        mBigDirectionKey.getRightKey().setClickable(clickable);
        mBigDirectionKey.setClickable(clickable);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录DOWN事件的点击位置，因为不拦截DOWN事件，移动的时候需要这个起点坐标来计算距离。
                    lastX = ev.getX();
                    lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //拖动距离超过最小拖动量才会被拖动
                if (Math.abs(ev.getX() - lastX) > clickOffset && Math.abs(ev.getY() - lastY )> clickOffset){
                    if (canDrag && isDraggable && ev.getAction() == MotionEvent.ACTION_MOVE){
                        return true;
                    }
                }
                break;
        }
        return false;
    }

//    private void setGesture(){
//        final GestureDetector gestureDetector = new GestureDetector(mContext,new MyGestureListener());
//        gestureDetector.setIsLongpressEnabled(false);
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP){
//                    checkBoundary();
//                }
//                return gestureDetector.onTouchEvent(event) ;
//            }
//        });
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            //抬手就进行一次边界检测
            checkBoundary();
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            //进行移动操作
            if (canDrag && isDraggable) {
                int offX = (int) (event.getX() - lastX);
                int offY = (int) (event.getY() - lastY);
                LayoutParams params =
                        (LayoutParams) getLayoutParams();
                params.leftMargin = params.leftMargin + offX;
                params.rightMargin = params.rightMargin - offX;
                params.topMargin += offY;
                params.bottomMargin -= offY;
//                layout(getLeft() + offX,getTop() + offY,getRight() - offX,getBottom() - offY);
                setLayoutParams(params);

                return true;
            }
            return false;
        }
        return super.onTouchEvent(event);
    }

    //检测View是否跑出边界，如果是则移回来
    private void checkBoundary(){
        Log.d("checkBoundary", "checkBoundary: ");
        ViewGroup parent = (ViewGroup) getParent();
        boolean isOut = false;
        int moveX = 0;
        int moveY = 0;
        if (getLeft() < 0){
            moveX = getLeft();
            isOut = true;
            Log.d("checkBoundary", "getLeft(): " + getLeft());
        }
        if (getTop() < 0){
            moveY = getTop();
            isOut = true;
            Log.d("checkBoundary", "getTop(): " + getTop());
        }
        if (getRight() > parent.getWidth()){
            moveX = (getRight() - parent.getWidth());
            isOut = true;
            Log.d("checkBoundary", "moveX: " + moveX);
        }
        if (getBottom() > parent.getHeight()){
            moveY = (getBottom() - parent.getHeight());
            isOut = true;
            Log.d("checkBoundary", "moveY: " + moveY);
        }
        //有出界才进行LayoutParams的设置，节省性能
        if (isOut) {
            LayoutParams params =
                    (LayoutParams) getLayoutParams();
            params.setMargins(params.leftMargin - moveX,
                    params.topMargin - moveY,
                    params.rightMargin + moveX,
                    params.bottomMargin + moveY);
            setLayoutParams(params);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        if (canDrag && isDraggable) {
//            RelativeLayout.LayoutParams params =
//                    (LayoutParams) getLayoutParams();
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
////                lastX = x;
////                lastY = y;
//                Log.d(TAG, "onTouchEvent: ACTION_DOWN" + lastX);
//                return true;
////                break;
//            case MotionEvent.ACTION_MOVE:
//
//                    Log.d(TAG, "onTouchEvent: ACTION_MOVE" + lastX);
//                int offX = x - getWidth() / 2;
//                int offY = y - getHeight() / 2;
//
//                params.setMargins(params.leftMargin + offX ,
//                        params.topMargin + offY ,
//                        params.rightMargin - offX ,
//                        params.bottomMargin - offY);
//                setLayoutParams(params);
//
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "onTouchEvent: ACTION_UP");
//                checkBoundary();
//                break;
//        }
//
//        }
//
//        return super.onTouchEvent(event);
//
//    }

    public void setOnBigKeyClickListener(BigDirectionKey.OnKeyClickListener mOnKeyClickListener){
        mBigDirectionKey.setOnKeyClickListener(mOnKeyClickListener);
    }

    public void setBigHeight(int bigHeight) {
        this.mBigHeight = mBigHeight;
    }

    public void setBigWidth(int bigWidth) {
        this.mBigWidth = mBigWidth;
    }

    public void setSmallHeight(int smallHeight) {
        this.mSmallHeight = mSmallHeight;
    }

    public void setSmallWidth(int smallWidth) {
        this.mSmallWidth = mSmallWidth;
    }



//    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
//
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            return super.onSingleTapUp(e);
//        }
//
//        @Override
//        public boolean onDown(MotionEvent e) {
////            lastX = e.getX();
////            lastY = e.getY();
//            return true;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if (canDrag && isDraggable) {
////                if (isSmallMode) {
////                    int offX = (int) (e2.getX() - e1.getX());
////                    int offY = (int) (e2.getY() - e1.getY());
////                    Log.d(TAG, "onScroll: offX：" + offX);
////                    Log.d(TAG, "onScroll: offY: " + offY);
////                    LayoutParams params =
////                            (LayoutParams) getLayoutParams();
////                    params.setMargins(params.leftMargin + offX,
////                            params.topMargin + offY,
////                            params.rightMargin - offX,
////                            params.bottomMargin - offY);
////                    setLayoutParams(params);
////                }else {
//                    int offX = (int) (e2.getX() - lastX);
//                    int offY = (int) (e2.getY() - lastY);
//                    LayoutParams params =
//                            (LayoutParams) getLayoutParams();
//                    params.leftMargin = params.leftMargin + offX;
//                    params.rightMargin = params.rightMargin - offX;
//                    params.topMargin += offY;
//                    params.bottomMargin -= offY;
//                    setLayoutParams(params);
////                }
//
//                return true;
//            }
//            return false;
//        }
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Log.d(TAG, "onSingleTapConfirmed: ");
////            if (isSmallMode){
////                mSmallKey.performClick();
////            }
//            return super.onSingleTapConfirmed(e);
//        }
//    }


}
