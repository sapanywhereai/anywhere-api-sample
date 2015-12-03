package com.sap.integration.erp.dummy.conversion;

import java.util.ArrayList;
import java.util.List;

import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpInvoiceDto;
import com.sap.integration.erp.dto.ErpInvoiceLineDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.entity.Invoice;
import com.sap.integration.erp.dummy.entity.InvoiceLine;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.erp.dummy.entity.SalesOrder;

/**
 * Class contains methods for converting objects between ERP data transfer object to ANW data transfer objects or to DB entities
 * for Invoices.
 */
public class InvoiceConversion {

    /**
     * Method, which converts ErpInvoiceDto object to Invoice.
     * 
     * @param erpInvoice - object, which will be converted to Invoice object
     * @return null - if entered value is null <br>
     *         Invoice - object of invoice converted from erpInvoice <br>
     * @throws Exception possible exception during the processing
     */
    public static final Invoice run(final ErpInvoiceDto erpInvoice) throws Exception {

        if (erpInvoice == null) {
            return null;
        }

        Invoice invoice = new Invoice();
        invoice.setId(erpInvoice.getId());
        invoice.setAnwId(erpInvoice.getAnwId());
        invoice.setStatus(erpInvoice.getStatus());
        invoice.setPaymentStatus(erpInvoice.getPaymentStatus());
        invoice.setDocTotal(erpInvoice.getDocTotal());

        if (erpInvoice.getCustomer() != null) {
            Customer linkedCustomer = findCustomer(erpInvoice.getCustomer().getCode());
            if (linkedCustomer == null) {
                throw new Exception("One Customer with code = " + erpInvoice.getCustomer().getCode()
                        + " can't be found in ERP's DB");
            }
            invoice.setBp(linkedCustomer);
        } else {
            invoice.setBp(null);
        }

        if (erpInvoice.getInvoiceLines() != null) {
            ArrayList<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();

            for (ErpInvoiceLineDto erpInvoiceLineDto : erpInvoice.getInvoiceLines()) {
                InvoiceLine invoiceLine = new InvoiceLine();

                invoiceLine.setQuantity(erpInvoiceLineDto.getQuantity());
                invoiceLine.setUnitPrice(erpInvoiceLineDto.getUnitPrice());
                invoiceLine.setLineTotal(erpInvoiceLineDto.getLineTotal());
                invoiceLine.setInvoice(invoice);

                SalesOrder salesOrderLine = findSalesOrder(erpInvoiceLineDto.getBaseDocument().getAnwId().toString());
                if (salesOrderLine == null) {
                    throw new Exception("One SalesOrder with anwId = " + erpInvoiceLineDto.getBaseDocument().getId().toString()
                            + " can't be found in ERP's DB");
                }
                invoiceLine.setSalesOrder(salesOrderLine);

                Item item = findItem(erpInvoiceLineDto.getSku().getItemCode(), erpInvoiceLineDto.getSku().getItemName());
                if (item == null) {
                    throw new Exception("One Item with code = " + erpInvoiceLineDto.getSku().getItemCode() + " and name = "
                            + erpInvoiceLineDto.getSku().getItemName() + " can't be found in ERP's DB");
                }
                invoiceLine.setItem(item);
                invoiceLines.add(invoiceLine);
            }
            invoice.setLines(invoiceLines);
        } else {
            invoice.setLines(null);
        }

        return invoice;
    }

