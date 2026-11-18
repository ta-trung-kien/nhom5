package com.nhom5.policy;

/** Phòng máy chiếu: 20.000đ mỗi giờ. */
public class ProjectorRoomFeePolicy implements RoomFeePolicy {

    public static final long HOURLY_RATE = 20_000L;

    @Override
    public long calculateFee(long minutes) {
        return Math.round(minutes / 60.0 * HOURLY_RATE);
    }

    @Override
    public long getHourlyRate() {
        return HOURLY_RATE;
    }

    @Override
    public String describeRate() {
        return "20.000đ/giờ";
    }
}
