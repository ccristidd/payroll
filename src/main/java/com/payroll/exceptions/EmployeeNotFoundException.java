package com.payroll.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
   public EmployeeNotFoundException(Long id){
        super("could not find the employee " + id);
    }
}
