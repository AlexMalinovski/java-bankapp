package ru.practicum.bankapp.chassis.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class UtilMoney {
    public BigDecimal toPrice(long centsPrice) {
        return BigDecimal.valueOf(centsPrice, 2);
    }

    public static Long toPriceInternal(BigDecimal price) {
        if (price == null) {
            return null;
        }
        long ceil = price.longValue();
        long digits = price.subtract(new BigDecimal(ceil)).multiply(new BigDecimal(100)).longValue();

        return 100 * ceil + digits;
    }
}
