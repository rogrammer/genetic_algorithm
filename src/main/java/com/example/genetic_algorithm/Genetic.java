package com.example.genetic_algorithm;

import java.util.*;
/**
 * WIDTH diktörtgenin x ekseni için
 * HEIGHT dikdörtgenin y ekseni için
 * selection amount genetic algoritmada önce crossover ile nesiller oluşturyorsun
 * sonra da bundan en iyilerini seçiyorsun bu o seçim miktarı
 * rectangles [dikdöretgen sayısı][genişlik ve uzunluğundan] oluşan bir array
 * size kaç dikdörtgen olduğu
 * xgrid ygrid 150 diye aldığımız küçükse arttırıyoruz en dıştakini genişlik ve uzunluğu
 * smallest olanlar da arttırma mikdarını belirlemek için eğer en dıştaki dikdörtgen küöçük ise onun
 * x eksenini en küçük dikdörtgen genişliği ile y sini de en küçük dikdortgen uzunluğu miktarında arttrıyoruz
 * */
public class Genetic {
    private final int WIDTH = 0;
    private final int HEIGHT = 1;
    private final int selectionAmount = 50;
    private int[][] rectangles;
    private int size;
    private int smallestX;
    private int smallestY;
    private int xgrid;
    private int ygrid;
    public static int bestArea = 0;


    public Genetic(int[][] rectangles, int x, int y) {
        this.rectangles = rectangles;
        size = rectangles.length;
        xgrid = x;
        ygrid = y;

        for(int i = 0; i < size; i++) {
            bestArea += rectangles[i][0] * rectangles[i][1];
        }
        smallestX = Integer.MAX_VALUE;
        smallestY = Integer.MAX_VALUE;
        int width;
        int height;

//        smallestları burda buluyoruz
        for (int i = 0; i < size; i++) {
            width = rectangles[i][0];
            height = rectangles[i][1];
            if (smallestX > width)
                smallestX = width;
            if (smallestY > height)
                smallestY = height;
        }
    }
/**
 * bu çözümü yapan fonksiyon
 * ilk satırda bir list oluşturuyor sonra
 * initializede 20 tane grid oluşturuyor
 * grid en dıştaki dikdörtgen ama büyüklüğünü fitnessda hesaplıyoruz yani xgrid ve ygrid ile aynı değil
 * gridin içinde aynı zamanda bir string array tutuyoruz ["1f", "2f", "3f"] gibi sayılar dikörtgenleri
 * t ve f dönderilmiş mi değil mi onu tutuyor t dönderilmiş demek
 * en altta Grid classı var
 * 100 olan for dögüsü crosover ile 20 tane gridi birbiri arasında çaprazlıyo sonra select ile en iyi
 * 10 tanesini seçiyor selectionAmount onun için
 * en alttaki sort da sıralıyor en iyisini seçip ona dönüyor
 * */
    public Grids solve() {
        ArrayList<Grids> list = new ArrayList<>();
        ArrayList<Grids> newList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Grids g = initialize();
            list.add(g);
        }

        for (int i = 0; i < 300; i++) {
            newList = crossOver(list);
            list.addAll(newList);
            list = select(list);
            Collections.sort(list);
            System.out.println(list.get(0));
        }

        return list.get(0);
    }
/**
 * encode() da ["1f", "2f", "3t" ....] için kaç tane dikdörtgen varsa
 * bu bize crossover yaparken kolaylık sağlıyor
 *shuffle karıştırıyor sadece
 * mutate de 0.25 olasılık ile f yi t ya da t yi f yapıyor
 * gende mutasyon yapıyo
 * fitness de yerleşene kadar yerleştirmeye çalışıyor
 * */
    private Grids initialize() {
        String[] s = encode();
        List<String> list = Arrays.asList(s);
        Collections.shuffle(list);
        list.toArray(s);
        Grids g = new Grids(s);
        mutate(g);
        while (!fitness(g)) ;

        return g;
    }

    private String[] encode() {
        String[] arr = new String[size];
        for (int i = 1; i <= size; i++) {
            arr[i - 1] = i + "f";
        }

        return arr;
    }

    private void mutate(Grids g) {
        if (Math.random() < 0.25) {
            int num = (int) (Math.random() * size);
            String temp = g.s[num];
            if (temp.charAt(1) == 't') {
                temp = temp.replace('t', 'f');
            } else {
                temp = temp.replace('f', 't');
            }
            g.s[num] = temp;
        }
    }

    private ArrayList<Grids> select(ArrayList<Grids> list) {
        Collections.sort(list);
        ArrayList<Grids> newList = new ArrayList<>();
        for (int i = 0; i < selectionAmount; i++)
            newList.add(list.get(i));
        xgrid = newList.get(0).x;
        ygrid = newList.get(0).y;
        return newList;
    }

  /**
   *  pdfde detaylı anlatmış sayfa 27
   *  pdf i gite atarım
   *  onun aynısını yaptım sayılır
  * */
    private ArrayList<Grids> crossOver(ArrayList<Grids> list) {
        ArrayList<Grids> newList = new ArrayList<>();
        int lsize = list.size();
        for (int i = 0; i < lsize; i++)
            for (int j = i + 1; j < lsize; j++) {
                String[] s1 = list.get(i).s;
                String[] s2 = list.get(j).s;
                int start = (int) (Math.random() * (size - 2));
                int end = start + 2;
                int[] keeper = new int[size - 2];
                int z = 0;
                for (int k = 0; k < size; k++) {
                    if (s2[k].charAt(0) != s1[start + 1].charAt(0) && s2[k].charAt(0) != s1[end].charAt(0)) {
                        keeper[z] = k;
                        z++;
                    }
                }

                z = 0;
                for (int k = 0; k <= start; k++) {
                    s1[k] = s2[keeper[z]];
                    z++;
                }

                for (int k = end + 1; k < size; k++) {
                    s1[k] = s2[keeper[z]];
                    z++;
                }

                Grids g = new Grids(s1);
                while (!fitness(g)) ;
                mutate(g);
                newList.add(g);
            }
        return newList;
    }

