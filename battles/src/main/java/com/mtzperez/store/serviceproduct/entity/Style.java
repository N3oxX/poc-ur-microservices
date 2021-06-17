package com.mtzperez.store.serviceproduct.entity;

import com.mtzperez.store.serviceproduct.model.Score;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "styles")
public class Style {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;

    private Long styleOrder;
    private Long styleTotal;

    private Long patterns;

    @NotEmpty(message = "El nombre no debe ser vacio")
    private String name;
    private String description;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Transient
    private List<Score> scores;



}
