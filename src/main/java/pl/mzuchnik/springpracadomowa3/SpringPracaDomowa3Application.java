package pl.mzuchnik.springpracadomowa3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.mzuchnik.springpracadomowa3.model.Car;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class SpringPracaDomowa3Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringPracaDomowa3Application.class, args);
    }



    @Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


    @Bean
    public List<Car> getCarList()
    {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1L,"Toyota","Auris","blue"));
        carList.add(new Car(2L,"Citroen","C3","gray"));
        carList.add(new Car(3L,"Honda","Civic","black"));
        return carList;
    }


}
