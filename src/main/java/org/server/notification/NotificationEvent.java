package org.server.notification;

import org.db.flyway.tables.pojos.Participants;

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

    public NotificationCategory getCategory() {
        return category_;
    }
    public void setCategory(NotificationCategory category) {
        this.category_ = category;
    }

    public String getRollNo() {
        return rollNo_;
    }

    public void setRollNo(String rollNo) {
        this.rollNo_ = rollNo;
    }

    public String getLanguage() {
        return language_;
    }

    public void setLanguage(String language) {
        this.language_ = language;
    }

    private String mobile_;
    private String mail_;
    private String country_;
    private NotificationCategory category_;
    private String rollNo_;
    private String language_;
    private String distance_;
    public int id;

    public String getDistance_() {
        return distance_;
    }

    public NotificationEvent setDistance_(String distance_) {
        this.distance_ = distance_;
        return this;
    }
}
