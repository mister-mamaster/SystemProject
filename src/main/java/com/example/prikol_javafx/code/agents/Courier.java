package com.example.prikol_javafx.code.agents;

import com.example.prikol_javafx.code.agents.resources.CourierR;
import com.example.prikol_javafx.code.world.World;

import java.util.*;

public class Courier extends Agent<CourierR>{
    private final String transport;
    private int[] location = new int[2];


    public Courier(String transport, int[] location) {
        super(new CourierR(transport));
        this.transport = transport;
        this.location = location;
    }

    @Override
    public void tick(World world) {
        for (String key : links_list.keySet()) {
            if (key.equals("class com.example.prikol_javafx.code.agents.Way")) {
                for (Agent agent : links_list.get(key)) {

                    Way agentWay = (Way) agent;
                    Deque<int[]> way = agentWay.getWay();

                    if (way.size() > 0) {
                        int resource = this.resource.getSpeed();
                        if (this.location[0] != way.peekFirst()[0] || this.location[1] != way.peekFirst()[1]) {
                            if (resource > Math.abs(this.location[0] - way.peekFirst()[0])) {
                                resource -= Math.abs(this.location[0] - way.peekFirst()[0]);
                                this.location[0] = way.peekFirst()[0];
                                if (resource > Math.abs(this.location[1] - way.peekFirst()[1])) {
                                    resource -= Math.abs(this.location[1] - way.peekFirst()[1]);
                                    this.location[1] = way.peekFirst()[1];
                                } else {
                                    if (this.location[1] < way.peekFirst()[1]) {
                                        this.location[1] += resource;
                                        resource = 0;
                                    }
                                    if (this.location[1] > way.peekFirst()[1]) {
                                        this.location[1] -= resource;
                                        resource = 0;
                                    }
                                }
                            } else {
                                if (this.location[0] < way.peekFirst()[0]) {
                                    this.location[0] += resource;
                                    resource = 0;
                                }
                                if (this.location[0] > way.peekFirst()[0]) {
                                    this.location[0] -= resource;
                                    resource = 0;
                                }
                            }
                        }

                        if (this.location[0] == way.peekFirst()[0] && this.location[1] == way.peekFirst()[1]) {
                            way.removeFirst();
                            ((Way) agent).setWay(way);
                            if(way.size() == 0){
                                links_list.remove(String.valueOf(agent.getClass()));
                                agent.delete();
                                world.deleteAgent(agent);
                            }
                            for (String key1: links_list.keySet()) {
                                if (key1.equals("class com.example.prikol_javafx.code.agents.Order")) {
                                    Iterator<Agent> linksIter = links_list.get(key1).iterator();
                                    while(linksIter.hasNext()) {
                                        Agent order = linksIter.next();
                                        if (this.location[0] == ((Order) order).getLocation()[0] && this.location[1] == ((Order) order).getLocation()[1]) {
                                            contentment -= this.contentmentSize(order);
                                            if (contentment < 0) contentment = 0;
                                            linksIter.remove();
                                            world.deleteAgent(order);
                                            order = null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public int[] getLocation() {
        return location;
    }

    @Override
    public boolean proposal(Agent origin, World world){
        switch (String.valueOf(origin.getClass())){
            case "class com.example.prikol_javafx.code.agents.Order": {
                if(!this.haveLink("class com.example.prikol_javafx.code.agents.Order")){
                    this.contentment = this.contentmentSize(origin);
                    return true;
                }
                if(this.contentmentSize(origin) + contentment > contentment && this.links_list.get("class com.example.prikol_javafx.code.agents.Order").size() < 3){
                    this.contentment = this.contentmentSize(origin);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int contentmentSize(Agent origin){
        int size = 0;
        switch (String.valueOf(origin.getClass())){
            case "class com.example.prikol_javafx.code.agents.Order": {
                if(this.haveLink("class com.example.prikol_javafx.code.agents.Way")){
                    Way way = (Way) this.links_list.get("class com.example.prikol_javafx.code.agents.Way").get(0);
                    int[] position = way.getWay().peekFirst();
                    size = 100 - ((Math.abs(origin.getLocation()[0] - this.location[0]) + Math.abs(origin.getLocation()[1] - this.location[1])) / 2);
                } else {
                    size = 100 - ((Math.abs(origin.getLocation()[0] - this.location[0]) + Math.abs(origin.getLocation()[1] - this.location[1])) / 2);
                }
            }
            break;
        }
        return (int)((sigmoid(size)) * 100);
    }



    public String toStringWithLinks() {
        return "Courier{" +
                "balance=" + balance +
                ", resource=" + resource +
                ", contentment=" + contentment +
                ", links_list=" + links_list +
                ", transport='" + transport + '\'' +
                '}';
    }
}
