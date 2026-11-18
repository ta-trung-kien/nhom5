package com.nhom5.policy;

/** Phòng thường không thu phí. */
public class FreeRoomFeePolicy implements RoomFeePolicy {

    @Override
    public long calculateFee(long minutes) {
        return 0L;
    }

    @Override
    public long getHourlyRate() {
        return 0L;
    }

    @Override
    public String describeRate() {
        return "Miễn phí";
    }
}
