package com.yashwanthmk.employeeservice.service;

import com.yashwanthmk.employeeservice.dto.EmployeeRequest;
import com.yashwanthmk.employeeservice.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
}

