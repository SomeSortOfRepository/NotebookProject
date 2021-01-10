package ru.dolinini.notebook.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class simpleErrorHandleController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode==403) {
            Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
            return String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
                            + "<div>Exception Message: <b>You don't have rights for using what you tried to use</b></div>" +
                            "<a href=\"/notebook/notes\">TO NOTES</a><body></html>",
                    statusCode);
        }
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        return String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
                        + "<div>Exception Message: <b>%s</b></div><body></html>",
                statusCode, exception==null? "N/A": exception.getMessage());
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
