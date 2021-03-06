package com.dianba.pos.item.repository;

import com.dianba.pos.item.po.LifeItemTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by zhangyong on 2017/5/25.
 */
@Transactional
@Repository
public interface LifeItemTemplateJpaRepository extends JpaRepository<LifeItemTemplate, Long> {


    /**
     * 获取分类商品列表
     *
     * @param id
     * @return
     */

    List<LifeItemTemplate> getAllById(Long id);

    /**
     * 根据code码搜索商品信息
     *
     * @param
     * @return
     */
    @Query("SELECT it FROM LifeItemTemplate it where it.barcode=:barcode and it.ascriptionType=1")
    LifeItemTemplate getItemTemplateByBarcode(@Param("barcode") String barcode);

    LifeItemTemplate findByBarcodeAndAscriptionType(String barcode,Integer type);
    /***
     * 判断模板名字是否重复
     * @param
     * @return
     */
    @Query("select itemp from LifeItemTemplate  itemp where itemp.name=:name and itemp.ascriptionType=1")
    LifeItemTemplate getItemTemplateByName(@Param("name") String name);

    LifeItemTemplate getItemTemplateById(Long id);

    LifeItemTemplate findByAscriptionType(Integer type);
    List<LifeItemTemplate> findByBarcodeIn(List<String> barcodes);
}
