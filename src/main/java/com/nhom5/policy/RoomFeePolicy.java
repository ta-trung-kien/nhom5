package com.nhom5.policy;

/**
 * Chính sách tính phí phòng.
 * Muốn thêm loại phòng mới thì tạo một class chính sách mới, không sửa công thức cũ.
 */
public interface RoomFeePolicy {

    /** Phí theo số phút sử dụng, làm tròn đến đồng. */
    long calculateFee(long minutes);

    long getHourlyRate();

    String describeRate();
}
