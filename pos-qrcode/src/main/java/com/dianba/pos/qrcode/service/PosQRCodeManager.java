package com.dianba.pos.qrcode.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.qrcode.po.PosQRCode;

import javax.servlet.http.HttpServletResponse;

public interface PosQRCodeManager {

    PosQRCode getQRCodeByMerchantId(Long passportId);

    PosQRCode getQRCodeByCode(String code);

    void showQRCodeByPassportId(Long passportId, Integer width, Integer height
            , HttpServletResponse response) throws Exception;

    BasicResult showQRCodeContentByPassportId(Long passportId) throws Exception;

    void showQRCodeByCode(String code, Integer width, Integer height
            , HttpServletResponse response) throws Exception;

    void generateQRCodeByContent(String content, Integer width, Integer height
            , HttpServletResponse response) throws Exception;
}
