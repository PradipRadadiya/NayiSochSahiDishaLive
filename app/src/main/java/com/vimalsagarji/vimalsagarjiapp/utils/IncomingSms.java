package com.vimalsagarji.vimalsagarjiapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Grapes-Pradip on 02-Nov-17.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                Log.e("length", "------------"+pdusObj.length);
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    Log.e("phoneNumber", "-------------: " + phoneNumber);
                    Log.e("message", "-------------: " + currentMessage);
                    String senderNum = phoneNumber;
//                    String message = currentMessage.getDisplayMessageBody().split(":")[1];
                    String message1 = currentMessage.getMessageBody();
                    String message = message1.substring(message1.length()-6);
                    Log.e("message", "-------------: " + message);
//                    Log.e("newmsg", "-------------: " + newmsg);
//                    message = message.substring(0, message.length()-1);
                    Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {

            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
