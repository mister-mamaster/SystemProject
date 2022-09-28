package com.example.prikol_javafx.code.world;



import com.example.prikol_javafx.code.agents.*;

import java.util.*;

public class World {
    private final int xSize = 51, ySize = 51;
    private Map<String, ArrayList<Agent>> agents = new HashMap<>();
    private int avrContentment;

    public World() {
        agents.put("class com.example.prikol_javafx.code.agents.Courier", new ArrayList<>());
        agents.put("class com.example.prikol_javafx.code.agents.Order", new ArrayList<>());
        agents.put("class com.example.prikol_javafx.code.agents.Way", new ArrayList<>());
    }

    public void setNewAgent(Agent nAgent){
        if(agents.containsKey(String.valueOf(nAgent.getClass()))){
            agents.get(String.valueOf(nAgent.getClass())).add(nAgent);
        } else {
            ArrayList<Agent> agentArrayList = new ArrayList<>();
            agentArrayList.add(nAgent);
            agents.put(String.valueOf(nAgent.getClass()), agentArrayList);
        }
        int contentment = 0, aCount = 0;
        for(String key: this.agents.keySet()){
            for(Agent agent: this.agents.get(key)){
                contentment += agent.getContentment();
                aCount++;
            }
        }
        this.avrContentment = contentment / aCount;
    }

    public void deleteAgent(Agent dAgent){
        this.agents.get(String.valueOf(dAgent.getClass())).remove(dAgent);
    }

    private void autoAdding(){
        for(Agent courier: agents.get("class com.example.prikol_javafx.code.agents.Courier")){
            int[] start = courier.getLocation();
            ArrayList<Order> orders;
            for(int i = 0; i < agents.get("class com.example.prikol_javafx.code.agents.Order").size(); i++){
                orders = new ArrayList<>();
                orders.add((Order) (agents.get("class com.example.prikol_javafx.code.agents.Order").get(i)));
                this.setNewAgent(new Way(start, orders));
                for(int j = i + 1; j < agents.get("class com.example.prikol_javafx.code.agents.Order").size(); j++){
                    for(int k = j; k < agents.get("class com.example.prikol_javafx.code.agents.Order").size(); k++){
                        orders.add((Order)agents.get("class com.example.prikol_javafx.code.agents.Order").get(k));
                        this.setNewAgent(new Way(start, orders));
                    }
                    orders = new ArrayList<>();
                    orders.add((Order) agents.get("class com.example.prikol_javafx.code.agents.Order").get(i));
                }
            }
        }
    }

    private void autoDeleting(){
        int size = agents.get("class com.example.prikol_javafx.code.agents.Way").size();
        for (int i = 0; i < size; i++){
            if(!(agents.get("class com.example.prikol_javafx.code.agents.Way").size() <= i)) {
                Agent way = agents.get("class com.example.prikol_javafx.code.agents.Way").get(i);
                Map<String, ArrayList<Agent>> wayMap = way.getLinks_list();
                ArrayList<Agent> courierList = wayMap.get("class com.example.prikol_javafx.code.agents.Courier");
                if (wayMap.size() < 2 || courierList == null || courierList.size() == 0) {
                    way.delete();
                    this.deleteAgent(way);
                    i--;
                }

            }
        }

        /*
        ArrayList<Agent> couriersList = agents.get("class com.example.prikol_javafx.code.agents.Courier");
        Iterator<Agent> courierIterator = couriersList.iterator();
        for (Agent courier: agents.get("class com.example.prikol_javafx.code.agents.Courier")) {
            ArrayList<Agent> wayList = (ArrayList<Agent>) courier.getLinks_list().get("class com.example.prikol_javafx.code.agents.Way");
            ListIterator<Agent> waysIterator = wayList.listIterator();
            while(waysIterator.hasNext()){
                ArrayList<Agent> courierList = (ArrayList<Agent>) courier.getLinks_list().get("class com.example.prikol_javafx.code.agents.Order");
                if(courierList.size() != ((Way) waysIterator.next()).getWay().size()){
                    waysIterator.previous();
                    this.deleteAgent(waysIterator.next());
                    waysIterator.previous();
                    waysIterator.next().delete();
                }
            }
        }

         */
    }

