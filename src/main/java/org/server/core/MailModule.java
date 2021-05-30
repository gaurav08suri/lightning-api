package org.server.core;

import org.simplejavamail.api.email.CalendarMethod;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class MailModule {

    static {
        mailer = MailerBuilder
                .withSMTPServer("xxxxxx", 1234, "xxxxxx", "xxxxxx")
//                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
    }


    public static void sendMail(String to, String subject, String text) {
        Email email = EmailBuilder.startingBlank()
                .from("xxxxxx")
                .to(to)
                .withSubject(subject)
                .withPlainText(text)
                .buildEmail();
        mailer.sendMail(email);
    }

    private static Mailer mailer;
}
