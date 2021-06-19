package org.server.notification;

public enum NotificationCategory {

    OTP("JSCA! Your one-time password (OTP) is %s - Dada Bhagwan Vignan Foundation", "JSCA! Your one-time password is %s - Dada Bhagwan Vignan Foundation", "1107160654358640880"),
    REGISTRATION("Time to play Level 1 of Gurupurnima quiz.\n" +
            "Use your registration number %s.\n" +
            "Visit: %s\n" +
            "Date: 20 & 21 June\n" +
            "Time: Full Day\n" +
            "-DBVF", "Time to play Level 1 of Gurupurnima quiz.\n" +
            "Use your registration number %s.\n" +
            "Visit: %s\n" +
            "Date: 20 & 21 June\n" +
            "Time: Full Day\n" +
            "-DBVF", "1107162348544182203"),
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
