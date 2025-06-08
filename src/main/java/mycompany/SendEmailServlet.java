//package mycompany;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//import java.io.IOException;
//
//@WebServlet("/sendEmail")
//public class SendEmailServlet extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//    String toEmail = req.getParameter("toEmail");
//    if (toEmail == null || toEmail.isEmpty()) {
//        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        resp.getWriter().write("Missing recipient email!");
//        return;
//    }
//    EmailSender.sendEmail(toEmail, "Test Subject", "Hello, this is a test email from Servlet!");
//    resp.setContentType("text/plain;charset=UTF-8");
//    resp.getWriter().write("Email sent successfully to " + toEmail);
//}
//
//}
