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
import org.db.flyway.tables.pojos.Runner;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.server.models.Response;
import org.server.notification.NotificationCategory;
import org.server.notification.NotificationEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.db.flyway.tables.Participants.PARTICIPANTS;
import static org.db.flyway.tables.Runner.RUNNER;
import static org.jooq.impl.DSL.count;


public class Application {

    public static void main(String[] args)  {
        QueuedThreadPool threadPool = new QueuedThreadPool(600, 10, 60);
        Random rnd = new Random();
        RingBuffer<NotificationEvent> ringBuffer = ApplicationModule.init();
        Javalin app = Javalin.create(c -> {
            c.enableCorsForAllOrigins();
            c.server(() -> new Server(threadPool));
        } ).start(8081);
        DSLContext dslContext = DSL.using(StoreSource.REGISTRATION.dataSource(), SQLDialect.POSTGRES);
        app.get("/", (ctx) -> ctx.json(Response.of("Server is up and running")));
        app.post("/notify", (ctx) -> {
            String body = ctx.body();
            ObjectMapper mapper = new ObjectMapper();
            String[] tokens = mapper.readValue(body, String[].class);
            // ["2/18/2022 22:41:18","G","a","a","2/8/1989","Male","5km","L - 40inch chestsize","Sector 4 Library"]
            Runner runner = new Runner();
            runner.setAddress(tokens[3]);
            runner.setBDay(tokens[4]);
            runner.setCollectionPlace(tokens[8]);
            runner.setGender(tokens[5]);
            runner.setMobile(tokens[2]);
            runner.setName(tokens[1]);
            runner.setRace(tokens[6]);
            runner.setTshirtSize(tokens[7]);
            runner.setId(dslContext.nextval(Sequences.RUNNER_ID_SEQ));
//            System.out.println(participant.getRollno());
            BaseRDao dao = new BaseRDao(RUNNER.asTable(), Runner.class, dslContext.configuration());
            try {
                dao.insert(runner);
            } catch (DataAccessException e) {
                String sqlState = e.sqlState();
                for(PSQLState p : PSQLState.values()) {
                    if (p.getState().equals(sqlState)) {
                        sqlState = p.name();
                        break;
                    }
                }
                ctx.json(Response.of(sqlState)).status(500);
                return;
            }
            Runner p = dao.fetchOne(RUNNER.ID, runner.getId());
            long sequence = ringBuffer.next();
            try {
                NotificationEvent event = ringBuffer.get(sequence);
                event.setCountry("INDIA");
//                event.setMail(p.getMail());
                event.setMobile(p.getMobile());
                event.setCategory(NotificationCategory.REGISTRATION);
                event.id = p.getId();
                event.setLanguage(p.getRace());
            } finally {
                ringBuffer.publish(sequence);
            }
            ctx.json(p);
        });
        app.post("/participant_check", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            String sql = null;
            String param = null;
            if("INDIA".equalsIgnoreCase(participant.getCountry())) {
                if(participant.getMobile() == null || participant.getMobile().isEmpty()) {
                    ctx.json(Response.of("Mobile not present")).status(500);
                    return;
                }
                sql = "select id from registration_app.participants where mobile = :param";
                param = participant.getMobile();
                long sequence = ringBuffer.next();
                try {
                    NotificationEvent event = ringBuffer.get(sequence);
                    event.setCountry(participant.getCountry());
                    event.setMail(participant.getMail());
                    event.setMobile(participant.getMobile());
                    event.setCategory(NotificationCategory.OTP);
                } finally {
                    ringBuffer.publish(sequence);
                }
            } else if("REST OF WORLD".equalsIgnoreCase(participant.getCountry())) {
                if(participant.getMail() == null || participant.getMail().isEmpty()) {
                    ctx.json(Response.of("Mail not present")).status(500);
                    return;
                }
                long sequence = ringBuffer.next();
                try {
                    NotificationEvent event = ringBuffer.get(sequence);
                    event.setCountry(participant.getCountry());
                    event.setMail(participant.getMail());
                    event.setMobile(participant.getMobile());
                    event.setCategory(NotificationCategory.OTP);
                } finally {
                    ringBuffer.publish(sequence);
                }
                sql = "select id from registration_app.participants where mail = :param";
                param = participant.getMail();
            }
            Result<Record> result = dslContext.resultQuery(sql, DSL.param("param", param)).fetch();
            Map<String, String> map = new HashMap<>();
            map.put("exists", String.valueOf(result.size() > 0 ? 1 : 0));
            if(result.size() > 0) {
                map.put("id", String.valueOf(result.get(0).get("id")));
            }
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
                    ctx.json(Response.of("Otp does not match")).status(226);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.json(Response.of(e.getMessage())).status(500);
            }
        });
        app.post("/participants", (ctx) -> {
            Participants participant = ctx.bodyAsClass(Participants.class);
            participant.setRollno(dslContext.nextval(Sequences.ROLL_NO_SEQ).toString());
            System.out.println(participant.getRollno());
            BaseDao dao = new BaseDao(PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            try {
                dao.insert(participant);
            } catch (DataAccessException e) {
                String sqlState = e.sqlState();
                for(PSQLState p : PSQLState.values()) {
                    if (p.getState().equals(sqlState)) {
                        sqlState = p.name();
                        break;
                    }
                }
                ctx.json(Response.of(sqlState)).status(500);
                return;
            }
            Participants p = dao.fetchOne(PARTICIPANTS.ROLLNO, participant.getRollno());
            long sequence = ringBuffer.next();
            try {
                NotificationEvent event = ringBuffer.get(sequence);
                event.setCountry(p.getCountry());
                event.setMail(p.getMail());
                event.setMobile(p.getMobile());
                event.setCategory(NotificationCategory.REGISTRATION);
                event.setRollNo(p.getRollno());
                event.setLanguage(p.getLanguage());
            } finally {
                ringBuffer.publish(sequence);
            }
            ctx.json(p);
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
            ctx.json(p);
        });
        app.get("/participantscnt", (ctx) -> {
            Result<Record2<String, Integer>> result = dslContext.select(PARTICIPANTS.LANGUAGE, count().as("CNT")).from(PARTICIPANTS).groupBy(PARTICIPANTS.LANGUAGE).fetch();
//            BaseDao dao = new BaseDao(PARTICIPANTS.asTable(), Participants.class, dslContext.configuration());
            String data = "";
            int totalCount = 0;
            for(Record2 r : result) {
                data += "<tr>" +
                        "<td>" +
                        r.get(PARTICIPANTS.LANGUAGE) +
                        "</td>" +
                        "<td>" +
                        r.get("CNT") +
                        "</td>" +
                        "</tr>";
                totalCount += r.get("CNT", Integer.class);
            }
            String table = "<table border>" +
                    "<tr>" +
                    "<th>Language</th>" +
                    "<th>Count</th>" +
                    "</tr>" +
                   data +
                    "</table>";
            ctx.html("<html>" +
                    "<body>" +
                    table +
                    "<h1>Total Count: " +
                    totalCount +
                    "</h1>" +
                    "</body>" +
                    "</html>");
        });
    }
}