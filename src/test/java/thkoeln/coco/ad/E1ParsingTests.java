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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class E1ParsingTests extends MovementTests {

    private CentralControlService service;

    @Autowired
    public E1ParsingTests(WebApplicationContext appContext, CentralControlService service) {
        super(appContext);

        this.service = service;
    }

    @BeforeEach
    public void init() {
        createWorld(service);
    }



    @Test
    public void taskFailedParsingTest() {
        assertThrows(MiningMachineException.class, () -> {
            service.executeCommand(miningMachine1, "[soo,4]");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.executeCommand(miningMachine1, "[4,no]");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.executeCommand(miningMachine1, "[[no,4]]");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.executeCommand(miningMachine1, "(no,4)");
        });
    }


    @Test
    public void pointFailedParsingTest() {
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,,3)",
                    field2, "(3,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,3)",
                    field2, "(3,,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,3]",
                    field2, "(3,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "[2,3)",
                    field2, "(3,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,2,3)",
                    field2, "(3,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,)",
                    field2, "(3,4)");
        });
        assertThrows(MiningMachineException.class, () -> {
            UUID uuid = service.addConnection(transportTechnology1, field1, "(2,3)",
                    field2, "(,4)");
        });
    }

    @Test
    public void barrierParsingFailedTest() {
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "()");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(1,2)");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(1,2)-(3,2))");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(1,2)-");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(1,2)-(3,2)-(5,2)");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "[1,2]-[3,2]");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(1,2,3)-(3,2))");
        });
        assertThrows(MiningMachineException.class, () -> {
            service.addBarrier(field1, "(1,2)-(3,)");
        });
    }


}
