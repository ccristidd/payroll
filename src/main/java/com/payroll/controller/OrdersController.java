package com.payroll.controller;

import com.payroll.exceptions.EmployeeNotFoundException;
import com.payroll.model.OrderStatus;
import com.payroll.model.Orders;
import com.payroll.modelassembler.OrderModelAssembler;
import com.payroll.repository.OrderRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrdersController {
    OrderRepository orderRepository;
    OrderModelAssembler orderModelAssembler;

    public OrdersController(OrderRepository orderRepository, OrderModelAssembler orderModelAssembler) {
        this.orderRepository = orderRepository;
        this.orderModelAssembler = orderModelAssembler;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Orders>> getOrders() {
        List<EntityModel<Orders>> collectionModel = orderRepository.findAll().stream()
                .map(order -> orderModelAssembler.toModel(order))
                .collect(Collectors.toList());
        return CollectionModel.of(collectionModel,
                linkTo(methodOn(OrdersController.class).getOrders()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Orders> getOrdersById(@PathVariable Long id) {
        return EntityModel.of(orderRepository.findById(id)
                        .orElseThrow(() -> new EmployeeNotFoundException(id)),
                linkTo(methodOn(OrdersController.class).getOrdersById(id)).withSelfRel(),
                linkTo(methodOn(OrdersController.class).getOrders()).withRel("/orders"));
    }

    @PostMapping("/orders")
    public EntityModel<?> addOrder(@RequestBody Orders order){
        Orders existingOrder = orderRepository.findById(order.getId()).orElse(null);
        if(existingOrder != null){
            existingOrder.setStatus(order.getStatus());
            existingOrder.setDescription(order.getDescription());
            orderRepository.save(existingOrder);
            return orderModelAssembler.toModel(existingOrder);
        }
        return orderModelAssembler.toModel(orderRepository.save(order));
    }



    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Orders orderToBeCanceled = orderRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        if (orderToBeCanceled.getStatus() == OrderStatus.IN_PROGRESS) {
            orderToBeCanceled.setStatus(OrderStatus.CANCELED);
            orderRepository.save(orderToBeCanceled);
            return ResponseEntity.ok(orderModelAssembler.toModel(orderToBeCanceled));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method Not Allowed")
                        .withDetail("You can't cancel an order that is in the " + orderToBeCanceled.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Orders orderToBeCompleted = orderRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        if(orderToBeCompleted.getStatus() == OrderStatus.IN_PROGRESS){
            orderToBeCompleted.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(orderToBeCompleted);
            return ResponseEntity.ok(orderModelAssembler.toModel(orderToBeCompleted));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Problem.create()
                        .withTitle("Order is not in progress")
                        .withDetail("order status cannot be changed"));
    }
}
