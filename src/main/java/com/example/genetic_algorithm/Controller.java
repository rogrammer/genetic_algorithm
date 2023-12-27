package com.example.genetic_algorithm;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Controller {
    @FXML
    private Pane rectangleContainer;
    @FXML

    public void addRectangle(int[][] rectangles) {
        Genetic genetic = new Genetic(rectangles, 50, 50);
        Grids g = genetic.solve();

        int size = g.rectArrayList.size();
        Rectangle grid = new Rectangle(0, 0, g.x, g.y);
        rectangleContainer.getChildren().add(grid);
        grid.setFill(Color.TRANSPARENT);
        grid.setStroke(Color.BLACK);
        for(Rect r : g.rectArrayList) {
            Rectangle rectangle = new Rectangle(r.x, r.y, r.width, r.height);
            rectangle.setFill(getRandomColor());
            rectangleContainer.getChildren().add(rectangle);

        }
    }

    private Color getRandomColor() {
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();
        if(red == 1.0 && green == 1.0 && blue == 1.0) {
            Color.color(1.0, 0, 0);
        }

        return new Color(red, green, blue, 1.0);
    }

}