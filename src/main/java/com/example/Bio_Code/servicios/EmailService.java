    package com.example.Bio_Code.servicios;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${parqueadero.email.from}")
    private String emailFrom;

    @Value("${parqueadero.email.to}")
    private String emailTo;

    @Value("${parqueadero.email.subject}")
    private String emailSubject;

    public void enviarNotificacionNuevoVehiculo(ParqueaderoVehiculo vehiculo) {
        try {
            enviarCorreoHTML(vehiculo);
            logger.info("Correo enviado exitosamente para el vehículo con placa: {}", vehiculo.getPlaca());
        } catch (Exception e) {
            logger.error("Error al enviar correo para el vehículo con placa: {}. Error: {}", 
                        vehiculo.getPlaca(), e.getMessage());
        }
    }

    private void enviarCorreoHTML(ParqueaderoVehiculo vehiculo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(emailFrom);
        helper.setTo(emailTo);
        helper.setSubject(emailSubject);

        String contenidoHTML = generarContenidoHTML(vehiculo);
        helper.setText(contenidoHTML, true);

        mailSender.send(message);
    }

    private String generarContenidoHTML(ParqueaderoVehiculo vehiculo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaEntrada = vehiculo.getFechaEntrada() != null ? 
            vehiculo.getFechaEntrada().atZone(java.time.ZoneId.systemDefault()).format(formatter) : 
            "No registrada";

        return """
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 15px; text-align: center; }
                    .content { padding: 20px; border: 1px solid #ddd; }
                    .info-table { width: 100%%; border-collapse: collapse; margin: 15px 0; }
                    .info-table th, .info-table td { 
                        border: 1px solid #ddd; 
                        padding: 10px; 
                        text-align: left; 
                    }
                    .info-table th { background-color: #f2f2f2; }
                    .footer { margin-top: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h2>🚗 Nuevo Vehículo Registrado - Sistema de Parqueadero</h2>
                </div>
                
                <div class="content">
                    <p>Se ha registrado un nuevo vehículo en el parqueadero:</p>
                    
                    <table class="info-table">
                        <tr>
                            <th>Placa</th>
                            <td><strong>%s</strong></td>
                        </tr>
                        <tr>
                            <th>Tipo de Vehículo</th>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <th>Marca</th>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <th>Modelo</th>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <th>Color</th>
                            <td>%s</td>
                        </tr>
                        <tr>
                            <th>Fecha de Entrada</th>
                            <td>%s</td>
                        </tr>
                    </table>
                    
                    <p><strong>Estado:</strong> <span style="color: green;">✅ Vehículo parqueado</span></p>
                </div>
                
                <div class="footer">
                    <p>Este correo fue generado automáticamente por el Sistema de Control de Parqueadero.</p>
                    <p>Fecha de envío: %s</p>
                </div>
            </body>
            </html>
            """.formatted(
                vehiculo.getPlaca() != null ? vehiculo.getPlaca() : "No registrada",
                vehiculo.getTipo() != null ? vehiculo.getTipo().toString() : "No especificado",
                vehiculo.getMarca() != null ? vehiculo.getMarca() : "No especificada",
                vehiculo.getModelo() != null ? vehiculo.getModelo() : "No especificado",
                vehiculo.getColor() != null ? vehiculo.getColor() : "No especificado",
                fechaEntrada,
                java.time.LocalDateTime.now().format(formatter)
            );
    }

    public void enviarCorreoSimple(ParqueaderoVehiculo vehiculo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(emailTo);
            message.setSubject(emailSubject);
            
            String contenido = generarContenidoTexto(vehiculo);
            message.setText(contenido);
            
            mailSender.send(message);
            logger.info("Correo simple enviado para vehículo con placa: {}", vehiculo.getPlaca());
        } catch (Exception e) {
            logger.error("Error al enviar correo simple: {}", e.getMessage());
        }
    }

    private String generarContenidoTexto(ParqueaderoVehiculo vehiculo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaEntrada = vehiculo.getFechaEntrada() != null ? 
            vehiculo.getFechaEntrada().atZone(java.time.ZoneId.systemDefault()).format(formatter) : 
            "No registrada";

        return String.format("""
            NUEVO VEHÍCULO REGISTRADO EN EL PARQUEADERO
            
            Detalles del vehículo:
            - Placa: %s
            - Tipo: %s
            - Marca: %s
            - Modelo: %s
            - Color: %s
            - Fecha de entrada: %s
            
            Estado: Vehículo parqueado
            
            ---
            Este correo fue generado automáticamente por el Sistema de Control de Parqueadero.
            Fecha de envío: %s
            """,
            vehiculo.getPlaca() != null ? vehiculo.getPlaca() : "No registrada",
            vehiculo.getTipo() != null ? vehiculo.getTipo().toString() : "No especificado",
            vehiculo.getMarca() != null ? vehiculo.getMarca() : "No especificada",
            vehiculo.getModelo() != null ? vehiculo.getModelo() : "No especificado",
            vehiculo.getColor() != null ? vehiculo.getColor() : "No especificado",
            fechaEntrada,
            java.time.LocalDateTime.now().format(formatter)
        );
    }
}
