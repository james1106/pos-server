package com.alipay.demo.trade.model;


import com.alibaba.fastjson.annotation.JSONField;

public class ExtendParams {

    @JSONField(name = "sys_service_provider_id")
    private String sysServiceProviderId;

    public String toString() {
        StringBuilder sb = new StringBuilder("ExtendParams{");
        sb.append("sysServiceProviderId='").append(this.sysServiceProviderId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getSysServiceProviderId() {
        return this.sysServiceProviderId;
    }

    public ExtendParams setSysServiceProviderId(String sysServiceProviderId) {
        this.sysServiceProviderId = sysServiceProviderId;
        return this;
    }
}
