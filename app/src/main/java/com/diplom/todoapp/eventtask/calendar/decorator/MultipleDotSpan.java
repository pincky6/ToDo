package com.diplom.todoapp.eventtask.calendar.decorator;

import static com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import androidx.annotation.NonNull;

import java.util.HashSet;

public class MultipleDotSpan implements LineBackgroundSpan {
    private final float radius;
    private HashSet<Integer> colors;

    public MultipleDotSpan() {
        this.radius = DEFAULT_RADIUS;
        colors = new HashSet<>();
    }

    public MultipleDotSpan(float radius) {
        this.radius = radius;
        colors = new HashSet<>();
    }


    public MultipleDotSpan(float radius, HashSet<Integer> colors) {
        this.radius = radius;
        this.colors = colors;
    }

    @Override
    public void drawBackground(
            @NonNull Canvas canvas, @NonNull Paint paint,
            int left, int right, int top, int baseline, int bottom,
            @NonNull CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        if(colors.isEmpty()) return;
        int leftMost = (colors.size() - 1) * -10;
        for (Integer color: colors) {
            int oldColor = paint.getColor();
            paint.setColor(color);
            canvas.drawCircle((left + right) / 2 - leftMost, bottom + radius, radius, paint);
            paint.setColor(oldColor);
            leftMost = leftMost + 20;
        }
    }
}