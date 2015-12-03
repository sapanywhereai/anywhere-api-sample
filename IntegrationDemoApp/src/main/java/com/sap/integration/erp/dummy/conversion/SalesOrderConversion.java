package com.sap.integration.erp.dummy.conversion;

import java.util.ArrayList;
import java.util.List;

import com.sap.integration.erp.dto.ErpChannelDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.ErpSalesOrderDto;
import com.sap.integration.erp.dto.ErpSalesOrderLineDto;
import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.erp.dummy.entity.SalesOrder;
import com.sap.integration.erp.dummy.entity.SalesOrderLine;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

public class SalesOrderConversion {

    /**
     * Convert ERP to DB sales order object
     * 
     * @param erpSo ERP sales order data transfer object
     * @return SalesOrder
     */
    public static SalesOrder run(ErpSalesOrderDto erpSo) throws Exception {
        if (erpSo == null) {
            return null;
        }

        SalesOrder so = new SalesOrder();
        if (erpSo.getId() != null) {
            so.setId(erpSo.getId());
        }
        so.setAnwId(erpSo.getAnwId());
        so.setLastUpdateTime(erpSo.getOrderTime());
        so.setOrderTime(erpSo.getOrderTime());
        so.setChannelId(new Long(erpSo.getChannel().getId().toString()));
        so.setPricingMethod(erpSo.getPricingMethod());
        so.setDocTotal(erpSo.getDocTotal());

        if (erpSo.getCustomer() != null) {
            Customer customer = findCustomer(erpSo.getCustomer().getCode());
            if (customer == null) {
                throw new Exception("One Customer with code = " + erpSo.getCustomer().getCode() + " can't be found in ERP's DB");
            }
            so.setBusinessPartner(customer);
        } else {
            so.setBusinessPartner(null);
        }

        if (erpSo.getProductLines() != null) {
            ArrayList<SalesOrderLine> salesOrderLines = new ArrayList<SalesOrderLine>();

            for (ErpSalesOrderLineDto erpSalesOrderLineDto : erpSo.getProductLines()) {
                SalesOrderLine salesOrderLine = new SalesOrderLine();
                salesOrderLine.setSalesOrder(so);
                salesOrderLine.setQuantity(erpSalesOrderLineDto.getQuantity());
                salesOrderLine.setInventoryUomQuantity(erpSalesOrderLineDto.getInventoryUomQuantity());
                salesOrderLine.setUnitPrice(erpSalesOrderLineDto.getUnitPrice());
                salesOrderLine.setLineTotal(erpSalesOrderLineDto.getLineTotal());
                salesOrderLine.setCalculationBase(erpSalesOrderLineDto.getCalculationBase());

                Item item = findItem(erpSalesOrderLineDto.getSku().getItemCode(), erpSalesOrderLineDto.getSku().getItemName());
                if (item == null) {
                    throw new Exception("One Item with code = " + erpSalesOrderLineDto.getSku().getItemCode() + " and name = "
                            + erpSalesOrderLineDto.getSku().getItemName() + " can't be found in ERP's DB");
                }
                salesOrderLine.setItem(item);

                salesOrderLines.add(salesOrderLine);
            }
            so.setLines(salesOrderLines);
        } else {
            so.setLines(null);
        }

        return so;
    }

