package ru.jtconsulting.voicerecognition;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bssys.spitchmobilesdk.*;



public class MainActivity extends Activity  implements View.OnClickListener {
    Button btn;
    Button btn1;
    Button btn2;
    Button btn3;
    TextView txtOut;
    TextView txtOut1;
    TextView txtOut2;
    final String LOG_TAG = "myLogs";
    public boolean isBlocked=true;
    TextView taskOutput;


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
      // SpitchMobileService.initService("111", null, null, null, h, this);
        showInitializationDialog();
        h= new Handl(this);
        initSpService();

    }

    public final static  int SERVICE_NOT_INITED = 0;
    public final static  int SERVICE_PROCESSING_INITIALIZATION = 1;
    public final static  int SERVICE_READY_TO_WORK = 2;
    public final static  int SERVICE_ERROR_INIT = 3;
    public final static  int SERVICE_BUSY = 4;

    public void initSpService(){
        int serviceState=0;
        //int serviceState = SpitchMobileService.getServiceState();
        if (serviceState==SERVICE_NOT_INITED||serviceState==SERVICE_ERROR_INIT){
            disableEnableButtons(false);




            SpitchMobileService.initService("111", null, null, null, h, this);
        } else {
            showAlert("Service is busy");

        }
    }

    private void showAlert(String txt){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Важное сообщение!")
                .setMessage(txt)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }



    public ProgressDialog  pd;
    private void showInitializationDialog(){
        pd = new ProgressDialog(this);
        pd.setTitle("Старт.");
        pd.setMessage("");
        // добавляем кнопку
        pd.setButton(Dialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //System.exit(0);
            }
        });
        pd.show();
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

    public MenuItem initMenuitem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        initMenuitem = menu.findItem(R.id.action_init);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_exit:
                finish();
                System.exit(0);
                break;
            case  R.id.action_init:
                initSpService();
                break;
        }




        return super.onOptionsItemSelected(item);
    }

    private void startFreeVoiceRecognition(){
        Log.d(LOG_TAG, "start FreeVoiceRecognition");
        if (!SpitchMobileService.startRecognition("Mobile SDK grammar", new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(LOG_TAG, "startFreeVoiceRecognition beginning Status="+String.valueOf(msg.what));
                Log.d(LOG_TAG, "getLastErrorMessage="+SpitchMobileService.getLastErrorMessage());
                Log.d(LOG_TAG, "getLastErrorCode="+String.valueOf(SpitchMobileService.getLastErrorCode()));
                Log.d(LOG_TAG, "getServiceAvailability="+String.valueOf(SpitchMobileService.getServiceAvailability()));


            }
        })){
            Log.d(LOG_TAG, "startFreeVoiceRecognition FAIL");
            showAlert("Recognition error "+SpitchMobileService.getLastErrorMessage());

        }

    }
    private void stopFreeVoiceRecognition(){
        Log.d(LOG_TAG, "stop FreeVoiceRecognition");
        Log.d(LOG_TAG, "getSpitchResult="+String.valueOf(SpitchMobileService.getSpitchResult()));
        Log.d(LOG_TAG, "stop getServiceState="+String.valueOf(SpitchMobileService.getServiceState()));
        SpitchMobileService.stopRecognition();
        String res =  SpitchMobileService.getSpitchResult();
        Log.d(LOG_TAG, "getSpitchResult = "+res);
        showAlert(res);
    }
    private void invertBtn( int id){
        Button b = (Button) findViewById(id);
        if (btnIsPressed) {
            unpressButton(b);

        } else {
            pressButton(b);
        }
        btnIsPressed = !btnIsPressed;
    }

    private void switchBtn(View v) {

        Button b = null;

        int btnId=v.getId();
        switch (btnId){
            case R.id.button: // свободное распознование
                if (btnIsPressed)  stopFreeVoiceRecognition(); else startFreeVoiceRecognition();
                invertBtn(btnId);
                break;

            case R.id.button1: // распознование по грамматике
                invertBtn(btnId);
                break;

            case R.id.button2: // слепок голоса
                invertBtn(btnId);
                break;

            case R.id.button3: // проверка по слепку
                invertBtn(btnId);
                break;
        }

        /*if (v.getId() == R.id.button) {
            b = btn;
            taskOutput = txtOut;

            taskNumber=1;


        }
        if (v.getId() == R.id.button1) {
            b = btn1;
            taskOutput = txtOut1;

            taskNumber=2;
        }
        if (v.getId() == R.id.button2) { // слепок голоса
            b = btn2;
            taskOutput = txtOut2;
            taskNumber=3;

        }
        if (v.getId() == R.id.button3) { //проверка по слепку
            b = btn3;
            taskOutput = txtOut2;
            taskNumber=4;

        }*/
      /*  if (btnIsPressed) { // выключаем
            if (v.getId() == R.id.button) { // свободное распознавание
                int servRes = SpitchMobileService.getServiceState();

                Log.d(LOG_TAG, "getServiceState="+String.valueOf(servRes));
                Log.d(LOG_TAG, "button1 OFF");
                if (servRes==2) SpitchMobileService.stopRecognition();

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


        }

            btnIsPressed = !btnIsPressed;

*/
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

    private void unpressButtons(){
        unpressButton(btn);
        unpressButton(btn1);
        unpressButton(btn2);
        unpressButton(btn3);
    }

    private void  resetButtons(){
        btnIsPressed=false;
        unpressButtons();
        cancelTask();
        disableEnableButtons(true);
    }
    public void disableEnableButtons(boolean enable){
        btn.setEnabled(enable);
        btn1.setEnabled(enable);
        btn2.setEnabled(enable);
        btn3.setEnabled(enable);
    }

    public void setTextToAll(String s){
        txtOut.setText(s);
        txtOut1.setText(s);
        txtOut2.setText(s);
    }


    public void  resetButtonsAndTxt(){
        btnIsPressed=false;
        cancelTask();
        unpressButtons();
        setTextToAll("");
        disableEnableButtons(true);
    }

    protected void cancelTask() {

    }

}
