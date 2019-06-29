package com.solutions.rhythm.bankholidays2018;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

/**
 * Created by Rhythm on 1/16/2018.
 */

public class MyCalendarDecorator implements DayViewDecorator {

    private CalendarDay date;
    Drawable drawable;
    int colorId;


    public MyCalendarDecorator(Context context, CalendarDay date,int colorId)
    {
        this.date=date;
        drawable = ContextCompat.getDrawable(context,R.drawable.bg_date);
        this.colorId = colorId;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new ForegroundColorSpan(Color.GREEN));
        //view.setSelectionDrawable(drawable);
        //view.addSpan(new BackgroundColorSpan(Color.GREEN));
        view.addSpan(new DotSpan(10,colorId));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new AbsoluteSizeSpan(50));
    }
}
