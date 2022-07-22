package com.payroll.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_ORDER")
@Data
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private OrderStatus status;

    public Orders(String description, OrderStatus status) {
        this.description = description;
        this.status = status;
    }


}
