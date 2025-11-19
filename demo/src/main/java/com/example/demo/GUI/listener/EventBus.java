package com.example.demo.GUI.listener;

import com.example.demo.utils.EventHandler;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EventBus {
    private final Map<String, List<Runnable>> listeners=new HashMap<>();
    private final Map<String, List<EventHandler<?>>> dataListeners = new HashMap<>();

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
    // ---- Eventos con parámetros (generic T) ----
    public <T> void subscribe(String event, EventHandler<T> listener) {
        dataListeners.computeIfAbsent(event, e -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(String event, T data) {
        List<EventHandler<?>> subs = dataListeners.get(event);

        if (subs != null) {
            for (EventHandler<?> listener : subs) {
                ((EventHandler<T>) listener).onEvent(data);
            }
        }
    }

}
