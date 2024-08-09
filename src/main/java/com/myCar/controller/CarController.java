package com.myCar.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myCar.domain.Car;
import com.myCar.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null) {
            return ResponseEntity.ok(car);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{userId}/add")
    public ResponseEntity<Car> addCarToUser(@PathVariable Long userId, @RequestBody Car car) {
        Car savedCar = carService.addCarToUser(userId, car);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @PostMapping("/user/{userId}/addWithImages")
    public ResponseEntity<Car> addCarWithImagesToUser(
            @PathVariable Long userId,
            @RequestPart("car") Car car,
            @RequestPart("files") List<MultipartFile> files) {

        try {
            Car createdCar = carService.addCarWithImages(userId, car, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{carId}/images")
    public ResponseEntity<Car> uploadImagesToCar(@PathVariable Long carId, @RequestParam("files") List<MultipartFile> files) {
        try {
            Car car = carService.addImagesToCar(carId, files);
            return ResponseEntity.ok(car);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/latest")
    public ResponseEntity<List<Car>> getLatestCars() {
        List<Car> cars = carService.getLatestCars(6); // Fetch last 3 cars
        return ResponseEntity.ok(cars);
    }
    
}