package dd.trafficinfo.core;

import java.util.List;

/**
 * Created by DejanD on 18.9.2014.
 */
public class Dogodki {

    public Dogodki() {

    }

    private List<Dogodek> dogodek;
    private String lastUpdate;
    private String language;
    private String periodFrom;
    private String crsId;
    private String version;
    private String periodTo;
    private String currentDateTime;


    public List<Dogodek> getDogodek() {
        return dogodek;
    }

    public void setDogodek(List<Dogodek> dogodek) {
        this.dogodek = dogodek;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getCrsId() {
        return crsId;
    }

    public void setCrsId(String crsId) {
        this.crsId = crsId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
