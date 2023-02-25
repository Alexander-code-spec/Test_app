package ru.morozov.test.Test_app.model;
import lombok.Data;
import java.io.Serializable;

@Data
public class TwoIp implements Serializable {
    String ip;
    String countryCode;
    String country;
    String countryRus;
    String region;
    String regionRus;
    String city;
    String cityRus;
    float latitude;
    float longitude;
    int zipCode;
}
