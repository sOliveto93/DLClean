package com.example.demo.utils;

import com.example.demo.config.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImageLoader {

    private Map<String, BufferedImage> mapaImagenes;
    private static ImageLoader instance;
    private Config instanceConfig;
    private ImageLoader(){
        mapaImagenes=new HashMap<>();
        instanceConfig=Config.getInstance("config/config.properties");
        cargarImagenes();
    }

    public static ImageLoader getInstance() {
        if(instance == null){
            instance = new ImageLoader();
        }
        return instance;
    }

    public void cargarImagenes(){
            Map<String,String> mapaRutas = instanceConfig.imagenes();
            for (String key : mapaRutas.keySet()) {
                try {
                    BufferedImage img = ImageIO.read(new File(mapaRutas.get(key)));

                    if (img == null) {
                        System.err.println("⚠ La imagen [" + key + "] tiene formato no soportado o está vacía");
                    }
                    mapaImagenes.put(key, img); // puede ser null para que el programa arranque igual
                } catch (IOException e) {
                    mapaImagenes.put(key, null);
                    System.err.println("⚠ No se pudo cargar imagen [" + key + "]");
                }
    }}


    public Optional<BufferedImage> getImage(String key){
        return Optional.ofNullable(mapaImagenes.get(key));
    }
}