    /**
     * Convert DB to ERP sales order object
     * 
     * @param so DB sales order object
     * @return ErpSalesOrderDto
     */
    public static ErpSalesOrderDto run(SalesOrder so) {
        if (so == null) {
            return null;
        }

        ErpSalesOrderDto erpSo = new ErpSalesOrderDto();
        erpSo.setId(so.getId());
        erpSo.setLastUpdateTime(so.getLastUpdateTime());
        erpSo.setOrderTime(so.getOrderTime());
        erpSo.setDocTotal(so.getDocTotal());
        erpSo.setAnwId(so.getAnwId());

        ErpChannelDto channel = new ErpChannelDto();
        channel.setId(1);
        erpSo.setChannel(channel);
        erpSo.setPricingMethod(so.getPricingMethod());

        if (so.getBusinessPartner() != null) {
            ErpCustomerDto customer = new ErpCustomerDto();
            customer.setCode(so.getBusinessPartner().getCode());
            customer.setName(so.getBusinessPartner().getName());
            customer.setLastUpdateTime(so.getBusinessPartner().getLastUpdateTime());

            erpSo.setCustomer(customer);
        } else {
            erpSo.setCustomer(null);
        }

        if (so.getLines() != null) {
            ArrayList<ErpSalesOrderLineDto> erpSalesOrderLines = new ArrayList<ErpSalesOrderLineDto>();

            for (SalesOrderLine salesOrderLine : so.getLines()) {
                ErpSalesOrderLineDto erpSalesOrderLine = new ErpSalesOrderLineDto();
                erpSalesOrderLine.setQuantity(salesOrderLine.getQuantity());
                erpSalesOrderLine.setInventoryUomQuantity(salesOrderLine.getInventoryUomQuantity());
                erpSalesOrderLine.setUnitPrice(salesOrderLine.getUnitPrice());
                erpSalesOrderLine.setLineTotal(salesOrderLine.getLineTotal());
                erpSalesOrderLine.setOrderId(salesOrderLine.getSalesOrder().getAnwId());
                erpSalesOrderLine.setCalculationBase(salesOrderLine.getCalculationBase());

                ErpProductDto product = new ErpProductDto();
                product.setItemCode(salesOrderLine.getItem().getItemCode());
                product.setItemName(salesOrderLine.getItem().getItemName());
                product.setLastUpdateTime(salesOrderLine.getItem().getLastUpdateTime());

                erpSalesOrderLine.setSku(product);

                erpSalesOrderLines.add(erpSalesOrderLine);
            }
            erpSo.setProductLines(erpSalesOrderLines);
        } else {
            erpSo.setProductLines(null);
        }

        return erpSo;
    }

    /**
     * Generic convert of DB / ERP sales order object list
     * 
     * @param clazz Class type of objects in sosToConvert list
     * @param sosToConvert DB / ERP sales order object list
     * @return List<?>
     */
    @SuppressWarnings("unchecked")
    public static List<?> run(Class<?> clazz, List<?> sosToConvert) throws Exception {
        // salesOrders to erpSalesOrders
        if (clazz.equals(SalesOrder.class)) {
            List<ErpSalesOrderDto> convertedSos = new ArrayList<ErpSalesOrderDto>();
            if (sosToConvert != null) {
                for (SalesOrder soToConvert : (List<SalesOrder>) sosToConvert) {
                    convertedSos.add(run(soToConvert));
                }
            }
            return convertedSos;
        }
        // erpSalesOrders to salesOrders
        else if (clazz.equals(ErpSalesOrderDto.class)) {
            List<SalesOrder> convertedSos = new ArrayList<SalesOrder>();
            if (sosToConvert != null) {
                for (ErpSalesOrderDto soToConvert : (List<ErpSalesOrderDto>) sosToConvert) {
                    convertedSos.add(run(soToConvert));
                }
            }
            return convertedSos;
        }

        return null;
    }

    private static Customer findCustomer(String code) {
        List<Customer> foundCustomers = JpaLayer.find(Customer.class,
                "SELECT c FROM Customer c WHERE c.code = ?1", code);
        if (foundCustomers.size() == 1) {
            return foundCustomers.get(0);
        } else {
            return null;
        }
    }

    private static Item findItem(String code, String name) {
        List<Item> foundItems = JpaLayer.find(Item.class,
                "SELECT i FROM Item i WHERE i.itemCode = ?1 AND i.itemName = ?2", code, name);
        if (foundItems.size() == 1) {
            return foundItems.get(0);
        } else {
            return null;
        }
    }
}