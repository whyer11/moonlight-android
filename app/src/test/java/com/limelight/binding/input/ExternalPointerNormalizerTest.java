package com.limelight.binding.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class ExternalPointerNormalizerTest {
    @Test
    public void identifiesOnlyCapturedXiaomiTouchpad() {
        ExternalPointerNormalizer normalizer = new ExternalPointerNormalizer(100);

        assertTrue(normalizer.isXiaomiCapturedTouchpad("Xiaomi Touch", true));
        assertFalse(normalizer.isXiaomiCapturedTouchpad("Xiaomi Touch", false));
        assertFalse(normalizer.isXiaomiCapturedTouchpad("Xiaomi Mouse", true));
        assertFalse(normalizer.isXiaomiCapturedTouchpad("", true));
        assertFalse(normalizer.isXiaomiCapturedTouchpad(null, true));
    }

    @Test
    public void usesCorrectedUnitBaselineInsteadOfLegacyFactor() {
        ExternalPointerNormalizer normalizer = new ExternalPointerNormalizer(100);

        assertEquals(235, normalizer.normalizeVerticalScroll(235.19f));
    }

    @Test
    public void preservesPositiveAndNegativeFractionalRemainders() {
        ExternalPointerNormalizer positive = new ExternalPointerNormalizer(25);
        assertEquals(0, positive.normalizeVerticalScroll(1));
        assertEquals(0, positive.normalizeVerticalScroll(1));
        assertEquals(0, positive.normalizeVerticalScroll(1));
        assertEquals(1, positive.normalizeVerticalScroll(1));

        ExternalPointerNormalizer negative = new ExternalPointerNormalizer(25);
        assertEquals(0, negative.normalizeVerticalScroll(-1));
        assertEquals(0, negative.normalizeVerticalScroll(-1));
        assertEquals(0, negative.normalizeVerticalScroll(-1));
        assertEquals(-1, negative.normalizeVerticalScroll(-1));
    }

    @Test
    public void clampsGainAndKeepsAxisRemaindersIndependent() {
        ExternalPointerNormalizer maximum = new ExternalPointerNormalizer(999);
        assertEquals(6, maximum.normalizeVerticalScroll(2));
        assertEquals(-6, maximum.normalizeHorizontalScroll(-2));

        ExternalPointerNormalizer minimum = new ExternalPointerNormalizer(-1);
        assertEquals(0, minimum.normalizeVerticalScroll(1));
        assertEquals(0, minimum.normalizeHorizontalScroll(1));
        assertEquals(0, minimum.normalizeVerticalScroll(1));
        assertEquals(0, minimum.normalizeHorizontalScroll(1));
        assertEquals(0, minimum.normalizeVerticalScroll(1));
        assertEquals(0, minimum.normalizeHorizontalScroll(1));
        assertEquals(1, minimum.normalizeVerticalScroll(1));
        assertEquals(1, minimum.normalizeHorizontalScroll(1));
    }

    @Test
    public void saturatesInsteadOfWrappingAndRejectsNonFiniteInput() {
        ExternalPointerNormalizer normalizer = new ExternalPointerNormalizer(300);

        assertEquals(Short.MAX_VALUE, normalizer.normalizeVerticalScroll(Float.MAX_VALUE));
        assertEquals(Short.MIN_VALUE, normalizer.normalizeHorizontalScroll(-Float.MAX_VALUE));
        assertEquals(0, normalizer.normalizeVerticalScroll(Float.NaN));
        assertEquals(0, normalizer.normalizeHorizontalScroll(Float.POSITIVE_INFINITY));
    }

    @Test
    public void gatesOnlyXiaomiMotionWhileWorkBuddyPrimaryIsDown() {
        ExternalPointerNormalizer normalizer = new ExternalPointerNormalizer(100);
        normalizer.observePrimaryButton("WorkBuddy Virtual Mouse", true);

        assertTrue(normalizer.shouldSuppressPhysicalMotion("Xiaomi Touch", true, true));
        assertFalse(normalizer.shouldSuppressPhysicalMotion("Xiaomi Touch", false, true));
        assertFalse(normalizer.shouldSuppressPhysicalMotion("Xiaomi Touch", true, false));
        assertFalse(normalizer.shouldSuppressPhysicalMotion("WorkBuddy Virtual Mouse", true, true));
        assertFalse(normalizer.shouldSuppressPhysicalMotion("Generic Mouse", true, true));

        normalizer.observePrimaryButton("Xiaomi Touch", false);
        assertTrue(normalizer.shouldSuppressPhysicalMotion("Xiaomi Touch", true, true));

        normalizer.observePrimaryButton("WorkBuddy Virtual Mouse", false);
        assertFalse(normalizer.shouldSuppressPhysicalMotion("Xiaomi Touch", true, true));
    }

    @Test
    public void resetReleasesOnlyOnceAndClearsAxisRemainders() {
        ExternalPointerNormalizer normalizer = new ExternalPointerNormalizer(25);
        normalizer.observePrimaryButton("WorkBuddy Virtual Mouse", true);
        assertEquals(0, normalizer.normalizeVerticalScroll(1));
        assertEquals(0, normalizer.normalizeVerticalScroll(1));
        assertEquals(0, normalizer.normalizeVerticalScroll(1));

        assertTrue(normalizer.reset());
        assertFalse(normalizer.reset());
        assertEquals(0, normalizer.normalizeVerticalScroll(1));
    }
}
