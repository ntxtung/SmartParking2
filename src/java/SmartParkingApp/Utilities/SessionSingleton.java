package SmartParkingApp.Utilities;

import org.hibernate.Session;

public class SessionSingleton {
    private static Session session;

    private SessionSingleton() {

    }

    public static Session getInstance() {
        if(session == null) {
            synchronized(SessionSingleton.class) {
                if(null == session) {
                    session  = HibernateUtils.getSessionFactory().openSession();
                }
            }
        }
        return session;
    }
}
