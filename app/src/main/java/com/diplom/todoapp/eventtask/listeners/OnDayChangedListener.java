package com.diplom.todoapp.eventtask.listeners;

import com.diplom.todoapp.eventtask.calendar.MaterialCalendarViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public interface OnDayChangedListener {
        public void listen(CalendarDay day, MaterialCalendarViewModel model);
}
