///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package servlets;
//
//import com.itextpdf.text.Chapter;
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.List;
//import com.itextpdf.text.ListItem;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//import hotel.*;
//import order.*;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// *
// * @author Lin Jianxiong
// */
//public class OrderPDFServlet extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("application/pdf");
//        int orderID = Integer.parseInt(request.getParameter("OrderID"));
//        Order o = Order.getOrderByOrderID(orderID);
//        Hotel h = Hotel.getHotelByID(o.getHotelID());
//        HotelRoom r = HotelRoom.getHotelRoom(o.getHotelID(), o.getRoomType());
//        Document document = new Document();
//        try {
//            StringBuilder sb = new StringBuilder();
//            sb.append("Hotel: \t\t\t" + h.getHotelName() + "\n");
//            sb.append("Room: \t\t\t" + r.getRoomName() + "\n");
//            sb.append("CHECK-IN: \t\t\t" + o.getCIDate() + "\n");
//            sb.append("CHECK-OUT: \t\t\t" + o.getCODate() + "\n");
//            sb.append("Price: \t\t\t$" + o.getPrice() + "\n");
//            PdfWriter.getInstance(document, response.getOutputStream());
//            document.open();
//            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD);
//            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.NORMAL);
//            Chunk chunk = new Chunk("Order Confirmation: " + o.getOrderID(), chapterFont);
//            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
//            chapter.setNumberDepth(0);
//            chapter.add(new Paragraph(sb.toString(), paragraphFont));
//            document.add(chapter);
//            
//            
//            
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            document.close();
//        }
//
//        
////        try {
////            /* TODO output your page here. You may use following sample code. */
////            out.println("<!DOCTYPE html>");
////            out.println("<html>");
////            out.println("<head>");
////            out.println("<title>Servlet OrderPDFServlet</title>");            
////            out.println("</head>");
////            out.println("<body>");
////            out.println("<h1>Servlet OrderPDFServlet at " + request.getContextPath() + "</h1>");
////            out.println("</body>");
////            out.println("</html>");
////        } finally {
////            out.close();
////        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
