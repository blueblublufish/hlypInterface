package com.hlyp.api.param.account;

import java.util.Date;

public class ApplyContactUsAddParam {
    private String name;

    private String tel;

    private String type;

    private String pricture;

    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPricture() {
        return pricture;
    }

    public void setPricture(String pricture) {
        this.pricture = pricture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
