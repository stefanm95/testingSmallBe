package BE.Controller;

import BE.Model.Employee;
import BE.Model.Project;
import BE.Service.EmployeeService;
import BE.Service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {
    private final ProjectService projectService;
    private final EmployeeService employeeService;
    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectService.getProjectById(id);
    }
    @GetMapping("/{id}/monthly-cost")
    public ResponseEntity<String> calculateMonthlyCost(@PathVariable Long id){
        double monthlyCost = projectService.monthlyProjectCost(id);
        return ResponseEntity.ok("Costul lunar al proiectului cu id " + id + " este de: " + monthlyCost);
    }
    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByProject(@PathVariable Long id){
        Project project = projectService.getProjectById(id);
        if(project != null){
            return projectService.getEmployeesByProject(project);
        }
        return null;
    }
    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody Project project){
        List<Employee> employees = employeeService.getEmployeesByIds(
                project.getEmployees().stream()
                        .map(Employee:: getId)
                        .collect(Collectors.toList())
        );
        project.setEmployees(employees);
        Project savedProject = projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        boolean projectExists = projectService.projectExists(id);
        if(!projectExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project no found");
        }
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body("Project deleted");
    }
}
