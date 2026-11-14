
package com.example.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Component
@Slf4j
public class LoggingSanitizer {

    private static final String LAZY_NOT_INITIALIZED = "[LAZY_NOT_INITIALIZED]";
    private static final String CIRCULAR_REFERENCE = "[CIRCULAR_REFERENCE]";

    public String sanitize(Object value, int maxLength) {

        if (value == null) {
            return "null";
        }

        String result = format(value, new IdentityHashMap<>());

        if (result.length() > maxLength) {
            return result.substring(0, maxLength) + "...[TRUNCATED]";
        }

        return result;
    }

    private String format(
            Object value,
            IdentityHashMap<Object, Boolean> visited) {

        if (value == null) {
            return "null";
        }

        if (isSimpleType(value.getClass())) {
            return String.valueOf(value);
        }

        // Protection contre le chargement des proxies et collections LAZY
        if (!Hibernate.isInitialized(value)) {
            return LAZY_NOT_INITIALIZED;
        }

        // Détection des références circulaires
        if (visited.containsKey(value)) {
            return CIRCULAR_REFERENCE;
        }

        visited.put(value, Boolean.TRUE);

        try {

            if (value instanceof Collection<?> collection) {
                List<String> result = new ArrayList<>();

                for (Object item : collection) {
                    result.add(format(item, visited));
                }

                return result.toString();
            }

            if (value instanceof Map<?, ?> map) {
                Map<String, String> result = new LinkedHashMap<>();

                map.forEach((key, val) ->
                        result.put(String.valueOf(key), format(val, visited)));

                return result.toString();
            }

            if (value.getClass().isArray()) {
                List<String> result = new ArrayList<>();

                for (int i = 0; i < Array.getLength(value); i++) {
                    result.add(format(Array.get(value, i), visited));
                }

                return result.toString();
            }

            return formatObject(value, visited);

        } finally {
            visited.remove(value);
        }
    }

    private String formatObject(
            Object value,
            IdentityHashMap<Object, Boolean> visited) {

        List<String> fields = new ArrayList<>();

        for (Field field : getAllFields(value.getClass())) {

            if (Modifier.isStatic(field.getModifiers())
                    || field.isSynthetic()
                    || field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }

            try {
                if (!field.trySetAccessible()) {
                    continue;
                }

                Object fieldValue = field.get(value);

                // Ignore les relations LAZY non initialisées
                if (fieldValue != null && !Hibernate.isInitialized(fieldValue)) {
                    fields.add(field.getName() + "=" + LAZY_NOT_INITIALIZED);
                    continue;
                }

                fields.add(field.getName() + "="
                        + format(fieldValue, visited));

            } catch (Exception exception) {
                fields.add(field.getName() + "=[UNAVAILABLE]");
            }
        }

        return value.getClass().getSimpleName()
                + "{" + String.join(", ", fields) + "}";
    }

    private List<Field> getAllFields(Class<?> type) {

        List<Field> fields = new ArrayList<>();

        while (type != null && type != Object.class) {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
            type = type.getSuperclass();
        }

        return fields;
    }

    private boolean isSimpleType(Class<?> type) {

        return type.isPrimitive()
                || type.isEnum()
                || Number.class.isAssignableFrom(type)
                || CharSequence.class.isAssignableFrom(type)
                || Boolean.class.equals(type)
                || Character.class.equals(type)
                || Date.class.isAssignableFrom(type)
                || TemporalAccessor.class.isAssignableFrom(type)
                || UUID.class.equals(type);
    }
}