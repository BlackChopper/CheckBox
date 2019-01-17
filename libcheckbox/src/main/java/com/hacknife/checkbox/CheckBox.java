package com.hacknife.checkbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
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
        setChecked(checkedStatus);
        setOnClickListener(this);
    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            setBackgroundResource(checked);
        } else {
            setBackgroundResource(uncheck);
        }
        if (checkedStatus != isChecked) {
            checkedStatus = isChecked;
            if (checkedChangeListener != null)
                checkedChangeListener.onCheckedChanged(null, isChecked);
            if (listener != null)
                listener.onCheckedChanged(this, isChecked);
        }


    }

    @Override
    public void onClick(View v) {
        boolean last = checkedStatus;
        checkedStatus = !checkedStatus;
        if (listener != null)
            if (!listener.onCheckedChanged(this, checkedStatus))
                checkedStatus = !checkedStatus;
        if (last == checkedStatus)
            return;
        if (checkedChangeListener != null)
            checkedChangeListener.onCheckedChanged(null, checkedStatus);
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

    public boolean isChecked() {
        return checkedStatus;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        setChecked(checkedStatus);
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
        boolean onCheckedChanged(View view, boolean isChecked);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener;

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }
}