    public void bargain(){
        for(String key: agents.keySet()){
            switch (key) {
                case "class com.example.prikol_javafx.code.agents.Courier": {
                    for(Agent courier: agents.get(key)){
                        if(!courier.haveLink("class com.example.prikol_javafx.code.agents.Order")) {
                            for(Agent order: agents.get("class com.example.prikol_javafx.code.agents.Order")){
                                if(order.proposal(courier, this)) {
                                    courier.setContentment(courier.contentmentSize(order));
                                    courier.setNewLink(order, true);
                                    Map<String, ArrayList<Agent>> map = (Map<String, ArrayList<Agent>>) courier.getLinks_list();
                                    Way way = null;
                                    for(Agent agent: agents.get("class com.example.prikol_javafx.code.agents.Way")){
                                        Way way1 = (Way) agent;
                                        if(way1.getStart()[0] == courier.getLocation()[0] && way1.getStart()[1] == courier.getLocation()[1]) {
                                            ArrayList<Agent> orderList = (ArrayList<Agent>) courier.getLinks_list().get("class com.example.prikol_javafx.code.agents.Order");
                                            if (orderList.size() == way1.getWay().size()) {
                                                boolean flag = true, undflag = false;
                                                for (Agent order1: orderList) {
                                                    undflag = false;
                                                    for (int[] point : way1.getWay()) {
                                                        if (point[0] == order1.getLocation()[0] && point[1] == order1.getLocation()[1])
                                                            undflag = true;
                                                    }
                                                    flag = undflag;
                                                }
                                                if (flag) {
                                                    way = way1;
                                                    courier.setNewLink(agent, true);
                                                }
                                            }
                                        }
                                    }
                                    /*for(Agent order1: map.get("class com.example.prikol_javafx.code.agents.Order")){
                                        Map<String, ArrayList<Agent>> map2 = (Map<String, ArrayList<Agent>>) order1.getLinks_list();
                                        map2.get("class com.example.prikol_javafx.code.agents.Way").clear();
                                        map2.get("class com.example.prikol_javafx.code.agents.Way").add(way);
                                        order.setLinks_list(map2);
                                    }*/
                                    break;
                                }
                            }
                        }
                        Map<String, ArrayList<Agent>> map = (Map<String, ArrayList<Agent>>) courier.getLinks_list();
                        if(courier.haveLink("class com.example.prikol_javafx.code.agents.Way") && courier.haveLink("class com.example.prikol_javafx.code.agents.Order")) {
                            Way way = (Way) map.get("class com.example.prikol_javafx.code.agents.Way").get(0);
                            if (map.get("class com.example.prikol_javafx.code.agents.Order").size() != way.getWay().size()){
                                map.get("class com.example.prikol_javafx.code.agents.Way").clear();
                                for(Agent agent: agents.get("class com.example.prikol_javafx.code.agents.Way")){
                                    Way way1 = (Way) agent;
                                    if(way1.getStart()[0] == courier.getLocation()[0] && way1.getStart()[1] == courier.getLocation()[1]) {
                                        ArrayList<Agent> orderList = (ArrayList<Agent>) courier.getLinks_list().get("class com.example.prikol_javafx.code.agents.Order");
                                        if (orderList.size() == way1.getWay().size()) {
                                            boolean flag = true, undflag = false;
                                            for (Agent order : orderList) {
                                                undflag = false;
                                                for (int[] point : way1.getWay()) {
                                                    if (point[0] == order.getLocation()[0] && point[1] == order.getLocation()[1])
                                                        undflag = true;
                                                }
                                                flag = undflag;
                                            }
                                            if (flag) {
                                                way = way1;
                                                courier.setNewLink(agent, true);
                                            }
                                        }
                                    }
                                }
                                /*for(Agent order: map.get("class com.example.prikol_javafx.code.agents.Order")){
                                    Map<String, ArrayList<Agent>> map2 = (Map<String, ArrayList<Agent>>) order.getLinks_list();
                                    map2.get("class com.example.prikol_javafx.code.agents.Way").clear();
                                    map2.get("class com.example.prikol_javafx.code.agents.Way").add(way);
                                    order.setLinks_list(map2);
                                }*/
                            }
                        }
                        if(!courier.haveLink("class com.example.prikol_javafx.code.agents.Way") && courier.haveLink("class com.example.prikol_javafx.code.agents.Order")){
                            Way way = null;
                                for(Agent agent: agents.get("class com.example.prikol_javafx.code.agents.Way")){
                                    Way way1 = (Way) agent;
                                    if(way1.getStart()[0] == courier.getLocation()[0] && way1.getStart()[1] == courier.getLocation()[1]) {
                                        ArrayList<Agent> orderList = (ArrayList<Agent>) courier.getLinks_list().get("class com.example.prikol_javafx.code.agents.Order");
                                        if (orderList.size() == way1.getWay().size()) {
                                            boolean flag = true, undflag = false;
                                            for (Agent order : orderList) {
                                                undflag = false;
                                                for (int[] point : way1.getWay()) {
                                                    if (point[0] == order.getLocation()[0] && point[1] == order.getLocation()[1])
                                                        undflag = true;
                                                }
                                                flag = undflag;
                                            }
                                            if (flag) {
                                                way = way1;
                                                courier.setNewLink(agent, true);
                                            }
                                        }
                                    }
                                }
                                /*for(Agent order: map.get("class com.example.prikol_javafx.code.agents.Order")){
                                    Map<String, ArrayList<Agent>> map2 = (Map<String, ArrayList<Agent>>) order.getLinks_list();
                                    if(map2.containsKey("class com.example.prikol_javafx.code.agents.Way")) {
                                        map2.get("class com.example.prikol_javafx.code.agents.Way").clear();
                                        map2.get("class com.example.prikol_javafx.code.agents.Way").add(way);
                                    } else {
                                        ArrayList<Agent> agentArrayList = new ArrayList<>();
                                        agentArrayList.add(way);
                                        map2.put("class com.example.prikol_javafx.code.agents.Way", agentArrayList);
                                    }
                                    order.setLinks_list(map2);
                                }*/
                        }
                    }
                }
                break;
                case "class com.example.prikol_javafx.code.agents.Order": {
                    for(Agent order: agents.get(key)){
                        if(!order.haveLink("class com.example.prikol_javafx.code.agents.Courier")) {
                            for(Agent courier: agents.get("class com.example.prikol_javafx.code.agents.Courier")){
                                if(courier.proposal(order, this)) {
                                    order.setContentment(order.contentmentSize(courier));
                                    courier.setNewLink(order, true);
                                    Map<String, ArrayList<Agent>> map = (Map<String, ArrayList<Agent>>) order.getLinks_list();
                                    if(map.size() != 0) map.getOrDefault("class com.example.prikol_javafx.code.agents.Way", new ArrayList<>()).clear();
                                    order.setLinks_list(map);
                                    map = (Map<String, ArrayList<Agent>>) courier.getLinks_list();
                                    if(map.size() != 0) map.getOrDefault("class com.example.prikol_javafx.code.agents.Way", new ArrayList<>()).clear();
                                    courier.setLinks_list(map);
                                    for(Agent agent: agents.get("class com.example.prikol_javafx.code.agents.Way")){
                                        Way way = (Way) agent;
                                        if(way.getStart()[0] == courier.getLocation()[0] && way.getStart()[1] == courier.getLocation()[1]) {
                                            ArrayList<Agent> orderList = (ArrayList<Agent>) courier.getLinks_list().get("class com.example.prikol_javafx.code.agents.Order");
                                            if (orderList.size() == way.getWay().size()) {
                                                boolean flag = true, undflag = false;
                                                for (Agent order1: orderList) {
                                                    undflag = false;
                                                    for (int[] point : way.getWay()) {
                                                        if (point[0] == order1.getLocation()[0] && point[1] == order1.getLocation()[1])
                                                            undflag = true;
                                                    }
                                                    flag = undflag;
                                                }
                                                if (flag) {
                                                    courier.setNewLink(agent, true);
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        if(order.getContentment() < 20 && order.haveLink("class com.example.prikol_javafx.code.agents.Courier")) {
                            for (Agent courier: agents.get("class com.example.prikol_javafx.code.agents.Courier")) {
                                if(!order.haveLink(courier) && (order.contentmentSize(courier) > order.getContentment())) {
                                    if(courier.proposal(order, this)){
                                        ArrayList<Agent> courierList = (ArrayList<Agent>) order.getLinks_list().get("class com.example.prikol_javafx.code.agents.Courier");
                                        courierList.get(0).getLinks_list().remove("class com.example.prikol_javafx.code.agents.Order", order);
                                        order.getLinks_list().remove("class com.example.prikol_javafx.code.agents.Courier");
                                        order.setContentment(order.contentmentSize(courier));
                                        courier.setNewLink(order, true);
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

    public void tick(int count){
        for(; count > 0; count--) {
            this.autoAdding();
            this.bargain();
            this.autoDeleting();
            for (String key : agents.keySet()) {
                for (Agent agent : agents.get(key)) {
                    agent.tick(this);
                }
            }
        }
        int contentment = 0, aCount = 0;
        for(String key: this.agents.keySet()){
            if(!Objects.equals(key, "class com.example.prikol_javafx.code.agents.Way")) {
                for (Agent agent : this.agents.get(key)) {
                    contentment += agent.getContentment();
                    aCount++;
                }
            }
        }
        this.avrContentment = contentment / aCount;
    }

    public void print() {
        System.out.print('/');
        for(int i = 0; i < xSize; i++){
            System.out.print('-');
        }
        System.out.println('\\');

        for(int i = 0; i < ySize; i++){
            System.out.print('|');
            for(int j = 0; j < xSize; j++) {
                boolean flag = false;
                for(String key: agents.keySet()){
                    for(Agent agent: agents.get(key)){
                        if (agent.getLocation().length > 0) {
                            if (i == agent.getLocation()[1] && j == agent.getLocation()[0]) {
                                flag = true;
                                switch (String.valueOf(agent.getClass())) {
                                    case "class com.example.prikol_javafx.code.agents.Courier": {
                                        System.out.print("C");
                                        j++;
                                    }
                                    break;
                                    case "class com.example.prikol_javafx.code.agents.Order": {
                                        System.out.print("O");
                                        j++;
                                    }
                                }
                            }
                        }
                    }
                }
                if(!flag){
                    System.out.print(" ");
                }
                flag = false;
            }
            System.out.print("|\n");
        }

        System.out.print('\\');
        for(int i = 0; i < xSize; i++){
            System.out.print('-');
        }
        System.out.print("/\n");
    }

    public Map<String, ArrayList<Agent>> getAgents() {
        return agents;
    }

    public int getAvrContentment() {
        return avrContentment;
    }

    @Override
    public String toString() {
        return "World{\n" +
                "xSize=" + xSize +
                ", ySize=" + ySize +
                ", \nagents=" + agents +
                '}';
    }
}
