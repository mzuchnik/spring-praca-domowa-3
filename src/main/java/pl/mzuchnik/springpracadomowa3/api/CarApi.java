package pl.mzuchnik.springpracadomowa3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.mzuchnik.springpracadomowa3.model.Car;
import pl.mzuchnik.springpracadomowa3.model.CarWithManagerResourceAssembler;
import pl.mzuchnik.springpracadomowa3.service.CarService;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private CarService carService;
    private CarWithManagerResourceAssembler carWithManagerResourceAssembler;

    @Autowired
    public CarApi(CarService carService, CarWithManagerResourceAssembler carWithManagerResourceAssembler) {
        this.carService = carService;
        this.carWithManagerResourceAssembler = carWithManagerResourceAssembler;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<Car>>> getCars() {
        return ResponseEntity.ok(carWithManagerResourceAssembler.toCollectionModel(carService.getAllCars()));
    }

    @GetMapping(value = "/{id}", produces = {"application/hal+json", MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EntityModel<Car>> getCarById(@PathVariable long id) {
        EntityModel<Car> carEntityModel =
                carWithManagerResourceAssembler.toModel(carService.getCarById(id));
        return ResponseEntity.ok(carEntityModel);

    }

    @GetMapping(value = "/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<Car>>> getCarsByColor(@PathVariable String color) {
        return ResponseEntity.ok(carWithManagerResourceAssembler.toCollectionModel(carService.getCarsByColor(color)));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> addCar(@Validated @RequestBody Car car) {
        return ResponseEntity.ok(carService.addCar(car));
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> modifyCar(@Validated @RequestBody Car newCar) {
        carService.removeCarById(newCar.getId());
        return addCar(newCar);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> patchCar(@PathVariable long id, @RequestBody Map<String, Object> updatesCar) {

            Car currentCar = carService.getCarById(id);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> currentCarMap = objectMapper.convertValue(currentCar, Map.class);
            for (Map.Entry<String, Object> entry : updatesCar.entrySet()) {
                if (currentCarMap.containsKey(entry.getKey()))
                    currentCarMap.replace(entry.getKey(), entry.getValue());
            }
            currentCar = objectMapper.convertValue(currentCarMap, Car.class);
            carService.removeCarById(id);
         return addCar(currentCar);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> removeCar(@PathVariable long id) {
        return ResponseEntity.ok(carService.removeCarById(id));
    }

}
