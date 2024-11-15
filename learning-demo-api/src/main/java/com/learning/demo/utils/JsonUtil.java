package com.learning.demo.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将JSON字符串解析为指定类型的Java对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to object", e);
        }
    }

    /**
     * 将JSON字符串解析为List<T>类型的Java对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to list", e);
        }
    }

    /**
     * 将JSON字符串解析为Map<K, V>类型的Java对象
     *
     * @param json
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> jsonToMap(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<Map<K, V>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to map", e);
        }
    }

    /**
     * 将Java对象转换为JSON字符串
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    /**
     * 将Java对象转换为格式化的JSON字符串
     *
     * @param object
     * @return
     */
    public static String objectToFormattedJson(Object object) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to formatted JSON", e);
        }
    }

    /**
     * 将Java对象转换为JSON字符串，仅包括指定的属性
     *
     * @param object
     * @param includeProperties
     * @return
     */
    public static String objectToJsonWithFilter(Object object, String... includeProperties) {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("propertyFilter", SimpleBeanPropertyFilter.filterOutAllExcept(Set.of(includeProperties)));
            MAPPER.setFilterProvider(filterProvider);
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON with filter", e);
        }
    }
}