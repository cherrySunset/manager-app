package com.example.managerapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private Boolean completed;
    @ManyToOne
    private User assignedTo;

    @OneToOne(cascade = CascadeType.ALL)
    private Deadline deadline;

    @ManyToOne
    @JsonBackReference
    private Project project;

}
