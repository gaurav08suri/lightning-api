package org.server.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.RingBuffer;
import io.javalin.Javalin;
import org.db.flyway.Sequences;
import org.db.flyway.tables.pojos.Participants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Sequence;
import org.jooq.impl.DSL;
import org.server.models.Response;
import org.server.notification.NotificationEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.db.flyway.tables.Participants.PARTICIPANTS;


public class Application {

    public static void main(String[] args)  {
        QueuedThreadPool threadPool = new QueuedThreadPool(600, 10, 60);
        Random rnd = new Random();
        RingBuffer<NotificationEvent> ringBuffer = ApplicationModule.init();
        Javalin app = Javalin.create(c -> {
//            c.enableCorsForAllOrigins();
            c.server(() -> new Server(threadPool));
        } ).start(8081);
        DSLContext dslContext = DSL.using(StoreSource.REGISTRATION.dataSource(), SQLDialect.POSTGRES);
        app.get("/", (ctx) -> ctx.json(Response.of("Server is up and running")));
        app.post("/participant_check", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            String sql = null;
            String param = null;
            int otp = rnd.nextInt(999999);
            String msg = String.format("JSCA! Your one-time password is %s - Dada Bhagwan Vignan Foundation", otp);
            if("INDIA".equalsIgnoreCase(participant.getCountry())) {
                if(participant.getMobile() == null || participant.getMobile().isEmpty()) {
                    ctx.json(Response.of("Mobile not present")).status(441);
                }
                sql = "select id from registration_app.participants where mobile = :param";
                param = participant.getMobile();
                long sequence = ringBuffer.next();
                try {
                    NotificationEvent event = ringBuffer.get(sequence);
                    event.setCountry(participant.getCountry());
                    event.setMail(participant.getMail());
                    event.setMobile(participant.getMobile());
                } finally {
                    ringBuffer.publish(sequence);
                }
            } else if("REST OF WORLD".equalsIgnoreCase(participant.getCountry())) {
                if(participant.getMail() == null || participant.getMail().isEmpty()) {
                    ctx.json(Response.of("Mail not present")).status(441);
                }
                long sequence = ringBuffer.next();
                try {
                    NotificationEvent event = ringBuffer.get(sequence);
                    event.setCountry(participant.getCountry());
                    event.setMail(participant.getMail());
                    event.setMobile(participant.getMobile());
                } finally {
                    ringBuffer.publish(sequence);
                }
                sql = "select id from registration_app.participants where mail = :param";
                param = participant.getMail();
            }
            Integer result = dslContext.resultQuery(sql, DSL.param("param", param)).execute();
            Map<String, String> map = new HashMap<>();
            map.put("exists", String.valueOf(result > 0 ? 1 : 0));
            map.put("id", String.valueOf(result));
            ctx.json(map);
        });
        app.post("/verify_otp", (ctx) -> {
            String data = ctx.body();
            ObjectMapper obj = new ObjectMapper();
            Map<String, String> m = obj.readValue(data, new TypeReference<Map<String,String>>(){});
            try {
                String otp = m.get("otp");
                String country = m.get("country");
                String expectedOtp = "INDIA".equalsIgnoreCase(country) ? RedisModule.module().get(m.get("mobile")) : RedisModule.module().get(m.get("mail"));
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
                    ctx.json(Response.of("Otp does not match")).status(441);
                }
            } catch (Exception e) {
                ctx.json(Response.of(e.getMessage())).status(500);
            }
        });
        app.post("/participants", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            participant.setRollno("QUIZ-" + dslContext.nextval(Sequences.ROLL_NO_SEQ));
            System.out.println(participant.getRollno());
            BaseDao dao = new BaseDao(PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            dao.insert(participant);
            ctx.json(Response.of("Successfully Inserted"));
        });
        app.put("/participants", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            BaseDao dao = new BaseDao(PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            dao.update(participant);
            ctx.json(Response.of("Successfully Inserted"));
        });
        app.get("/participants/:id", (ctx) -> {
            BaseDao dao = new BaseDao(PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            Participants p = dao.findById(Integer.parseInt(ctx.pathParam("id")));
        });
        app.get("/participants", (ctx) -> {
            BaseDao dao = new BaseDao(PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            ctx.json(dao.findAll());
        });
    }
}