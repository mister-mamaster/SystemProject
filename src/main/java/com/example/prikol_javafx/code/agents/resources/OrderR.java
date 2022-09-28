package com.example.prikol_javafx.code.agents.resources;

public class OrderR implements Resources{
    private int resource;

    public OrderR(){
        this.resource = 3;
    }

    public OrderR(int resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "CourierN{" +
                "Resource=" + this.resource +
                '}';
    }
}
