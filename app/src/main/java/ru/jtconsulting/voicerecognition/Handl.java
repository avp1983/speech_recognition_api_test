package ru.jtconsulting.voicerecognition;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bssys.spitchmobilesdk.*;

import java.util.ArrayList;

/**
 * Created by www on 11.01.2016.
 */
public class Handl extends Handler {
    private MainActivity context;

    public static final int SDK_NOT_INITED=0;
    public static final int INITIALIZATION_PROCESSING=1;
    public static final int INITIALIZATION_COMPLETED=2;
    public static final int ERROR_INITIALIZATION=3;

    public Handl(MainActivity a) {
        super();
        context=a;
        Log.d(LOG_TAG, "handleMessage:START");

        context.pd.setMessage("Инициализация");
    }

    final String LOG_TAG = "Handl";
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ArrayList<String> grammars;
        Log.d(LOG_TAG, "handleMessage:"+msg.what);
       switch(msg.what){
           case INITIALIZATION_COMPLETED:
               context.disableEnableButtons(true);
               context.isBlocked=false;
               context.pd.setMessage("ИНИЦИАЛИЗАЦИЯ УСПЕШНА");
               context.pd.dismiss();
               grammars= SpitchMobileService.getGrammarList();
               if (grammars!=null){
                   Log.d(LOG_TAG, "getGrammarList is successfull");
                   //Log.d(LOG_TAG, "getGrammarList[0] is "+grammars.get(0));
                   for (int i=0;i<grammars.size();i++){
                       Log.d(LOG_TAG,grammars.get(i));
                   }
               } else {
                   Log.d(LOG_TAG, "getGrammarList is NOT successfull");
               }
               break;
           case ERROR_INITIALIZATION:
               context.setTextToAll("ОШИБКА ИНИЦИАЛИЗАЦИИ");
               context.pd.setMessage("ОШИБКА ИНИЦИАЛИЗАЦИИ");
               context.initMenuitem.setEnabled(true);
               break;

       }



        /*Toast toast = Toast.makeText(context,
                "Статус: "+ String.valueOf(msg.what),
                Toast.LENGTH_SHORT);
       // toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
        context.pd.setMessage("handleMessage:"+String.valueOf(msg.what));
    }
}
