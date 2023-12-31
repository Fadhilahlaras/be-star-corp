package coba.star.corp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coba.star.corp.model.dto.ProductDto;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = Product.TABLE_NAME)
@Data
public class Product {
    public static final String TABLE_NAME = "t_product";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME)
    @SequenceGenerator(name = TABLE_NAME, sequenceName = "t_product_seq")
    private Integer id;

    private String productName;

    private Integer stock;

    private Double price;

    private String pictureUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    private ProductCategory productCategory;
    @Column(name = "id_category", nullable = false)
    private Integer idCategory;

}
