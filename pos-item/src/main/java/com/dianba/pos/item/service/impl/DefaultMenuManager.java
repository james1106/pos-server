package com.dianba.pos.item.service.impl;

import com.dianba.pos.item.po.Menu;
import com.dianba.pos.item.repository.MenuJpaRepository;
import com.dianba.pos.item.service.MenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */
@Service
public class DefaultMenuManager implements MenuManager {

    @Autowired
    private MenuJpaRepository menuJpaRepository;


    @Override
    public List<Menu> findAllByIsFlashAndMerchantIdAndPrintTypeAndCodeId(Integer isFlash, Integer merchantId,
                                                                         Integer printTypeId, Integer codeId) {
        return menuJpaRepository.findAllByIsFlashAndMerchantIdAndPrintTypeAndCodeId(isFlash, merchantId,
                printTypeId, codeId);
    }

    @Override
    public Menu findByPrintType(Integer printType) {

        return menuJpaRepository.findByPrintType(printType);
    }

    @Override
    public Menu findByMenuKeyAndDisplayAndIsDelete(String menuKey,String display,String isDelete) {
        return menuJpaRepository.findByMenuKeyAndDisplayAndIsDelete(menuKey,display,isDelete);
    }


}
