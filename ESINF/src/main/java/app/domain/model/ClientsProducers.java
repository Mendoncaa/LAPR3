package app.domain.model;

import app.domain.shared.UserAttributes;

import java.util.Objects;

public class ClientsProducers {

    private String locId;
    private float latitude;
    private float longitude;
    private String code;

    public ClientsProducers(String locId, float latitude, float longitude, String code) {
        this.locId = locId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.code = code;
    }

    public ClientsProducers(ClientsProducers clientsProducers){
        this.locId = clientsProducers.getLocationID();
        this.latitude = clientsProducers.getLatitude();
        this.longitude = clientsProducers.getLongitude();
        this.code = clientsProducers.getCode();
    }

    public ClientsProducers() {
    }

    @Override
    public String toString() {
        return String.format("Location ID : "+this.locId+"    Latitude : "+this.latitude+"    Longitude : "+this.longitude+ "    Code : "+this.code);
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

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {return longitude;}

    public String getCode() {
        return code;
    }

    public String getType() {

        if(code.substring(0,1).equalsIgnoreCase("C")) {
            return "Cliente";
        }else if(code.substring(0,1).equalsIgnoreCase("P")) {
            return "Produtor";
        }else {
            return "Empresa";
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(locId, longitude, latitude, code);
    }
}


