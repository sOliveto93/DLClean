package com.example.demo.utils;

@FunctionalInterface
public interface EventHandler<T> {
    void onEvent(T data);
}
