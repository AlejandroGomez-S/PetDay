/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author og218
 */
public class MailSend {
    
    private Properties props;
    private final String PETDAYMAIL = "og2189@gmail.com";
    private final String PASSWORD = "alejandro950109";
    
    public boolean sendEmail(String receiver, String subject, String message) throws AddressException, MessagingException {
        try {
            props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");   // Protocolo de transferencia de archivos
            props.setProperty("mail.smtp.starttls.enable", "true");         // Protocolo de seguridad para la transferencia de archivos
            props.setProperty("mail.smtp.port", "587");              // Puerto a uilizar para para el smtp -- Es el predeterminado
            props.setProperty("mail.smtp.auth", "true");             // Propiedad en donde se dice que es necesaria la autentificación para aceder al servicio

            System.out.println("llega hasta aqui");
            Session session = Session.getInstance(props);            // Preparar la sesion
            
            //Message mes = prepareMessage(session);
            Message mimeMessage = new MimeMessage(session); // Permite darle estilo al mensaje que se desa enviar.
            mimeMessage.setFrom(new InternetAddress(PETDAYMAIL)); // Primero se pasa el correo desde el que se envia 
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver)); // Se agrega el receptor  // Se usa TO porque es solo un destinatario // CC es para enviar una copia al remitente 
            mimeMessage.setSubject(subject);              // Se setea el asunto
            mimeMessage.setText(message);                  // Se pone el contenido del mensaje
            
            Transport trasport = session.getTransport("smtp");    // Permite iniciar el transporte del mensaje a travez del protocolo smtp
            trasport.connect(PETDAYMAIL, PASSWORD); // Se setea el usuario gmail y se pasa la contraseña 
            trasport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO)); // Se dice que se envia el mimeMessage y se le pasa los receptores que se setearon
            trasport.close();
            return true;
        } catch (AddressException e) {
            e.printStackTrace();
        }catch(MessagingException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
