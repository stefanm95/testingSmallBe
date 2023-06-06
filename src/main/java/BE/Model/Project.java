package BE.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double monthlyCost;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties("project")
    private List<Employee> employees;
    public Project(){
        this.employees = new ArrayList<>();
    }
}
