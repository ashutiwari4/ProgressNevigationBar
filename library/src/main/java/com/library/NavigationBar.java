package com.library;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

/**
 * Created by user on 26/11/15.
 */
public class NavigationBar extends View {
    private static final int INDICATOR_ANIM_DURATION = 1500;
    int stateColor[] = {0xff777777, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff5599, 0xff6699ff};
    //    int stateColor[] = {0xff777777, 0xffff0000, 0xff00ff00, 0xff8a8a8a, 0xffff5599, 0xff6699ff};
    int stateTextColor[] = {0xff777777, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff5599, 0xff6699ff};
    boolean isBorder = false;

    boolean onlyBorder = false;
    //    int stateBorderColor[] = {0xff777777, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff5599, 0xff6699ff};
    int stateBorderColor[] = {0xff777777, 0xffff0000, 0xff00ff00, 0xff8a8a8a, 0xffff5599, 0xff6699ff};
    ObjectAnimator pagerAnim;
    int width;
    int height;
    float borderSize = 2;
    private int currentPosition = 0;
    private int tabCount = 2;
    private float tabTextSize = 20;
    private int indicatorColor = 0xff00ff00;
    //    private int indicatorColor = 0xff8a8a8a;
    private int centralLineColor = 0xff888888;
    private float tabRadius = 30, tabPadding = 20, centralLineHeight;
    private float radiusMin = tabRadius / 3;
    private float headMoveOffset = 0.6f;
    private float acceleration = 0.5f;
    private int marginsLine = 20;
    private ArrayList<NvTab> nvTabs;
    private int previousPosition = 0;
    private float op;
    private float p;
    private float radiusOffset;
    private float footMoveOffset = 1 - headMoveOffset;
    private Paint indicatorPaint, linePant;
    private Path path;
    private Point headPoint;
    private Point footPoint;
    private OnTabClick onTabClick;
    private OnTabSelected onTabSelected;

