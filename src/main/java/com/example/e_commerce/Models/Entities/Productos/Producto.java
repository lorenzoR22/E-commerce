package com.example.e_commerce.Models.Entities.Productos;

import com.example.e_commerce.Models.Entities.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id",nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "producto",cascade = CascadeType.ALL)
    private Set<ProductoImg> imagenes;

    private Integer stock;

    private Double precio;

    public Producto(String nombre, String descripcion, Integer stock, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = new Categoria();
        this.stock = stock;
        this.precio = precio;
        this.imagenes=new HashSet<>();
    }
    public void addImg(ProductoImg productoImg){
        this.imagenes.add(productoImg);
    }
    public Integer restarStock(){
        if(this.stock>0){
            return this.stock-=1;
        }
        return 0;
    }
    public Integer sumarStock(){
        if(this.stock>0){
            return this.stock+=1;
        }
        return 0;
    }
}
