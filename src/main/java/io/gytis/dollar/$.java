package io.gytis.dollar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class $ {

    public static <T> boolean equals(@Nullable final T object1, @Nullable final T object2) {
        if (object1 == object2) {
            return true;
        }
        return object1 != null && object1.equals(object2);
    }

    @Contract("null -> true")
    public static boolean isBlank(@Nullable final CharSequence chars) {
        final int length = chars == null ? 0 : chars.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(chars.charAt(i))) {
                return false;
            }
        }
        return length == 0;
    }

    @Contract("null -> true")
    public static boolean isEmpty(@Nullable final CharSequence chars) {
        return chars == null || chars.length() == 0;
    }

    @Contract("null, null -> null")
    public static <T extends CharSequence> T defaultIfBlank(@Nullable final T string, @Nullable final T defaultString) {
        return isBlank(string) ? defaultString : string;
    }

    @Contract("null, null -> null")
    public static <T extends CharSequence> T defaultIfEmpty(@Nullable final T string, @Nullable final T defaultString) {
        return isEmpty(string) ? defaultString : string;
    }

    @Contract("null, null -> null")
    public static <T extends CharSequence> T getIfBlank(@Nullable final T string, @Nullable final Supplier<T> defaultSupplier) {
        return isBlank(string)
                ? (defaultSupplier == null ? null : defaultSupplier.get())
                : string;
    }

    @Contract("null, null -> null")
    public static <T extends CharSequence> T getIfEmpty(@Nullable final T string, @Nullable final Supplier<T> defaultSupplier) {
        return isEmpty(string)
                ? (defaultSupplier == null ? null : defaultSupplier.get())
                : string;
    }

    @Contract("null -> null")
    public static String trim(@Nullable final String string) {
        return string == null ? null : string.trim();
    }

    @Contract("null, _ -> null; _, null -> null")
    public static <T, R> R ifNotNull(@Nullable T input, @Nullable Function<T, R> mapper) {
        return input != null
                ? (mapper == null ? null : mapper.apply(input))
                : null;
    }

    @Contract("null, null -> null")
    public static <T> T ifNull(@Nullable T input, @Nullable Supplier<? extends T> mapper) {
        return input == null
                ? (mapper == null ? null : mapper.get())
                : input;
    }

    /**
     * @return first non-null element
     */
    @Contract("null, null -> null")
    public static <T> T or(@Nullable T first, @Nullable T second) {
        return first != null ? first : second;
    }

    /**
     * @return first non-null element
     */
    @Contract("null, null, null -> null")
    public static <T> T or(@Nullable T first, @Nullable T second, @Nullable T third) {
        return first != null
                ? first
                : (second != null ? second : third);
    }

    /**
     * @return first non-null element
     */
    @SafeVarargs
    public static <T> T or(T... values) {
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (CollectionUtils.isEmpty(collection)) {
            return Map.of();
        }
        final Map<K, V> result = new HashMap<K, V>(collection.size());
        for (T item : collection) {
            result.put(keyMapper.apply(item), valueMapper.apply(item));
        }
        return result;
    }

    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        return toMap(collection, keyMapper, Function.identity());
    }

    public static <T, M extends Collection<T>, R extends Collection<T>> R flatMapTo(R result, Collection<T> collection, Function<T, M> mapper) {
        if (CollectionUtils.isEmpty(collection)) {
            return result;
        }
        for (T element : collection) {
            result.addAll(mapper.apply(element));
        }

        return result;
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(collection)) {
            return List.of();
        }
        final List<T> result = new ArrayList<>(collection.size());
        for (T element : collection) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static <T, R> List<R> map(Collection<T> collection, Function<T, ? extends R> mapper) {
        if (CollectionUtils.isEmpty(collection)) {
            return List.of();
        }
        final List<R> result = new ArrayList<>(collection.size());
        for (T element : collection) {
            result.add(mapper.apply(element));
        }
        return result;
    }

    public static <T, R> List<R> mapNotNull(Collection<T> collection, Function<T, ? extends R> mapper) {
        if (CollectionUtils.isEmpty(collection)) {
            return List.of();
        }
        final List<R> result = new ArrayList<>(collection.size());
        for (T element : collection) {
            R mappedValue = mapper.apply(element);
            if (mappedValue != null) {
                result.add(mappedValue);
            }
        }
        return result;
    }

    public static <T> T find(List<T> collection, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        for (T element : collection) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        if (list1 != null && list2 != null) {
            return new HashSet<>(list1).equals(new HashSet<>(list2));
        }
        return Objects.equals(list1, list2);
    }
}
