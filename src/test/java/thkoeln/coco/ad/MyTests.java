package thkoeln.coco.ad;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.coco.ad.core.MovementTests;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MyTests extends MovementTests {

    private final CentralControlService service;

    @Autowired
    protected MyTests(WebApplicationContext appContext, CentralControlService service) {
        super(appContext);
        this.service = service;
    }

    @BeforeEach
    public void init() {
        createWorld(service);
    }

}
