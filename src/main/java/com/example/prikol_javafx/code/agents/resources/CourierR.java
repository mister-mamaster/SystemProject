package com.example.prikol_javafx.code.agents.resources;

public class CourierR implements Resources {
    private int speed;

    public CourierR(String transport) {
        switch (transport){
            case "legs": this.speed = 5;
            break;
            case "bike": this.speed = 15;
            break;
            case "car": this.speed = 30;
        }
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "CourierR{" +
                "speed=" + this.speed +
                '}';
    }
}
