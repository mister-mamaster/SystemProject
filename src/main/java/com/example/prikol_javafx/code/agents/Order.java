package com.example.prikol_javafx.code.agents;

import com.example.prikol_javafx.code.agents.resources.OrderR;
import com.example.prikol_javafx.code.world.World;

import java.util.Arrays;

public class Order extends Agent<OrderR>{
    private int[] location = new int[2];

    public Order(OrderR resource, int[] location) {
        super(resource);
        this.location = location;
    }

    @Override
    public void tick(World world) {

    }

    public int[] getLocation() {
        return location;
    }

    @Override
    public boolean proposal(Agent origin, World world) {
        switch (String.valueOf(origin.getClass())){
            case "class com.example.prikol_javafx.code.agents.Courier": {
                if(!this.haveLink("class com.example.prikol_javafx.code.agents.Courier")){
                    if(this.contentment < contentmentSize(origin)) {
                        this.contentment = this.contentmentSize(origin);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int contentmentSize(Agent origin) {
        int size = 0;
        switch (String.valueOf(origin.getClass())){
            case "class com.example.prikol_javafx.code.agents.Courier": {
                if(!this.haveLink("class com.example.prikol_javafx.code.agents.Courier")) {
                    Courier courier = (Courier) origin;
                    if (courier.haveLink("class com.example.prikol_javafx.code.agents.Way")) {
                        Way way = (Way) courier.links_list.get("class com.example.prikol_javafx.code.agents.Way").get(0);
                        double length = way.getLength();
                        length += (Math.abs(this.location[0] - way.getWay().peekFirst()[0]) + Math.abs(this.location[1] - way.getWay().peekFirst()[1]));
                        size = (int) (100 - ((length / (double) courier.getResource().getSpeed()) * 10));
                    } else {
                        double length = Math.abs(this.location[0] - courier.getLocation()[0]) + Math.abs(this.location[1] - courier.getLocation()[1]);
                        size = (int) (100 - ((length / (double) courier.getResource().getSpeed()) * 10));
                    }
                }
            }
        }
        return (int)(( sigmoid(size)) * 100);
    }

    public String toStringWithLinks() {
        return "Order{" +
                "balance=" + balance +
                ", resource=" + resource +
                ", contentment=" + contentment +
                ", links_list=" + links_list +
                '}';
    }

    @Override
    public String toString() {
        return "Order{" +
                "balance=" + balance +
                ", resource=" + resource +
                ", contentment=" + contentment +
                ", location=" + Arrays.toString(location) +
                '}';
    }
}
