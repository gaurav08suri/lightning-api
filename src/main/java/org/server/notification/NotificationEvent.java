package org.server.notification;

public class NotificationEvent {

    public void setMobile(String mobile) {
        this.mobile_ = mobile;
    }

    public String getMobile() {
        return mobile_;
    }

    public void setMail(String mail) {
        this.mail_ = mail;
    }

    public String getMail() {
        return mail_;
    }

    public void setCountry(String country) {
        this.country_ = country;
    }

    public String getCountry() {
        return country_;
    }

    private String mobile_;
    private String mail_;
    private String country_;
}
