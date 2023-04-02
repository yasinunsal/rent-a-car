package kodlama.io.rentacar.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//Lombok
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand {
    @Id // Primary Key -> PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Model> models;
}

