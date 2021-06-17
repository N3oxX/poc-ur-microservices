package com.mtzperez.store.serviceproduct.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "rounds")
public class Round {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private Long roundOrder;
    private Long roundTotal;

    @NotEmpty(message = "El nombre no debe ser vacio")
    private String name;
    private String description;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Valid
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private List<Style> styles;



}
