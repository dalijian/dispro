//package com.lijian.dispro.quartz.transaction;
//
//import com.mysql.cj.Session;
//import com.mysql.cj.xdevapi.SessionFactory;
//import org.hibernate.HibernateException;
//import org.hibernate.engine.spi.SessionFactoryImplementor;
//import org.springframework.context.annotation.Description;
//import org.springframework.orm.hibernate5.SessionFactoryUtils;
//import org.springframework.orm.hibernate5.SpringSessionContext;
//
//import java.beans.beancontext.BeanContext;
//
//public class HwCurrentSessionContext extends SpringSessionContext {
//    public static final ThreadLocal<TlSessionBean> sessions = new ThreadLocal<>();
//    private static SessionFactory sessionFactory;
//
//    public HwCurrentSessionContext(SessionFactoryImplementor factory) {
//        super(factory);
//    }
//
//    public static SessionFactory getSessionFactory() {
//        if (sessionFactory == null) {
//            synchronized (HibernateUtil.class) {
//                if (sessionFactory == null) sessionFactory = BeanContext.getBean(SessionFactory.class);
//            }
//        }
//        return sessionFactory;
//    }
//
//    @Override
//    public Session currentSession() throws HibernateException {
//        TlSessionBean tlSessionBean = sessions.get();
//        if (tlSessionBean != null && tlSessionBean.getCustom()) {
//            // todo: 如果ThreadLocal存在session，并且是用户自定义session且返回这个session,否则使用springSessionContext的session
//            return tlSessionBean.getSession();
//        }
//        return super.currentSession();
//    }
//
//    @Description("获取当前session")
//    static Session getCurrentSession() {
//        Session session = getTlCurrentSession();
//        if (session ==null) session = getSessionFactory().getCurrentSession();
//        return session;
//    }
//    @Description("为线程在ThreadLocal中自定义session")
//    static void setTlCurrentSession(Session session) {
//        if (getTlCurrentSession() == null) {
//            TlSessionBean sessionBean = new TlSessionBean();
//            sessionBean.setCustom(true);
//            sessionBean.setSession(session);
//            sessions.set(sessionBean);
//        }
//    }
//    @Description("获取线程在ThreadLocal中自定义session，没有返回Null")
//    static Session getTlCurrentSession() {
//        //通过线程对象.get()方法安全创建Session
//        TlSessionBean s = sessions.get();
//        // 如果该线程还没有Session,则创建一个新的Session
//        if (s != null && s.getCustom()) {
//            // 将获得的Session变量存储在ThreadLocal变量session里
//            return s.getSession();
//        }
//        return null;
//    }
//
//    //关闭Session
//    static void closeSession() throws HibernateException {
//        SessionFactoryUtils.closeSession(getTlCurrentSession());
//        sessions.set(null);
//    }
//}
