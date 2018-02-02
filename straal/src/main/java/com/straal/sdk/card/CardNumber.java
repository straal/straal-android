package com.straal.sdk.card;

/**
 * Wrapper class for card number.
 */
public class CardNumber {
    public final String value;

    public CardNumber(String value) {
        this.value = StringUtils.unwrapNull(value);
    }

    /**
     * @return sanitized card number
     */
    public String sanitized() {
        return value.replaceAll("[\\s-]+", "");
    }
}
