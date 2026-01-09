package com.example.demo.utils;

import com.example.demo.entity.DetalleVenta;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrintTicket58 {
    public static String generarTicket(List<DetalleVenta> detallesVenta, double total, LocalDateTime fecha) {
        StringBuilder ticket = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        ticket .append(center("DLClean"))
                .append(center("Gadini 437 - Tristán Suárez"))
                .append("Fecha: ").append(fecha.format(dtf)).append("\n")
                .append("--------------------------------\n")
                .append("Cant  Descripción      Importe  \n")
                .append("--------------------------------\n");

        for (DetalleVenta detalle : detallesVenta) {
            int cantidad = detalle.getCantidad();
            String nombre = detalle.getProducto().getNombre();
            double precio = detalle.getPrecioUnitario() * cantidad; // precio total del item
            ticket.append(line(cantidad, nombre, (int)precio));
        }

        ticket.append("--------------------------------\n")
                .append(right("TOTAL: $" + total))
                .append(center("¡Gracias por su compra!"))
                .append("\n\n\n");
        return ticket.toString();
    }

    public static void imprimir(String ticket) {
        // Convertir a bytes compatibles
        byte[] data = null;
        data = ticket.getBytes(StandardCharsets.UTF_8);
        //data = ticket.getBytes(Charset.forName("CP850")); probar esta..
        // Concatenar saltos y corte
        byte[] finalData = joinBytes(
                data,
                LINE_BREAK, LINE_BREAK, LINE_BREAK,  // Mínimo 3 saltos
                CUT                                  // Corte de ticket
        );
        InputStream is = new ByteArrayInputStream(finalData);

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(is, flavor, null);

        // Buscar la impresora por nombre
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService thermalPrinter = null;

        for (PrintService ps : services) {
            if (ps.getName().toLowerCase().contains("xp")
                    || ps.getName().toLowerCase().contains("xprinter")
                    || ps.getName().toLowerCase().contains("58")) {
                thermalPrinter = ps;
                break;
            }
        }

        if (thermalPrinter == null) {
            System.out.println("No se encontró la XPrinter XP-58.");
            return;
        }

        System.out.println("Imprimiendo en: " + thermalPrinter.getName());

        DocPrintJob job = thermalPrinter.createPrintJob();

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));

        try {
            job.print(doc, aset);
        } catch (PrintException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Ticket enviado correctamente.");
    }

    // ESC/POS commands
    public static final byte[] CUT = {0x1D, 'V', 1}; // Corte parcial
    public static final byte[] LINE_BREAK = {0x0A};  // Salto de línea
    // Centrar texto a 32 caracteres
    private static String center(String text) {
        int width = 32;
        int pad = (width - text.length()) / 2;
        return repeat(" ", Math.max(0, pad)) + text + "\n";
    }
    private static String repeat(String s, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    // Alinear texto a la derecha
    private static String right(String text) {
        int width = 32;
        int pad = width - text.length();
        return repeat(" ", Math.max(0, pad)) + text + "\n";
    }

    // Formato de línea de producto: cantidad, descripción, importe
    private static String line(int cant, String desc, int precio) {
        String c = String.valueOf(cant);
        String p = String.valueOf(precio);

        String descCut = desc.length() > 18 ? desc.substring(0, 18) : desc;

        return String.format("%-5s %-18s %7s\n", c, descCut, p);
    }
    // Metodo para unir varios arrays de bytes
    public static byte[] joinBytes(byte[]... arrays) {
        int length = 0;
        for (byte[] a : arrays) length += a.length;

        byte[] result = new byte[length];
        int pos = 0;

        for (byte[] a : arrays) {
            System.arraycopy(a, 0, result, pos, a.length);
            pos += a.length;
        }

        return result;
    }
    public void previewTicket(String ticket) {
        System.out.println("=== TICKET PREVIEW ===");
        System.out.println(ticket);
        System.out.println("=====================");
    }
}
