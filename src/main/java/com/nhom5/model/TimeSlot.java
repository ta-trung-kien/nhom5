package com.nhom5.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/** Khung giờ một lần sử dụng phòng. */
public class TimeSlot {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isEndAfterStart() {
        return startTime != null && endTime != null && endTime.isAfter(startTime);
    }

    public long getMinutes() {
        if (!isEndAfterStart()) {
            return 0L;
        }
        return Duration.between(startTime, endTime).toMinutes();
    }

    /** Hai khung trùng nhau khi giao nhau và không chỉ chạm ở mốc đầu/cuối. */
    public boolean overlaps(TimeSlot other) {
        if (other == null || date == null || other.date == null || !date.equals(other.date)) {
            return false;
        }
        if (!isEndAfterStart() || !other.isEndAfterStart()) {
            return false;
        }
        return startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
