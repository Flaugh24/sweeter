package com.gagarkin.sweeter.domain;

import com.gagarkin.sweeter.config.pg.StringJsonUserType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Entity
@Table(name = "car")
@Data
@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "modules", columnDefinition = "jsonb")
    @Type(type = "StringJsonObject")
    private String modulesJSONArray;
}
