package ru.jtconsulting.voicerecognition;

import android.app.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bssys.spitchmobilesdk.*;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity  implements View.OnClickListener {
    Button btn;
    Button btn1;
    Button btn2;
    Button btn3;
    TextView txtOut;
    TextView txtOut1;
    TextView txtOut2;
    final String LOG_TAG = "myLogs";
    MyTask mt;
    TextView taskOutput;
    int taskNumber=0;

    public boolean btnIsPressed=false;

    private Handler h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mainLayout.setOnTouchListener(new Slider(this));


        btn = (Button) findViewById(R.id.button);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        txtOut = (TextView) findViewById(R.id.txtOut);
        txtOut1 = (TextView) findViewById(R.id.txtOut1);
        txtOut2 = (TextView) findViewById(R.id.txtOut2);
        // присваиваем обработчик кнопкам
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        h= new Handl();
        SpitchMobileService.initService("111", null, null, null, h, this);

    }


    @Override
    public void onResume()
    {
        super.onResume();
        setVisible(true);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        setVisible(false);
        resetButtons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchBtn(View v) {

        Button b = null;


        if (v.getId() == R.id.button) {
            b = btn;
            taskOutput = txtOut;
            //txt = "Текст 1";
            taskNumber=1;


        }
        if (v.getId() == R.id.button1) {
            b = btn1;
            taskOutput = txtOut1;
            //txt = "Текст 2";
            taskNumber=2;
        }
        if (v.getId() == R.id.button2) { // слепок голоса
            b = btn2;
            taskOutput = txtOut2;
            taskNumber=3;
            //txt = "Текст 3.1";
        }
        if (v.getId() == R.id.button3) { //проверка по слепку
            b = btn3;
            taskOutput = txtOut2;
            taskNumber=4;
           //txt = "Текст 3.2";
        }
        if (btnIsPressed) { // выключаем
            if (v.getId() == R.id.button) { // свободное распознавание
                int servRes = SpitchMobileService.getServiceState();
                //if (servRes!=2) return;
                Log.d(LOG_TAG, "getServiceState="+String.valueOf(servRes));
                Log.d(LOG_TAG, "button1 OFF");
                SpitchMobileService.stopRecognition();

                String res = SpitchMobileService.getSpitchResult();
                Log.d(LOG_TAG, "getSpitchResult: "+res);
                taskOutput.setText(res);
            }

            unpressButton(b);
            cancelTask();
            taskOutput.setText("");
            taskNumber=0;

            if (v.getId() == R.id.button2) { // слепок голоса
                btn3.setEnabled(true);
            }
            if (v.getId() == R.id.button3) { // слепок голоса
                btn2.setEnabled(true);
            }
        } else { //включаем
            pressButton(b);
            //taskOutput.setText(txt);
            cancelTask();
            if (v.getId() == R.id.button) { // свободное распознавание
                Log.d(LOG_TAG, "button1 ON");
               boolean startRes= SpitchMobileService.startRecognition(null, h);
               if (!startRes)   {
                   Log.d(LOG_TAG, "startRecognition FAIL");
                   Log.d(LOG_TAG, "getServiceState="+String.valueOf(SpitchMobileService.getServiceState()));
                   Log.d(LOG_TAG, "getLastErrorMessage="+SpitchMobileService.getLastErrorMessage());
               }
            }
            if (v.getId() == R.id.button2) { // слепок голоса
                btn3.setEnabled(false);
            }
            if (v.getId() == R.id.button3) { // слепок голоса
                btn2.setEnabled(false);
            }

            mt = new MyTask();
            mt.execute();
        }

            btnIsPressed = !btnIsPressed;


    }
    @Override
    public void onClick(View v) {


            switchBtn(v);



    }
    public void unpressButton(Button b){
        b.setText(R.string.btnON);
        b.setBackgroundResource(R.drawable.button);
    }
    public void pressButton(Button b){
        b.setText(R.string.btnOFF);
        b.setBackgroundResource(R.drawable.button_off);
    }

    private void  resetButtons(){
        btnIsPressed=false;
        cancelTask();
        unpressButton(btn);
        unpressButton(btn1);
        unpressButton(btn2);unpressButton(btn3);
        btn3.setEnabled(true);btn2.setEnabled(true);
    }

    protected void cancelTask() {
        if (mt == null) return;
         mt.cancel(true);
    }
    class MyTask extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskOutput.setText("Begin");

        }
        protected String task1(){



            return "Recording...";

        }
        protected String task2(){
            return testTask("task2");

        }
        protected String task3(){
            return testTask("task3");

        }
        protected String task4() {
            return testTask("task4");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            taskOutput.setText(values[0]);
        }

        protected String testTask(String s){
            //taskOutput.setText(s);
                String r;
               try {
                   TimeUnit.SECONDS.sleep(2);
                   for (int i = 0; i < 10; i++) {
                       TimeUnit.SECONDS.sleep(2);
                       // if (isCancelled()) return null;
                       r = String.valueOf(i);
                       publishProgress(s+" ["+r+"]");
                       if (isCancelled()) return s+"Canseled";
                   }
               }catch (InterruptedException e) {
                   s =s+"Interapted";
                   e.printStackTrace();
               }
             return s+" Finished";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {


                switch (taskNumber) {
                    case 1:
                        return task1();

                    case 2:
                        return task2();

                    case 3:
                        return task3();

                    case 4:
                        return task4();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            taskOutput.setText(result);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            taskOutput.setText("Cancel");
            //Toast.makeText(this, "Нажата кнопка ОК", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "cancel");
          //rec.setReading(false);
        }
    }
}
