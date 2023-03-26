package com.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;

@NamedQuery(name = "Subject.getAllSubject", query = "select s from Subject s")

/*@NamedQuery(name = "Subject.getAllSubject", query = "select s from Subject s where s.id in (select w.subject " +
        "from semesterproject w where w.status='true')")*/
//vyber všetko z tabuľka
//po úprave query nám vytiahne iba tie kategórie, ktoré boli použité pri vkladaní produktu (tam je to FK)


@Data
//@Data - táto anotácia sa nám stara o gettery a settery, čiže nemusíme ich vobec pisat a vieme ich pouziť
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "subject")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;

}
