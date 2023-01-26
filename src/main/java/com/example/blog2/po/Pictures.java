package com.example.blog2.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @author mxp
 * @date 2023/1/24 19:25
 */
@Entity
@Table(name = "t_pictures")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@Data
public class Pictures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private Boolean isUse;
}
