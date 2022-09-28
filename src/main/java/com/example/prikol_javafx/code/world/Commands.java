package com.example.prikol_javafx.code.world;


import com.example.prikol_javafx.code.agents.Courier;
import com.example.prikol_javafx.code.agents.Order;
import com.example.prikol_javafx.code.agents.resources.OrderR;

public class Commands {
    private World world;

    public Commands(World world) {
        this.world = world;
    }

    public void input(String in){
        String[] splitIn = in.split(" ");
        switch (splitIn[0]){
            case "add":
                switch (splitIn[1]){
                    case "courier": {
                        int[] location = {Integer.parseInt(splitIn[2]), Integer.parseInt(splitIn[3])};
                        this.world.setNewAgent(new Courier(splitIn[4], location));
                    }
                    break;
                    case "order": {
                        int[] location = {Integer.parseInt(splitIn[2]), Integer.parseInt(splitIn[3])};
                        this.world.setNewAgent(new Order(new OrderR(), location));
                    }
                    break;
                }
                break;

            case "wait":
                this.world.tick(Integer.parseInt(splitIn[1]));
            break;

        }
    }
}
