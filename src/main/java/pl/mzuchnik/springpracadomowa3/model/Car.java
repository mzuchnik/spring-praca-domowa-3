package pl.mzuchnik.springpracadomowa3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Car extends RepresentationModel<Car> {

    @Min(1)
    private long id;

    @NotNull
    @Size(min = 2)
    private String mark;

    @NotNull
    @Size(min = 2)
    private String model;

    @NotNull
    private String color;

}
