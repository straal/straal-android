package com.straal.sdk.card;

import android.support.annotation.Nullable;

final class StringUtils {
    private StringUtils() {
    }

    static String unwrapNull(@Nullable String string) {
        return string == null ? "" : string;
    }
}
