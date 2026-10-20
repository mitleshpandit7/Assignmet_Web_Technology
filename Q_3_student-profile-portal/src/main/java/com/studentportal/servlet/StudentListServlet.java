package com.studentportal.servlet;

import com.studentportal.dao.StudentDAO;
import com.studentportal.model.Student;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Loads students from MySQL and forwards to the JSP that renders them. (Protected by AuthFilter.) */
@WebServlet(name = "StudentListServlet", urlPatterns = {"/students"})
public class StudentListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_SEARCH_LENGTH = 100;

    private final transient StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("q");
        if (search != null && search.length() > MAX_SEARCH_LENGTH) {
            search = search.substring(0, MAX_SEARCH_LENGTH);
        }

        try {
            List<Student> students = studentDAO.findAll(search);
            request.setAttribute("students", students);
            request.setAttribute("search", search == null ? "" : search.trim());
        } catch (SQLException e) {
            getServletContext().log("Database error while loading students", e);
            throw new ServletException("Could not load students from the database.", e);
        }
        request.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(request, response);
    }
}
