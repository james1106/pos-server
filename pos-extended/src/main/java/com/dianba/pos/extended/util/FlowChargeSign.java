package com.dianba.pos.extended.util;

import com.dianba.pos.common.util.MapUtil;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class FlowChargeSign {

    public static String getSignByMap(Map<String,Object> map){
        Map<String, Object> pdmap = MapUtil.sortMapByKey(map);
        String sign = MapUtil.createLinkString(pdmap);
        String result = FlowCharge19EUtil.getKeyedDigest(sign, FlowCharge19EUtil.FOR_KEY);
        return result;

    }
}
