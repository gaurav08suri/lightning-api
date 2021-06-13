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
        NotificationCategory category = notificationEvent.getCategory();
        if("INDIA".equalsIgnoreCase(notificationEvent.getCountry())) {
            if (notificationEvent.getMobile() != null && !notificationEvent.getMobile().isEmpty()) {
                if (category == NotificationCategory.OTP) {
                    int otp = rnd.nextInt(999999);
                    String msg = String.format(category.smsMsg(), otp);
                    RedisModule.module().set(notificationEvent.getMobile(), String.valueOf(otp));
                    String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxx&DLT_TE_ID=%s&message=%s", notificationEvent.getMobile(), category.templateId(), URLEncoder.encode(msg, StandardCharsets.UTF_8.toString()));
                    HttpModule.module().execute("http://api.msg91.com/api/sendhttp.php?" + queryParams);
                } else if(category == NotificationCategory.REGISTRATION) {
                    String msg = String.format(category.smsMsg(), notificationEvent.getRollNo(), notificationEvent.getLanguage());
                    String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxx&DLT_TE_ID=%s&message=%s", notificationEvent.getMobile(), category.templateId(), URLEncoder.encode(msg, StandardCharsets.UTF_8.toString()));
                    HttpModule.module().execute("http://api.msg91.com/api/sendhttp.php?" + queryParams);
                }
            }
        } else if("REST OF WORLD".equalsIgnoreCase(notificationEvent.getCountry())) {
            if (notificationEvent.getMail() != null && !notificationEvent.getMail().isEmpty()) {
                if (category == NotificationCategory.OTP) {
                    int otp = rnd.nextInt(999999);
                    String msg = String.format(category.mailMsg(), otp);
                    RedisModule.module().set(notificationEvent.getMail(), String.valueOf(otp));
                    MailModule.sendMail(notificationEvent.getMail(), "One Time Password (OTP) for Registration", msg);
                } else if(category == NotificationCategory.REGISTRATION) {
                    String msg = String.format(category.mailMsg(), notificationEvent.getRollNo(), notificationEvent.getLanguage());
                    MailModule.sendMail(notificationEvent.getMail(), "Registration for Gurupurnima Quiz", msg);
                }
            }
        }
    }
}
