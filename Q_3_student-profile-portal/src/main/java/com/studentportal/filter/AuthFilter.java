package com.studentportal.filter;

import com.studentportal.util.SessionKeys;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Gatekeeper: only logged-in users may reach the student pages and the file-streaming servlet.
 * Everything else (login page, CSS, placeholder image) stays public.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/students", "/students/*", "/file"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false); // never create a session here
        boolean loggedIn = session != null && session.getAttribute(SessionKeys.USERNAME) != null;

        if (loggedIn) {
            // Stop browsers/proxies from showing protected pages after logout (Back button).
            response.setHeader("Cache-Control", "private, no-cache, must-revalidate");
            chain.doFilter(req, res);
            return;
        }

        if ("/file".equals(request.getServletPath())) {
            // <img>/download requests: a plain 401 is more useful than an HTML redirect.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please log in first.");
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
