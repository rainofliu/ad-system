package com.ajin.ad.service;

import com.ajin.ad.Application;
import com.ajin.ad.constant.CommonStatus;
import com.ajin.ad.dao.AdPlanRepository;
import com.ajin.ad.dao.AdUnitRepository;
import com.ajin.ad.dao.CreativeRepository;
import com.ajin.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.ajin.ad.dao.unit_condition.AdUnitItRepository;
import com.ajin.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.ajin.ad.dao.unit_condition.CreativeUnitRepository;
import com.ajin.ad.dmp.DConstant;
import com.ajin.ad.dmp.table.*;
import com.ajin.ad.entity.AdCreative;
import com.ajin.ad.entity.AdPlan;
import com.ajin.ad.entity.AdUnit;
import com.ajin.ad.entity.unit_condition.AdUnitDistrict;
import com.ajin.ad.entity.unit_condition.AdUnitIt;
import com.ajin.ad.entity.unit_condition.AdUnitKeyword;
import com.ajin.ad.entity.unit_condition.CreativeUnit;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出数据服务，将数据库数据导出到磁盘中
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, // 启动类
        webEnvironment = SpringBootTest.WebEnvironment.NONE) // 不需要设置Web环境
public class DumpDataService {

    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitKeywordRepository keywordRepository;

    @Test
    public void dumpAdTableData() {

        dumpAdPlanTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_PLAN)
        );

        dumpAdUnitTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT)
        );

        dumpAdCreativeTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE)
        );

        dumpAdCreativeUnitTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE_UNIT)
        );

        dumpAdUnitDistrictTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_DISTRICT)
        );

        dumpAdUnitItTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_IT)
        );

        dumpAdUnitKeywordTable(
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_KEYWORD)
        );

    }


    private void dumpAdPlanTable(String fileName) {

        // 获取有效的广告计划
        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(CommonStatus.VALID.getStatus());

        if (CollectionUtils.isEmpty(adPlans)) {
            return;
        }
        List<AdPlanTable> planTables = new ArrayList<>();
        // 将adPlans的每一条数据添加到planTables中
        for (AdPlan adPlan : adPlans) {

            AdPlanTable table = new AdPlanTable();

            table.setId(adPlan.getId());
            table.setUserId(adPlan.getUserId());
            table.setPlanStatus(adPlan.getPlanStatus());
            table.setStartDate(adPlan.getStartDate());
            table.setEndDate(adPlan.getEndDate());

            planTables.add(table);

        }

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdPlanTable planTable : planTables) {
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdPlanTable error !");
        }
    }

    private void dumpAdUnitTable(String fileName) {

        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(CommonStatus.VALID.getStatus());

        if (CollectionUtils.isEmpty(adUnits)) {
            return;
        }

        List<AdUnitTable> unitTables = new ArrayList<>();

        adUnits.forEach(u -> unitTables.add(
                new AdUnitTable(
                        u.getId(),
                        u.getUnitStatus(),
                        u.getPositionType(),
                        u.getPlanId()
                )
        ));


        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitTable unitTable : unitTables) {
                writer.write(JSON.toJSONString(unitTable));
                // 换行
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdUnitTable error !");
        }

    }

    private void dumpAdCreativeTable(String fileName) {

        List<AdCreative> creatives = creativeRepository.findAll();

        if (CollectionUtils.isEmpty(creatives)) {
            return;
        }

        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(c -> creativeTables.add(
                new AdCreativeTable(
                        c.getId(),
                        c.getName(),
                        c.getType(),
                        c.getMaterialType(),
                        c.getHeight(),
                        c.getWidth(),
                        c.getAuditStatus(),
                        c.getUrl()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdCreativeTable creativeTable : creativeTables) {
                writer.write(JSON.toJSONString(creativeTable));
                // 换行
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdCreativeTable error !");
        }

    }

    private void dumpAdCreativeUnitTable(String fileName) {

        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();

        if (CollectionUtils.isEmpty(creativeUnits)) {
            return;
        }

        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();

        creativeUnits.forEach(c -> creativeUnitTables.add(
                new AdCreativeUnitTable(
                        c.getCreativeId(),
                        c.getUnitId()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdCreativeUnitTable creativeUnitTable : creativeUnitTables) {
                writer.write(JSON.toJSONString(creativeUnitTable));
                // 换行
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdCreativeUnitTable error !");
        }


    }

    private void dumpAdUnitDistrictTable(String fileName) {

        List<AdUnitDistrict> unitDistricts = districtRepository.findAll();

        if (CollectionUtils.isEmpty(unitDistricts)) {
            return;
        }

        List<AdUnitDistrictTable> unitDistrictTables = new ArrayList<>();

        unitDistricts.forEach(d -> unitDistrictTables.add(
                new AdUnitDistrictTable(
                        d.getUnitId(),
                        d.getProvince(),
                        d.getCity()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitDistrictTable unitDistrictTable : unitDistrictTables) {
                writer.write(JSON.toJSONString(unitDistrictTable));
                // 换行
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdUnitDistrictTable error !");
        }
    }

    private void dumpAdUnitItTable(String fileName) {

        List<AdUnitIt> unitIts = itRepository.findAll();

        if (CollectionUtils.isEmpty(unitIts)) {
            return;
        }

        List<AdUnitItTable> unitItTables = new ArrayList<>();
        unitIts.forEach(
                i -> unitItTables.add(
                        new AdUnitItTable(
                                i.getUnitId(),
                                i.getItTag()
                        )
                )
        );


        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitItTable unitItTable : unitItTables) {
                writer.write(JSON.toJSONString(unitItTable));
                // 换行
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdUnitItTable error !");
        }
    }

    private void dumpAdUnitKeywordTable(String fileName) {

        List<AdUnitKeyword> unitKeywords = keywordRepository.findAll();

        if (CollectionUtils.isEmpty(unitKeywords)) {
            return;
        }

        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(k -> unitKeywordTables.add(
                new AdUnitKeywordTable(
                        k.getUnitId(),
                        k.getKeyword()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitKeywordTable unitKeywordTable : unitKeywordTables) {
                writer.write(JSON.toJSONString(unitKeywordTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            log.error("dumpAdUnitItTable error !");
        }
    }
}
