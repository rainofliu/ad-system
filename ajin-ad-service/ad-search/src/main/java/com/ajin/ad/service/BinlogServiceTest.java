package com.ajin.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;

/**
 * @Auther: ajin
 * @Date: 2019/4/18 21:41
 */
public class BinlogServiceTest {

    public static void main(String[] args) throws IOException {

//        Write--------
//        WriteRowsEventData{tableId=125, includedColumns={0, 1, 2, 3, 4, 5, 6, 7}, rows=[
//    [1, 1, 测试计划, 0, Fri Apr 19 05:40:33 CST 2019, Sat Apr 27 05:40:36 CST 2019, Thu Jan 01 08:00:00 CST 1970, Thu Jan 01 08:00:00 CST 1970]
//]}
//        Update--------
//        UpdateRowsEventData{tableId=125, includedColumnsBeforeUpdate={0, 1, 2, 3, 4, 5, 6, 7}, includedColumns={0, 1, 2, 3, 4, 5, 6, 7}, rows=[
//            {before=[1, 1, 测试计划, 0, Fri Apr 19 05:40:33 CST 2019, Sat Apr 27 05:40:36 CST 2019, Thu Jan 01 08:00:00 CST 1970, Thu Jan 01 08:00:00 CST 1970], after=[1, 1, 测试计划1, 0, Fri Apr 19 05:40:33 CST 2019, Sat Apr 27 05:40:36 CST 2019, Thu Jan 01 08:00:00 CST 1970, Thu Jan 01 08:00:00 CST 1970]}
//]}



        // 连接数据库的客户端，伪装成slave
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                "1027"
        );

//        client.setBinlogPosition();
        client.setBinlogFilename("mysql-bin.000001");
        client.registerEventListener(event -> {

            EventData data = event.getData();

            if (data instanceof UpdateRowsEventData) {
                System.out.println("Update--------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println("Write--------");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {
                System.out.println("Delete--------");
                System.out.println(data.toString());
            }

        });

        client.connect();
    }
}
