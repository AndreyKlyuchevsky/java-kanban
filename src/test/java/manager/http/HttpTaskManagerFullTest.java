package manager.http;

import manager.server.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;

public class HttpTaskManagerFullTest extends HttpTaskManagerTest {
    private KVServer kvServer;

    @Override
    public HttpTaskManager getManager() {
        return new HttpTaskManager("http://localhost:"+ KVServer.PORT);
    }

    @BeforeEach
    @Override
    public void init() throws IOException {
        kvServer=new KVServer();
        kvServer.start();
        super.init();
    }

    @AfterEach
    public void complete(){
        kvServer.stop();
    }

}
