package me.devin.config;

public class ConversionUtils {
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value, Class<T> clazz) {
        if (value == null) {
            return null;
        }
        // If the value is already of the requested type, return it directly.
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        // Convert from String to a target type.
        if (value instanceof String) {
            String str = (String) value;
            try {
                if (clazz == Integer.class) {
                    return (T) Integer.valueOf(str);
                } else if (clazz == Double.class) {
                    return (T) Double.valueOf(str);
                } else if (clazz == Boolean.class) {
                    return (T) Boolean.valueOf(str);
                } else if (clazz == Long.class) {
                    return (T) Long.valueOf(str);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot convert \"" + str + "\" to " + clazz.getSimpleName());
            }
        }
        // Convert from Number if needed.
        if (value instanceof Number) {
            Number num = (Number) value;
            if (clazz == Integer.class) {
                return (T) Integer.valueOf(num.intValue());
            } else if (clazz == Double.class) {
                return (T) Double.valueOf(num.doubleValue());
            } else if (clazz == Long.class) {
                return (T) Long.valueOf(num.longValue());
            }
        }
        throw new IllegalArgumentException("Cannot convert " + value + " to " + clazz.getSimpleName());
    }
}