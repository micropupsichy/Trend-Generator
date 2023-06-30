package com.ded_technologies.functioninjava;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ParseToString extends Thread {
    /*
    - массив потоков генерации параметров
    - массив параметров потоков
    - разделитель
    - массив размерностей
    - метка разреженных данных
    - степень заполнения в о.е.
    */
    private final TrendCollector[] collectors;
    private final double[][] parameters = new double[4][];
    private final char seperator;
    private final String[] MEAS = {"m", "A", "K", "cd", "m^3", "Hz", "m/s", "m/s^2", "N", "Pa", "J", "kg*m/s", "C", "V", "F", "T", "Wb", "H", "A/m", "V/m"};
    private final boolean CHECK = GUI.jCheckBox1.isSelected();
    private final double degreesOfFilling = GUI.jSlider1.getValue()*0.01;
    /*Конструктор класса получает число столбцов и строк и инициализирует массивы*/
    public ParseToString(){
        collectors = new TrendCollector[GUI.totalColumns];
        for (int i=0; i<parameters.length; i++){
            parameters[i] = new double[GUI.totalColumns];
        }
        if (GUI.separator==0){
            seperator = 9;
        }else{
            seperator=59;
        }
    }
    /*Метод генерирует и сохраняет параметры потоков*/
    public void getParam(){
        for (int i=0; i<GUI.totalColumns; i++){
            parameters[0][i] = 1+Math.random()*10;//коэффициент k
            parameters[1][i] = (Math.random() * 3);//вид функции от 0 до 1 (включая)
            parameters[2][i] = 1+(Math.random()*(GUI.totalRows*0.01));//постоянная времени
            parameters[3][i] = (ThreadLocalRandom.current().nextDouble(0.1, 0.5));//демпфирование
        }
    }
    /*Метод генерирует строку с размерностями*/
    public String getMeas(){
        Random r = new Random();
        StringBuilder line = new StringBuilder();
        for (int i=0; i<GUI.totalColumns; i++){
            if (i<GUI.totalColumns-1){
                line = line.append(MEAS[r.nextInt(20)]).append(seperator);
            } else {
                line = line.append(MEAS[r.nextInt(20)]).append("\n");
            }
        }
        return line.toString();
    }
    /*Метод для каждого параметра создает отдельный поток с собственными параметрами и выполняет его*/
    public void sendThread(int current_loop) throws InterruptedException{
        for (int i=0; i<GUI.totalColumns; i++){
            collectors[i] = new TrendCollector(current_loop*TrendCollector.ROWS, (int) parameters[1][i], (int) parameters[0][i], parameters[2][i], parameters[3][i]);
            collectors[i].start();
        }
        /*ожидаем, пока все потоки завершат вычисления*/
        for (int i=0; i<GUI.totalColumns; i++){
            collectors[i].join();
        }
    }
    /*Метод генерирует строку с именами параметров*/
    public String getNameColumns(){
        StringBuilder line = new StringBuilder();
        for (int i=0; i<GUI.totalColumns; i++){
            if (i<GUI.totalColumns-1){
                line = line.append("par").append(i+1).append(seperator);
            } else {
                line = line.append("par").append(i+1).append("\n");
            }
        }
        return line.toString();
    }
    /*Метод парсит результирующие массивы в строку*/
    public String parse(){
        StringBuilder lines = new StringBuilder();
        ArrayList random = new ArrayList();
        for (int i=0; i<10000; i++){
            for (int k=0; k<(int) (GUI.totalColumns*degreesOfFilling);){
                int rInt = ThreadLocalRandom.current().nextInt(0, GUI.totalColumns);
                if (!random.isEmpty() && random.indexOf(rInt)==-1){
                    random.add(rInt);
                    k++;
                }else if (random.isEmpty()){
                    random.add(rInt);
                    k++;
                }
            }
            for (int j=0; j<GUI.totalColumns; j++){
                if (CHECK && !random.contains(j)){
                    if (j<GUI.totalColumns-1){
                        lines = lines.append(seperator);
                    } else {
                        lines = lines.append("\n");
                    }
                }else{
                    if (j<GUI.totalColumns-1){
                        lines = lines.append(collectors[j].getLines()[i]).append(seperator);
                    } else {
                        lines = lines.append(collectors[j].getLines()[i]).append("\n");
                    }
                }
            }
            random.clear();
        }
        return lines.toString();
    }
}
