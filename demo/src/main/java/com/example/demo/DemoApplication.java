package com.example.demo;

import com.example.demo.GUI.Ventana;
import com.example.demo.config.Config;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;
import com.example.demo.utils.CsvLoader;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final Ventana ventana;

    public DemoApplication(Ventana ventana) {
        this.ventana=ventana;
    }

    public static void main(String[] args) {
        Config config= Config.getInstance("config/config.properties");
        String theme=config.getTheme();
        String dbPath=config.get("mysql5.url");
        String dbDriver=config.get("mysql5.driver");
        String dialect=config.get("mysql5.dialect");
        String user=config.get("mysql5.user");
        String pass=config.get("mysql5.pass");

        try {
            switch (theme){
                case "FlatDarkLaf":UIManager.setLookAndFeel(new FlatDarkLaf());break;
                case "FlatDarculaLaf" : UIManager.setLookAndFeel(new FlatDarculaLaf()); break;
                default : UIManager.setLookAndFeel(new FlatLightLaf());break;

            }

        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el theme "+ theme);
        }
        System.setProperty("DB_PATH", dbPath);
        System.setProperty("DRIVER",dbDriver);
        System.setProperty("USERNAME",user);
        System.setProperty("PASSWORD",pass);
        System.setProperty("DIALECT",dialect);
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
