package com.limelight.binding.input;

public final class ExternalPointerNormalizer {
    static final String XIAOMI_TOUCH = "Xiaomi Touch";
    static final String WORKBUDDY_MOUSE = "WorkBuddy Virtual Mouse";

    private static final int MIN_SCROLL_PERCENT = 25;
    private static final int MAX_SCROLL_PERCENT = 300;

    private final double scrollGain;
    private final AxisAccumulator verticalScroll = new AxisAccumulator();
    private final AxisAccumulator horizontalScroll = new AxisAccumulator();
    private boolean workBuddyPrimaryDown;

    public ExternalPointerNormalizer(int scrollPercent) {
        int boundedPercent = Math.max(MIN_SCROLL_PERCENT,
                Math.min(MAX_SCROLL_PERCENT, scrollPercent));
        scrollGain = boundedPercent / 100.0;
    }

    public boolean isXiaomiCapturedTouchpad(String deviceName,
                                             boolean capturedRelative) {
        return capturedRelative && XIAOMI_TOUCH.equals(deviceName);
    }

    public void observePrimaryButton(String deviceName, boolean primaryDown) {
        if (WORKBUDDY_MOUSE.equals(deviceName)) {
            workBuddyPrimaryDown = primaryDown;
        }
    }

    public boolean shouldSuppressPhysicalMotion(String deviceName,
                                                 boolean capturedRelative,
                                                 boolean motionLikeAction) {
        return workBuddyPrimaryDown
                && motionLikeAction
                && isXiaomiCapturedTouchpad(deviceName, capturedRelative);
    }

    public short normalizeVerticalScroll(float rawAxis) {
        return verticalScroll.scale(rawAxis, scrollGain);
    }

    public short normalizeHorizontalScroll(float rawAxis) {
        return horizontalScroll.scale(rawAxis, scrollGain);
    }

    public boolean reset() {
        boolean primaryWasDown = workBuddyPrimaryDown;
        workBuddyPrimaryDown = false;
        verticalScroll.reset();
        horizontalScroll.reset();
        return primaryWasDown;
    }

    private static final class AxisAccumulator {
        private double remainder;

        short scale(float raw, double gain) {
            double value = raw * gain + remainder;
            if (!Double.isFinite(value)) {
                remainder = 0;
                return 0;
            }
            if (value >= Short.MAX_VALUE) {
                remainder = 0;
                return Short.MAX_VALUE;
            }
            if (value <= Short.MIN_VALUE) {
                remainder = 0;
                return Short.MIN_VALUE;
            }
            int whole = (int) value;
            remainder = value - whole;
            return (short) whole;
        }

        void reset() {
            remainder = 0;
        }
    }
}
