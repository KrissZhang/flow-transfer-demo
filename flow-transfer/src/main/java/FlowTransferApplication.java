import com.self.flowtransfer.web.WebConfig;
import com.jfinal.server.undertow.UndertowServer;

/**
 * 启动类
 */
public class FlowTransferApplication {

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        UndertowServer.create(WebConfig.class, "undertow.properties").start();
    }
    
}
