//package mycompany;
//
//import jakarta.mail.*;
//import jakarta.mail.internet.*;
//import java.util.Properties;
//
//public class EmailSender {
//
//    public static void sendEmail(String toEmail, String subject, String body) {
//        final String fromEmail = "aiqingyinggaideyangzi@gmail.com"; // email người gửi
//        final String password = "ewqaigboqdmqwhkp";        // mật khẩu ứng dụng
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");      // SMTP server Gmail
//        props.put("mail.smtp.port", "587");                 // TLS port
//        props.put("mail.smtp.auth", "true");                // enable auth
//        props.put("mail.smtp.starttls.enable", "true");     // enable STARTTLS
//
//        Session session = Session.getInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(fromEmail, password);
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(fromEmail));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse(toEmail));
//            message.setSubject(subject);
//            message.setText(body);
//
//            Transport.send(message);
//
//            System.out.println("Email sent successfully!");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//}
