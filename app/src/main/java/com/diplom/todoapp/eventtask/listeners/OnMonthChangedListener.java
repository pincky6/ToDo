package com.diplom.todoapp.eventtask.listeners;

import com.diplom.todoapp.eventtask.calendar.MaterialCalendarViewModel;

public interface OnMonthChangedListener {
    public void listen(int month, MaterialCalendarViewModel model);
}
