package com.sap.integration.utils.integrationdb.entity;

import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.erp.dummy.entity.StockData;

/**
 * Class, which provides methods for identification ranges of transactions in StockData table which will be subsequently
 * processed. This service belongs integration application.
 */
public class RangeService {

    private static final Logger LOG = Logger.getLogger(RangeService.class);

    /**
     * Method which returns list of all ranges in the table Range.
     * 
     * @return list of all ranges
     */
    public static final List<Range> getRanges() {
        return Range.getAll(Range.class);
    }

    /**
     * Method, which returns last range from the table Range.
     * 
     * @return Range - last range in the table Range
     */
    public static final Range getLastRange() {
        return Range.findFirst(Range.class, "SELECT r FROM Range r ORDER BY r.id DESC");
    }

    /**
     * Method, which saves entered range into table Range.
     * 
     * @param range - range, which will be saved as new record
     */
    public static final void saveRange(Range range) {
        if (range != null) {
            LOG.debug("Range - ranges for saving: " + range.toString());
            range.persist();
        }
    }

    /**
     * Method, which identifies, which ranges of transactions stored in StockData will be processed.
     * 
     * @return Range - ranges, which will be processed.
     */
    public static final Range getRangeForProcessing() {
        // identifies last inserted range in Range table
        Range lastProcessedRanges = getLastRange();

        // identify last entry in StockData table to get StockData id
        StockData lastStockDataInErp = StockData.findFirst(StockData.class, "SELECT s FROM StockData s ORDER BY s.id DESC");

        Range rangeToProcess = new Range();

        if (lastProcessedRanges != null && lastStockDataInErp != null) {

            LOG.debug("Range - selected lastProcessedRanges: " + lastProcessedRanges.toString());
            LOG.debug("Range - selected lastStockDataInErp: " + lastStockDataInErp.toString());

            if (lastProcessedRanges.getRangeTo() + 1 <= lastStockDataInErp.getId()) {
                rangeToProcess.setRangeFrom(lastProcessedRanges.getRangeTo() + 1);
                rangeToProcess.setRangeTo(lastStockDataInErp.getId());
                LOG.debug("Range - found correct range of transactions for integration");
            } else {
                rangeToProcess.setRangeFrom(null);
                rangeToProcess.setRangeTo(null);
                LOG.debug("Range - no new transactions to integration");
            }
        } else if (lastProcessedRanges == null && lastStockDataInErp != null) {

            LOG.debug("Range - selected lastProcessedRanges is null");
            LOG.debug("Range - selected lastStockDataInErp: " + lastStockDataInErp.toString());

            StockData firstStockDataInErp = StockData.findFirst(StockData.class, "SELECT s FROM StockData s ORDER BY s.id ASC");

            if (firstStockDataInErp != null) {
                rangeToProcess.setRangeFrom(firstStockDataInErp.getId());
                rangeToProcess.setRangeTo(lastStockDataInErp.getId());
                LOG.debug("Range - found correct range of transactions for integration");
            } else {
                rangeToProcess.setRangeFrom(null);
                rangeToProcess.setRangeTo(null);
                LOG.debug("Range - not possible to identify correct range");
            }
        } else if (lastProcessedRanges != null && lastStockDataInErp == null) {
            rangeToProcess.setRangeFrom(null);
            rangeToProcess.setRangeTo(null);
            LOG.debug("Range - lastProcessedRanges != null && lastStockDataInErp == null => inconsistent database data");
        } else if (lastProcessedRanges == null && lastStockDataInErp == null) {
            rangeToProcess.setRangeFrom(null);
            rangeToProcess.setRangeTo(null);
            LOG.debug("Range - lastProcessedRanges == null && lastStockDataInErp == null => no new transactions");
        }

        LOG.debug("Range - ranges for processing: " + rangeToProcess.toString());

        return rangeToProcess;
    }
}
