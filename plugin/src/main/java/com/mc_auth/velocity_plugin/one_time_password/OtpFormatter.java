package com.mc_auth.velocity_plugin.one_time_password;

import org.jetbrains.annotations.NotNull;

public class OtpFormatter {
    /**
     * Takes a 1 to 6 digit long number and formats it
     * into {@code "### ###"} with leading zeros if needed
     *
     * @param num The number to format
     * @return The formatted number as a {@link String}, never null
     */
    public static @NotNull String formatOTP(long num) {
        String numStr = Long.toString(num);

        if (numStr.length() > 6) {
            throw new IllegalArgumentException("Argument num may not consist of more than 6 digits");
        }

        StringBuilder sb = new StringBuilder(7);

        if (numStr.length() != 6) {
            int zeroCount = 6 - numStr.length();
            sb.append("000000", 0, zeroCount);
        }

        sb.append(numStr);
        sb.insert(3, ' ');

        return sb.toString();
    }
}
