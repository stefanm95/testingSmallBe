package BE.Service;

import BE.Model.Employee;
import BE.Model.Project;
import BE.Repository.EmployeeRepository;
import BE.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    public EmployeeService(EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    public boolean employeeExists(Long id){
        return employeeRepository.existsById(id);
    }
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }
    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id).orElse(null);
    }
    public List<Employee> saveEmployee(List<Employee> employees){
        return employeeRepository.saveAll(employees);
    }
    public List<Employee> getEmployeesByProject(Project project){
        return employeeRepository.findByProject(project);
    }
    public List<Employee> getEmployeesByIds(List<Long> employeeIds){
        return employeeRepository.findAllById(employeeIds);
    }
    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }

    //bonus task
    @Transactional
    public void increaseSalaryByHireDate(LocalDate hireDate, double percentage){
        List<Employee> employees = employeeRepository.findByHireDateBefore(hireDate);
        for(Employee employee: employees){
            double currentSalary = employee.getSalary();
            double increaseAmount = currentSalary * (percentage /100);
            double newSalary = currentSalary + increaseAmount;
            employee.setSalary(newSalary);
            employeeRepository.save(employee);
        }
    }
    public Employee updateEmployeeProject(Long id, Long projectId) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);
        if(employee != null && project != null){
            employee.setProject(project);
            employeeRepository.save(employee);
            return employee;
        }else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
    }
}
