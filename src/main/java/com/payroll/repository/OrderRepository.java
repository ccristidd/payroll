package com.payroll.repository;

import com.payroll.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Orders, Long> {
}
