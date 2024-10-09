package org.yang.study;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;


/**
 * @Description
 * @Author ljx
 * @Date 2024/7/19
 **/
public class TestKrb {

    private static String krb5 = "D:\\ideaspacesss\\SpringCloudAlibaba\\service-krb\\src\\main\\resources\\krb5.conf";
    private static String keytab = "D:\\ideaspacesss\\SpringCloudAlibaba\\service-krb\\src\\main\\resources\\bigdata.node78.keytab" ;
    private static final String PRINCIPAL = "bigdata/node78@LINEWELL.COM" ;
    private static String hiveUrl = "jdbc:hive2://10.231.6.79:10000/default;principal=bigdata/node79@LINEWELL.COM";
    @Test
    public void test() throws SQLException, IOException, ClassNotFoundException {
        System.setProperty ( "java.security.krb5.conf" , krb5);
        System.setProperty ("sun.security.krb5.debug","true" );//可选，用于调试 Kerberos认证过程//初始化Hadoop配置
        Configuration conf = new Configuration();
//        System.setProperty("hadoop.home.dir", "E:\\linewell\\软件包\\winutils-20180927\\winutils-20180927\\hadoop-2.6.4");



        conf.set ("hadoop.security.authentication" , "kerberos" );//使用Keytab进行kerberos身份验证
        UserGroupInformation.setConfiguration (conf);
        UserGroupInformation.loginUserFromKeytab(PRINCIPAL, keytab );

        Class.forName( "org.apache.hive.jdbc.HiveDriver" );

        Connection connection = DriverManager.getConnection(hiveUrl, "1","1");


        try {
            // 进入default数据库
            PreparedStatement ps = connection.prepareStatement("use default");
            ps.execute();
            // 展示所有表
            ResultSet rs = ps.executeQuery("show tables");
            // 处理结果集
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        String confPath = krb5;
        String keytabPath = keytab;

        System.setProperty("java.security.krb5.conf", confPath);
        System.setProperty("hadoop.home.dir", "E:\\linewell\\软件包\\winutils-20180927\\winutils-20180927\\hadoop-2.6.4");

        System.setProperty("sun.security.krb5.debug", "true");
     System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        Configuration configuration = new Configuration();
        configuration.set("hadoop.security.authentication", "KERBEROS");

        UserGroupInformation.setConfiguration(configuration);
        try {
            UserGroupInformation.loginUserFromKeytab(PRINCIPAL, keytabPath);
        } catch (IOException e) {
            System.out.println("authKerberos exception");
            e.printStackTrace();
        }
    }



}