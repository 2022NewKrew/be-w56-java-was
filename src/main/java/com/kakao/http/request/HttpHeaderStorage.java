package com.kakao.http.request;

import com.kakao.http.header.HttpHeader;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class HttpHeaderStorage {
    /**
     * Key of this map should be case-insensitive
     * because HTTP header names are case-insensitive, according to RFC 2616.
     * For example, initialize with {@code new TreeMap<>(String.CASE_INSENSITIVE_ORDER)}
     */
    private final Map<String, HttpHeader> nameMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public HttpHeaderStorage(List<HttpHeader> headerList) {
        for (HttpHeader header : headerList) {
            nameMap.put(header.key(), header);
        }
    }

    public Optional<HttpHeader> findByName(String name) {
        return Optional.ofNullable(this.nameMap.get(name));
    }
}
