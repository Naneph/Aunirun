package com.example.auniruntest3;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.auniruntest3.utils.AppConfigUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        /*int i =0;
        while (i < 1000000){
            double peisu = AppConfigUtils.getTime(4501)*1.0/4501;
            i++;
            //System.out.println(peisu);
            if (peisu<6 || peisu>11){
                System.out.println("超过了！！   i="+i);
                System.out.println("peisu:"+peisu);
                return;
            }
        }
        System.out.println("正常");*/

       /* System.out.println("1111111");
        long distance = AppConfigUtils.getDistance(5000, 6000);
        int time = AppConfigUtils.getTime(distance);
        System.out.println("距离测试：" + distance);
        System.out.println("时间测试：" + time);
        System.out.println("配速：" + time * 1.0 / distance *1000);*/
    }


}