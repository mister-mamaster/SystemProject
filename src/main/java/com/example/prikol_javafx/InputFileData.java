package com.example.prikol_javafx;

import com.example.prikol_javafx.code.world.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InputFileData {

    public InputFileData(World world) {
        try (BufferedReader br = new BufferedReader(new FileReader( "./src/main/java/com/example/prikol_javafx/input_data.txt"))) {
            String str;
            Commands cms = new Commands(world);
            while((str = br.readLine()) != null){
                cms.input(str);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
