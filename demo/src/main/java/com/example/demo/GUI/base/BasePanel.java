package com.example.demo.GUI.base;

import com.example.demo.GUI.listener.EventBus;

import javax.swing.*;

public abstract class BasePanel extends JPanel {
    protected final EventBus eventBus;
    public BasePanel(EventBus eventBus){
        this.eventBus=eventBus;
    }
    protected abstract void inicializarComponentes();
    public void init(){}
}
