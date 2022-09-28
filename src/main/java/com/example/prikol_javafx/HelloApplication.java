package com.example.prikol_javafx;

import com.example.prikol_javafx.code.agents.Courier;
import com.example.prikol_javafx.code.agents.Order;
import com.example.prikol_javafx.code.agents.resources.OrderR;
import com.example.prikol_javafx.code.world.World;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        World world = new World();
        InputFileData inputFileData = new InputFileData(world);

        stage.setTitle("Graphic");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> myLine = new LineChart<Number, Number>(x, y);
        myLine.setTitle("Средняя удовлетворённость агентов системы");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Удовлетворённость(0-100)");

        ObservableList<XYChart.Data> dataList1 = FXCollections.observableArrayList();

        int i = 0;
        while(world.getAgents().get("class com.example.prikol_javafx.code.agents.Order").size() > 0){
            System.out.println(world.getAvrContentment());
            dataList1.add(new XYChart.Data(i+1, world.getAvrContentment()));
            world.tick(1);
            i++;
        }


        dataList1.add(new XYChart.Data(30, world.getAvrContentment()));

        series1.setData(dataList1);

        Scene scene = new Scene(myLine, 1200, 600);

        myLine.getData().add(series1);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}