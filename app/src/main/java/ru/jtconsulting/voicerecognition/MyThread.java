package ru.jtconsulting.voicerecognition;

import android.os.Handler;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
/**
 * Created by www on 22.12.2015.
 */
public class MyThread extends Thread {


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;
    private int taskNumber;
    private String result;

    public TextView getTxt() {
        return txt;
    }

    public void setTxt(TextView txt) {
        this.txt = txt;
    }

    private TextView txt;


    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void run() {
        try {
            switch (taskNumber) {
                case 1:
                    task1();
                    break;
                case 2:
                    task2();
                    break;
                case 3:
                    task3();
                    break;
                case 4:
                    task4();
                    break;
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void postResult(String r){
        handler.post(new Runnable() {  // используя Handler, привязанный к UI-Thread
            @Override
            public void run() {
                txt.setText(result);         // выполним установку значения
            }
        });

    }

    private void test(String s){
        try {
            postResult("Start " + s);
            TimeUnit.SECONDS.sleep(5);
            postResult("End " + s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void task1(){
        result="task1";
        test(result);

    }
    private void task2(){
        result="task2";
        test(result);
    }
    private void task3(){
        result="task3";
        test(result);
    }
    private void task4(){
        result="task3";
        test(result);
    }
}
