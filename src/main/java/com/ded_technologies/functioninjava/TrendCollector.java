package com.ded_technologies.functioninjava;

public class TrendCollector extends Thread{
    /*
    - число строк: 10000
    - текущая строка
    - вид функции
    - коэффициент
    - постоянная времени
    - демпфирование
    - массив строк
    */
    public static int ROWS = 10000;
    private final int current_row,
                      c;
    private final double k,
                         post_time,
                         demp;
    private final double[] lines = new double[ROWS];
    /*Конструктор класса получает текущую строку, коэффициент, постоянную времени и демпфирование*/
    public TrendCollector(int current_row, int c, double k, double post_time, double demp){
        this.current_row = current_row;
        this.c = c;
        this.k = k;
        this.post_time = post_time;
        this.demp = demp;
    }
    /*Метод для получения массива строк*/
    public double[] getLines(){
        return lines;
    }
    /*Метод для получения последовательности данных*/
    private void doTrend(){
        double time;
        for (int j=current_row; j<current_row+ROWS; j++){
            time = j*0.05;
            switch(c){
                case 0:
                    lines[j-current_row] = Functions.aperiod(k, post_time, time);
                    break;
                case 1:
                    lines[j-current_row] = Functions.colebat(k, post_time, time, demp);
                    break;
                case 2:
                    lines[j-current_row] = Functions.aperiod(k, post_time, time)+Functions.colebat(k, post_time, time, demp);
                    break;
                default:
                    lines[j-current_row] = time;
                    break;
            }
        }
    }
    /*Метод потока генерации значений параметров в зависимости от позиции времени*/
    @Override
    public void run(){
        /*Метод для получения массива строк одного параметра по времени*/
        doTrend();
    }
}

