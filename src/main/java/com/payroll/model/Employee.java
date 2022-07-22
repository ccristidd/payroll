package com.payroll.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    private String role;
    //firstName si lastName au fost adaugate mai tarziu si refacuta implementarea.
    //FYI getters si setters daca se scriu explicit ii inlocuiesc pe cei default.
    private String firstName;
    private String lastName;


    public Employee(String nume, String role) {
        String[] split = nume.split(" ");
        this.firstName = split[0];
        this.lastName = split[1];
        this.role = role;
    }
    public Employee(String firstnamename, String lastName, String role) {
        this.firstName = firstnamename;
        this.lastName = lastName;
        this.role = role;
    }

    public String getName(){
        return this.firstName + " "+this.lastName;
    }

    public void setName(String name){
        String[] split = name.split(" ");
        firstName = split[0];
        lastName = split[1];
    }
}

