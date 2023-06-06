package BE.Controller;

import BE.Model.Employee;
import BE.Service.EmployeeService;
import BE.Service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public EmployeeController(EmployeeService employeeService, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody List<Employee> employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        List<Employee> savedEmployees = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(savedEmployees);
    }

    //Bonus task
    @PutMapping("/increase-salary")
    public ResponseEntity<String> increaseSalaryByDate(@RequestParam("hireDate") String hireDateStr, @RequestParam("percentage") double percentage) {
        try {
            LocalDate hireDate = LocalDate.parse(hireDateStr);
            if (percentage <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Percentage must be positive.");
            }
            employeeService.increaseSalaryByHireDate(hireDate, percentage);
            return ResponseEntity.ok("Salaries increased successfully.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid hire date format. Please provide the date in the format yyyy-MM-dd.");
        }
    }

    @PutMapping("/{id}/project/{projectId}")
    public ResponseEntity<?> updateEmployeeProject(@PathVariable Long id, @PathVariable Long projectId) {
        if (id == null || projectId == null) {
            return ResponseEntity.badRequest().body("Invalid employee or project id");
        }
        if (!employeeService.employeeExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        }
        Employee updatedEmployee = employeeService.updateEmployeeProject(id, projectId);
        if (updatedEmployee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            if (!employeeService.employeeExists(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
            }

            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete employee.");
        }
    }
}