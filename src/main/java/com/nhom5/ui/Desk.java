package com.nhom5.ui;

import com.nhom5.main.AppContext;

/** Cửa sổ chính để các màn hình dùng chung dữ liệu và chuyển tab. */
public interface Desk {

    AppContext app();

    String currentStudentId();

    void setStatus(String message);

    void showError(String message);

    void openBooking(String roomId);

    void reloadStudents(String preferredStudentId);

    void reloadBookings();

    void reloadRooms();
}
