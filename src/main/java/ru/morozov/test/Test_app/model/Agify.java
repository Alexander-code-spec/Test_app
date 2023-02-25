package ru.morozov.test.Test_app.model;
import lombok.Data;
import java.io.Serializable;

@Data
public class Agify implements Serializable {
    private Integer age;
    private Integer count;
    private String name;
}
