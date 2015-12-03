package com.sap.integration.erp.dummy.conversion;

import java.util.ArrayList;
import java.util.List;

import com.sap.integration.erp.dto.ErpAmountDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpPaymentDto;
import com.sap.integration.erp.dto.ErpPaymentLineDto;
import com.sap.integration.erp.dto.ErpTransactionDocumentDto;
import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.entity.Invoice;
import com.sap.integration.erp.dummy.entity.Payment;
import com.sap.integration.erp.dummy.entity.PaymentLine;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Class, which contains methods used for conversion Payment objects.
 */
public class PaymentConversion {

    /**
     * Method, which converts ErpPaymentDto object to Payment object.
     * 
     * @param erpPayment - object, which will be converted to the Payment object
     * @return Payment - converted object
     * @throws Exception possible exception during the processing
     */
    public static Payment run(ErpPaymentDto erpPayment) throws Exception {
        if (erpPayment == null) {
            return null;
        }

        Payment payment = new Payment();

        payment.setId(erpPayment.getId());
        payment.setAnwId(erpPayment.getAnwId());
        payment.setStatus(erpPayment.getStatus());
        payment.setDocTotal(erpPayment.getAmount().getAmount());
        payment.setLastUpdateTime(erpPayment.getLastUpdateTime());

        if (erpPayment.getCustomer() != null) {
            Customer linkedCustomer = findCustomer(erpPayment.getCustomer().getCode());
            if (linkedCustomer == null) {
                throw new Exception("One Customer with code = " + erpPayment.getCustomer().getCode()
                        + " can't be found in ERP's DB");
            }
            payment.setBp(linkedCustomer);
        } else {
            payment.setBp(null);
        }

        if (erpPayment.getPaymentLines() != null) {
            ArrayList<PaymentLine> paymentLines = new ArrayList<PaymentLine>();

            for (ErpPaymentLineDto erpPaymentLineDto : erpPayment.getPaymentLines()) {
                PaymentLine paymentLine = new PaymentLine();
                paymentLine.setAppliedAmount(erpPaymentLineDto.getAppliedAmount().getAmount());
                paymentLine.setPayment(payment);

                if (erpPaymentLineDto.getTransactionDocument().getType().equalsIgnoreCase("invoice")) {
                    Invoice lineInv = findInvoice(erpPaymentLineDto.getTransactionDocument().getId());
                    if (lineInv == null) {
                        throw new Exception("One Invoice with anwId = "
                                + erpPaymentLineDto.getTransactionDocument().getId().toString() + " can't be found in ERP's DB");
                    }
                    paymentLine.setInvoice(lineInv);
                } else {
                    throw new Exception("There is Payment Line of incorrect, or unsupported type ("
                            + erpPaymentLineDto.getTransactionDocument().getType() +
                            ") in SAP Anywhere's Payment with ID=" + erpPayment.getAnwId()
                            + ". Only Payments of Invoices are accepted.");
                }

                paymentLines.add(paymentLine);
            }
            payment.setLines(paymentLines);
        } else {
            payment.setLines(null);
        }

        return payment;
    }

