package org.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum YouthAppMenu {

    YOUTH_WEBSITE("Youth Website", "https://youth.dadabhagwan.org", "https://youth.dadabhagwan.org/images/youth-logo.svg"),
    AKRAM_YOUTH("Akram Youth", "https://youth.dadabhagwan.org/library/akram-youth", "https://download.dadabhagwan.org/Youth/AkramYouth/Eng/2023/img/eng-jan-2023.jpg"),
    YOUTH_EVENTS("Youth Events Schedule", "https://youth.dadabhagwan.org/schedule/", "https://youth.dadabhagwan.org/media/4thbntw2/schedule-banner.jpg"),
//    NOTIFY_AY("Notify New Akram Youth", "", ""),
    ABOUT_US("About Us", "https://youth.dadabhagwan.org/about-us/", "https://youth.dadabhagwan.org/media/5jziaxav/gnc-banner.jpg"),
    ;


    YouthAppMenu(String menu_title, String menu_link, String menu_image) {
        this.menu_title_ = menu_title;
        this.menu_link_ = menu_link;
        this.menu_image_ = menu_image;
    }

    @JsonGetter()
    public String menu_title() {
        return menu_title_;
    }

    @JsonGetter()
    public String menu_link() {
        return menu_link_;
    }

    @JsonGetter()
    public String menu_image() {
        return menu_image_;
    }

    private final String menu_title_;
    private final String menu_link_;
    private final String menu_image_;

}
