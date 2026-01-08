package com.example.demo.utils;

import com.example.demo.Enum.Categoria;
import com.example.demo.entity.Producto;
import com.example.demo.service.ProductoService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvLoader {
    public static List<Producto> cargarProductos(String path) throws Exception {
        List<Producto> productos = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        br.readLine(); // saltear header

        while ((line = br.readLine()) != null) {
            String[] campos = line.split(",", -1);

            // Trim para limpiar espacios
            for (int i = 0; i < campos.length; i++) {
                campos[i] = campos[i].trim();
            }

           Long codigo = campos[0].isEmpty() ? 0L : Long.parseLong(campos[0]);
            LocalDate fechaCreacion = campos[1].isEmpty() ? LocalDate.now() : LocalDate.parse(campos[1], formatter);
            double precioVenta = campos[2].isEmpty() ? 0 : Double.parseDouble(campos[2]);
            Categoria categoria = campos[3].isEmpty() ? Categoria.SIN_CATEGORIA : Categoria.values()[Integer.parseInt(campos[3])];
            String nombre = campos[4];
            String descripcion = campos[5];
            double precioCosto = campos[6].isEmpty() ? 0 : Double.parseDouble(campos[6]);
            int stockMinimo = campos[7].isEmpty() ? 0 : (int) Double.parseDouble(campos[7]);
            boolean eliminado = campos[8].equals("1");
            int stockMaximo = campos[10].isEmpty() ? 0 : (int) Double.parseDouble(campos[10]);
            String urlImage = campos.length > 11 ? campos[11] : "";

            Producto p = new Producto(
                    null,
                    codigo,
                    nombre,
                    descripcion,
                    precioVenta,
                    precioCosto,
                    categoria,
                    0,
                    eliminado,
                    fechaCreacion,
                    stockMinimo,
                    stockMaximo,
                    null,
                    urlImage
            );

            productos.add(p);
        }

        br.close();
        return productos;
    }
    public static void updateCodigoBarra(String path, ProductoService ps) throws Exception{

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] campos = line.split(",", -1);

            // Trim para limpiar espacios
            for (int i = 0; i < campos.length; i++) {
                campos[i] = campos[i].trim();
            }
            long codigo=Long.parseLong(campos[0]);
            String codigoBarra=campos[1];
            if(ps.updateCodigoBarra(codigoBarra,codigo)){
                System.out.println("exito");
            }else{
                System.out.println("algo salio mal");
            }

            }
        }

}
