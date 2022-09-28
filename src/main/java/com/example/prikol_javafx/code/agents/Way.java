package com.example.prikol_javafx.code.agents;

import com.example.prikol_javafx.code.agents.resources.OrderR;
import com.example.prikol_javafx.code.agents.resources.WayR;
import com.example.prikol_javafx.code.world.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Way extends Agent<WayR>{
    private int[] start = new int[2];
    private double length = 0;
    private Deque<int[]> way = new ArrayDeque<>();


    public Way(int[] start, ArrayList<Order> orders) {
        super(new WayR());
        this.start = start;
        ArrayList<Order> wayOrders = (ArrayList<Order>) orders.clone();

        if(true){ // Необходимо для локальной переменной снизу
            Agent min = new Order(new OrderR(), new int[]{1000, 1000});
            for(Agent order: wayOrders){
                if(Math.pow(Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.start[0])) - Double.parseDouble(String.valueOf(min.getLocation()[0]))), 2.0) + Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.start[1])) - Double.parseDouble(String.valueOf(min.getLocation()[1]))), 2.0), 0.5)
                    >
                        Math.pow(Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.start[0])) - Double.parseDouble(String.valueOf(order.getLocation()[0]))), 2.0) + Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.start[1])) - Double.parseDouble(String.valueOf(order.getLocation()[1]))), 2.0), 0.5)){
                    min = order;
                }
            }
            this.length += Math.pow(Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.start[0])) - Double.parseDouble(String.valueOf(min.getLocation()[0]))), 2.0) + Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.start[1])) - Double.parseDouble(String.valueOf(min.getLocation()[1]))), 2.0), 0.5);
            this.way.addLast(min.getLocation());
            ArrayList<Agent> orderList = new ArrayList<>();
            orderList.add(min);
            this.links_list.put(String.valueOf(min.getClass()), orderList);
            wayOrders.remove(wayOrders.indexOf(min));
        }

        while(wayOrders.size() != 0){
            Agent min = new Order(new OrderR(), new int[]{1000, 1000});
            for(Agent order: wayOrders){
                if(Math.pow(Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.way.peekLast()[0])) - Double.parseDouble(String.valueOf(min.getLocation()[0]))), 2.0) + Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.way.peekLast()[1])) - Double.parseDouble(String.valueOf(min.getLocation()[1]))), 2.0), 0.5)
                        >
                        Math.pow(Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.way.peekLast()[0])) - Double.parseDouble(String.valueOf(order.getLocation()[0]))), 2.0) + Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.way.peekLast()[1])) - Double.parseDouble(String.valueOf(order.getLocation()[1]))), 2.0), 0.5)) {
                    min = order;
                }
            }
            this.length += Math.pow(Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.way.peekLast()[0])) - Double.parseDouble(String.valueOf(min.getLocation()[0]))), 2.0) + Math.pow(Math.abs(Double.parseDouble(String.valueOf(this.way.peekLast()[1])) - Double.parseDouble(String.valueOf(min.getLocation()[1]))), 2.0), 0.5);
            this.way.addLast(min.getLocation());
            this.links_list.get(String.valueOf(min.getClass())).add(min);
            wayOrders.remove(wayOrders.indexOf(min));
        }
    }

    public void setWay(Deque<int[]> way) {
        this.way = way;
    }

    @Override
    public int[] getLocation() {
        return new int[0];
    }

    public double getLength() {
        return length;
    }

    public Deque<int[]> getWay() {
        return way;
    }

    public int[] getStart() {
        return start;
    }

    @Override
    public boolean proposal(Agent origin, World world) {
        this.contentment = contentmentSize(origin);
        return true;
    }

    @Override
    public int contentmentSize(Agent origin) {
        return 100;
    }

    @Override
    public void tick(World world) {

    }

    public boolean wayEquals(Way way){
        Deque<int[]> points1 = this.way;
        Deque<int[]> points2 = way.getWay();
        while(points1.size() != 0){
            if(points1.peekFirst()[0] != points2.peekFirst()[0] || points1.removeFirst()[1] != points2.removeFirst()[1]){
                return false;
            }
        }
        return true;
    }
}
