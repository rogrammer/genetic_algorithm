package com.example.genetic_algorithm;

import java.util.*;

public class Genetic {
    private final int WIDTH = 0;
    private final int HEIGHT = 1;
    private final int selectionAmount = 10;
    private int[][] rectangles;
    private int size;
    private int bestArea;
    private int xgrid;
    private int ygrid;
    private int smallestX;
    private int smallestY;

    public Genetic(int[][] rectangles, int x, int y) {
        this.rectangles = rectangles;
        size = rectangles.length;
        xgrid = x;
        ygrid = y;
        bestArea = 0;
        smallestX = Integer.MAX_VALUE;
        smallestY = Integer.MAX_VALUE;
        int width;
        int height;
        for(int i = 0; i < size; i++) {
            width = rectangles[i][0];
            height = rectangles[i][1];
            bestArea += width * height;
            if(smallestX > width)
                smallestX = width;
            if(smallestY > height)
                smallestY =  height;
        }
    }

    public Grids solve() {
        ArrayList<Grids> list = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            Grids g = initialize();
            list.add(g);
        }
        for(int i = 0; i < 100; i++) {
            list = crossOver(list);
            list = select(list);
        }
        Collections.sort(list);
        System.out.printf(list.get(0).toString());

        return list.get(0);
    }
    private Grids initialize() {
        String[] s = encode();
        List<String> list = Arrays.asList(s);
        Collections.shuffle(list);
        list.toArray(s);
        Grids g = new Grids(s);
        mutate(g);
        while(!fitness(g));

        return g;
    }
    private String[] encode() {
        String[] arr = new String[size];
        for(int i = 1; i <= size; i++) {
            arr[i - 1] = i + "f";
        }

        return arr;
    }
    private void mutate(Grids g) {
        if(Math.random() < 0.15) {
            String temp = g.s[(int) (Math.random() * size)];
            if(temp.charAt(1) == 't') {
                temp = temp.replace('t', 'f');
            }
            else {
                temp = temp.replace('f', 't');
            }
        }
    }

    private ArrayList<Grids> select(ArrayList<Grids> list) {
        Collections.sort(list);
        ArrayList<Grids> newList = new ArrayList<>();
        for(int i = 0; i < selectionAmount; i++)
            newList.add(list.get(i));

        return newList;
    }

//    pdfde detaylı anlatmış sayfa 27
    private ArrayList<Grids> crossOver(ArrayList<Grids> list) {
        ArrayList<Grids> newList = new ArrayList<>();
        int lsize = list.size();
        for(int i = 0; i < lsize; i++)
            for(int j = i + 1; j < lsize; j++) {
                String[] s1 = list.get(i).s;
                String[] s2 = list.get(j).s;
                int start = (int) (Math.random() * (size - 2));
                int end = start + 2;
                int[] keeper = new int[size - 2];
                int z = 0;
                for(int k = 0; k < size; k++) {
                    if(s2[k].charAt(0) != s1[start + 1].charAt(0) && s2[k].charAt(0) != s1[end].charAt(0)) {
                        keeper[z] = k;
                        z++;
                    }
                }

                z = 0;
                for(int k = 0; k <= start; k++) {
                    s1[k] = s2[keeper[z]];
                    z++;
                }

                for(int k = end + 1; k < size; k++) {
                    s1[k] = s2[keeper[z]];
                    z++;
                }

                Grids g = new Grids(s1);
                while(!fitness(g));
                mutate(g);
                newList.add(g);
            }
        return newList;
    }

    /**
     * dikdortgenlerin yerleştirdikten sonra fitness değerini hesapla
     * fitness değeri aldığın dikdörtgenin etrafını saran alan - bestArea olur sanırım
     * amaç fittest olanı bulmak
     * ve bulamdan önce büyük dikdörtgeni oluştur
     * bunu da pdf de 7.3 de işaretledim
     */
    private boolean fitness(Grids g) {
        String[] s = g.s;
        ArrayList<Pair> points = new ArrayList<>();
        points.add(new Pair(0, 0));
        int sum = 0;
        int area;

        for(int i = 0; i < size; i++) {
            int index = Integer.parseInt(s[i].charAt(0) + "") - 1;
            int rectx = rectangles[index][WIDTH];
            int recty = rectangles[index][HEIGHT];
            area = rectx * recty;
            if(s[i].charAt(1) == 't') {
                int temp = rectx;
                rectx = recty;
                recty = temp;
            }
            
            boolean donttake = true;
            Collections.sort(points);
            int psize = points.size();
            for(int j = 0; j < psize; j++) {
                Pair p = points.get(j);
                if(p.x + rectx <= xgrid && p.y + recty <= ygrid && (xgrid * ygrid - sum) > area) {
                    donttake = false;
                    points.add(new Pair(p.x + rectx, p.y));
                    points.add(new Pair(p.x, p.y + recty));
                    points.remove(p);
                    sum += area;
                    break;
                }
            }
            if(donttake) {
                System.out.println("the grid size is not enough!");
                increaseGridSize();
                return false;
            }
        }
        //calculate coverage
        int x_max = 0;
        int y_max = 0;

        for(Pair p : points) {
            if(x_max < p.x)
                x_max = p.x;
            if(y_max < p.y) {
                y_max = p.y;
            }
        }
        g.x = x_max;
        g.y = y_max;
        g.area = x_max * y_max;
        System.out.printf(g.toString());
        decreaseGridSize();
        return true;
    }

    private void increaseGridSize() {
        xgrid += smallestX;
        ygrid += smallestY;
    }

    private void decreaseGridSize() {
        xgrid -= smallestX;
        ygrid -= smallestY;
    }

}

class Pair implements Comparable<Pair> {
    protected int x;
    protected int y;
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int compareTo(Pair other) {
        if(x < other.x)
            return -1;
        else if(x > other.x)
            return 1;
        else {
            if(y < other.y) {
                return -1;
            }
            else if(y > other.y)
                return 1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "] - ";
    }
}

class Grids implements Comparable<Grids>{
    protected String[] s;
    protected int x = -1;
    protected int y = 1;

    protected int area;

    public Grids(String[] s) {
        this.s = s;
        area = x * y;
    }

    @Override
    public int compareTo(Grids o) {
        // check for uncompleted grids
        if(this.area == -1) {
            System.out.println("First item doesn't have area!");
            return -2;
        }
        if(o.area == -1) {
            System.out.println("Second item doesn't have area!");
            return -2;
        }

        //compare logic
        if(this.area > o.area)
            return 1;
        else if(this.area < o.area)
            return -1;

        return 0;
    }

    @Override
    public String toString() {
        String result = new String("Encode : ");
        int size = s.length;
        for(int i = 0; i < size; i++) {
            result += s[i] + " ";
        }

        result += "\nWidth of the rectangle: " + x;
        result += "\nHeight of the rectangle: " + y;
        result += "\nArea of the rectangle: " + area;
        result += "\n";

        return result;
    }
}
