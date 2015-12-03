package com.sap.integration.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;

/**
 * Base class used for getting / posting of SAP Anywhere / ERP data.
 */
public abstract class BaseService {

    protected String service;

    protected IntegrationState anwIntegrationState;

    protected IntegrationState erpIntegrationState;

    public BaseService(String service, Class<? extends IAnwDto> anwClass, Class<? extends IErpDto> erpClass) {
        this.service = service;
        this.anwIntegrationState = IntegrationState.getIntegrationStateFor(anwClass);
        this.erpIntegrationState = IntegrationState.getIntegrationStateFor(erpClass);

    }

    /**
     * Get SAP Anywhere data transfer object.
     * 
     * @param anwQueryFilter SAP Anywhere query filter string
     * @return IAnwDto
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public IAnwDto getAnwObject(String anwQueryFilter)
            throws Exception {
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(service)
                .parameter("limit", 1)
                .parameter("offset", 0)
                .parameter("filter", anwQueryFilter)
                .parameter("access_token", Property.getAccessToken());

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder);
        List<IAnwDto> anwCustomer;
        try {
            anwCustomer = (List<IAnwDto>) JsonUtil.getObjects(response.getContent(), anwIntegrationState.getDtoClass());
            return anwCustomer.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            getLogger().warn(urlBuilder.getURL().toString(), e);
            return null;
        }
    }

    /**
     * Get list of all SAP Anywhere data transfer objects between from and to date.
     * 
     * @param fromDate Date before first object creation date
     * @param toDate Date after last object creation date
     * @return List<IAnwDto>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<IAnwDto> getAllAnwObjectsInPeriod(DateTime fromDate, DateTime toDate, String queryExpand)
            throws Exception {
        List<IAnwDto> anwObjects = new ArrayList<IAnwDto>();
        List<IAnwDto> anwObjectsPage;
        int offset = 0;

        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(service)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter("filter",
                            "creationTime gt '" + DateUtil.convertDateTimeToString(fromDate) + "' and "
                          + "creationTime lt '" + DateUtil.convertDateTimeToString(toDate) + "' ")
                    .parameter("expand", queryExpand)
                    .parameter("access_token", Property.getAccessToken());

            anwObjectsPage = (List<IAnwDto>) JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(),
                    anwIntegrationState.getDtoClass());
            anwObjects.addAll(anwObjectsPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwObjectsPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwObjects;
    }

    /**
     * Get list of SAP Anywhere data transfer objects ordered by updateTime.
     * 
     * @return List<IAnwDto>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<IAnwDto> getAnwObjects() throws Exception {
        List<IAnwDto> anwObjects = new ArrayList<IAnwDto>();
        List<IAnwDto> anwObjectsPage;
        int offset = 0;

        do {
            UrlBuilder urlBuilder = new UrlBuilder()
                    .append(AnwUrlUtil.getOpenApiBaseUrl())
                    .append(service)
                    .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                    .parameter("offset", offset)
                    .parameter("orderby", "updateTime")
                    .parameter("filter", "updateTime gt '"
                            + DateUtil.convertDateTimeToString(anwIntegrationState.getLastSyncTime()) + "'")
                    .parameter("access_token", Property.getAccessToken());

            anwObjectsPage = (List<IAnwDto>) JsonUtil.getObjects(AnwServiceCall.get(urlBuilder).getContent(),
                    anwIntegrationState.getDtoClass());
            anwObjects.addAll(anwObjectsPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwObjectsPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwObjects;
    }

    /**
     * Evoke CREATE / UPDATE of SAP Anywhere objects and persist timestamp of last successfully transfered.
     * 
     * @param erpObjects ERP data transfer objects
     * @throws Exception
     */
    public void postToAnywhere(List<? extends IErpDto> erpObjects) throws Exception {
        int createdCount = 0, updatedCount = 0;

        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(service)
                .parameter("access_token", Property.getAccessToken());

        for (IErpDto erpObject : erpObjects) {
            AnwSimpleResponse response = null;
            DateTime lastErpSyncTime = erpObject.getLastSyncTime();
            if (lastErpSyncTime == null) {
                lastErpSyncTime = new DateTime(0);
            }
            IAnwDto anwObject = getAnwObject(getAnwQueryFilter(erpObject));
            IAnwDto dtoToUpdate = erpObject.transform();
            if (anwObject == null) { // create
                response = AnwServiceCall.post(urlBuilder, dtoToUpdate);
                if (response != null && !response.isError()) {
                    createdCount++;
                }
            } else { // update
                UrlBuilder urlPatch = new UrlBuilder(urlBuilder);
                if (dtoToUpdate.isDifferent(anwObject) && (erpObject.getLastSyncTime() == null
                        || lastErpSyncTime.isAfter(anwObject.getLastSyncTime()))) {
                    urlPatch.append("/");
                    urlPatch.append(anwObject.getId().toString());
                    response = AnwServiceCall.patch(urlPatch, dtoToUpdate);
                    if (response != null && !response.isError()) {
                        updatedCount++;
                    }
                }
            }

            // if the selected Erp object is same or there was no error while its update into Anw
            // and if the update time of the object is after the last sync time
            if ((!dtoToUpdate.isDifferent(anwObject) || (response != null && !response.isError()))
                    && lastErpSyncTime.isAfter(erpIntegrationState.getLastSyncTime())) {
                erpIntegrationState.setLastSyncTime(lastErpSyncTime);
                erpIntegrationState.merge();
            }
        }

        getLogger().info("Successfully created " + createdCount);
        getLogger().info("Successfully updated " + updatedCount);
        getLogger().info("Failed " + (erpObjects.size() - createdCount - updatedCount));
    }

    /**
     * Get logger of service implementation.
     * 
     * @return Logger
     */
    public abstract Logger getLogger();

    /**
     * Get SAP Anywhere query filter string used for retrieving SAP Anywhere data transfer object.
     * 
     * @param erpObject ERP data transfer object
     * @return String
     */
    public abstract String getAnwQueryFilter(IErpDto erpObject);

    /**
     * Get ERP data transfer object.
     * 
     * @param anwObject SAP Anywhere data transfer object
     * @return IErpDto
     */
    public abstract IErpDto getErpObject(IAnwDto anwObject);

    /**
     * Get list of ERP data transfer objects ordered by updateTime.
     * 
     * @return List<IErpDto>
     */
    public abstract List<IErpDto> getErpObjects();

    /**
     * Evoke CREATE / UPDATE of ERP objects and persist timestamp of last successfully transfered.
     * 
     * @param anwObjects SAP Anywhere data transfer objects
     */
    public abstract void postToErp(List<? extends IAnwDto> anwObjects);

}
