package org.server.notification;

public enum NotificationCategory {

    OTP("JSCA! Your one-time password (OTP) is %s - Dada Bhagwan Vignan Foundation", "JSCA! Your one-time password is %s - Dada Bhagwan Vignan Foundation", "1107160654358640880"),
    REGISTRATION("Congratulations. You have been registered for Gurupurnima Quiz. Your registration number is %s. Language: %s", "Congratulations. You have been registered for Gurupurnima Quiz. Your registration number is %s. Language: %s -DBVF", "1107162348535726381"),
    ;

    NotificationCategory(String mailMsg, String smsMsg, String templateId) {
        this.mailMsg_ = mailMsg;
        this.smsMsg_ = smsMsg;
        this.templateId_ = templateId;
    }

    public String mailMsg() {
        return mailMsg_;
    }

    public String smsMsg() {
        return smsMsg_;
    }

    public String templateId() {
        return templateId_;
    }

    private final String mailMsg_;
    private final String smsMsg_;
    private final String templateId_;
}
