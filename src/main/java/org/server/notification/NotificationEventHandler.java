package org.server.notification;

import com.lmax.disruptor.EventHandler;
import org.server.core.HttpModule;
import org.server.core.MailModule;
import org.server.core.RedisModule;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NotificationEventHandler implements EventHandler<NotificationEvent> {

    Random rnd = new Random();

    @Override
    public void onEvent(NotificationEvent notificationEvent, long l, boolean b) throws Exception {
        int otp = rnd.nextInt(999999);
//        System.out.println(Thread.currentThread().getName());
//        System.out.println(otp);
        String msg = String.format("JSCA! Your one-time password is %s - Dada Bhagwan Vignan Foundation", otp);
        if("INDIA".equalsIgnoreCase(notificationEvent.getCountry())) {
            if (notificationEvent.getMobile() != null && !notificationEvent.getMobile().isEmpty()) {
                RedisModule.module().set(notificationEvent.getMobile(), String.valueOf(otp));
                String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxxx&DLT_TE_ID=1107160654358640880&message=%s", notificationEvent.getMobile(), URLEncoder.encode(msg, StandardCharsets.UTF_8.toString()));
                HttpModule.module().execute("http://api.msg91.com/api/sendhttp.php?" + queryParams);
            }
        } else if("REST OF WORLD".equalsIgnoreCase(notificationEvent.getCountry())) {
            if (notificationEvent.getMail() != null && !notificationEvent.getMail().isEmpty()) {
                RedisModule.module().set(notificationEvent.getMail(), String.valueOf(otp));
                MailModule.sendMail(notificationEvent.getMail(), "OTP for Registration", msg);
            }
        }
    }
}
