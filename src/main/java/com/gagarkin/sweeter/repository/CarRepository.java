package com.gagarkin.sweeter.repository;

import com.gagarkin.sweeter.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car ,Integer> {
}
