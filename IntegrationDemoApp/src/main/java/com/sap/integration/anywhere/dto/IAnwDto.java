package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

import com.sap.integration.erp.dto.IErpDto;

/**
 * Interface used for unification of functionality. Not all SAP Anywhere objects have implemented this functionality - only
 * Customer and Product. It was created only for demo purposes, that it is possible to unify functionality on SAP Anywhere
 * objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface IAnwDto {

    public Long getId();

    public DateTime getLastSyncTime();

    public IErpDto transform();

    boolean isDifferent(IAnwDto other);

}
