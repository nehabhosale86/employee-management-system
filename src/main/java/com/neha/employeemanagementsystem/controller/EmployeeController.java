package com.neha.employeemanagementsystem.controller;

import com.neha.employeemanagementsystem.entity.Employee;
import com.neha.employeemanagementsystem.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            Model model) {

        if (department != null && !department.trim().isEmpty()) {
            model.addAttribute("employees", employeeService.filterByDepartment(department));
        } else if (keyword == null || keyword.trim().isEmpty()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
        } else {
            model.addAttribute("employees", employeeService.searchEmployees(keyword));
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("department", department);
        return "employees/list";
    }

    @GetMapping("/new")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + id));

        model.addAttribute("employee", employee);
        return "employees/form";
    }
    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {

        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + id));

        model.addAttribute("employee", employee);

        return "employees/details";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}