package com.blackchopper.checkbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * date    ：2018/4/26 19:24
 * project : CheckBox
 */
public class CheckBox extends ImageView implements View.OnClickListener, Animation.AnimationListener {
    public static final String TRUE = "true";
    public static final String CHECKED = "state_checked";
    public static final String DRAWABLE = "drawable";
    int checked, uncheck;
    boolean checkedStatus;
    private int duration;

    public CheckBox(Context context) {
        this(context, null);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckBox);
        checkedStatus = ta.getBoolean(R.styleable.CheckBox_android_checked, false);
        duration = ta.getInt(R.styleable.CheckBox_duration, 500);
        XmlResourceParser parser = getResources().getXml(ta.getResourceId(R.styleable.CheckBox_android_background, -1));
        try {
            int event = parser.getEventType();
            while (event != XmlResourceParser.END_DOCUMENT) {
                switch (event) {
                    case XmlResourceParser.START_TAG:
                        int count = parser.getAttributeCount();
                        Attr attr = new Attr();
                        for (int i = 0; i < count; i++) {
                            if (parser.getAttributeName(i).equalsIgnoreCase(CHECKED)) {
                                if (parser.getAttributeValue(i).equalsIgnoreCase(TRUE))
                                    attr.chech = true;
                                else
                                    attr.chech = false;
                            }
                            if (parser.getAttributeName(i).equalsIgnoreCase(DRAWABLE)) {
                                attr.drawable = Integer.parseInt(parser.getAttributeValue(i).substring(1));
                            }
                        }
                        if (count == 0) break;
                        if (attr.chech) {
                            checked = attr.drawable;
                        } else {
                            uncheck = attr.drawable;
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ta.recycle();
        setCheck(checkedStatus);
        setOnClickListener(this);
    }

    public void setCheck(boolean b) {
        if (b) {
            setBackgroundResource(checked);
        } else {
            setBackgroundResource(uncheck);
        }
    }

    @Override
    public void onClick(View v) {
        boolean last = checkedStatus;
        checkedStatus = checkedStatus != true;
        if (listener != null)
            if (!listener.onChange(this, checkedStatus))
                checkedStatus = checkedStatus != true;
        if (last == checkedStatus)
            return;
        startChangeAnimation();
    }

    private void startChangeAnimation() {
        AlphaAnimation alpa = new AlphaAnimation(1f, 0f);
        alpa.setDuration(duration / 2);
        alpa.setFillAfter(true);
        alpa.setFillBefore(false);
        alpa.setInterpolator(new LinearInterpolator());
        alpa.setAnimationListener(this);
        startAnimation(alpa);
    }

    public boolean checked() {
        return checkedStatus;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        setCheck(checkedStatus);
        clearAnimation();
        AlphaAnimation alpa = new AlphaAnimation(0f, 1f);
        alpa.setDuration(duration / 2);
        alpa.setFillAfter(true);
        alpa.setInterpolator(new LinearInterpolator());
        startAnimation(alpa);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    private static class Attr {
        public boolean chech;
        public int drawable;
    }

    OnCheckedChangeListener listener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        boolean onChange(View view, boolean isChecked);
    }
}
