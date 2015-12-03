package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.erp.dummy.entity.IErpDB;

/**
 * Interface used for unification of functionality. Not all ERP objects have implemented this functionality - only Customer and
 * Product. It was created only for demo purposes, that it is possible to unify functionality on ERP objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface IErpDto {

    Long getId();

    DateTime getLastSyncTime();

    IAnwDto transform();

    IErpDB convert();

    boolean isDifferent(IErpDto other);
}
