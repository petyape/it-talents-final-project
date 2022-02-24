package com.example.goodreads.controller;

import com.example.goodreads.exceptions.UnauthorizedException;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public abstract class BaseController {

    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";

    public static void validateSession(HttpSession session, HttpServletRequest request) {
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean)session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if (newSession || !logged || !sameIP) {
            throw new UnauthorizedException("Invalid session! Please, login!");
        }
    }

    public static void validateSession(HttpSession session) {
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean)session.getAttribute(LOGGED));
        if (newSession || !logged) {
            throw new UnauthorizedException("Invalid session! Please, login!");
        }
    }

}
