package com.project.figureout.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Produtos")
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id")
    private long id;

    @Column(name = "pro_ativo")
    private boolean active = true;

    @Column(name = "pro_nome")
    private String name;

    @Column(name = "pro_descricao")
    private String description;

    @Column(name = "pro_altura")
    private Float height;

    @Column(name = "pro_largura")
    private Float width;

    @Column(name = "pro_peso")
    private Float weight;

    @Column(name = "pro_comprimento")
    private Float length;

    @Column(name = "pro_valor_compra")
    private BigDecimal purchaseAmount;

    @ManyToMany
    @JoinTable(
            name = "ProdutosCategorias",
            joinColumns = @JoinColumn(name = "pro_id"),
            inverseJoinColumns = @JoinColumn(name = "cat_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductsActivation> productActivations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "pro_gdp_id")
    private PricingGroup pricingGroup;

    @Column(name = "pro_preco_venda")
    private BigDecimal price;

    @Lob
    @Column(name = "pro_foto", columnDefinition = "MEDIUMBLOB") // 16mb max size, .JPG/JPEG only
    @JsonIgnore
    private byte[] picture;

    @ManyToOne
    @JoinColumn(name = "pro_fab_id")
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "pro_tam_id")
    private Size size;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;

}
