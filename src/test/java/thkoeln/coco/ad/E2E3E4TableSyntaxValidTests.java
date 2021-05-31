package thkoeln.coco.ad;

import org.junit.jupiter.api.Test;
import thkoeln.st.springtestlib.specification.table.GenericTableSpecificationTests;
import thkoeln.st.springtestlib.specification.table.TableType;


public class E2E3E4TableSyntaxValidTests {

    private GenericTableSpecificationTests genericTableSpecificationTests = null;


    public E2E3E4TableSyntaxValidTests() {
        this.genericTableSpecificationTests = new GenericTableSpecificationTests();
    }

    @Test
    public void checkValidSyntax() throws Exception {
        genericTableSpecificationTests.testTableSyntax(
                "exercises/E2a.md",
                "table-config-2a.json",
                TableType.ROWS_AND_COLUMNS);
        genericTableSpecificationTests.testTableSyntax(
                "exercises/E2b.md",
                "table-config-2b-f.json",
                TableType.ROWS_AND_COLUMNS);
        genericTableSpecificationTests.testTableSyntax(
                "exercises/E2c.md",
                "table-config-2b-f.json",
                TableType.ROWS_AND_COLUMNS);
        genericTableSpecificationTests.testTableSyntax(
                "exercises/E2d.md",
                "table-config-2b-f.json",
                TableType.ROWS_AND_COLUMNS);
        genericTableSpecificationTests.testTableSyntax(
                "exercises/E2e.md",
                "table-config-2b-f.json",
                TableType.ROWS_AND_COLUMNS);
        genericTableSpecificationTests.testTableSyntax(
                "exercises/E2f.md",
                "table-config-2b-f.json",
                TableType.ROWS_AND_COLUMNS);

        genericTableSpecificationTests.testTableSyntax(
                "exercises/E4.md",
                "table-config-4.json",
                TableType.ROWS_AND_COLUMNS);
    }
}
