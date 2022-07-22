package com.payroll.configuration;

import com.payroll.model.Employee;
import com.payroll.model.OrderStatus;
import com.payroll.model.Orders;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.OrderRepository;
import net.bytebuddy.description.field.FieldDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.criteria.Order;


@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading Nume Prenume 1");
            employeeRepository.save(new Employee("Nume Prenume1", "role 1"));
            log.info("Preloading Nume Prenume 2");
            employeeRepository.save(new Employee("Nume Prenume2", "role 2"));
            log.info("Preloading Nume Prenume 3");
            employeeRepository.save(new Employee("Nume Prenume3", "role 3"));
            log.info("Preloading Nume Prenume 4");
            employeeRepository.save(new Employee("Nume Prenume4", "role 4"));
            log.info("Preloading Nume Prenume 5 " + employeeRepository.
                    save(new Employee("Nume Prenume5", "role 5")));

            log.info("Preloading Order  1");
            orderRepository.save(new Orders("Descrierea1", OrderStatus.IN_PROGRESS));
            log.info("Preloading Order  2");
            orderRepository.save(new Orders("Descrierea2", OrderStatus.IN_PROGRESS));
            log.info("Preloading Order  3");
            orderRepository.save(new Orders("Descrierea3", OrderStatus.IN_PROGRESS));
            log.info("Preloading Order  4");
            orderRepository.save(new Orders("Descrierea4", OrderStatus.COMPLETED));
            log.info("Preloading Order  5 " + orderRepository.
                    save(new Orders("Descrierea5", OrderStatus.CANCELED)));
        };
    }
}
