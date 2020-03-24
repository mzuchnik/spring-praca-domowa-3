package pl.mzuchnik.springpracadomowa3.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mzuchnik.springpracadomowa3.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> carList;

    @Autowired
    public CarApi(List<Car> carList) {
        this.carList = carList;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Car>> getCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Car>> getCarsByColor(@PathVariable String color) {

        Optional<List<Car>> carsWithColor =
                Optional.of(carList.stream()
                        .filter(car -> car.getColor().equalsIgnoreCase(color))
                        .collect(Collectors.toList()));

        if (carsWithColor.isPresent() && !carsWithColor.get().isEmpty()) {
            return new ResponseEntity<>(carsWithColor.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> addCar(@RequestBody Car car) {

        if (carList.add(car)) {
            return new ResponseEntity<>(car, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> modifyCar(@RequestBody Car newCar) {
        Optional<Car> first = carList.stream().filter(e -> e.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            Car modCar = first.get();
            if (carList.remove(modCar) && carList.add(newCar)) {
                return new ResponseEntity<>(newCar, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> patchCar(@PathVariable long id, @RequestBody Map<String, Object> updatesCar) {

        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();

        if (first.isPresent()) {
            Car currentCar = first.get();
            carList.remove(currentCar);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> currentCarMap = objectMapper.convertValue(currentCar, Map.class);
            for (Map.Entry<String, Object> entry : updatesCar.entrySet()) {
                if (currentCarMap.containsKey(entry.getKey()))
                    currentCarMap.replace(entry.getKey(), entry.getValue());
            }
            currentCar = objectMapper.convertValue(currentCarMap, Car.class);
            carList.add(currentCar);
            return new ResponseEntity<>(currentCar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> removeCar(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            return ResponseEntity.ok(first.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
