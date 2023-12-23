package com.example.genetic_algorithm;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;

public class Controller {
    @FXML
    private Pane rectangleContainer;
    @FXML

    public void addRectangle(int[][] rectangles, Grids g) {
        int num = g.s.length;
        Rectangle grid = new Rectangle(0, 0, g.x, g.y);
        grid.setFill(Color.TRANSPARENT);
        grid.setStroke(Color.BLACK);
        rectangleContainer.getChildren().add(grid);
        String[] s = g.s;
        ArrayList<Pair> points = new ArrayList<>();
        points.add(new Pair(0, 0));
        for(int i = 0; i < num; i++) {
                int index = Integer.parseInt(s[i].charAt(0) + "") - 1;
                int rectx = rectangles[index][0];
                int recty = rectangles[index][1];
            System.out.printf("rectx: " + rectx + " recty: " + recty);
                if(s[i].charAt(1) == 't') {
                    int temp = rectx;
                    rectx = recty;
                    recty = temp;
                }
                Collections.sort(points);
                 System.out.println(points);
                int psize = points.size();
                for(int j = 0; j < psize; j++) {
                    Pair p = points.get(j);
                    if(p.x + rectx <= g.x && p.y + recty <= g.y) {
                        Rectangle rect = new Rectangle(p.x, p.y, rectx, recty);
                        rect.setFill(getRandomColor());
                        rectangleContainer.getChildren().add(rect);
                        points.add(new Pair(p.x + rectx, p.y));
                        points.add(new Pair(p.x, p.y + recty));
                        points.remove(p);
                        break;
                    }
                }

        }
    }

    private Color getRandomColor() {
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();
        return new Color(red, green, blue, 1.0);
    }

}