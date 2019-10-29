//package com.lijian.dispro.quartz.transaction;
//
//import com.mysql.cj.Session;
//import org.hibernate.HibernateException;
//
////工具类 用于get和set session
//public class HibernateUtil {
//
//    //创建Session
//    public static Session currentSession() throws HibernateException {
//        return HwCurrentSessionContext.getCurrentSession();
//    }
//
//    //创建Session
//    public static void setCurrentSession(Session session) throws HibernateException {
//        HwCurrentSessionContext.setTlCurrentSession(session);
//    }
//
//    //关闭Session
//    public static void closeCurrentSession() throws HibernateException {
//        HwCurrentSessionContext.closeSession();
//    }
//}