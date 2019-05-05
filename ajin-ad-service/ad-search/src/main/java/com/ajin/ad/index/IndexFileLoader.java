package com.ajin.ad.index;

import com.ajin.ad.dmp.DConstant;
import com.ajin.ad.dmp.table.*;
import com.ajin.ad.handler.AdLevelDataHandler;
import com.ajin.ad.mysql.contant.OpType;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: ajin
 * @Date: 2019/4/16 19:41
 */
@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    /**
     * 全量索引的加载在检索系统启动的时候完成的
     */
    @PostConstruct  // PostConstruct注解用于在完成依赖项注入以执行任何初始化之后需要执行的方法
    public void init() {

        // 2 BEGIN
        List<String> adPlanStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN));
        // 对磁盘文件进行序列化
        adPlanStrings.forEach(p -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p, AdPlanTable.class), OpType.ADD
                )
        );

        List<String> adCreativeStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE));
        adCreativeStrings.forEach(c -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(c, AdCreativeTable.class),
                OpType.ADD));
        // 2 END

        // 3 BEGIN
        List<String> adUnitStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT
                )
        );
        adUnitStrings.forEach(u -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(u, AdUnitTable.class), OpType.ADD
        ));


        List<String> adCreativeUnitStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT));
        adCreativeUnitStrings.forEach(cu -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(cu, AdCreativeUnitTable.class), OpType.ADD
        ));
        // 3 END

        // 4 BEGIN

        List<String> adUnitDistrictStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT));
        adUnitDistrictStrings.forEach(ud -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(ud, AdUnitDistrictTable.class), OpType.ADD
        ));

        List<String> adUnitItStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT));
        adUnitItStrings.forEach(ut -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(ut, AdUnitItTable.class), OpType.ADD
        ));

        List<String> adUnitKeywordStrings = loadDumpData(String.format("%s%s",
                DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD));
        adUnitKeywordStrings.forEach(uk -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(uk, AdUnitKeywordTable.class), OpType.ADD
        ));

        // 4 END
    }

    /**
     * 读取磁盘文件，转换成String
     */
    private List<String> loadDumpData(String fileName) {

        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(fileName)
        )) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
