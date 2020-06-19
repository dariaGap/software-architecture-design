package service;

import business_logic.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import database.Repository;
import database.RepositoryInterface;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.List;


public class Server {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final int BACKLOG = 1;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int STATUS_OK = 200;

    private static Server server;

    public static Server getInstance(){
        if(server == null)
            server = new Server();
        return server;
    }

    private String servicesToJson(){
        RepositoryInterface repo = Repository.getInstance();
        List<Service> services = repo.getServices();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(services);
    }


    public void start() {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
            server.createContext("/", h -> {
                try {
                    final Headers headers = h.getResponseHeaders();
                    final String responseBody = servicesToJson();
                    headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                    final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                    h.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                    h.getResponseBody().write(rawResponseBody);
                } finally {
                    h.close();
                }
            });
            server.start();
            System.out.println("Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
