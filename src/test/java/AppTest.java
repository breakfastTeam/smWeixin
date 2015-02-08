import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Created by kkk .
 */
public class AppTest {
    public static void main(String[] args) {
        Server jettyServer = new Server(8080);

        WebAppContext wah = new WebAppContext();
        wah.setContextPath("/weixin");
        wah.setWar("src/main/webapp");
        jettyServer.setHandler(wah);
        try {
            jettyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
