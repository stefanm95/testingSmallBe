package BE.Repository;

import BE.Model.Employee;
import BE.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByProject(Project project);
    List<Employee> findByHireDateBefore(LocalDate hireDate);
//    List<Employee> findAllByOrderByDisplayOrderAsc();
}
