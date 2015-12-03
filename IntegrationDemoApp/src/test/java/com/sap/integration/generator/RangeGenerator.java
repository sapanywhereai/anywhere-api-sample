package com.sap.integration.generator;

import java.util.List;

import com.sap.integration.utils.integrationdb.entity.Range;

public class RangeGenerator {

    public static void main(String[] args) {
        createDummyData();
        printAllData();
    }

    private static void createDummyData() {
        new Range()
                .setRangeFrom(1L)
                .setRangeTo(2L)
                .persist();
        new Range()
                .setRangeFrom(3L)
                .setRangeTo(4L)
                .persist();
        new Range()
                .setRangeFrom(5L)
                .setRangeTo(6L)
                .persist();
        new Range()
                .setRangeFrom(7L)
                .setRangeTo(8L)
                .persist();
    }

    private static void printAllData() {
        List<Range> listOfRanges = Range.getAll(Range.class);

        if (listOfRanges != null && listOfRanges.size() > 0) {
            for (Range range : listOfRanges) {
                System.out.println(range);
            }
        }
    }
}
