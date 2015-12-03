package com.sap.integration.erp.dummy.conversion;

import java.util.ArrayList;
import java.util.List;

import com.sap.integration.core.service.SalesOrderService;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.ErpSalesDeliveryDto;
import com.sap.integration.erp.dto.ErpSalesDeliveryLineDto;
import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.erp.dummy.entity.SalesDelivery;
import com.sap.integration.erp.dummy.entity.SalesDeliveryLine;
import com.sap.integration.erp.dummy.entity.SalesOrder;

/**
 * Class, which contains methods used for conversion DB objects, ERP objects and ANW objects for sales delivery.
 */
public class SalesDeliveryConversion {

    /**
     * Convert sales delivery ERP object to sales delivery DB object.
     * 
     * @param erpSalesDelivery - ERP sales delivery data transfer object
     * @return SalesDelivery - converted object
     * @throws Exception possible exception during the processing
     */
    public static final SalesDelivery run(final ErpSalesDeliveryDto erpSalesDelivery) throws Exception {
        if (erpSalesDelivery == null) {
            return null;
        }

        SalesDelivery salesDelivery = new SalesDelivery();

        salesDelivery.setId(erpSalesDelivery.getId());
        salesDelivery.setAnwId(erpSalesDelivery.getAnwId());
        salesDelivery.setStatus(erpSalesDelivery.getStatus());

        SalesOrder salesOrder = findSalesOrderWithDocNumber(erpSalesDelivery.getSalesOrderNumber());
        if (salesOrder == null) {
            throw new Exception("One SalesOrder with anwId = " + erpSalesDelivery.getSalesOrderNumber()
                    + " can't be found in ERP's DB");
        }
        salesDelivery.setSalesOrder(salesOrder);

        if (erpSalesDelivery.getCustomer() != null) {
            Customer linkedCustomer = findCustomer(erpSalesDelivery.getCustomer().getCode());
            if (linkedCustomer == null) {
                throw new Exception("One Customer with code = " + erpSalesDelivery.getCustomer().getCode()
                        + " can't be found in ERP's DB");
            }
            salesDelivery.setBp(linkedCustomer);
        } else {
            salesDelivery.setBp(null);
        }

        if (erpSalesDelivery.getLines() != null) {
            ArrayList<SalesDeliveryLine> salesDeliveryLines = new ArrayList<SalesDeliveryLine>();

            for (ErpSalesDeliveryLineDto erpSalesDeliveryLineDto : erpSalesDelivery.getLines()) {
                SalesDeliveryLine salesDeliveryLine = new SalesDeliveryLine();
                salesDeliveryLine.setSalesDelivery(salesDelivery);
                salesDeliveryLine.setQuantity(erpSalesDeliveryLineDto.getRequiredQuantity());

                SalesOrder lineSalesOrder = findSalesOrder(erpSalesDeliveryLineDto.getBaseDocument().getAnwId().toString());
                if (lineSalesOrder == null) {
                    throw new Exception("One SalesOrder with anwId = "
                            + erpSalesDeliveryLineDto.getBaseDocument().getId().toString() + " can't be found in ERP's DB");
                }
                salesDeliveryLine.setSalesOrder(lineSalesOrder);

                Item item = findItem(erpSalesDeliveryLineDto.getSku().getItemCode(), erpSalesDeliveryLineDto.getSku()
                        .getItemName());
                if (item == null) {
                    throw new Exception("One Item with code = " + erpSalesDeliveryLineDto.getSku().getItemCode() + " and name = "
                            + erpSalesDeliveryLineDto.getSku().getItemName() + " can't be found in ERP's DB");
                }
                salesDeliveryLine.setItem(item);

                salesDeliveryLines.add(salesDeliveryLine);
            }
            salesDelivery.setLines(salesDeliveryLines);
        } else {
            salesDelivery.setLines(null);
        }

        return salesDelivery;
    }

