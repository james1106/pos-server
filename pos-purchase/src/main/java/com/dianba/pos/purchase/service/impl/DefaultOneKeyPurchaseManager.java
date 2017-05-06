package com.dianba.pos.purchase.service.impl;

import com.dianba.pos.common.util.HttpProxy;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.po.MenuType;
import com.dianba.pos.menu.service.MenuTypeManager;
import com.dianba.pos.purchase.mapper.OneKeyPurchaseMapper;
import com.dianba.pos.purchase.pojo.OneKeyPurchase;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import com.dianba.supplychain.service.GoodsManager;
import com.dianba.supplychain.vo.Items;
import com.dianba.supplychain.vo.MatchItems;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultOneKeyPurchaseManager implements OneKeyPurchaseManager {

    @Autowired
    private OneKeyPurchaseMapper oneKeyPurchaseMapper;
    @Autowired
    private GoodsManager goodsManager;
    @Autowired
    private MenuTypeManager menuTypeManager;

    public Map<String, Object> warnInvenstoryList(Integer merchantId, Integer userId)
            throws HttpProxy.HttpAccessException, IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 查询库存小于预警库存/当前库存小于周销量的日平均值的商品,且不在进货中的商品
        List<OneKeyPurchase> menuEntities = oneKeyPurchaseMapper.queryWarnInvenstory(merchantId, null);
        if (CollectionUtils.isEmpty(menuEntities)) {
            return resultMap;
        }
        // 取出商品类型,条码
        StringBuilder sb = new StringBuilder();
        List<Integer> typeIds = new ArrayList<>();
        Map<String, OneKeyPurchase> menuEntityMap = new HashMap<>();
        for (OneKeyPurchase purchase : menuEntities) {
            if (StringUtils.isNotBlank(purchase.getBarcode())) {
                sb.append(purchase.getBarcode()).append(",");
                menuEntityMap.put(purchase.getBarcode(), purchase);
            }
            typeIds.add(purchase.getTypeId());
        }
        sb = sb.delete(sb.length() - 1, sb.length());

        // 用 barcodes 去 供应链查找可进货的商品,极其规格.
        List<MatchItems> matchItemsList = goodsManager.matchItemsByBarcode(userId, sb.toString());
        List<MenuType> menutypeEntites = menuTypeManager.findMenuTypeByIds(typeIds);
        //系统内建议采购
        for (MatchItems matchItems : matchItemsList) {
            OneKeyPurchase menuEntity = menuEntityMap.get(matchItems.getBarcode());
            int todayRepertory = menuEntity.getTodayRepertory() <= 0 ? 0 : menuEntity.getTodayRepertory();
            String name = menuEntity.getName();
            // 设置当前库存，标准库存，预警库存
            matchItems.setRepertory(menuEntity.getTodayRepertory());
            matchItems.setStandardInventory(menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory());
            matchItems.setWarnInventory(menuEntity.getWarnInventory() == null ? 20 : menuEntity.getWarnInventory());
            matchItems.setMenuTypeId(menuEntity.getTypeId());
            for (Items item : matchItems.getItems()) {
                if (item.getStandard() == null) {
                    item.setStandard(12);
                }
                int defaultPurchase=calculationNeed(menuEntity.getDaySale()
                        ,todayRepertory,matchItems.getWarnInventory(),item.getStandard());
                if (defaultPurchase < item.getMinSales()) {
                    defaultPurchase = item.getMinSales();
                }
                item.setDefaultPurchase(defaultPurchase);
                item.setName(name);
            }
        }

        //系统外建议采购
        List<MatchItems> externalList = new ArrayList<>();
        boolean isNotExists = true;
        for (OneKeyPurchase menuEntity : menuEntityMap.values()) {
            for (MatchItems items : matchItemsList) {
                if (menuEntity.getBarcode().equals(items.getBarcode())) {
                    isNotExists = false;
                    break;
                }
            }
            if (isNotExists) {
                MatchItems matchItems = new MatchItems();
                int todayRepertory = menuEntity.getTodayRepertory() == null ? 0 : menuEntity.getTodayRepertory();
                Integer standard = menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory();
                matchItems.setRepertory(menuEntity.getTodayRepertory());
                matchItems.setStandardInventory(menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory());
                matchItems.setWarnInventory(menuEntity.getWarnInventory() == null ? 0 : menuEntity.getWarnInventory());
                matchItems.setBarcode(menuEntity.getBarcode());
                matchItems.setMenuTypeId(menuEntity.getTypeId());
                Items items = new Items();
                items.setStandard(standard);
                items.setImage(menuEntity.getImage());
                items.setUnit(menuEntity.getUnit());
                items.setDefaultPurchase(calculationNeed(menuEntity.getDaySale()
                        ,todayRepertory,matchItems.getWarnInventory(),items.getStandard()));
                items.setPrice(new BigDecimal(menuEntity.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                items.setName(menuEntity.getName());
                items.setId(menuEntity.getId());
                items.setStock(todayRepertory);
                matchItems.getItems().add(items);
                externalList.add(matchItems);
            }
        }
        resultMap.put("preferentialList", matchItemsList);
        resultMap.put("externalList", externalList);
        resultMap.put("menutypeList", menutypeEntites);
        return resultMap;
    }

    private int calculationNeed(int daySale,int todayRepertory,int warnInventory,int standard){
        int need = 0;
        if (daySale > 0) {
            // 所需补充的库存=上周（日平均销量）*3-剩余库存
            need = daySale * 3 - todayRepertory;
        }
        if (need <= 0 && warnInventory > todayRepertory) {
            // 所需补充的库存=预警库存-剩余库存
            need = warnInventory - todayRepertory;
        }
        // 设置默认采购数量
        // 设置默认采购数量=所需补充库/供应链商品规格（如一箱12个），除不尽且大于规格一半的话采购数量加1
        int remainder = need % standard;
        if (remainder != 0 && remainder > (standard / 2)) {
            remainder = 1;
        }
        return need / standard + remainder;
    }
}