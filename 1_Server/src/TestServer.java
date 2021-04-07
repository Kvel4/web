import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


class TestServer {
    private TestServer(File root, int port) {
        try (Socket socket = new Socket("https://0a1be22e95f5.ngrok.io/", port);){
            process(socket);
        } catch (IOException ignored) {
            System.out.println(123);
        }

    }

    private void process(Socket socket) throws IOException {
        Response response = new Response();
        response.setHeader("Connection", "close");

        try {
            process(response);
        } catch (Exception ignored) {
            response.setStatusCode(502);
        }

        try {
            writeResponse(socket, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(Socket socket, Response response) throws IOException {
        try (socket) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            result.write(("HTTP/1.1 " + response.getStatusCode() + " NA\r\n").getBytes());
            for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
                result.write((entry.getKey() + ": " + entry.getValue() + "\r\n").getBytes());
            }
            result.write("\r\n".getBytes());
            if (response.getBody() != null) {
                result.write(response.getBody());
            }

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(result.toByteArray());
            outputStream.close();
        }
    }

    private void process(Response response) throws IOException {
        File file = new File("static/images/1.png");
        byte[] body = readFile(file);
        response.setBody(body);
        response.setHeader("Content-Length", Integer.toString(body.length));
        response.setHeader("Content-Type", getContentType(file));
    }

    private String getContentType(File file) {
        String path = file.getAbsolutePath();
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html";
        } else if (path.endsWith(".png")) {
            return "image/png";
        }
        throw new IllegalArgumentException();
    }

    private void silentClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception ignored) {
            // No operations.
        }
    }

    private byte[] readInputStream(InputStream inputStream, boolean breakOnCrLf) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        byte[] lastTwoBytes = new byte[2];
        int lastTwoBytesSize = 0;

        try {
            byte[] buffer = new byte[1024];
            while (true) {
                if (breakOnCrLf && lastTwoBytes[0] == 13 && lastTwoBytes[1] == 10) {
                    break;
                }

                int read = inputStream.read(buffer);
                if (breakOnCrLf) {
                    if (read == 1) {
                        if (lastTwoBytesSize > 1) {
                            lastTwoBytes[0] = lastTwoBytes[1];
                        }
                        lastTwoBytesSize = Math.max(0, lastTwoBytesSize - 1);
                        lastTwoBytes[lastTwoBytesSize++] = buffer[read - 1];
                    } else if (read >= 2) {
                        lastTwoBytesSize = 2;
                        lastTwoBytes[0] = buffer[read - 2];
                        lastTwoBytes[1] = buffer[read - 1];
                    }
                }

                if (read >= 0) {
                    bytes.write(buffer, 0, read);
                } else {
                    break;
                }
            }
        } finally {
            silentClose(bytes);
        }

        return bytes.toByteArray();
    }

    private byte[] readFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            return readInputStream(inputStream, false);
        } finally {
            silentClose(inputStream);
        }
    }

    private static final class Response {
        private int statusCode;
        private final Map<String, String> headers = new HashMap<>();
        private byte[] body;

        private void setHeader(String name, String value) {
            headers.put(name, value);
        }

        private int getStatusCode() {
            return statusCode;
        }

        private void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            this.body = ("Error " + statusCode).getBytes();
        }

        private byte[] getBody() {
            return body;
        }

        private void setBody(byte[] body) {
            this.body = body;
        }

        private Map<String, String> getHeaders() {
            return Collections.unmodifiableMap(headers);
        }
    }

    public static void main(String[] args) {
        new TestServer(new File("static"), 443);
    }
}
