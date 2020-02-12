package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.Car;
import com.gagarkin.sweeter.repository.CarRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping(value = "/test")
    public List<Car> test(){
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "2");
        jsonObject.put("b",3);

        jsonArray.put(jsonObject);

        Car car = new Car();
        car.setModulesJSONArray(JSONObject.valueToString(jsonArray));



        carRepository.save(car);

        List<Car> all = carRepository.findAll();

        for (Car car1 : all) {
            System.out.println(new JSONArray(car1.getModulesJSONArray()));
        }
        return all;
    }
}
