package com.example.demo.GUI.listener;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventBus {
    private final Map<String, List<Runnable>> listeners=new HashMap<>();

    public void subscribe(String event, Runnable listener) {

        if (!listeners.containsKey(event)) {

            listeners.put(event, new ArrayList<Runnable>());
        }

        listeners.get(event).add(listener);
    }
    public void publish(String event){
        List<Runnable> subs=listeners.get(event);
        if(subs != null){
            subs.forEach(runnable->runnable.run());
        }
    }
}
