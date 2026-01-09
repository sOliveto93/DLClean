package com.example.demo.config;

import java.io.FileInputStream;
import java.util.*;

public class Config {
    private static Config instance;
    private final Properties props;

    private Config(String path){
        props = new Properties();
        try(FileInputStream fis = new FileInputStream(path)){
            props.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar config: " + path, e);
        }
    }

    public static Config getInstance(String path){
        if(instance==null){
            instance= new Config(path);
        }
        return instance;
    }

    public String get(String key) {
        return props.getProperty(key);
    }
    public Map<String,String> imagenes(){
        Map<String,String> mapaImagenes=new HashMap<>();

        //esto devuelve las claves
        for(String key: props.stringPropertyNames()){
            if(key.endsWith("PNG") || key.endsWith("JPG")){
                //esto devuelve el valor
                String ruta= props.getProperty(key);

                if(ruta != null && !ruta.isEmpty()){
                    mapaImagenes.put(key,ruta);
                }
            }
        }
        return mapaImagenes;
    }
    // Getter específico para el tema
    public String getTheme() {
        return props.getProperty("theme", "FlatLightLaf"); // Por defecto FlatLightLaf
    }
}