    public NavigationBar(Context context) {
        super(context);
        init(null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public OnTabSelected getOnTabSelected() {
        return onTabSelected;
    }

    public void setOnTabSelected(OnTabSelected tabSelected) {
        this.onTabSelected = tabSelected;
    }

    public int[] getStateTextColor() {
        return stateTextColor;
    }

    public void setStateTextColor(int[] stateTextColor) {
        this.stateTextColor = stateTextColor;
    }

    public boolean isOnlyBorder() {
        return onlyBorder;
    }

    public void setOnlyBorder(boolean onlyBorder) {
        this.onlyBorder = onlyBorder;
    }

    public int[] getStateBorderColor() {
        return stateBorderColor;
    }

    public void setStateBorderColor(int[] stateBorderColor) {
        this.stateBorderColor = stateBorderColor;
    }

    public OnTabClick getOnTabClick() {
        return onTabClick;
    }

    public void setOnTabClick(OnTabClick onTabClick) {
        this.onTabClick = onTabClick;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public void setBorder(boolean border) {
        isBorder = border;
    }

    private void init(AttributeSet attrs) {

        nvTabs = new ArrayList<>();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationBar);
        onlyBorder = a.getBoolean(R.styleable.NavigationBar_only_border, onlyBorder);
        isBorder = a.getBoolean(R.styleable.NavigationBar_enable_border, isBorder);

        int idItemTextColorId = a.getResourceId(R.styleable.NavigationBar_tab_color_array, 0);
        int idTabBorder = a.getResourceId(R.styleable.NavigationBar_tab_border_color_array, 0);
        int idStateTextColor = a.getResourceId(R.styleable.NavigationBar_tab_text_color_array, 0);

        TypedArray colortype = null;
        if (idItemTextColorId != 0)
            colortype = getResources().obtainTypedArray(idItemTextColorId);

        TypedArray borderColor = null;
        if (idTabBorder != 0)
            borderColor = getResources().obtainTypedArray(idTabBorder);

        TypedArray typeStateTextColor = null;
        if (idStateTextColor != 0)
            typeStateTextColor = getResources().obtainTypedArray(idStateTextColor);

        if (typeStateTextColor != null && typeStateTextColor.length() >= 6)
            for (int i = 0; i < 6; i++) {
                stateTextColor[i] = typeStateTextColor.getColor(i, stateTextColor[i]);
            }


//        if (borderColor != null && borderColor.length() >= 6)
//            for (int i = 0; i < 6; i++) {
//                stateBorderColor[i] = borderColor.getColor(i, stateBorderColor[i]);
//            }

        if (colortype != null && colortype.length() >= 6)
            for (int i = 0; i < 6; i++) {
                stateColor[i] = colortype.getColor(i, stateColor[i]);
            }
        tabTextSize = a.getDimension(R.styleable.NavigationBar_tab_text_size, tabTextSize);
        tabPadding = a.getDimension(R.styleable.NavigationBar_tab_padding, tabPadding);
        centralLineHeight = a.getDimension(R.styleable.NavigationBar_central_line_height, centralLineHeight);
        centralLineColor = a.getColor(R.styleable.NavigationBar_central_line_color, centralLineColor);
        indicatorColor = a.getColor(R.styleable.NavigationBar_tab_indicator_color, indicatorColor);
        borderSize = a.getDimension(R.styleable.NavigationBar_tab_strok_width, borderSize);
        a.recycle();

        headPoint = new Point();
        footPoint = new Point();
        path = new Path();
        indicatorPaint = new Paint();
        indicatorPaint.setStrokeJoin(Paint.Join.BEVEL);
        indicatorPaint.setStrokeCap(Paint.Cap.ROUND);
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        indicatorPaint.setStrokeWidth(5);

        linePant = new Paint();
        linePant.setStrokeJoin(Paint.Join.BEVEL);
        linePant.setStrokeCap(Paint.Cap.ROUND);
        linePant.setColor(centralLineColor);
        linePant.setAntiAlias(true);
        linePant.setStyle(Paint.Style.FILL_AND_STROKE);
        linePant.setStrokeWidth(centralLineHeight);

    }

    public void resetItems() {
        nvTabs.clear();
        currentPosition = 0;
        previousPosition = 0;
    }

    public int getTabCount() {
        return tabCount;
    }

    public void setTabCount(int tabCount) {
        if (tabCount <= 0)
            tabCount = 1;
        this.tabCount = tabCount;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0f + marginsLine, height >> 1, (width * p) - marginsLine, height >> 1, linePant);
        for (int i = 0; i < nvTabs.size(); i++)
            nvTabs.get(i).onDrawBG(canvas, p);
        drawSpring(canvas);
        for (int i = 0; i < nvTabs.size(); i++)
            nvTabs.get(i).onDrawFG(canvas, p);
    }

    private float getTabX(int position) {
        try {
            if (currentPosition > previousPosition)
                position = previousPosition;
            return nvTabs.get(position).getPoint().getX();
        } catch (Exception e) {
            return 0;
        }
    }

    private float getPositionDistance(int position) {
        int c = previousPosition - currentPosition;
        c *= c < 0 ? -1 : 1;
        float tarX = nvTabs.get(c).getPoint().getX();
        float oriX = nvTabs.get(0).getPoint().getX();
        return oriX - tarX;
    }

    public void onPageScrolled(int position, float positionOffset) {
        if (previousPosition == currentPosition) {
            positionOffset = 1;
        }
        if (previousPosition < currentPosition)
            position -= 1;
        else
            positionOffset = 1 - positionOffset;
        if (position == -1) {
            position = 0;
            positionOffset = 1;
        }
        if (nvTabs == null)
            return;
        if (position < nvTabs.size() - 1) {
            // radius
            float radiusOffsetHead = 0.5f;
            if (positionOffset < radiusOffsetHead) {
                headPoint.setRadius(radiusMin);
            } else {
                headPoint.setRadius(
                        ((positionOffset - radiusOffsetHead) / (1 - radiusOffsetHead) * radiusOffset + radiusMin));
            }
            float radiusOffsetFoot = 0.5f;
            if (positionOffset < radiusOffsetFoot) {
                footPoint.setRadius(
                        (1 - positionOffset / radiusOffsetFoot) * radiusOffset + radiusMin);
            } else {
                footPoint.setRadius(radiusMin);
            }

            // x
            float headX = 1f;
            if (positionOffset < headMoveOffset) {
                float positionOffsetTemp = positionOffset / headMoveOffset;
                headX = (float) ((Math
                        .atan(positionOffsetTemp * acceleration * 2 - acceleration) + (Math
                        .atan(acceleration))) / (2 * (Math.atan(acceleration))));
            }
            headPoint.setX(getTabX(position) - headX * getPositionDistance(position));
            float footX = 0f;
            if (positionOffset > footMoveOffset) {
                float positionOffsetTemp = (positionOffset - footMoveOffset) / (1 - footMoveOffset);
                footX = (float) ((Math
                        .atan(positionOffsetTemp * acceleration * 2 - acceleration) + (Math
                        .atan(acceleration))) / (2 * (Math.atan(acceleration))));
            }
            footPoint.setX(getTabX(position) - footX * getPositionDistance(position));

            // reset radius
            if (positionOffset == 0) {
                headPoint.setRadius(tabRadius);
                footPoint.setRadius(tabRadius);
            }
        } else {
            headPoint.setX(getTabX(position));
            footPoint.setX(getTabX(position));
            headPoint.setRadius(tabRadius);
            footPoint.setRadius(tabRadius);
        }

        // set indicator colors
        // https://github.com/TaurusXi/GuideBackgroundColorAnimation
//        if (indicatorColorsId != 0) {
//            float length = (position + positionOffset) / viewPager.getAdapter().getCount();
//            int progress = (int) (length * INDICATOR_ANIM_DURATION);
//            seek(progress);
//        }
//        HorizontalScrollView parent = (HorizontalScrollView) getParent();
//        int fullwidth = getChildAt(0).getWidth();
//        System.out.print("Full Width :" + fullwidth);
//        System.out.print("child Width :" + tabs.get(position).getWidth());
//        if (getTabX(position) > half_width)
//            lastScroll += tabs.get(position).getLeft();
//        parent.scrollTo(tabs.get(position).getLeft() - half_width, 0);

//        animator.setDuration(200);
//        animator.se
//        float length = (position + positionOffset-2) / tabCount;
//        int progress = (int) (length * INDICATOR_ANIM_DURATION);
//        seek(progress);
//      int xc= (int) nvTabs.get(position).getPoint().getX();
//        HorizontalScrollView scrollView= (HorizontalScrollView) getParent();
////      scrollView.scrollTo((int) (xc-getPositionDistance(position)+(xc-getPositionDistance(position)) * positionOffset), scrollView.getScrollY());
////        lastSeek=scrollView.getScrollX();;
//        int clickPosition = (int) (nvTabs.get(position).getPoint().getX() + tabRadius);
//        int d=clickPosition-lastSeek;
//        if (previousPosition<currentPosition)
//        {
//            Log.i("lastSeek clickPosition  +d", lastSeek + "  " + clickPosition+"  "+d);
//            scrollView.smoothScrollTo((int) (lastSeek +( d * .5 * positionOffset)), scrollView.getScrollY());
//        }
//        else
//        {if(d<half_width)
//            scrollView.smoothScrollTo((int) (lastSeek -half_width- (d * .5 * positionOffset)), scrollView.getScrollY());
//            else
//            scrollView.smoothScrollTo((int) (lastSeek  - (d * .5 * positionOffset)), scrollView.getScrollY());
//
//            Log.i("lastSeek clickPosition  -d", lastSeek + "  " + clickPosition+"  "+d);
//
//        }
//        if((previousPosition<currentPosition&&positionOffset>=1)||(previousPosition>currentPosition&&positionOffset<=0))
//            lastSeek=scrollView.getScrollX();

        //else if(positionOffset=1)

        // scrollView.postInvalidate();
        //     postInvalidate();
    }

    private void seek(long seekTime) {
        if (pagerAnim == null) {
            createPagerAnim();
        }
        pagerAnim.setCurrentPlayTime(seekTime);
    }

    private void createPagerAnim() {
        pagerAnim = ObjectAnimator
                .ofInt(getParent(), "scrollX", getWidth());
        pagerAnim.setInterpolator(new LinearInterpolator());
        pagerAnim.setDuration(INDICATOR_ANIM_DURATION);
    }

    private void drawSpring(Canvas canvas) {
        makePath();
        canvas.drawPath(path, indicatorPaint);
        canvas.drawCircle(headPoint.getX(), headPoint.getY(), headPoint.getRadius(),
                indicatorPaint);
        canvas.drawCircle(footPoint.getX(), footPoint.getY(), footPoint.getRadius(),
                indicatorPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(nvTabs == null)

        //else nvTabs.clear();

        height = getMeasuredHeight();
        tabRadius = (height - getPaddingTop() - getPaddingBottom()) / 2;
        width = (int) ((tabRadius * 2 + tabPadding) * tabCount + getPaddingRight() + getPaddingLeft() - tabPadding);
        int y = height >> 1, x = (int) (getPaddingLeft() + (nvTabs.size() == 0 ? tabRadius : (tabRadius * 2 + tabPadding) * nvTabs.size() + tabRadius));
        headPoint.setX(x);
        headPoint.setY(y);
        footPoint.setX(x);
        footPoint.setY(y);
        for (int i = nvTabs.size(); i < tabCount && tabCount > nvTabs.size(); i++) {
            Point point = new Point();
            point.setX(x);
            point.setY(y);
            point.setRadius(tabRadius);
            NvTab nvTab = new NvTab(this, point, (i + 1) + "", borderSize);
            nvTab.setStates(NvTab.STATES.DEFAULT);
            if (i < currentPosition)
                nvTab.setStates(NvTab.STATES.PEOCESSED);
            nvTabs.add(nvTab);
            x += tabRadius * 2 + tabPadding;
        }

        radiusMin = tabRadius / 3;
        radiusOffset = tabRadius - radiusMin;

        setMeasuredDimension(width, height);
        animateViewR(INDICATOR_ANIM_DURATION, currentPosition);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (onTabClick == null)
            return false;
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int touchPosition = -1, p = -1;
                for (int i = 0; i < nvTabs.size(); i++) {
                    if (nvTabs.get(i).getPoint().getRect().contains(x, y)) {
                        touchPosition = i;
                        p = previousPosition;
                        previousPosition = currentPosition;
                        currentPosition = i;
                        if (previousPosition != -1)
                            nvTabs.get(previousPosition).setStates(NvTab.STATES.PEOCESSED);
                        nvTabs.get(i).setStates(NvTab.STATES.CURRENT);
                        break;
                    }
                }
                if (touchPosition != -1) {
                    onTabClick.onTabClick(touchPosition, nvTabs.get(p), nvTabs.get(touchPosition));
                    animateViewR(INDICATOR_ANIM_DURATION, touchPosition);
                } else
                    invalidate();
        }
        return true;
    }


 /*   private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    final GestureDetector gdt = new GestureDetector(new GestureListener());
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
    }*/


    private void makePath() {

        float headOffsetX = (float) (headPoint.getRadius() * Math.sin(Math.atan(
                (footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX()))));
        float headOffsetY = (float) (headPoint.getRadius() * Math.cos(Math.atan(
                (footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX()))));

        float footOffsetX = (float) (footPoint.getRadius() * Math.sin(Math.atan(
                (footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX()))));
        float footOffsetY = (float) (footPoint.getRadius() * Math.cos(Math.atan(
                (footPoint.getY() - headPoint.getY()) / (footPoint.getX() - headPoint.getX()))));

        float x1 = headPoint.getX() - headOffsetX;
        float y1 = headPoint.getY() + headOffsetY;

        float x2 = headPoint.getX() + headOffsetX;
        float y2 = headPoint.getY() - headOffsetY;

        float x3 = footPoint.getX() - footOffsetX;
        float y3 = footPoint.getY() + footOffsetY;

        float x4 = footPoint.getX() + footOffsetX;
        float y4 = footPoint.getY() - footOffsetY;

        float anchorX = (footPoint.getX() + headPoint.getX()) / 2;
        float anchorY = (footPoint.getY() + headPoint.getY()) / 2;

        path.reset();
        path.moveTo(x1, y1);
        path.quadTo(anchorX, anchorY, x3, y3);
        path.lineTo(x4, y4);
        path.quadTo(anchorX, anchorY, x2, y2);
        path.lineTo(x1, y1);
    }

    public int getIndicatorColor() {
        return indicatorPaint.getColor();
    }

    public void setIndicatorColor(int color) {
        indicatorPaint.setColor(color);
    }

//    public void moveWithAnimation()
//    {
//        setPivotX(headPoint.getX());
//        setPivotY(footPoint.getY());
//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator oaX = ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 1f);
//        ObjectAnimator oaY = ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 1f);
//        ObjectAnimator oaA = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1f);
//        animatorSet.play(oaX).with(oaY).with(oaA);
//        animatorSet.setDuration(500);
//        animatorSet.setInterpolator(new OvershootInterpolator());
//        animatorSet.setStartDelay(300);
//        animatorSet.start();
//    }

    public void animateView(int time) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "p", 0, 1);
        objectAnimator.setInterpolator(new BounceInterpolator());
        objectAnimator.setDuration(time);
        objectAnimator.start();

    }

    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.p = p;

        invalidate();
    }

    public float getOp() {
        return op;
    }

    public void setOp(float op) {

        this.op = op;
        onPageScrolled(currentPosition, op);
        invalidate();
    }

    public void animateViewR(int time, int currentPosition) {
        if (nvTabs != null) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "op", 0, 1);
            objectAnimator.setInterpolator(new BounceInterpolator());
            objectAnimator.setDuration(time);

            objectAnimator.start();
            ObjectAnimator animator = ObjectAnimator.ofInt(getParent(), "scrollX",
                    (int) (nvTabs.get(currentPosition).getPoint()
                            .getX() - (tabRadius * 2 + tabPadding) * 2));
            animator.setInterpolator(new BounceInterpolator());
            animator.setDuration(time);

            animator.start();
        }

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean setCurrentPosition(int currentPosition) {

        try {
            if (nvTabs != null && currentPosition < nvTabs.size()) {
                this.previousPosition = currentPosition == 0 ? 0 : this.currentPosition;
                this.currentPosition = currentPosition;

                if (nvTabs != null) {
                    if (previousPosition != -1)
                        nvTabs.get(previousPosition).setStates(NvTab.STATES.PEOCESSED);
                    if (currentPosition > nvTabs.size() - 1) {
                        currentPosition -= 1;
                    }
                    nvTabs.get(currentPosition).setStates(NvTab.STATES.CURRENT);
                    if (onTabSelected != null)
                        onTabSelected.onTabSelected(currentPosition, nvTabs.get(previousPosition <= 0 ? 0 : previousPosition), nvTabs.get(currentPosition <= 0 ? 0 : currentPosition));
                }

                animateViewR(INDICATOR_ANIM_DURATION, currentPosition);
                Log.i("count", currentPosition + "");
                return true;
            } else {
                //onTabSelected.onTabSelected(this.currentPosition, nvTabs.get(nvTabs.size() - 2), nvTabs.get(nvTabs.size() - 1));
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //onTabSelected.onTabSelected(this.currentPosition, nvTabs.get(nvTabs.size() - 2), nvTabs.get(nvTabs.size() - 1));
            return false;
        }
    }

    public float getTabTextSize() {
        return tabTextSize;
    }

    public void setTabTextSize(float tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    public int[] getStateColor() {
        return stateColor;
    }

    public void setStateColor(int[] stateColor) {
        this.stateColor = stateColor;
    }

    public interface OnTabClick {
        void onTabClick(int touchPosition, NvTab prev, NvTab nvTab);
    }

    public interface OnTabSelected {
        void onTabSelected(int touchPosition, NvTab prev, NvTab nvTab);
    }


}
