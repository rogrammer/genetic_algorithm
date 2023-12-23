package com.example.genetic_algorithm;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Scanner input = new Scanner(new File("input.txt"));
            int number = input.nextInt(), x = input.nextInt(), y = input.nextInt();
            int[][] rectangles = new int[number][2];
            for(int i = 0; i < number; i++) {
                for(int j = 0; j < 2; j++) {
                    rectangles[i][j] = input.nextInt();
                }
            }
            Genetic genetic = new Genetic(rectangles, 80, 80);
            Grids g = genetic.solve();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view.fxml"));
            Parent root = fxmlLoader.load();
//        Group group = new Group();
//        group.getChildren().add(fxmlLoader.load());
            Controller controller = fxmlLoader.getController();
            controller.addRectangle(rectangles, g);
            Scene scene = new Scene(root, 700, 600);
            stage.setTitle("Genetic Algorthim AI");
            stage.setScene(scene);
            stage.show();
        } catch (FileNotFoundException e) {
            System.out.println("input.txt can not loaded! : " + e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}