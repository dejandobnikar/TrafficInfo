package dd.trafficinfo.core;

/**
 * Created by DejanD on 18.9.2014.
 */
public class Dogodek {

    public Dogodek() {

    }


    /*
                    "dovoljenjeDatKon": null,
                "y_wgs": 45.573341557442745,
                "kategorija": "H5",
                "zbrisano": null,
                "isMejniPrehod": false,
                "opis": "Na hitri cesti Škofije - Koper med prehodom Škofije in razcepom Srmin v smeri Kopra bo do 18. 9. 2014 zaradi vzdrževalnih del oviran promet.",
                "vir": "DARS",
                "operater_izbris": null,
                "id": 168752,
                "veljavnostDo": 1411034400000,
                "prioriteta": 2,
                "operater_sprememba": null,
                "prioritetaCeste": 11,
                "veljavnostOd": 1411022220000,
                "cesta": "H5-E751 Škofije - Koper",
                "vneseno": 1411022341220,
                "updated": 1411022341220,
                "x_wgs": 13.79266407395227,
                "dovoljenjeSt": null,
                "opisEn": "On the H5-E751 expressway near Koper in the direction of Koper until 18/9/2014 traffic will be obstructed due to roadworks.",
                "stacionaza": 1071,
                "operater_vnos": "Bojan Renko",
                "odsek": "0388",
                "vzrokEn": "Roadworks",
                "icon": "http://kazipot1.promet.si/kazipot/services/DataExport/icons/dogodki_v1/delo1.gif",
                "spremenjeno": null,
                "vzrok": "Delo na cesti",
                "dovoljenjeDatZac": null,
                "y": 48381.4447469182,
                "x": 406149.937426436
    */

    private double y_wgs;
    private double x_wgs;
    private String icon;
    private String opis;
    private String kategorija;
    private String vzrok;


    public double getY_wgs() {
        return y_wgs;
    }

    public void setY_wgs(double y_wgs) {
        this.y_wgs = y_wgs;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public double getX_wgs() {
        return x_wgs;
    }

    public void setX_vgs(double x_wgs) {
        this.x_wgs = x_wgs;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getVzrok() {
        return vzrok;
    }

    public void setVzrok(String vzrok) {
        this.vzrok = vzrok;
    }
}
