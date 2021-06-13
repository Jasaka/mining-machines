package thkoeln.coco.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.coco.ad.miningMachine.MiningMachineException;
import thkoeln.coco.ad.core.MovementTests;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class E1ValidationTests extends MovementTests  {

    private CentralControlService service;

    @Autowired
    public E1ValidationTests(WebApplicationContext appContext, CentralControlService service) {
        super(appContext);
        this.service = service;
    }

    @BeforeEach
    public void init() {
        createWorld(service);
    }

    @Test
    public void noDiagonalBarrierTest() {
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(2,2)-(5,6)");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(2,2)-(2,2)");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(0,0)-(1,1)");
        });
    }



    @Test
    public void noNegativePointTest() {
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,3)",
                    field2, "(-3,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(-1,3)",
                    field2, "(3,4)");
        });
    }



    @Test
    public void noNegativeStepsOnTaskTest() {
        assertThrows(MiningMachineException.class, () -> {
            executeTasks(service, miningMachine1, new String[]{
                    "[en," + field2 + "]",
                    "[ea,1]",
                    "[ea,-1]",
            });
        });
    }
}
