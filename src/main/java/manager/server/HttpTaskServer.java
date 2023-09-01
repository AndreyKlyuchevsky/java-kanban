package manager.server;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/hello", new HelloHandler());




        httpServer.start();
    }



    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /hello запроса от клиента.");

            String response;

            switch(method) {
                case "POST":
                    response = "Вы использовали метод POST!";
                    break;
                case "GET":
                    response = "Вы использовали метод GET!";
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }

            httpExchange.getResponseHeaders().set("content-type", "text/plain; charset=utf-8");
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes(DEFAULT_CHARSET));

            }
        }
    }
}