    /**
     * Convert sales delivery DB object to sales delivery ERP object.
     * 
     * @param salesDelivery - DB sales delivery object
     * @return ErpSalesDeliveryDto - converted object
     * @throws Exception possible exception during the processing
     */
    public static final ErpSalesDeliveryDto run(final SalesDelivery salesDelivery) throws Exception {
        if (salesDelivery == null) {
            return null;
        }

        ErpSalesDeliveryDto erpSd = new ErpSalesDeliveryDto();

        erpSd.setId(salesDelivery.getId());
        erpSd.setAnwId(salesDelivery.getAnwId());
        erpSd.setLastUpdateTime(salesDelivery.getLastUpdateTime());
        erpSd.setStatus(salesDelivery.getStatus());

        if ((salesDelivery.getSalesOrder() != null) && (salesDelivery.getSalesOrder().getAnwId() != null)) {
            erpSd.setSalesOrderNumber(SalesOrderService.getAnwSalesOrder(salesDelivery.getSalesOrder().getAnwId().toString())
                    .getDocNumber());
        } else
            throw new Exception("ERP's Sales Delivery with id = " + salesDelivery.getId().toString()
                    + " has unspecified Sales Order.");

        if (salesDelivery.getBp() != null) {
            ErpCustomerDto customer = new ErpCustomerDto();
            customer.setCode(salesDelivery.getBp().getCode());
            customer.setName(salesDelivery.getBp().getName());
            customer.setLastUpdateTime(salesDelivery.getBp().getLastUpdateTime());

            erpSd.setCustomer(customer);
        } else {
            erpSd.setCustomer(null);
        }

        if (salesDelivery.getLines() != null) {
            ArrayList<ErpSalesDeliveryLineDto> erpSalesDeliveryLines = new ArrayList<ErpSalesDeliveryLineDto>();

            for (SalesDeliveryLine salesDeliveryLine : salesDelivery.getLines()) {
                ErpSalesDeliveryLineDto erpSalesDeliveryLine = new ErpSalesDeliveryLineDto();
                erpSalesDeliveryLine.setSalesDelivery(erpSd);
                erpSalesDeliveryLine.setBaseDocument(SalesOrderConversion.run(salesDeliveryLine.getSalesOrder()));

                ErpProductDto product = new ErpProductDto();
                product.setId(salesDeliveryLine.getItem().getId());
                product.setItemCode(salesDeliveryLine.getItem().getItemCode());
                product.setItemName(salesDeliveryLine.getItem().getItemName());
                product.setLastUpdateTime(salesDeliveryLine.getItem().getLastUpdateTime());

                erpSalesDeliveryLine.setSku(product);

                erpSalesDeliveryLines.add(erpSalesDeliveryLine);
            }
            erpSd.setLines(erpSalesDeliveryLines);
        } else {
            erpSd.setLines(null);
        }

        return erpSd;
    }

    /**
     * Generic convert of DB objects to ERP objects of sales delivery and vice versa.
     * 
     * @param clazz Class type of objects in sdsToConvert list
     * @param salesDeliveriesToConvert DB / ERP sales delivery object list
     * @return List<?>
     */
    @SuppressWarnings("unchecked")
    public static List<?> run(Class<?> clazz, List<?> salesDeliveriesToConvert) throws Exception {

        // conversion salesDeliveries to erpSalesDeliveries
        if (clazz.equals(SalesDelivery.class)) {
            List<ErpSalesDeliveryDto> convertedSalesDeliveries = new ArrayList<ErpSalesDeliveryDto>();
            if (salesDeliveriesToConvert != null) {
                for (SalesDelivery sdToConvert : (List<SalesDelivery>) salesDeliveriesToConvert) {
                    convertedSalesDeliveries.add(run(sdToConvert));
                }
            }
            return convertedSalesDeliveries;
        }

        // conversion erpSalesDeliveries to salesDeliveries
        else if (clazz.equals(ErpSalesDeliveryDto.class)) {
            List<SalesDelivery> convertedSalesDeliveries = new ArrayList<SalesDelivery>();
            if (salesDeliveriesToConvert != null) {
                for (ErpSalesDeliveryDto sdToConvert : (List<ErpSalesDeliveryDto>) salesDeliveriesToConvert) {
                    convertedSalesDeliveries.add(run(sdToConvert));
                }
            }
            return convertedSalesDeliveries;
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
    private static Customer findCustomer(String code) {
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
    private static Item findItem(String code, String name) {
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
    private static SalesOrder findSalesOrder(String id) {
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

    /**
     * Method, which find sales order filtered by docNumber.
     * 
     * @param docNumber - parameter used as filter for finding sales order
     * @return null - if entered parameter is null or number of found sales orders is 0 <br>
     *         SalesOrder - found sales order <br>
     */
    private static SalesOrder findSalesOrderWithDocNumber(String docNumber) throws Exception {
        if (docNumber == null) {
            return null;
        } else {
            List<SalesOrder> foundSalesOrders = SalesOrder.find(SalesOrder.class,
                    "SELECT so FROM SalesOrder so where so.anwId = ?1", SalesOrderService
                            .getAnwSalesOrderWithDocNumber(docNumber).getId());
            if (foundSalesOrders.size() == 1) {
                return foundSalesOrders.get(0);
            } else {
                return null;
            }
        }
    }
}
