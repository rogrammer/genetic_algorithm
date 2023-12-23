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
        ArrayList<Rect> rectArrayList = new ArrayList<>();
        int size = g.s.length;

        Rectangle grid = new Rectangle(0, 0, g.x, g.y);
        grid.setFill(Color.TRANSPARENT);
        grid.setStroke(Color.BLACK);
        rectangleContainer.getChildren().add(grid);

        String[] s = g.s;
        ArrayList<Pair> points = new ArrayList<>();
        points.add(new Pair(0, 0));
        boolean ifchecked = false;
        for (int i = 0; i < size; i++) {
            int index = Integer.parseInt(s[i].charAt(0) + "") - 1;
            int rectx = rectangles[index][0];
            int recty = rectangles[index][1];
            if (s[i].charAt(1) == 't') {
                int temp = rectx;
                rectx = recty;
                recty = temp;
            }

            Collections.sort(points);
            int psize = points.size();
            for (int j = 0; j < psize; j++) {
                Pair p = points.get(j);
                Rect rect = new Rect(p.x, p.y, rectx, recty);
                rectArrayList.add(rect);
                if(j != 0)
                    ifchecked = Rect.checkOverlap(rectArrayList);
                points.remove(p);
                if (!ifchecked) {
                    points.add(new Pair(p.x + rectx, p.y));
                    points.add(new Pair(p.x, p.y + recty));
                    Rectangle r = new Rectangle(p.x, p.y, rectx, recty);
                    r.setFill(getRandomColor());
                    rectangleContainer.getChildren().add(r);
                    break;
                }
                rectArrayList.remove(rect);
            }
        }
    }

    private Color getRandomColor() {
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();
        if(red == 1 && green == 1 && blue == 1) {
            Color.color(1.0, 0, 0);
        }

        return new Color(red, green, blue, 1.0);
    }

}