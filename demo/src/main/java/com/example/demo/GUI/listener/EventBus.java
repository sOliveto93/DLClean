package com.example.demo.GUI.listener;

import com.example.demo.entity.Venta;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

@Component
public class EventBus {
    private final Map<String, List<Runnable>> listeners=new HashMap<>();
    private final Map<String, List<Consumer<Venta>>> ventaListeners = new HashMap<>();

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
    // =============================
    // Eventos tipados: Venta
    // =============================
    public void subscribe(String event, Consumer<Venta> handler) {
        ventaListeners.computeIfAbsent(event, e -> new ArrayList<>()).add(handler);
    }

    public void publish(String event, Venta data) {
        List<Consumer<Venta>> subs = ventaListeners.get(event);
        if (subs != null) {
            subs.forEach(h -> h.accept(data));
        }
    }


}
