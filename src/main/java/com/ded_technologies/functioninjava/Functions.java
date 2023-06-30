package com.ded_technologies.functioninjava;

public class Functions {
    /*
    k-коэффициент масштабирования
    post_time-постоянная времени
    current_time-текущая позиция времени
    demp-демпфирование (для колебат. звена)
    */
    public static double aperiod (double k, double post_time, double current_time){
        return (k*(1-Math.exp(-current_time/post_time)))+((Math.random()*0.5)*Math.sin(2*Math.PI*current_time*0.1))+(Math.random()*0.1);
    }
    
    public static double colebat (double k, double post_time, double current_time, double demp){
        return (k*(1-Math.exp(-demp*current_time/post_time)*(Math.cos(2*Math.PI*(Math.sqrt(1-(demp*demp))/post_time)*current_time)+
                (demp/(Math.sqrt(1-(demp*demp))))*Math.sin(2*Math.PI*(Math.sqrt(1-(demp*demp))/post_time)*current_time))))+
                ((Math.random()*0.5)*Math.sin(2*Math.PI*current_time*0.1))+(Math.random()*0.1);
    }
}
