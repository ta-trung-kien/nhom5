package com.nhom5.model;

/** Hành vi của đối tượng có thể được đặt lịch. */
public interface Bookable {

    String getBookableCode();

    boolean isAvailableForBooking();
}
