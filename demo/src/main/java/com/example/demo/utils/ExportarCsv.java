package com.example.demo.utils;

import com.example.demo.dto.ProductoMasVendidoDto;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class ExportarCsv {

    public static void exportar(List<ProductoMasVendidoDto> lista, File archivo) {
        try (PrintWriter pw = new PrintWriter(archivo)) {
            pw.println("Codigo,nombre,Precio,Total");

            for (ProductoMasVendidoDto p : lista) {
                pw.println(
                        p.getProducto().getCodigo() + "," +
                                p.getProducto().getNombre() + "," +
                                p.getProducto().getPrecio() + "," +
                                (p.getTotalFacturado())
                );
            }
            JOptionPane.showMessageDialog(null, "CSV exportado correctamente");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void exportarExcel(List<ProductoMasVendidoDto> lista, File archivo) {

    }
    public static void exportarPDF(List<ProductoMasVendidoDto> lista, File archivo) {

    }
}
