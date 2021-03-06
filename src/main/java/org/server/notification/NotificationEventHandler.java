package org.server.notification;

import com.lmax.disruptor.EventHandler;
import org.server.core.HttpModule;
import org.server.core.MailModule;
import org.server.core.RedisModule;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;


enum Link {
    GUJ("Gujarati", "https://rebrand.ly/GP2-guj"),
    HIN("Hindi", "https://rebrand.ly/GP2-hin"),
    ENG("English", "https://rebrand.ly/GP2-eng"),
    GER("German", "https://rebrand.ly/GP2-ger"),
    SPA("Spanish", "https://rebrand.ly/GP2-spa"),
    POR("Portuguese", "https://rebrand.ly/GP2-por"),
    ;

    private final String language;
    private final String link;


    Link(String language, String link) {

        this.language = language;
        this.link = link;
    }

    public static String ofLink(String language) {
        for(Link l :values()) {
            if(l.language.equalsIgnoreCase(language)) {
                return l.link;
            }
        }
        return "";
    }

}

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
                    String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxxx&DLT_TE_ID=%s&message=%s", notificationEvent.getMobile(), category.templateId(), URLEncoder.encode(msg, StandardCharsets.UTF_8.toString()));
                    HttpModule.module().execute("http://api.msg91.com/api/sendhttp.php?" + queryParams);
                } else if(category == NotificationCategory.REGISTRATION) {
                    System.out.println(notificationEvent);
                    String msg = String.format(category.smsMsg(), notificationEvent.getRollNo(), Link.ofLink(notificationEvent.getLanguage()));
                    String queryParams = String.format("country=91&sender=AMBMHT&route=4&mobiles=%s&authkey=xxxxxxxx&DLT_TE_ID=%s&message=%s", notificationEvent.getMobile(), category.templateId(), URLEncoder.encode(msg, StandardCharsets.UTF_8.toString()));
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
                    String msg = String.format(category.mailMsg(), notificationEvent.getRollNo(), Link.ofLink(notificationEvent.getLanguage()));
                    MailModule.sendMail(notificationEvent.getMail(), "Gurupurnima Quiz - Level 1", msg);
                }
            }
        }
    }
}
