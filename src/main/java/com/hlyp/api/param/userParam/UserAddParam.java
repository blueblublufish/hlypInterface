package com.hlyp.api.param.userParam;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/26.
 */
public class UserAddParam {
    private String userName;
    private String sex;
    private List<TestParam> testParams;


    public List<TestParam> getTestParams() {
        return testParams;
    }

    public void setTestParams(List<TestParam> testParams) {
        this.testParams = testParams;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
