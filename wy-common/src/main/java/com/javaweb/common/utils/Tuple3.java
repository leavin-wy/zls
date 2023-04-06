package com.javaweb.common.utils;

/**
 * @author myweb
 * @since 2023/3/4 10:40
 */
public class Tuple3<T1,T2,T3> {

    public final T1 fieldOne;
    public final T2 fieldTwo;
    public final T3 fieldThree;

    private Tuple3(T1 t1, T2 t2,T3 t3) {
        fieldOne = t1;
        fieldTwo = t2;
        fieldThree = t3;
    }

    @Override
    public String toString() {
        return "("+fieldOne +"," +fieldTwo+","+fieldThree+")";
    }

    public static <T1,T2,T3> Tuple3<T1,T2,T3> of(T1 t1, T2 t2, T3 t3){
        return new Tuple3<>(t1,t2,t3);
    }
}
