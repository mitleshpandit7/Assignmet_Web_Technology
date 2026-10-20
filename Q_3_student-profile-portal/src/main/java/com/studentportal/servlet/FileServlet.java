package com.studentportal.servlet;

import com.studentportal.dao.StudentDAO;
import com.studentportal.model.Student;
import com.studentportal.util.AppConfig;
import com.studentportal.util.SafePath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Streams a student's image or document from the configured upload folder.
 *
 *   GET /file?id=3&type=image
 *   GET /file?id=3&type=document            (shown in browser when possible)
 *   GET /file?id=3&type=document&download=1 (forces "Save as...")
 *
 * Security: the browser sends only a student id; the file path comes from the database and is
 * validated by SafePath, so a visitor can never ask for an arbitrary file. Protected by AuthFilter.
 */
@WebServlet(name = "FileServlet", urlPatterns = {"/file"})
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Only these extensions are ever served (SVG/HTML are deliberately excluded: script risk). */
    private static final Map<String, String> CONTENT_TYPES = new HashMap<>();
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>();

    static {
        CONTENT_TYPES.put("jpg", "image/jpeg");
        CONTENT_TYPES.put("jpeg", "image/jpeg");
        CONTENT_TYPES.put("png", "image/png");
        CONTENT_TYPES.put("gif", "image/gif");
        CONTENT_TYPES.put("webp", "image/webp");
        CONTENT_TYPES.put("pdf", "application/pdf");
        CONTENT_TYPES.put("txt", "text/plain; charset=UTF-8");
        CONTENT_TYPES.put("doc", "application/msword");
        CONTENT_TYPES.put("docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        IMAGE_EXTENSIONS.add("jpg");
        IMAGE_EXTENSIONS.add("jpeg");
        IMAGE_EXTENSIONS.add("png");
        IMAGE_EXTENSIONS.add("gif");
        IMAGE_EXTENSIONS.add("webp");
    }

    private final transient StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        boolean wantsImage = "image".equals(type);
        if (!wantsImage && !"document".equals(type)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown file type.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found.");
            return;
        }

        Student student;
        try {
            student = studentDAO.findById(id);
        } catch (SQLException e) {
            getServletContext().log("Database error while loading file for student " + id, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        if (student == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found.");
            return;
        }

        String storedPath = wantsImage ? student.getImagePath() : student.getDocumentPath();
        Optional<Path> resolved = SafePath.resolve(AppConfig.uploadDir(), storedPath);

        String ext = resolved.map(p -> extensionOf(p.getFileName().toString())).orElse("");
        String contentType = CONTENT_TYPES.get(ext);
        boolean allowed = resolved.isPresent() && contentType != null
                && (!wantsImage || IMAGE_EXTENSIONS.contains(ext));

        if (!allowed) {
            // Missing / unsafe / unsupported file: degrade gracefully instead of showing a stack trace.
            if (wantsImage) {
                response.sendRedirect(request.getContextPath() + "/images/no-image.svg");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "The requested document is not available.");
            }
            return;
        }

        Path file = resolved.get();
        boolean inline = (wantsImage || "pdf".equals(ext) || "txt".equals(ext))
                && !"1".equals(request.getParameter("download"));

        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(file));
        response.setHeader("Cache-Control", "private, max-age=300");
        response.setHeader("Content-Disposition",
                (inline ? "inline" : "attachment") + "; " + filenameParams(file.getFileName().toString()));

        try (InputStream in = Files.newInputStream(file);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }

    private static String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    /** RFC 6266 filename parameters: ASCII fallback plus UTF-8 encoded name. */
    private static String filenameParams(String name) {
        String ascii = name.replaceAll("[^A-Za-z0-9._-]", "_");
        String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        return "filename=\"" + ascii + "\"; filename*=UTF-8''" + encoded;
    }
}
