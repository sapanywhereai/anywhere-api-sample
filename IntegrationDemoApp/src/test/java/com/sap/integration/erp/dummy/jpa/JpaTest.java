package com.sap.integration.erp.dummy.jpa;

import java.util.List;

import org.joda.time.DateTime;

import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.erp.dummy.entity.SalesOrder;
import com.sap.integration.erp.dummy.entity.SalesOrderLine;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;
import com.sap.integration.utils.integrationdb.entity.Range;

public class JpaTest {

    // only for developers - just for testing purposes - whether the SQLite +
    // JPA (Eclipselink) works
    public static void main(String[] args) {
        // goJpaManagerIntegrationDb();
        goJpaManagerDummyErp();
    }

    static public void goJpaManagerIntegrationDb() {
        IntegrationState is = IntegrationState.getIntegrationStateFor(JpaTest.class);
        is.setLastSyncTime(DateTime.now());
        is.merge();

        // read entities
        // List<IntegrationState> results =
        // com.sap.integration.anywhere.database.integrationdb.jpa.JpaLayer.find(IntegrationState.class,
        // "SELECT i FROM IntegrationState i");
        List<IntegrationState> results = IntegrationState.find(IntegrationState.class,
                "SELECT i FROM IntegrationState i where i.lastSyncTime > ?1", DateTime.now().minusMinutes(5));
        System.out.println("List of filtered IntegrationStates\n-------------------------");
        for (IntegrationState p : results) {
            System.out.println(p.toString());
        }

        listIntEntries(IntegrationState.class);
        listIntEntries(Range.class);
    }

    static public void goJpaManagerDummyErp() {
        Customer bp = JpaLayer.findFirst(Customer.class, "select b from Customer b where b.code = ?1", "c01");
        if (bp == null) {
            bp = new Customer();
            bp.setCode("c01");
            bp.setName("c01");
            bp.persist();
        }

        Item item = JpaLayer.findFirst(Item.class, "select i from Item i where i.itemCode = ?1", "i01");
        if (item == null) {
            item = new Item();
            item.setItemCode("i01");
            item.setItemName("i01");
            item.persist();
        }

        SalesOrder so = new SalesOrder();
        so.setBusinessPartner(bp);
        so.setDocTotal(111.0);
        SalesOrderLine sol = new SalesOrderLine();
        sol.setItem(item);
        sol.setLineTotal(111.0);
        sol.setQuantity(1);
        sol.setInventoryUomQuantity(1);
        sol.setTaxCode("TAX_CODE_ON_LINE");
        sol.setUnitPrice(111.0);
        so.addLine(sol);
        so.persist();

        listErpEntries(Customer.class);
        listErpEntries(Item.class);
        listErpEntries(SalesOrder.class);

    }

    private static <T extends com.sap.integration.utils.integrationdb.jpa.JpaLayer> void listIntEntries(Class<T> clazz) {
        List<T> list = T.getAll(clazz);
        System.out.println("List of " + clazz.getSimpleName() + "s\n-------------------------");
        for (T entry : list) {
            System.out.println(entry.toString());
        }
    }

    private static <T extends JpaLayer> void listErpEntries(Class<T> clazz) {
        List<T> list = T.getAll(clazz);
        System.out.println("List of " + clazz.getSimpleName() + "s\n-------------------------");
        for (T entry : list) {
            System.out.println(entry.toString());
        }
    }
}
