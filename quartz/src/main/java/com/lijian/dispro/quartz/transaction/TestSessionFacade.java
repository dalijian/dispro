//package com.lijian.dispro.quartz.transaction;
//
//import org.hibernate.SessionFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.orm.hibernate5.HibernateTemplate;
//
//public class TestSessionFacade {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private final static int THREAD_NUM = 10;
//
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    public Response doAllInSession() {
//        Session session = sessionFactory.getCurrentSession();
//        hibernateTemplate.get(TestEmployee.class, 1);
//        hibernateTemplate.save(EmployeeUtil.getRanE());
//              // todo 经断点调试，以上get save 和以下10个线程中使用的session系同一个内存地址
//        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < THREAD_NUM; i++) {
//            // todo: 开启10个线程，保存Employee
//            executorService.submit(() -> {
//                HibernateUtil.setCurrentSession(session); //为当前线程赋值session
//                ThreadUtilKt.sleep(1000); //模拟每种资源业务的处理需要1秒
//                synchronized (session) {
//                    hibernateTemplate.save(EmployeeUtil.getRanE());
//                }
//            });
//        }
//        try {
//            executorService.shutdown();
//            executorService.awaitTermination(3, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (RandomUtil.getNum(4) % 2 == 1) {
//            logger.error("========奇数回滚操作");
//            session.doWork(Connection :: rollback);
//        } else {
//            logger.error("========偶数提交操作");
//            session.doWork(Connection :: commit);
//        }
//        SoutUtil.print("Session还open的吗？ - " + session.isOpen());
//        return Response.success("经debug调试确认使用了同一个session").add("COST_TIME", System.currentTimeMillis() - startTime);
//    }
//
//}