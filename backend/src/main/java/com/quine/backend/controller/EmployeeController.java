    package com.quine.backend.controller;

    import com.quine.backend.model.Employee;
    import com.quine.backend.service.EmployeeService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;

    @RestController
    @RequestMapping("/api/v1/employees")
    @CrossOrigin(origins = "*")
    public class EmployeeController {

        @Autowired
        private EmployeeService employeeService;

        @PostMapping
        public Employee addEmployee(@RequestBody Employee employee) {
            return employeeService.addEmployee(employee);
        }

        @GetMapping
        public List<Employee> getAllEmployees() {
            return employeeService.getAllEmployees();
        }

        @GetMapping("/{id}")
        public Employee getEmployeeById(@PathVariable String id ){

            return employeeService.findEmpById(id);
        }
    }