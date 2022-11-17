package com.yang.study.zkservice;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ZKClientConfig {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.1.10:2181", 10000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                    Event.KeeperState state = watchedEvent.getState();
                    if (state == Event.KeeperState.SyncConnected) {
                        countDownLatch.countDown();
                        System.out.println("连接成功");
                    }
                }
            });
            countDownLatch.await();
            // 创建节点
//            String s = zooKeeper.create("/yangtest/aaa", "yjscontent".getBytes(),
//                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            System.out.println(s);

            Id digest = new Id("digest", DigestAuthenticationProvider.generateDigest("yjs:123456"));
            ACL acl = new ACL(ZooDefs.Perms.READ, digest);
            ArrayList<ACL> acls = new ArrayList<>();
            acls.add(acl);
            String s = zooKeeper.create("/yangtest4", "yjscontent".getBytes(),
                    acls, CreateMode.PERSISTENT);
            System.out.println(s);

            // 节点类型
            // 临时节点 会话关闭节点消失
            // 临时有序节点
            // 持久节点 会话关闭节点还是存在
            // 持久有序节点

            Thread.sleep(5000);
            // 关闭会话

            zooKeeper.addAuthInfo("digest", "yjs:123456".getBytes());
            byte[] data = zooKeeper.getData("/yangtest3", null, new Stat());


            System.out.println(new String(data));


            zooKeeper.close();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
