package pl.mzuchnik.springpracadomowa3.model;

import lombok.Data;

@Data
public class Car {

    private long id;
    private String mark;
    private String model;
    private String color;

    public Car() {
    }

    public Car(long id, String mark, String model, String color) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }
}
