package com.neha.employeemanagementsystem.controller;

import com.neha.employeemanagementsystem.entity.Employee;
import com.neha.employeemanagementsystem.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
public class DashboardController {

    private final EmployeeService employeeService;

    public DashboardController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Employee> employees = employeeService.getAllEmployees();

        // Total employees
        int totalEmployees = employees.size();

        // Total departments
        long totalDepartments = employees.stream()
                .map(Employee::getDepartment)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        // Average salary
        BigDecimal averageSalary = employees.stream()
                .map(Employee::getSalary)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalEmployees > 0) {
            averageSalary = averageSalary
                    .divide(BigDecimal.valueOf(totalEmployees), 2, java.math.RoundingMode.HALF_UP);
        }

        // Recent employees
        List<Employee> recentEmployees = employees.stream()
                .limit(5)
                .toList();
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("averageSalary", "₹" + averageSalary);
        model.addAttribute("recentEmployees", recentEmployees);

        return "employees/dashboard/dashboard";
    }
}