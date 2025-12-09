package com.example.lab6.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Student buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Student seller;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Student getBuyer() {
        return buyer;
    }

    public void setBuyer(Student buyer) {
        this.buyer = buyer;
    }

    public Student getSeller() {
        return seller;
    }

    public void setSeller(Student seller) {
        this.seller = seller;
    }
}

