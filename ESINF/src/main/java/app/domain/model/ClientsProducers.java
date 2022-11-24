package app.domain.model;

import app.domain.shared.UserAttributes;

import java.util.Objects;

public class ClientsProducers {

    private String locId;

    private String latitude;
    private String longitude;
    private String code;

    public ClientsProducers(String locId, String latitude, String longitude, String code) {
        this.locId = locId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.code = code;
    }

    public ClientsProducers() {
    }

    @Override
    public String toString() {
        return String.format("Location ID : "+this.locId+"   Latitude : "+this.latitude+"   Longitude : "+this.longitude+ " Code : "+this.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientsProducers clientProd = (ClientsProducers) o;
        return code == clientProd.getCode();
    }

    public String getLocationID() {
        return locId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {return longitude;}

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locId, longitude, latitude, code);
    }
}


