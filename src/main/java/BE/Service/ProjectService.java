package BE.Service;

import BE.Model.Employee;
import BE.Model.Project;
import BE.Repository.EmployeeRepository;
import BE.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }
    public boolean projectExists(Long id){
        return projectRepository.existsById(id);
    }
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }
    public Project getProjectById(Long id){
        return projectRepository.findById(id).orElse(null);
    }
    public Project saveProject(Project project){
        return projectRepository.save(project);
    }
    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }
    public List<Employee> getEmployeesByProject(Project project){
        return project.getEmployees();
    }
    public List<Employee> getEmployeeByIds(List<Long> employeeIds){
        return employeeRepository.findAllById(employeeIds);
    }
    public double monthlyProjectCost(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bad project Id" + id));
        double totalSalary = project.getEmployees().stream()
                .mapToDouble(Employee :: getSalary).sum();
        return project.getMonthlyCost() + totalSalary;
    }
}
