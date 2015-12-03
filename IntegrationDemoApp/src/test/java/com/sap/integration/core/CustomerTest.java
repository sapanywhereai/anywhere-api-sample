package com.sap.integration.core;

import org.joda.time.DateTime;

import com.sap.integration.erp.dummy.entity.Customer;

public class CustomerTest {

    // only for developers - just for testing purposes
    public static void main(String[] args) {
        try {
            // AccessToken.load();

            createCustomersInErp();
            printErpCustomers();

            // CustomerIntegration.syncFromSAPAnywhere();
            // CustomerIntegration.syncToSAPAnywhere();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createCustomersInErp() {
        Customer customer = new Customer();
        customer.setCode("dummy code now");
        customer.setName("dummy name now");
        customer.setLastUpdateTime(DateTime.now());
        customer.persist();

        customer = new Customer();
        customer.setCode("dummy code future");
        customer.setName("dummy name future");
        customer.setLastUpdateTime(new DateTime(9999 - 01 - 01));
        customer.persist();
    }

    private static void printErpCustomers() {
        System.out.println("List of Customers\n--------------");
        for (Customer c : Customer.getAll(Customer.class)) {
            System.out.println(c.toString());
        }
    }

}
