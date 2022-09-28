package com.example.prikol_javafx.code.agents;

import com.example.prikol_javafx.code.world.World;

import java.util.*;

public abstract class Agent<T> {
    protected double balance;
    protected T resource;
    protected int contentment;
    protected Map<String, ArrayList<Agent>> links_list = new HashMap<>();

    public Agent(T resource) {
        this.balance = 200;
        this.resource = resource;
        this.contentment = 20;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public T getResource() {
        return resource;
    }

    public int getContentment() {
        return contentment;
    }

    public void setContentment(int contentment) {
        this.contentment = contentment;
    }

    public void setLinks_list(Map<String, ArrayList<Agent>> links_list) {
        this.links_list = links_list;
    }

    public Map<String, ArrayList<Agent>> getLinks_list() {
        return links_list;
    }

    public void setNewLink(Agent agent, boolean flag) {
        if(!links_list.containsKey(String.valueOf(agent.getClass()))) {
            ArrayList<Agent> agents = new ArrayList<>();
            agents.add(agent);
            links_list.put(String.valueOf(agent.getClass()), agents);
        } else {links_list.get(String.valueOf(agent.getClass())).add(agent);}
        if(flag) agent.setNewLink(this, false);
    }

    public abstract void tick(World world);

    public boolean haveLink(String oClass) {
        for(String key: links_list.keySet()){
            if(key.equals(oClass) && links_list.get(oClass).size() > 0) return true;
        }
        return false;
    }

    public boolean haveLink(Agent agent){
        for(String key: links_list.keySet()){
            for(Agent link: links_list.get(key)){
                if(agent.equals(link)) return true;
            }
        }
        return false;
    }

    public abstract int[] getLocation();

    public abstract boolean proposal(Agent origin, World world);

    public abstract int contentmentSize(Agent origin);

    @Override
    public String toString() {
        return  this.getClass() + "{" +
                "balance=" + balance +
                ", resource=" + resource +
                ", contentment=" + contentment +
                '}';
    }

    public void delete(){
        for(String key: this.links_list.keySet()){
            for(Agent agent: this.links_list.get(key)){
                for(Object agentKey: agent.getLinks_list().keySet()){
                    ArrayList<Agent> agentList = (ArrayList<Agent>) agent.getLinks_list().get(agentKey);
                    agentList.remove(this);
                }
            }
        }
        Iterator<ArrayList<Agent>> arrayListIterator = links_list.values().iterator();
        while(arrayListIterator.hasNext()){

            Iterator<Agent> agentIterator = arrayListIterator.next().iterator();
            while(agentIterator.hasNext()){
                agentIterator.next();
                agentIterator.remove();
            }
        }


    }

    public static double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, (-1 * (x / 25))));
    }
}
