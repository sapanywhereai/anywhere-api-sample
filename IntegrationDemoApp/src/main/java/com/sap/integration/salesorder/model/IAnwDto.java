package com.sap.integration.salesorder.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 * Interface used for unification of functionality. Not all SAP Anywhere objects have implemented this functionality - only
 * Customer and Product. It was created only for demo purposes, that it is possible to unify functionality on SAP Anywhere
 * objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface IAnwDto {

    public Long getId();

    public DateTime getLastSyncTime();

    boolean isDifferent(IAnwDto other);

}
