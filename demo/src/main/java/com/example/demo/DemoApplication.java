package com.example.demo;

import com.example.demo.GUI.Ventana;
import com.example.demo.config.Config;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final Ventana ventana;

    public DemoApplication(Ventana ventana) {
        this.ventana=ventana;
    }

    public static void main(String[] args) {
        Config config= Config.getInstance("config/config.properties");
        String theme=config.getTheme();
        try {
            switch (theme){
                case "FlatDarkLaf":UIManager.setLookAndFeel(new FlatDarkLaf());break;
                case "FlatDarculaLaf" : UIManager.setLookAndFeel(new FlatDarculaLaf()); break;
                default : UIManager.setLookAndFeel(new FlatLightLaf());break;

            }

        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el theme "+ theme);
        }
        SpringApplication app = new SpringApplication(DemoApplication.class);

        app.setHeadless(false);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() -> {
            //nombre de clases si hay clases iguales en distintos paquetes
            //se tiene que cambiar a @Component("productos")
            //                        public class panelProductos{..}
            // para evitar conflicto
            ventana.cambiarPanel("mainPanel");
            ventana.setVisible(true);
        });
    }
}
