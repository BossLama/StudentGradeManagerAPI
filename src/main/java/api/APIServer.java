package api;

import api.graderequest.GradeRequestHandler;
import api.studentrequest.StudentRequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class APIServer{

    private static HttpServer server;
    private static final int PORT = 8974;

    public static int start(){
        System.out.println("[i] Startet Server auf Port: " + PORT);
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/api/student", new StudentRequestHandler());
            server.createContext("/api/grade", new GradeRequestHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("[i] Server läuft auf Port " + PORT);
        } catch (IOException e) {
            System.out.println("[!] IOException während des Startens - Error: " + e.getMessage());
            return 0;
        }
        return 0;
    }
}
