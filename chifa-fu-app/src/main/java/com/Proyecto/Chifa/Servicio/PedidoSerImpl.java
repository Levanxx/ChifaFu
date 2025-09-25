package com.Proyecto.Chifa.Servicio;

import com.Proyecto.Chifa.Modelo.DetallePedido;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.Proyecto.Chifa.Repositorio.PedidoRepo;
import org.springframework.stereotype.Service;
import com.Proyecto.Chifa.Modelo.Pedido;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.awt.*;
import java.util.stream.Stream;

@Service
public class PedidoSerImpl implements PedidoSer {

    @Autowired
    private PedidoRepo repo;

    //Guardar el pedido en la bd
    @Override
    @Transactional
    public void guardarPedido(Pedido pedido) {
        repo.save(pedido);
    }

    //Obtener todos los pedidos
    @Override
    public List<Pedido> obtenerTodos() {
        return repo.findAll();
    }

    @Override
    public List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario){
        return repo.findByUsuarioIdUsuario(idUsuario);
    }

    @Override
    public Pedido obtenerPorId(Integer id) {
        return repo.findById(id)
                .orElse(null); // O puedes lanzar una excepción si prefieres
    }


    /*public void generarBoletaPDF(Integer idPedido, HttpServletResponse response) throws IOException {
        Optional<Pedido> pedidoOpt = repo.findById(idPedido);

        if (pedidoOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado");
            return;
        }

        Pedido pedido = pedidoOpt.get();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            document.add(new Paragraph("Boleta de Pedido"));
            document.add(new Paragraph("Fecha: " + pedido.getFechaPedido()));
            document.add(new Paragraph("Estado: " + pedido.getEstado()));
            document.add(new Paragraph("Total: S/. " + pedido.getTotal()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Detalles del pedido:"));

            for (DetallePedido detalle : pedido.getDetalles()) {
                String linea = "- " + detalle.getMenu().getNombre() +
                        " x " + detalle.getCantidad() +
                        " = S/. " + (detalle.getCantidad() * detalle.getPrecioUnitario());
                document.add(new Paragraph(linea));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar PDF");
        }
    }*/
    public void generarBoletaPDF(Integer idPedido, HttpServletResponse response) throws IOException {
        Optional<Pedido> pedidoOpt = repo.findById(idPedido);

        if (pedidoOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado");
            return;
        }

        Pedido pedido = pedidoOpt.get();

        // Configuración del documento PDF
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=boleta_" + idPedido + ".pdf");

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // 1. Logo (si lo tienes en resources/static/images/logo.png)
            try {
                String logoPath = "src/main/resources/static/images/logo_chifa.png";
                Image logo = Image.getInstance(logoPath);
                logo.scaleToFit(100, 100);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception ex) {
                System.out.println("No se encontró el logo. Continuando sin él...");
            }

            // 2. Título
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("BOLETA DE PEDIDO", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            // 3. Tabla con info general
            Font textoNormal = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(15);

            infoTable.addCell(new Phrase("Fecha:", bold));
            infoTable.addCell(new Phrase(pedido.getFechaPedido().toString(), textoNormal));

            infoTable.addCell(new Phrase("Estado:", bold));
            infoTable.addCell(new Phrase(pedido.getEstado().toString(), textoNormal));

            infoTable.addCell(new Phrase("Total:", bold));
            infoTable.addCell(new Phrase("S/. " + pedido.getTotal(), textoNormal));

            document.add(infoTable);

            // 4. Tabla de detalles del pedido
            PdfPTable detallesTable = new PdfPTable(4); // Producto, Cantidad, Precio U, Subtotal
            detallesTable.setWidthPercentage(100);
            detallesTable.setSpacingBefore(10f);
            detallesTable.setSpacingAfter(10f);

            // Cabeceras con fondo gris
            Stream.of("Producto", "Cantidad", "P. Unitario", "Subtotal").forEach(tituloColumna -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setPhrase(new Phrase(tituloColumna, bold));
                detallesTable.addCell(header);
            });

            for (DetallePedido detalle : pedido.getDetalles()) {
                detallesTable.addCell(new Phrase(detalle.getMenu().getNombre(), textoNormal));
                detallesTable.addCell(new Phrase(String.valueOf(detalle.getCantidad()), textoNormal));
                detallesTable.addCell(new Phrase("S/. " + detalle.getPrecioUnitario(), textoNormal));
                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                detallesTable.addCell(new Phrase("S/. " + subtotal, textoNormal));
            }

            document.add(detallesTable);

            // 5. Mensaje final
            Paragraph gracias = new Paragraph("Gracias por su preferencia.", textoNormal);
            gracias.setAlignment(Element.ALIGN_CENTER);
            gracias.setSpacingBefore(20);
            document.add(gracias);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar PDF");
        }
    }



}
