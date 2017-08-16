package com.sap.integration.anywhere;

import java.io.Serializable;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;

import com.sap.integration.utils.configuration.Property;

public class IntegrationState implements Serializable {

    private static final long serialVersionUID = 694440423128955443L;

    private String dtoType;

    private DateTime lastSyncTime;

    public IntegrationState() {
        super();
    }

    public IntegrationState(String dtoType, DateTime lastSyncTime) {
        super();
        this.dtoType = dtoType;
        this.lastSyncTime = lastSyncTime;
    }

    public String getDtoType() {
        return this.dtoType;
    }

    public Class<?> getDtoClass() throws ClassNotFoundException {
        return Class.forName(dtoType);
    }

    public void setDtoType(final String dtoType) {
        this.dtoType = dtoType;
    }

    public DateTime getLastSyncTime() {
        return this.lastSyncTime;
    }

    public void setLastSyncTime(DateTime lastUpdateTime) {
        this.lastSyncTime = lastUpdateTime;
    }

    /**
     * Get last sync time of SAP Anywhere business object.
     * 
     * @param dtoClass
     * @return
     */
    public static IntegrationState getIntegrationStateFor(Class<?> dtoClass) {
        try {
            // record last sync time, to filter data in the next cycle.
            DateTime lastSycnTime = Property.getLastSyncTime(dtoClass.getSimpleName());
            return new IntegrationState(dtoClass.getName(), lastSycnTime);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            return new IntegrationState(dtoClass.getName(), new DateTime());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [dtoType=");
        sb.append(this.dtoType);
        sb.append(", lastSyncTime=");
        sb.append(this.lastSyncTime);
        sb.append("]");
        return sb.toString();
    }
}
