package thkoeln.coco.ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.coco.ad.core.MovementTests;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class E1MovementTests extends MovementTests {

    private CentralControlService service;

    @Autowired
    public E1MovementTests(WebApplicationContext appContext, CentralControlService service) {
        super(appContext);

        this.service = service;
    }

    @BeforeEach
    public void init() {
        createWorld(service);
    }

    @Test
    @Transactional
    public void miningMachinesSpawnOnSamePositionTest() {
        assertTrue(service.executeCommand(miningMachine1, "[en," + field1 + "]"));
        assertFalse(service.executeCommand(miningMachine2, "[en," + field1 + "]"));
    }

    @Test
    @Transactional
    public void moveToAnotherFieldAndBackTest() throws Exception {
        executeTasks(service, miningMachine1, new String[]{
                "[en," + field2 + "]",
                "[ea,1]",
                "[tr," + field3 + "]"        
        });

        assertPosition(service, miningMachine1, field3, "(2,2)");

        executeTasks(service, miningMachine1, new String[]{
                "[tr," + field2 + "]"
        });

        assertPosition(service, miningMachine1, field2, "(1,0)");
    }

    @Test
    @Transactional
    public void moveToAnotherFieldOnWrongPositionTest() throws Exception {
        service.executeCommand(miningMachine1, "[en," + field1 + "]");
        assertFalse(service.executeCommand(miningMachine1, "[tr," + field2 + "]"));

        assertPosition(service, miningMachine1, field1, "(0,0)");
    }

    @Test
    @Transactional
    public void bumpIntoBarrierTest() throws Exception {
        executeTasks(service, miningMachine1, new String[]{
                "[en," + field2 + "]",
                "[ea,2]",
                "[no,3]",
                "[we,1]",
        });

        assertPosition(service, miningMachine1, field2, "(1,1)");
    }

    @Test
    @Transactional
    public void moveTwoMiningMachinesAtOnceTest() throws Exception {
        executeTasks(service, miningMachine1, new String[]{
                "[en," + field1 + "]",
                "[ea,1]",
                "[no,4]"
        });

        executeTasks(service, miningMachine2, new String[]{
                "[en," + field1 + "]",
                "[ea,2]",
                "[no,5]"
        });

        assertPosition(service, miningMachine1, field1, "(1,2)");
        assertPosition(service, miningMachine2, field1, "(2,5)");
    }

    @Test
    @Transactional
    public void moveOutOfBoundariesTest() throws Exception {
        executeTasks(service, miningMachine1, new String[]{
                "[en," + field3 + "]",
                "[no,5]",
                "[ea,5]",
                "[so,5]",
                "[we,5]",
                "[no,1]"
        });

        assertPosition(     service, miningMachine1, field3, "(0,1)");
    }

    @Test
    @Transactional
    public void traverseOnOneFieldTest() throws Exception {
        service.executeCommand(miningMachine1, "[en," + field1 + "]");
        assertPosition(service, miningMachine1, field1, "(0,0)");

        service.executeCommand(miningMachine1, "[ea,2]");
        assertPosition(service, miningMachine1, field1, "(2,0)");

        service.executeCommand(miningMachine1, "[no,4]");
        assertPosition(service, miningMachine1, field1, "(2,4)");

        service.executeCommand(miningMachine1, "[we,5]");
        assertPosition(service, miningMachine1, field1, "(0,4)");
    }

    @Test
    @Transactional
    public void traverseAllFieldsTest() throws Exception {
        executeTasks(service, miningMachine1, new String[]{
                "[en," + field1 + "]",
                "[no,1]",
                "[ea,1]",
                "[tr," + field2 + "]",
                "[so,2]",
                "[ea,1]",
                "[tr," + field3 + "]",
                "[we,5]",
                "[ea,2]",
                "[tr," + field2 + "]",
                "[no,4]",
                "[ea,3]"
        });

        assertPosition(service, miningMachine1, field2, "(4,1)");
    }

}
