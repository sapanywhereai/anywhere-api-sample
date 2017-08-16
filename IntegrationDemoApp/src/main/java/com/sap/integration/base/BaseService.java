package com.sap.integration.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwSimpleResponse;
import com.sap.integration.anywhere.IntegrationState;
import com.sap.integration.anywhere.url.AnwUrlUtil;
import com.sap.integration.salesorder.model.IAnwDto;
import com.sap.integration.utils.DateUtil;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.UrlBuilder;

/**
 * Base class used for getting / posting of SAP Anywhere / ERP data.
 */
public abstract class BaseService {

    protected String service;

    protected IntegrationState anwIntegrationState;

    public BaseService(String service, Class<? extends IAnwDto> anwClass) {
        this.service = service;
        this.anwIntegrationState = IntegrationState.getIntegrationStateFor(anwClass);

    }

    /**
     * Get SAP Anywhere data transfer object.
     * 
     * @param anwQueryFilter
     *            SAP Anywhere query filter string
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
                .parameter("filter", anwQueryFilter);

        AnwSimpleResponse response = AnwServiceCall.get(urlBuilder, null);
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
     * @param fromDate
     *            Date before first object creation date
     * @param toDate
     *            Date after last object creation date
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
                    .parameter("expand", queryExpand);

            anwObjectsPage = (List<IAnwDto>) JsonUtil.getObjects(AnwServiceCall.get(urlBuilder, null).getContent(),
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
                            + DateUtil.convertDateTimeToString(anwIntegrationState.getLastSyncTime()) + "'");

            anwObjectsPage = (List<IAnwDto>) JsonUtil.getObjects(AnwServiceCall.get(urlBuilder, null).getContent(),
                    anwIntegrationState.getDtoClass());
            anwObjects.addAll(anwObjectsPage);
            offset += AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT;
        } while (anwObjectsPage.size() == AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT);

        return anwObjects;
    }

    /**
     * Get a object of SAP Anywhere data transfer objects ordered by id.
     * 
     * @return List<IAnwDto>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public IAnwDto getAnwObjectWithId(String resourceId) throws Exception {
        List<IAnwDto> anwObjectsPage;
        int offset = 0;
        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(service)
                .parameter("limit", AnwUrlUtil.OPENAPI_QEURY_OPTION_LIMIT)
                .parameter("offset", offset)
                .parameter("orderby", "updateTime")
                .parameter("filter", "id  eq '" + resourceId + "'");

        return (IAnwDto) JsonUtil.getObject(AnwServiceCall.get(urlBuilder, null).getContent(),
                anwIntegrationState.getDtoClass());

    }

    /**
     * Evoke CREATE / UPDATE of SAP Anywhere objects and persist timestamp of last successfully transfered.
     * 
     * @param erpObjects
     *            ERP data transfer objects
     * @throws Exception
     */
    public void postToAnywhere(List<?> erpObjects) throws Exception {
        int createdCount = 0, updatedCount = 0;

        UrlBuilder urlBuilder = new UrlBuilder()
                .append(AnwUrlUtil.getOpenApiBaseUrl())
                .append(service);

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
     * Evoke CREATE / UPDATE of ERP objects and persist timestamp of last successfully transfered.
     * 
     * @param anwObjects
     *            SAP Anywhere data transfer objects
     */
    public abstract void postToErp(List<? extends IAnwDto> anwObjects);

}
