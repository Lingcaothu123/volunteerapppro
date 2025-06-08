package com.example.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

@WebServlet("/QRCodeGenerator")
public class QRCodeGenerator extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idactive = request.getParameter("idactive"); // Lấy ID hoạt động từ query string

        if (idactive == null || idactive.isEmpty()) {
            response.getWriter().println("❌ Không có ID hoạt động.");
            return;
        }

        String qrContent = "https://37a9-2001-ee0-4f05-c8d0-4965-45d6-d12b-52d5.ngrok-free.app/QRScanServlet?idactive=" + idactive;  // URL điểm danh mới

        // Thiết lập kích thước mã QR
        int width = 300;
        int height = 300;
        String format = "PNG";

        try {
            // Tạo mã QR
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, width, height);
            
            // Tạo mã QR và chuyển nó thành một mảng byte
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, baos);
            byte[] qrImageBytes = baos.toByteArray();

            // Chuyển mã QR thành Base64
            String base64QRCode = Base64.getEncoder().encodeToString(qrImageBytes);

            // Trả về mã QR dưới dạng Base64
            response.setContentType("text/plain");
            response.getWriter().write(base64QRCode);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("❌ Lỗi khi tạo mã QR.");
        }
    }
}
