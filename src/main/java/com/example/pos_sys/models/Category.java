package com.example.pos_sys.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@jakarta.persistence.Table(name = "tb_categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "category_name",nullable = false,length = 50)
    @NotBlank
    @Size(max=50, message="Catgory name must be under 50 character")
    private String category_name;

    /*

        //if no lombok library must be use property (get,set)

        public void setCategory_name(String category){
            this.category_name = category;
        }

        public String getCatogry_name(){
            return category_name;
        }
    
    */

}