package com.example.Bio_Code.servicios;

import com.example.Bio_Code.dto.DispositivoFiltroDTO;
import com.example.Bio_Code.modelo.Dispositivo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Servicio para generar reportes PDF de dispositivos
 */
@Service
public class DispositivoPdfService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.GRAY);
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * Genera un PDF con la información de todos los dispositivos (sin filtros)
     */
    public byte[] generarReporteDispositivos(List<Dispositivo> dispositivos) {
        return generarReporteDispositivos(dispositivos, null);
    }
    
    /**
     * Genera un PDF con la información de dispositivos aplicando filtros
     */
    public byte[] generarReporteDispositivos(List<Dispositivo> dispositivos, DispositivoFiltroDTO filtros) {
        try {
            Document document = new Document(PageSize.A4.rotate()); // Orientación horizontal para más columnas
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();

            // Título del documento
            agregarTitulo(document, filtros);
            
            // Información de filtros aplicados (si los hay)
            if (filtros != null) {
                agregarInfoFiltros(document, filtros);
            }
            
            // Información del reporte
            agregarInfoReporte(document, dispositivos.size(), filtros);

            // Tabla de dispositivos
            agregarTablaDispositivos(document, dispositivos);

            // Pie de página
            agregarPiePagina(document);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
    }

    private void agregarTitulo(Document document, DispositivoFiltroDTO filtros) throws DocumentException {
        String tituloTexto = filtros != null ? "REPORTE DE DISPOSITIVOS - FILTRADO" : "REPORTE DE DISPOSITIVOS - COMPLETO";
        Paragraph titulo = new Paragraph(tituloTexto, TITLE_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
    }
    
    private void agregarInfoFiltros(Document document, DispositivoFiltroDTO filtros) throws DocumentException {
        // Título de la sección de filtros
        Paragraph tituloFiltros = new Paragraph("FILTROS APLICADOS", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY));
        tituloFiltros.setAlignment(Element.ALIGN_LEFT);
        tituloFiltros.setSpacingAfter(10);
        document.add(tituloFiltros);
        
        // Tabla de filtros
        PdfPTable filtrosTable = new PdfPTable(2);
        filtrosTable.setWidthPercentage(100);
        filtrosTable.setSpacingAfter(15);
        
        // Agregar filtros aplicados
        if (filtros.getNombre() != null && !filtros.getNombre().trim().isEmpty()) {
            agregarFilaFiltro(filtrosTable, "Nombre/Serie/Código:", filtros.getNombre());
        }
        if (filtros.getTipo() != null) {
            agregarFilaFiltro(filtrosTable, "Tipo:", filtros.getTipo().getDisplayName());
        }
        if (filtros.getEstado() != null) {
            agregarFilaFiltro(filtrosTable, "Estado:", filtros.getEstado().getDisplayName());
        }
        if (filtros.getUbicacion() != null && !filtros.getUbicacion().trim().isEmpty()) {
            agregarFilaFiltro(filtrosTable, "Ubicación:", filtros.getUbicacion());
        }
        if (filtros.getResponsable() != null && !filtros.getResponsable().trim().isEmpty()) {
            agregarFilaFiltro(filtrosTable, "Responsable:", filtros.getResponsable());
        }
        
        document.add(filtrosTable);
    }
    
    private void agregarFilaFiltro(PdfPTable table, String etiqueta, String valor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(etiqueta, new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(3);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(valor, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(3);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void agregarInfoReporte(Document document, int totalDispositivos, DispositivoFiltroDTO filtros) throws DocumentException {
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(15);
        infoTable.setWidths(new float[]{1f, 1f});

        // Fecha de generación
        PdfPCell fechaLabel = new PdfPCell(new Phrase("Fecha de generación:", NORMAL_FONT));
        fechaLabel.setBorder(Rectangle.NO_BORDER);
        fechaLabel.setPaddingBottom(5);
        
        PdfPCell fechaValue = new PdfPCell(new Phrase(DATETIME_FORMAT.format(new java.util.Date()), NORMAL_FONT));
        fechaValue.setBorder(Rectangle.NO_BORDER);
        fechaValue.setPaddingBottom(5);

        // Total de dispositivos
        String labelTexto = filtros != null ? "Dispositivos encontrados:" : "Total de dispositivos:";
        PdfPCell totalLabel = new PdfPCell(new Phrase(labelTexto, NORMAL_FONT));
        totalLabel.setBorder(Rectangle.NO_BORDER);
        
        PdfPCell totalValue = new PdfPCell(new Phrase(String.valueOf(totalDispositivos), NORMAL_FONT));
        totalValue.setBorder(Rectangle.NO_BORDER);
        
        // Tipo de reporte
        PdfPCell tipoLabel = new PdfPCell(new Phrase("Tipo de reporte:", NORMAL_FONT));
        tipoLabel.setBorder(Rectangle.NO_BORDER);
        
        String tipoTexto = filtros != null ? "Filtrado" : "Completo";
        PdfPCell tipoValue = new PdfPCell(new Phrase(tipoTexto, NORMAL_FONT));
        tipoValue.setBorder(Rectangle.NO_BORDER);

        infoTable.addCell(fechaLabel);
        infoTable.addCell(fechaValue);
        infoTable.addCell(totalLabel);
        infoTable.addCell(totalValue);
        infoTable.addCell(tipoLabel);
        infoTable.addCell(tipoValue);

        document.add(infoTable);
    }

    private void agregarTablaDispositivos(Document document, List<Dispositivo> dispositivos) throws DocumentException {
        // Crear tabla con 8 columnas principales
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        
        // Anchos de columnas (porcentajes)
        float[] columnWidths = {12f, 15f, 10f, 12f, 12f, 12f, 15f, 12f};
        table.setWidths(columnWidths);

        // Encabezados
        String[] headers = {
            "Nombre", "Tipo", "Marca", "Modelo", "N° Serie", "Estado", "Ubicación", "Responsable"
        };

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(new BaseColor(52, 73, 94)); // Color azul oscuro
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Datos de dispositivos
        for (Dispositivo dispositivo : dispositivos) {
            // Nombre
            PdfPCell nombreCell = new PdfPCell(new Phrase(
                dispositivo.getNombre() != null ? dispositivo.getNombre() : "N/A", 
                NORMAL_FONT
            ));
            nombreCell.setPadding(5);
            table.addCell(nombreCell);

            // Tipo
            PdfPCell tipoCell = new PdfPCell(new Phrase(
                dispositivo.getTipo() != null ? dispositivo.getTipo().getDisplayName() : "N/A", 
                NORMAL_FONT
            ));
            tipoCell.setPadding(5);
            table.addCell(tipoCell);

            // Marca
            PdfPCell marcaCell = new PdfPCell(new Phrase(
                dispositivo.getMarca() != null ? dispositivo.getMarca() : "N/A", 
                NORMAL_FONT
            ));
            marcaCell.setPadding(5);
            table.addCell(marcaCell);

            // Modelo
            PdfPCell modeloCell = new PdfPCell(new Phrase(
                dispositivo.getModelo() != null ? dispositivo.getModelo() : "N/A", 
                NORMAL_FONT
            ));
            modeloCell.setPadding(5);
            table.addCell(modeloCell);

            // Número de serie
            PdfPCell serieCell = new PdfPCell(new Phrase(
                dispositivo.getNumeroSerie() != null ? dispositivo.getNumeroSerie() : "N/A", 
                NORMAL_FONT
            ));
            serieCell.setPadding(5);
            table.addCell(serieCell);

            // Estado con color
            PdfPCell estadoCell = crearCeldaEstado(dispositivo.getEstado());
            table.addCell(estadoCell);

            // Ubicación
            PdfPCell ubicacionCell = new PdfPCell(new Phrase(
                dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "N/A", 
                NORMAL_FONT
            ));
            ubicacionCell.setPadding(5);
            table.addCell(ubicacionCell);

            // Responsable
            PdfPCell responsableCell = new PdfPCell(new Phrase(
                dispositivo.getResponsable() != null ? dispositivo.getResponsable() : "N/A", 
                NORMAL_FONT
            ));
            responsableCell.setPadding(5);
            table.addCell(responsableCell);
        }

        document.add(table);
    }

    private PdfPCell crearCeldaEstado(Dispositivo.EstadoDispositivo estado) {
        String estadoTexto = estado != null ? estado.getDisplayName() : "N/A";
        PdfPCell cell = new PdfPCell(new Phrase(estadoTexto, NORMAL_FONT));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Colores según el estado
        if (estado != null) {
            switch (estado) {
                case ACTIVO:
                    cell.setBackgroundColor(new BaseColor(212, 237, 218)); // Verde claro
                    break;
                case EN_MANTENIMIENTO:
                    cell.setBackgroundColor(new BaseColor(255, 243, 205)); // Amarillo claro
                    break;
                case DAÑADO:
                    cell.setBackgroundColor(new BaseColor(248, 215, 218)); // Rojo claro
                    break;
                case INACTIVO:
                    cell.setBackgroundColor(new BaseColor(233, 236, 239)); // Gris claro
                    break;
                default:
                    break;
            }
        }

        return cell;
    }

    private void agregarPiePagina(Document document) throws DocumentException {
        Paragraph pie = new Paragraph("Reporte generado automáticamente por el Sistema de Gestión de Dispositivos", SMALL_FONT);
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(20);
        document.add(pie);
    }
}