/**
 * firness EN ÖNEMLİSİ BÜYÜK İHTİMALLE HATA BURDA HATTA KESİN BURADA
 * Pair class dikdörtgenlerin köşesini tutuyor
 * Rect dikdörtgenleri tutmak için bunu sadece overlapi önlemek için kullanıyoruz
 * ifcheked overlap var mı diye bakıyor eğer true ise var demek
 * take yerleşme başarılı mı diye bakıyor eğer başarılı ise true oluyor
 * fitnessın mantığın birazıda pdf deki pseudo koddan
* */
    private boolean fitness(Grids g) {
        String[] s = g.s;
        ArrayList<Pair> points = new ArrayList<>();
        ArrayList<Rect> rectArrayList = new ArrayList<>();

//  ilk noktalara (0, 0) ı ekle
        points.add(new Pair(0, 0));
        boolean ifchecked;
        boolean taked;

//  dikdörtgenlerin hepsini dön
        for (int i = 0; i < size; i++) {
            int index = Integer.parseInt(s[i].charAt(0) + "") - 1;
            int rectx = rectangles[index][WIDTH]; // bir dikdörtgenin genişliğini al
            int recty = rectangles[index][HEIGHT];// uzunluğunu al

//      eğer dönderilmiş ise genişlik ve uzunluğu yer değiştir
            if (s[i].charAt(1) == 't') {
                int temp = rectx;
                rectx = recty;
                recty = temp;
            }

            taked = false;
            Collections.sort(points);
            int psize = points.size();
//      pair sayısı kadar dönder
            for (int j = 0; j < psize; j++) {
                Pair p = points.get(j);

                if(p.x + rectx <= xgrid && p.y + recty <= ygrid) {
                    Rect rect = new Rect(p.x, p.y, rectx, recty);
                    rectArrayList.add(rect);
                    ifchecked = Rect.checkOverlap(rectArrayList);
                    if(ifchecked) {
                        rectArrayList.remove(rect);
                    } else {
                        taked = true;
                        points.add(new Pair(p.x + rectx, p.y));
                        points.add(new Pair(p.x, p.y + recty));
                        g.rectArrayList.add(rect);
                        points.remove(p);
                        break;
                    }
                }
            }
//            yerleştiremez ise en dıştaki dikdörtgen boyutunu arttır
            if (!taked) {
                increaseGridSize();
                return false;
            }
        }
        //calculate coverage
        int x_max = 0;
        int y_max = 0;

        for (Pair p : points) {
            if (x_max < p.x)
                x_max = p.x;
            if (y_max < p.y) {
                y_max = p.y;
            }
        }

//        en dıştaki dikdörtgeni hesapla
        g.x = x_max;
        g.y = y_max;
        g.area = x_max * y_max;
        return true;
    }


    private void increaseGridSize() {
        xgrid += smallestX;
        ygrid += smallestY;
    }


}

class Rect{
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public  Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * iki dikdörtgen al x ve y leri arasındaki fark genişlik ve uzunluk kadar mı bak
     * */
    private boolean check(Rect other) {
        boolean xoverlap = true;
        if(this.x >= other.x + other.width)
            xoverlap = false;
        if(other.x >= this.x + this.width)
            xoverlap = false;

        if(this.y >= other.y + other.height)
            xoverlap = false;
        if(other.y >= this.y + this.height)
            xoverlap = false;


            /*boolean xOverlap = this.x < (other.x + other.width) && other.x < (this.x + this.width);
            boolean yOverlap = this.y < (other.y + other.height) && other.y < (this.y + this.height);*/
        return xoverlap;
    }

//    bütün dikdörtgenler için yap check() i
    public static boolean checkOverlap(ArrayList<Rect> list) {
        int size = list.size();
        for(int i = 0; i < size; i++)
            for(int j = i + 1; j < size; j++) {
                if(list.get(i).check(list.get(j))) {
                    return true;
                }
            }
        return false;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " width: " + width + " height: " + height;
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
        /*if (x < other.x)
            return -1;
        else if (x > other.x)
            return 1;
        else {
            if (y < other.y)
                return -1;
        }
        return 1;*/
        if(x + y < other.x + other.y)
            return -1;
        else
            return 1;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "] - ";
    }
}


class Grids implements Comparable<Grids> {
    protected String[] s;
    protected int x = -1;
    protected int y = 1;
    protected ArrayList<Rect> rectArrayList = new ArrayList<>();

    protected int area;

    public Grids(String[] s) {
        this.s = s;
        area = x * y;
    }

    @Override
    public int compareTo(Grids o) {
        // check for uncompleted grids
        if (this.area == -1) {
            System.out.println("First item doesn't have area!");
            return -2;
        }
        if (o.area == -1) {
            System.out.println("Second item doesn't have area!");
            return -2;
        }

        //compare logic
        if (this.area > o.area)
            return 1;
        else if (this.area < o.area)
            return -1;

        return 0;
    }

    @Override
    public String toString() {
        String result = new String("Encode : ");
        int size = s.length;
        for (int i = 0; i < size; i++) {
            result += s[i] + " ";
        }

        result += "\nInstant Area: " + area;
        result += "\nBest Area: " + Genetic.bestArea;
        result += "\nFitness: " + (area - Genetic.bestArea);
        result += "\n";

        return result;
    }
}