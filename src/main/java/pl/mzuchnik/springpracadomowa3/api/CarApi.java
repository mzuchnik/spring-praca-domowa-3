package pl.mzuchnik.springpracadomowa3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.mzuchnik.springpracadomowa3.exception.CarNotFoundException;
import pl.mzuchnik.springpracadomowa3.model.Car;
import pl.mzuchnik.springpracadomowa3.model.CarWithManagerResourceAssembler;
import pl.mzuchnik.springpracadomowa3.service.CarService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class CarApi {

    private CarService carService;
    private CarWithManagerResourceAssembler carWithManagerResourceAssembler;

    @Autowired
    public CarApi(CarService carService, CarWithManagerResourceAssembler carWithManagerResourceAssembler) {
        this.carService = carService;
        this.carWithManagerResourceAssembler = carWithManagerResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Car>>> getCars() {
        return ResponseEntity.ok(carWithManagerResourceAssembler.toCollectionModel(carService.getAllCars()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Car>> getCarById(@PathVariable long id) {

        Car carById = carService.getCarById(id).orElseThrow(() -> new CarNotFoundException(id));
        EntityModel<Car> carResult = carWithManagerResourceAssembler.toModel(carById);

        return ResponseEntity.ok(carResult);
    }

    @GetMapping(value = "/color/{color}")
    public ResponseEntity<CollectionModel<EntityModel<Car>>> getCarsByColor(@PathVariable String color) {

        List<Car> carList = carService.getCarsByColor(color);
        if(carList.isEmpty() || carList == null)
        {
            throw new CarNotFoundException();
        }
        CollectionModel<EntityModel<Car>> carListModel = carWithManagerResourceAssembler.toCollectionModel(carList);

        return ResponseEntity.ok(carListModel);
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@Validated @RequestBody Car car) {
        return ResponseEntity.ok(carService.addCar(car));
    }

    @PutMapping
    public ResponseEntity<Car> modifyCar(@Validated @RequestBody Car newCar) {
        carService.removeCarById(newCar.getId());
        return addCar(newCar);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Car> patchCar(@PathVariable long id, @RequestBody Map<String, Object> updatesCar) {

        Car currentCar = carService.getCarById(id).orElseThrow(() -> new CarNotFoundException(id));

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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable long id) {
        return ResponseEntity.ok(carService.removeCarById(id));
    }

}
