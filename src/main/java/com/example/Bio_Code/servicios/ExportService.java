package com.example.Bio_Code.servicios;

import com.example.Bio_Code.dto.ExportFiltroDTO;
import com.example.Bio_Code.dto.PersonaConAsistenciaDTO;
import com.example.Bio_Code.modelo.Control_Asistencia;
import com.example.Bio_Code.modelo.Persona;
import com.example.Bio_Code.repositorio.AsistenciaRepository;
import com.example.Bio_Code.repositorio.FichaRepository;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final UsuarioRepository usuarioRepository;
    private final FichaRepository fichaRepository;
    private final AsistenciaRepository asistenciaRepository;  // <-- inyectar repo asistencia

    @Autowired
    public ExportService(UsuarioRepository usuarioRepository, FichaRepository fichaRepository,
                         AsistenciaRepository asistenciaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.fichaRepository = fichaRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

    public List<PersonaConAsistenciaDTO> obtenerDatosFiltradosConAsistencia(ExportFiltroDTO filtros) {
        Integer idFicha = filtros.getIdFicha();

        // Si no hay idFicha pero sí código de ficha, buscar idFicha desde código
        if (idFicha == null && filtros.getCodigoFicha() != null) {
            fichaRepository.findByCodigo(filtros.getCodigoFicha()).ifPresent(f -> {
                filtros.setIdFicha(f.getId_ficha());
            });
        }

        // Filtrar personas según filtros
        List<Persona> personas = usuarioRepository.findAll().stream()
                .filter(p -> filtros.getNombreUsuario() == null || p.getNombres().toLowerCase().contains(filtros.getNombreUsuario().toLowerCase()))
                .filter(p -> {
                    if (filtros.getEstadoAsistencia() == null) return true;
                    if (filtros.getEstadoAsistencia().equalsIgnoreCase("Activo")) return Boolean.TRUE.equals(p.getEstado());
                    if (filtros.getEstadoAsistencia().equalsIgnoreCase("Inactivo")) return Boolean.FALSE.equals(p.getEstado());
                    return true;
                })
                .filter(p -> filtros.getIdFicha() == null ||
                        (p.getFicha() != null && filtros.getIdFicha() != null && p.getFicha().getId_ficha() == filtros.getIdFicha()))
                .collect(Collectors.toList());


        for (Persona p : personas) {
            System.out.println("- " + p.getNombres() + " " + p.getApellidos());
        }

        // Construir lista DTO con estado y fecha de última asistencia
        return personas.stream().map(p -> {
            List<Control_Asistencia> asistencias = asistenciaRepository.findByPersona_Idpersona(p.getIdpersona());

            String estadoAsistencia = "";
            Date fechaUltimaAsistencia = null;

            if (!asistencias.isEmpty()) {
                Control_Asistencia ultima = asistencias.stream()
                        .filter(a -> a.getFechaAsistencia() != null)
                        .sorted((a1, a2) -> a2.getFechaAsistencia().compareTo(a1.getFechaAsistencia()))
                        .findFirst()
                        .orElse(null);

                if (ultima != null) {
                    fechaUltimaAsistencia = ultima.getFechaAsistencia();

                    if (Boolean.TRUE.equals(ultima.isEstancia())) {
                        estadoAsistencia = "Asistió";
                    } else if ("novedad".equalsIgnoreCase(ultima.getNovedad())) {
                        estadoAsistencia = "Novedad";
                    } else {
                        estadoAsistencia = "No asistió";
                    }
                }
            }
            System.out.println("Programa de " + p.getNombres() + ": " +
                    (p.getFicha() != null && p.getFicha().getPrograma() != null
                            ? p.getFicha().getPrograma().getNombre()
                            : "Sin programa"));
            return new PersonaConAsistenciaDTO(
                    p.getIdpersona(),
                    p.getNombres(),
                    p.getApellidos(),
                    p.getCorreo(),
                    p.getTelefono() != null ? p.getTelefono().toString() : "",
                    p.getFicha() != null ? p.getFicha().getCodigo() : "",
                    (p.getFicha() != null && p.getFicha().getPrograma() != null) ? p.getFicha().getPrograma().getNombre() : "",
                    (p.getFicha() != null && p.getFicha().getCompetencia() != null) ? p.getFicha().getCompetencia().getNombre() : "",
                    p.getEstado(),
                    estadoAsistencia,
                    fechaUltimaAsistencia
            );
        }).collect(Collectors.toList());
    }

    public byte[] generarExcel(List<PersonaConAsistenciaDTO> personasConAsistencia) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Personas");

            // Crear estilos
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setWrapText(true);

            // Crear fila de encabezado y aplicar estilo
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Nombre Completo", "Correo", "Teléfono", "Ficha",
                    "Programa", "Estado Asistencia", "Competencia", "Fecha Asistencia"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Rellenar filas con datos y aplicar estilo normal
            int rowNum = 1;
            for (PersonaConAsistenciaDTO p : personasConAsistencia) {
                Row row = sheet.createRow(rowNum++);

                Cell c0 = row.createCell(0);
                c0.setCellValue((p.getNombres() != null ? p.getNombres() : "") + " " + (p.getApellidos() != null ? p.getApellidos() : ""));
                c0.setCellStyle(normalStyle);

                Cell c1 = row.createCell(1);
                c1.setCellValue(p.getCorreo() != null ? p.getCorreo() : "");
                c1.setCellStyle(normalStyle);

                Cell c2 = row.createCell(2);
                c2.setCellValue(p.getTelefono() != null ? p.getTelefono() : "");
                c2.setCellStyle(normalStyle);

                Cell c3 = row.createCell(3);
                c3.setCellValue(p.getFichaCodigo() != null ? p.getFichaCodigo() : "");
                c3.setCellStyle(normalStyle);

                Cell c4 = row.createCell(4);
                c4.setCellValue(p.getProgramaNombre() != null ? p.getProgramaNombre() : "");
                c4.setCellStyle(normalStyle);

                Cell c5 = row.createCell(5);
                c5.setCellValue(p.getEstadoAsistencia() != null ? p.getEstadoAsistencia() : "");
                c5.setCellStyle(normalStyle);

                Cell c6 = row.createCell(6);
                c6.setCellValue(p.getCompetenciaNombre() != null ? p.getCompetenciaNombre() : "");
                c6.setCellStyle(normalStyle);

                Cell c7 = row.createCell(7);
                if (p.getFechaUltimaAsistencia() != null) {
                    String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(p.getFechaUltimaAsistencia());
                    c7.setCellValue(fechaStr);
                } else {
                    c7.setCellValue("Sin registro");
                }
                c7.setCellStyle(normalStyle);
            }

            // Auto-ajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
