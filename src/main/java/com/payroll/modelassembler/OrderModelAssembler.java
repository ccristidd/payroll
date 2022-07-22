package com.payroll.modelassembler;

import com.payroll.controller.OrdersController;
import com.payroll.model.OrderStatus;
import com.payroll.model.Orders;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Orders, EntityModel<Orders>> {
    @Override
    public EntityModel<Orders> toModel(Orders order) {
        // Unconditional links to single-item resource and aggregate root
        EntityModel<Orders> entityModel = EntityModel.of(order,
                linkTo(methodOn(OrdersController.class).getOrders()).withSelfRel(),
                linkTo(methodOn(OrdersController.class).getOrdersById(order.getId())).withRel("orders"));

        // Conditional links based on state of the order
        if(order.getStatus() == OrderStatus.IN_PROGRESS){
            entityModel.add(linkTo(methodOn(OrdersController.class).cancel(order.getId())).withRel("cancel"),
                    linkTo(methodOn(OrdersController.class).complete(order.getId())).withRel("complete"));
        }

        return entityModel;

    }
}
