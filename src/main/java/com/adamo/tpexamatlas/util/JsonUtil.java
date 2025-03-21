package com.adamo.tpexamatlas.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    // Exclude fields that cause circular references
                    return f.getName().equals("borrowings") &&
                            (f.getDeclaringClass().getSimpleName().equals("Document") ||
                                    f.getDeclaringClass().getSimpleName().equals("User"));
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}