package com.sap.integration.utils.integrationdb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.PrimaryKey;
import org.joda.time.DateTime;

import com.sap.integration.utils.integrationdb.jpa.JpaLayer;

/**
 * Entity IntegrationState stores data used during the process of integration. This entity and table belongs to integration
 * application.
 */
@Entity
@PrimaryKey(columns = @Column(name = "dtoType"))
public class IntegrationState extends JpaLayer implements Serializable {

    private static final long serialVersionUID = 694440423128955443L;

    private String dtoType;

    @Convert("JodaDate")
    private DateTime lastSyncTime;

    public IntegrationState() {
        super();
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

    private static IntegrationState createNewEntry(final String dtoType) {
        IntegrationState is = new IntegrationState();
        is.setDtoType(dtoType);
        is.setLastSyncTime(new DateTime("1990-01-01T00:00:00.000Z"));
        is.persist();
        return is;
    }

    /**
     * Get last sync time of SAP Anywhere business object.
     * 
     * @param dtoClass
     * @return
     */
    public static <E> IntegrationState getIntegrationStateFor(Class<E> dtoClass) {
        IntegrationState is = JpaLayer.findFirst(IntegrationState.class, "SELECT i FROM IntegrationState i WHERE i.dtoType = ?1",
                dtoClass.getName());
        if (is == null) {
            is = createNewEntry(dtoClass.getName());
        }
        return is;
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