    /**
     * Method, which converts Invoice object to ErpInvoiceDto.
     * 
     * @param invoice - object, which will be converted to ErpInvoiceDto object
     * @return null - if entered value is null <br>
     *         ErpInvoiceDto - object of invoice converted from erpInvoice <br>
     * @throws Exception possible exception during the processing
     */
    public static final ErpInvoiceDto run(final Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        ErpInvoiceDto erpInvoice = new ErpInvoiceDto();
        erpInvoice.setId(invoice.getId());
        erpInvoice.setAnwId(invoice.getAnwId());
        erpInvoice.setLastUpdateTime(invoice.getLastUpdateTime());
        erpInvoice.setPaymentStatus(invoice.getPaymentStatus());
        erpInvoice.setStatus(invoice.getStatus());
        erpInvoice.setDocTotal(invoice.getDocTotal());

        if (invoice.getBp() != null) {
            ErpCustomerDto customer = new ErpCustomerDto()
                    .setCode(invoice.getBp().getCode())
                    .setName(invoice.getBp().getName())
                    .setLastUpdateTime(invoice.getBp().getLastUpdateTime());

            erpInvoice.setCustomer(customer);
        } else {
            erpInvoice.setCustomer(null);
        }

        if (invoice.getLines() != null) {
            ArrayList<ErpInvoiceLineDto> erpInvoiceLines = new ArrayList<ErpInvoiceLineDto>();

            for (InvoiceLine invoiceLine : invoice.getLines()) {
                ErpInvoiceLineDto erpInvoiceLine = new ErpInvoiceLineDto();

                erpInvoiceLine.setUnitPrice(invoiceLine.getUnitPrice());
                erpInvoiceLine.setLineTotal(invoiceLine.getLineTotal());
                erpInvoiceLine.setBaseDocument(SalesOrderConversion.run(invoiceLine.getSalesOrder()));
                ErpProductDto product = new ErpProductDto();
                product.setId(invoiceLine.getItem().getId());
                product.setItemCode(invoiceLine.getItem().getItemCode());
                product.setItemName(invoiceLine.getItem().getItemName());
                product.setLastUpdateTime(invoiceLine.getItem().getLastUpdateTime());

                erpInvoiceLine.setSku(product);
                erpInvoiceLines.add(erpInvoiceLine);
            }
            erpInvoice.setInvoiceLines(erpInvoiceLines);
        } else {
            erpInvoice.setInvoiceLines(null);
        }

        return erpInvoice;
    }

    /**
     * Generic conversion of list of DB object to list of ERP Invoice object and vice versa.
     * 
     * @param clazz - Class type of objects in invoicesToConvert list
     * @param invoicesToConvert - database or ERP invoice object list
     * @return List<?> - converted list
     * @throws Exception possible exception during the processing
     */
    @SuppressWarnings("unchecked")
    public static final List<?> run(final Class<?> clazz, final List<?> invoicesToConvert) throws Exception {

        // convert invoices to erpInvoices
        if (clazz.equals(Invoice.class)) {
            List<ErpInvoiceDto> convertedInvs = new ArrayList<ErpInvoiceDto>();
            if (invoicesToConvert != null) {
                for (Invoice invoiceToConvert : (List<Invoice>) invoicesToConvert) {
                    convertedInvs.add(run(invoiceToConvert));
                }
            }
            return convertedInvs;
        }
        // convert erpInvoices to invoices
        else if (clazz.equals(ErpInvoiceDto.class)) {
            List<Invoice> convertedInvs = new ArrayList<Invoice>();
            if (invoicesToConvert != null) {
                for (ErpInvoiceDto invToConvert : (List<ErpInvoiceDto>) invoicesToConvert) {
                    convertedInvs.add(run(invToConvert));
                }
            }
            return convertedInvs;
        }

        return null;
    }

    /**
     * Method, which find customer filtered by code.
     * 
     * @param code - parameter used as filter for finding customers
     * @return null - if entered parameter is null or number of found customers is 0 <br>
     *         Customer - found customer <br>
     */
    private static final Customer findCustomer(final String code) {
        if (code == null) {
            return null;
        } else {
            List<Customer> foundCustomers = Customer.find(Customer.class, "SELECT c FROM Customer c where c.code = ?1", code);
            if (foundCustomers.size() == 1) {
                return foundCustomers.get(0);
            } else {
                return null;
            }
        }
    }

    /**
     * Method, which find item filtered by code and name.
     * 
     * @param code - parameter used as filter for finding item
     * @param name - parameter used as filter for finding item
     * @return null - if entered parameter is null or number of found items is 0 <br>
     *         Item - found item <br>
     */
    private static final Item findItem(final String code, final String name) {
        if (code == null || name == null) {
            return null;
        } else {
            List<Item> foundItems = Item.find(Item.class, "SELECT i FROM Item i where i.itemCode = ?1 and i.itemName = ?2",
                    code, name);
            if (foundItems.size() == 1) {
                return foundItems.get(0);
            } else {
                return null;
            }
        }
    }

    /**
     * Method, which find sales order filtered by id.
     * 
     * @param id - parameter used as filter for finding sales order
     * @return null - if entered parameter is null or number of found sales orders is 0 <br>
     *         SalesOrder - found sales order <br>
     */
    private static final SalesOrder findSalesOrder(final String id) {
        if (id == null) {
            return null;
        } else {
            List<SalesOrder> foundSalesOrders = SalesOrder.find(SalesOrder.class,
                    "SELECT so FROM SalesOrder so where so.anwId = ?1", new Long(id));
            if (foundSalesOrders.size() == 1) {
                return foundSalesOrders.get(0);
            } else {
                return null;
            }
        }
    }
}
