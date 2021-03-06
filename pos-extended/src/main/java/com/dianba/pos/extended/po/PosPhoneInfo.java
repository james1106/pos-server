package com.dianba.pos.extended.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "life_pos.pos_phone_info")
public class PosPhoneInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "areaCode")
    private String areaCode;
    @Column(name = "zip")
    private String zip;
    @Column(name = "company")
    private String company;

    @Column(name = "code")
    private Integer code;

    @Column(name = "print_type")
    private Integer printType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }
}
