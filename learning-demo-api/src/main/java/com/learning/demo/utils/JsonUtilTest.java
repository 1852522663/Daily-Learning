package com.learning.demo.utils;

import java.util.List;
import java.util.Map;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-10-19
 */
public class JsonUtilTest {

//    public static void main(String[] args) {
//        testObjectToJsonWithFilter();
//        testJsonToObject();
//        testJsonToList();
//        testJsonToMap();
//        testObjectToJson();
//        testObjectToFormattedJson();
//    }

    public static void testJsonToObject() {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        User user = JsonUtil.jsonToObject(json, User.class);
        System.out.println(user.getName()); // Output: John
        System.out.println(user.getAge()); // Output: 30
        System.out.println(user.getCity()); // Output: New York
    }

    public static void testJsonToList() {
        String json = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";
        List<User> userList = JsonUtil.jsonToList(json, User.class);
        for (User user : userList) {
            System.out.println(user.getName() + " - " + user.getAge());
        }
        // Output:
        // John - 30
        // Jane - 25
    }

    public static void testJsonToMap() {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        Map<String, Object> userMap = JsonUtil.jsonToMap(json);
        System.out.println(userMap.get("name")); // Output: John
        System.out.println(userMap.get("age")); // Output: 30
        System.out.println(userMap.get("city")); // Output: New York
    }

    public static void testObjectToJson() {
        User user = new User("John", 30, "New York");
        String json = JsonUtil.objectToJson(user);
        System.out.println(json); // Output: {"name":"John","age":30,"city":"New York"}
    }

    public static void testObjectToFormattedJson() {
        User user = new User("John", 30, "New York");
        String json = JsonUtil.objectToFormattedJson(user);
        System.out.println(json);
        // Output:
        // {
        //     "name": "John",
        //     "age": 30,
        //     "city": "New York"
        // }
    }

    public static void testObjectToJsonWithFilter() {
        User user = new User("John", 30, "New York");
        String json = JsonUtil.objectToJsonWithFilter(user, "name", "city");
        System.out.println(json); // Output: {"name":"John","city":"New York"}
    }


}
