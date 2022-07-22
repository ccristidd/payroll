package com.payroll.controller;

import com.payroll.exceptions.EmployeeNotFoundException;
import com.payroll.model.Employee;
import com.payroll.modelassembler.EmployeeModelAssembler;
import com.payroll.repository.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/emps")
    public CollectionModel<EntityModel<Employee>> getEmployess() {
        //lista de employee trebuie sa o convertesc in List<EntityModel<Employee>>
        List<EntityModel<Employee>> entityModelEmployeeList = employeeRepository.findAll().stream()
                //.map(employee -> employeeModelAssembler.toModel(employee))
                //randul de deasupra se mai poate scrie ca mai jos daca interfata este functionala
                .map(employeeModelAssembler::toModel)
//                        EntityModel.of(employee, linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel()))
                .collect(Collectors.toList());
        //lista convertita acum trebuie returnata in CollectionModel
        return CollectionModel.of(entityModelEmployeeList, linkTo(methodOn(EmployeeController.class).getEmployess()).withSelfRel());
    }

    @GetMapping("/emps/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable Long id) {
//        if(employeeRepository.findById(id).isPresent()){
//            return new ResponseEntity<>("the employee was found" + employeeRepository.findById(id), HttpStatus.OK);
//        }else return new ResponseEntity<>("the employee was not found", HttpStatus.BAD_REQUEST);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
//        return EntityModel.of(employee,
//                linkTo(methodOn(EmployeeController.class).getEmployeeById(id)).withSelfRel(),
//                linkTo(methodOn(EmployeeController.class).getEmployess()).withRel("emps"));
        return employeeModelAssembler.toModel(employee);
    }

    @PostMapping("/emps")
    public EntityModel<Employee> postEmployee(@RequestBody Employee employeeToAdd) {
        employeeRepository.save(employeeToAdd);
        return EntityModel.of(employeeToAdd,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(employeeToAdd.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getEmployess()).withSelfRel());
    }

    @PutMapping("/emps/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = employeeRepository.findById(id)
                .map(employee ->
                {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);

                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });
        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(updatedEmployee);
//        return entityModel;
        //the return type of ResponseEntity gives us a more detaild http response than EntityModel
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/emps/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        EntityModel<?> entityModel = employeeModelAssembler.toModel(employeeRepository.findById(id).get());
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(entityModel + " was deleted", HttpStatus.OK);
    }
}
