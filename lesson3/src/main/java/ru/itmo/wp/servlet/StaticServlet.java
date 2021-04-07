package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<File> files = processRequest(request);
        if (files != null) {
            response.setContentType(getContentTypeFromName(files.get(0).getName()));
            OutputStream outputStream = response.getOutputStream();
            for (File file : files) {
                Files.copy(file.toPath(), outputStream);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private List<File> processRequest(HttpServletRequest request) {
        List<File> files = new ArrayList<>();
        String[] uris = request.getRequestURI().split("\\+");

        for (String uri : uris) {
            File file = new File(
                    new File(
                            getServletContext().getRealPath(""),
                            "../../src/main/webapp/static"),
                    uri);

            if (!file.isFile()) {
                file = new File(getServletContext().getRealPath("/static"), uri);
            }

            if (file.isFile()) {
                files.add(file);
            } else {
                return null;
            }
        }
        return files;
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
