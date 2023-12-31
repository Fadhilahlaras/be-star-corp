package coba.star.corp.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = ProductCategory.TABLE_NAME)
@Data
public class ProductCategory {
    public static final String TABLE_NAME = "t_product_category";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME)
    @SequenceGenerator(name = TABLE_NAME, sequenceName = "t_category_product_seq")
    @Column(name = "id_category")
    private Integer id;

    @Column(name = "category_name")
    private String categoryName;

}
