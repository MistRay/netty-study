package com.mistray.http.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.mistray.http.serialize.Serializer;


public class JSONSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
