package com.javaweb.common.utils;

/**
 * @author myweb
 * @since 2023/3/4 10:40
 */
public class Tuple2<T1,T2> {

    public final T1 _1;
    public final T2 _2;

    public Tuple2(T1 t1, T2 t2) {
        _1 = t1;
        _2 = t2;
    }

    @Override
    public String toString() {
        return "("+_1 +"," +_2 + ")";
    }

    public static <T1,T2> Tuple2<T1,T2> of(T1 t1, T2 t2){
        return new Tuple2<>(t1,t2);
    }
}