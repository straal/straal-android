package com.straal.sdk.card;

import androidx.annotation.Nullable;

final class StringUtils {
    private StringUtils() {
    }

    static String unwrapNull(@Nullable String string) {
        return string == null ? "" : string;
    }
}
