package com.studentportal.servlet;

import com.studentportal.dao.UserDAO;
import com.studentportal.model.User;
import com.studentportal.util.AppConfig;
import com.studentportal.util.PasswordUtil;
import com.studentportal.util.SessionKeys;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Shows the login form (GET) and validates credentials against the users table (POST). */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LOGIN_VIEW = "/WEB-INF/views/login.jsp";
    private static final int MAX_FIELD_LENGTH = 100;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final transient UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SessionKeys.USERNAME) != null) {
            response.sendRedirect(request.getContextPath() + "/students");
            return;
        }
        if ("1".equals(request.getParameter("logout"))) {
            request.setAttribute("message", "You have been logged out.");
        }
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = trim(request.getParameter("username"));
        String password = request.getParameter("password");

        if (username.isEmpty() || password == null || password.isEmpty()
                || username.length() > MAX_FIELD_LENGTH || password.length() > MAX_FIELD_LENGTH) {
            fail(request, response, username, "Please enter your username and password.");
            return;
        }

        User user;
        try {
            user = userDAO.findByUsername(username);
        } catch (SQLException e) {
            getServletContext().log("Database error during login", e);
            throw new ServletException("Database error during login", e);
        }

        // Always run a hash comparison so response time does not reveal whether the user exists.
        String storedHash = user != null ? user.getPasswordHash() : PasswordUtil.DUMMY_HASH;
        boolean passwordOk = PasswordUtil.verify(password, storedHash);

        if (user == null || !passwordOk) {
            fail(request, response, username, "Invalid username or password.");
            return;
        }

        // Prevent session fixation: discard any old session and start a fresh one.
        HttpSession old = request.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(AppConfig.sessionTimeoutMinutes() * 60);
        session.setAttribute(SessionKeys.USERNAME, user.getUsername());
        session.setAttribute(SessionKeys.FULL_NAME,
                user.getFullName() != null ? user.getFullName() : user.getUsername());
        session.setAttribute(SessionKeys.CSRF_TOKEN, newToken());

        response.sendRedirect(request.getContextPath() + "/students");
    }

    private void fail(HttpServletRequest request, HttpServletResponse response, String username, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("enteredUsername", username);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static String newToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
