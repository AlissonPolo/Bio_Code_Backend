package com.example.Bio_Code.servicios;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${parqueadero.email.from}")
    private String emailFrom;

    @Value("${parqueadero.email.subject}")
    private String emailSubject;

    public void enviarNotificacionNuevoVehiculo(ParqueaderoVehiculo vehiculo) {
        System.out.println("📧 [LOG] Enviando correo a: " + vehiculo.getCorreoElectronico());

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
        helper.setTo(vehiculo.getCorreoElectronico());
        // Forzar UTF-8 en el asunto
        helper.setSubject(new String(emailSubject.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                java.nio.charset.StandardCharsets.UTF_8));

        helper.setText(generarContenidoHTML(vehiculo), true);
        mailSender.send(message);
    }

    private String generarContenidoHTML(ParqueaderoVehiculo vehiculo) {
        return """
<html>
<head>
    <style>
        body { font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #f4f4f6; margin: 0; padding: 0; }
        .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 12px 25px rgba(0,0,0,0.1); }
        .header { background: linear-gradient(90deg, #FF6F00, #FFA000); color: white; padding: 40px 20px; text-align: center; }
        .header h1 { margin: 0; font-size: 28px; letter-spacing: 1px; }
        .content { padding: 30px; color: #333333; line-height: 1.6; }
        .content p { font-size: 16px; margin-bottom: 20px; }
        .info-card { background: #FFF3E0; border-radius: 16px; padding: 25px; margin: 20px 0; box-shadow: 0 6px 15px rgba(0,0,0,0.08); }
        .info-card table { width: 100%%; border-collapse: collapse; }
        .info-card th, .info-card td { text-align: left; padding: 12px; border-bottom: 1px solid #FFE0B2; }
        .info-card th { color: #FF6F00; font-weight: 600; background-color: #FFECB3; }
        .status { text-align: center; margin-top: 25px; font-size: 18px; font-weight: 600; color: #FF6F00; }
        .footer { background-color: #FFF8E1; padding: 20px; font-size: 13px; color: #555555; text-align: center; border-top: 1px solid #FFE0B2; }
        @media screen and (max-width: 620px) {
            .container { margin: 20px; }
            .header h1 { font-size: 24px; }
            .content { padding: 20px; }
            .info-card { padding: 15px; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🚗 Vehículo Registrado</h1>
        </div>
        <div class="content">
            <p>Se ha registrado un nuevo vehículo en el parqueadero con los siguientes detalles:</p>
            <div class="info-card">
                <table>
                    <tr><th>🆔 Placa</th><td>%s</td></tr>
                    <tr><th>🚙 Tipo</th><td>%s</td></tr>
                    <tr><th>🏷 Marca</th><td>%s</td></tr>
                    <tr><th>🛠 Modelo</th><td>%s</td></tr>
                    <tr><th>🎨 Color</th><td>%s</td></tr>
                </table>
            </div>
            <p class="status">🏢 Este vehículo ha sido registrado oficialmente en el parqueadero</p>
        </div>
        <div class="footer">
            Este correo fue generado automáticamente por el Sistema de Parqueadero.
        </div>
    </div>
</body>
</html>
""".formatted(
                vehiculo.getPlaca() != null ? vehiculo.getPlaca() : "No registrada",
                vehiculo.getTipo() != null ? vehiculo.getTipo().toString() : "No especificado",
                vehiculo.getMarca() != null ? vehiculo.getMarca() : "No especificado",
                vehiculo.getModelo() != null ? vehiculo.getModelo() : "No especificado",
                vehiculo.getColor() != null ? vehiculo.getColor() : "No especificado"
        );
    }
}
