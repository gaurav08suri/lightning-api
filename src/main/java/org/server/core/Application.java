package org.server.core;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.models.YouthAppMenu;

import java.util.Arrays;


public class Application {

    public static void main(String[] args)  {
        QueuedThreadPool threadPool = new QueuedThreadPool(600, 10, 60);

        Javalin app = Javalin.create(c -> {
            c.enableCorsForAllOrigins();
            c.server(() -> new Server(threadPool));
        } ).start(8081);

        app.get("/", (ctx) -> ctx.result("Server is up and running !!!"));
        app.get("/youth_app_menu", (ctx) -> ctx.json(YouthAppMenu.values()));
    }
}