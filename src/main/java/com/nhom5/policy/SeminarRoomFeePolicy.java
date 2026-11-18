package com.nhom5.policy;

/** Phòng seminar: 50.000đ mỗi giờ. */
public class SeminarRoomFeePolicy implements RoomFeePolicy {

    public static final long HOURLY_RATE = 50_000L;

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
        return "50.000đ/giờ";
    }
}
