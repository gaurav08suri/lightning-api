package org.server.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.db.flyway.tables.pojos.Participants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Application {

    public static void main(String[] args) throws SQLException {
        QueuedThreadPool threadPool = new QueuedThreadPool(600, 10, 60);
        Random rnd = new Random();

        Javalin app = Javalin.create(c -> c.server(() -> new Server(threadPool)) ).start(5433);
        DSLContext dslContext = DSL.using(StoreSource.REGISTRATION.dataSource(), SQLDialect.POSTGRES);
        app.get("/", (ctx) -> ctx.result("Server is up and running"));
        app.post("/participant_check", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            String sql = null;
            String param = null;
            int otp = rnd.nextInt(999999);
            if("INDIA".equalsIgnoreCase(participant.getCountry())) {
                sql = "select 1 from registration_app.participants where mobile = :param";
                param = participant.getMobile();
                RedisModule.module().set(participant.getMobile(), String.valueOf(otp));
                String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxxx&DLT_TE_ID=1107160654358640880&message=%s", participant.getMobile(), URLEncoder.encode(String.format("JSCA! Your one-time password is %s - Dada Bhagwan Vignan Foundation", otp), StandardCharsets.UTF_8.toString()));
                HttpModule.module().execute("http://api.msg91.com/api/sendhttp.php?" + queryParams);
            } else if("REST OF WORLD".equalsIgnoreCase(participant.getCountry())) {
                sql = "select 1 from registration_app.participants where mail = :param";
                param = participant.getMail();
            }
            int result = dslContext.resultQuery(sql, DSL.param("param", param)).execute();
            Map<String, String> map = new HashMap<>();
            map.put("exists", String.valueOf(result));
            ctx.json(map);
        });
        app.post("/send_otp", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            try {
                int otp = rnd.nextInt(999999);

                if("INDIA".equalsIgnoreCase(participant.getCountry())) {
                    RedisModule.module().set(participant.getMobile(), String.valueOf(otp));
                    String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxxx&DLT_TE_ID=1107160654358640880&message=%s", participant.getMobile(), URLEncoder.encode(String.format("JSCA! Your one-time password is %s - Dada Bhagwan Vignan Foundation", otp), StandardCharsets.UTF_8.toString()));
                    HttpModule.module().execute("http://api.msg91.com/api/sendhttp.php?" + queryParams);
                } else if("REST OF WORLD".equalsIgnoreCase(participant.getCountry())) {

                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("Error occurred").status(500);
            }
            ctx.status(200).result("Otp Sent");
        });
        app.post("/verify_otp", (ctx) -> {
            String data = ctx.body();
            ObjectMapper obj = new ObjectMapper();
            Map<String, String> m = obj.readValue(data, new TypeReference<Map<String,String>>(){});
            try {
                String otp = m.get("otp");
                String expectedOtp = RedisModule.module().get(m.get("mobile"));
                if(otp.equals(expectedOtp)) {
                    String token = null;
                    try {
                        Algorithm algorithm = Algorithm.HMAC256("registrationApp");
                        token = JWT.create()
                                .withIssuer("auth0")
                                .sign(algorithm);
                    } catch (JWTCreationException exception){
                        //Invalid Signing configuration / Couldn't convert Claims.
                    }
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("token", token);
                    ctx.json(m1).status(200);
                } else {
                    ctx.result("Otp does not match").status(441);
                }
            } catch (Exception e) {
                ctx.result(e.getMessage()).status(500);
            }
        });
        app.post("/participants", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            BaseDao dao = new BaseDao(org.db.flyway.tables.Participants.PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            dao.insert(participant);
            ctx.result("Successfully Inserted");
        });
        app.put("/participants", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            BaseDao dao = new BaseDao(org.db.flyway.tables.Participants.PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            dao.update(participant);
            ctx.result("Successfully Inserted");
        });
        app.get("/participants/:id", (ctx) -> {
            BaseDao dao = new BaseDao(org.db.flyway.tables.Participants.PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            Participants p = dao.findById(Integer.parseInt(ctx.pathParam("id")));
            ctx.json(p);
        });
        app.get("/participants", (ctx) -> {
            BaseDao dao = new BaseDao(org.db.flyway.tables.Participants.PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            ctx.json(dao.findAll());
        });
    }
}