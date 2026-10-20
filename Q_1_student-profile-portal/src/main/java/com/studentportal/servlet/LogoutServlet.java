package com.studentportal.servlet;

import com.studentportal.util.SessionKeys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Logs the user out: POST with a valid CSRF token invalidates the session. */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object expected = session.getAttribute(SessionKeys.CSRF_TOKEN);
            String supplied = request.getParameter("csrfToken");
            if (!(expected instanceof String) || supplied == null
                    || !MessageDigest.isEqual(
                            ((String) expected).getBytes(StandardCharsets.UTF_8),
                            supplied.getBytes(StandardCharsets.UTF_8))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid request token.");
                return;
            }
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login?logout=1");
    }

    /** Typing /logout in the address bar must not log anyone out; just send them on. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/students");
    }
}
