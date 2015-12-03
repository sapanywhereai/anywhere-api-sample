package com.sap.integration.erp.dummy.entity;

import com.sap.integration.erp.dto.IErpDto;

/**
 * Interface used for unification of functionality. Not all ERP entities have implemented this functionality - only Customer and
 * Product. It was created only for demo purposes, that it is possible to unify functionality on ERP objects.
 */
public interface IErpDB {

    IErpDto convert();
}