    /**
     * Method, which converts Payment object to ErpPaymentDto object.
     * 
     * @param payment - object, which will be converted to the ErpPaymentDto object
     * @return ErpPaymentDto - converted object
     * @throws Exception possible exception during the processing
     */
    public static ErpPaymentDto run(Payment payment) throws Exception {

        if (payment == null) {
            return null;
        }

        ErpPaymentDto erpPayment = new ErpPaymentDto();
        erpPayment.setId(payment.getId());
        erpPayment.setAnwId(payment.getAnwId());
        erpPayment.setStatus(payment.getStatus());
        erpPayment.setLastUpdateTime(payment.getLastUpdateTime());

        if (payment.getDocTotal() != null) {
            ErpAmountDto amount = new ErpAmountDto();
            amount.setAmount(payment.getDocTotal());
            amount.setAmountLocalCurrency(payment.getDocTotal());
            erpPayment.setAmount(amount);
        } else {
            erpPayment.setAmount(null);
        }

        if (payment.getBp() != null) {
            ErpCustomerDto customer = new ErpCustomerDto();
            customer.setCode(payment.getBp().getCode());
            customer.setName(payment.getBp().getName());
            customer.setLastUpdateTime(payment.getBp().getLastUpdateTime());

            erpPayment.setCustomer(customer);
        } else {
            erpPayment.setCustomer(null);
        }

        if (payment.getLines() != null) {
            ArrayList<ErpPaymentLineDto> erpPaymentLines = new ArrayList<ErpPaymentLineDto>();

            for (PaymentLine paymentLine : payment.getLines()) {
                ErpPaymentLineDto erpPaymentLine = new ErpPaymentLineDto();

                ErpAmountDto amount = new ErpAmountDto();
                amount.setAmount(paymentLine.getAppliedAmount());
                amount.setAmountLocalCurrency(paymentLine.getAppliedAmount());
                erpPaymentLine.setAppliedAmount(amount);

                if (paymentLine.getInvoice() != null) {
                    ErpTransactionDocumentDto erpTransactionDocument = new ErpTransactionDocumentDto();
                    erpTransactionDocument.setId(paymentLine.getInvoice().getAnwId());
                    erpTransactionDocument.setType("INVOICE");
                    erpPaymentLine.setTransactionDocument(erpTransactionDocument);
                } else
                    throw new Exception("Problem with getting transaction document (invoice) for payment line.");

                erpPaymentLines.add(erpPaymentLine);
            }
            erpPayment.setPaymentLines(erpPaymentLines);
        } else {
            erpPayment.setPaymentLines(null);
        }

        return erpPayment;
    }

    /**
     * Generic convert of DB / ERP Payment object list
     * 
     * @param clazz Class type of objects in paysToConvert list
     * @param paysToConvert DB / ERP payment object list
     * @return List<?>
     */
    @SuppressWarnings("unchecked")
    public static final List<?> run(final Class<?> clazz, final List<?> paysToConvert) throws Exception {

        // conversion payments to erpPayments
        if (clazz.equals(Payment.class)) {
            List<ErpPaymentDto> convertedPays = new ArrayList<ErpPaymentDto>();
            if (paysToConvert != null) {
                for (Payment payToConvert : (List<Payment>) paysToConvert) {
                    convertedPays.add(run(payToConvert));
                }
            }
            return convertedPays;
        }

        // conversion erpPayments to payments
        else if (clazz.equals(ErpPaymentDto.class)) {
            List<Payment> convertedPays = new ArrayList<Payment>();
            if (paysToConvert != null) {
                for (ErpPaymentDto payToConvert : (List<ErpPaymentDto>) paysToConvert) {
                    convertedPays.add(run(payToConvert));
                }
            }
            return convertedPays;
        }

        return null;
    }

    /**
     * Method, which selects customer based on entered code.
     * 
     * @param code - filter used for selecting customer
     * @return null - if entered parameter is null <br>
     *         Customer - found customer <br>
     */
    private static final Customer findCustomer(final String code) {
        if (code == null) {
            return null;
        } else {
            List<Customer> foundCustomers = JpaLayer.find(Customer.class, "SELECT c FROM Customer c where c.code = ?1", code);
            if (foundCustomers.size() == 1) {
                return foundCustomers.get(0);
            } else {
                return null;
            }
        }
    }

    /**
     * Method, which selects invoices based on entered id.
     * 
     * @param id - filter used for selecting invoices
     * @return null - if entered parameter is null <br>
     *         Invoice - found invoice <br>
     */
    private static final Invoice findInvoice(final Long id) {
        if (id == null) {
            return null;
        } else {
            List<Invoice> foundInvoices = JpaLayer.find(Invoice.class, "SELECT i FROM Invoice i where i.anwId = ?1", id);
            if (foundInvoices.size() == 1) {
                return foundInvoices.get(0);
            } else {
                return null;
            }
        }
    }
}
