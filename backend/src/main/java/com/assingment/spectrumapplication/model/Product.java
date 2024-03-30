package com.assingment.spectrumapplication.model;

import com.assingment.spectrumapplication.model.super_classes.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    private String productName;
    private String voucherNo;
    private Float price;

}
