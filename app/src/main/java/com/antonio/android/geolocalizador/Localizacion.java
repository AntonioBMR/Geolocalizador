package com.antonio.android.geolocalizador;

import android.location.Location;

/**
 * Created by Antonio on 03/03/2015.
 */
public class Localizacion {
    String fecha, localidad,calle;
    String altitud,longitud;
    Location location;

    public Localizacion(String fecha, String localidad, String calle, Location location) {
        this.fecha = fecha;
        this.localidad = localidad;
        this.calle = calle;
        this.location = location;
    }

    public Localizacion(String fecha, Location location) {
        this.fecha = fecha;
        this.location = location;
        this.localidad = "";
        this.calle = "";
    }

    public Localizacion() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longuitud) {
        this.longitud = longuitud;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Localizacion)) return false;

        Localizacion that = (Localizacion) o;

        if (calle != null ? !calle.equals(that.calle) : that.calle != null) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        if (localidad != null ? !localidad.equals(that.localidad) : that.localidad != null)
            return false;
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fecha != null ? fecha.hashCode() : 0;
        result = 31 * result + (localidad != null ? localidad.hashCode() : 0);
        result = 31 * result + (calle != null ? calle.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Localizacion{" +
                "fecha='" + fecha + '\'' +
                ", localidad='" + localidad + '\'' +
                ", calle='" + calle + '\'' +
                ", location=" + location +
                '}';
    }
}
