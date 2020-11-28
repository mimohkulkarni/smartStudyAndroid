package com.kulkarni.mimoh.smartstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver{

    SmsMessage[] smsm;
    private String sms_str ="";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[Objects.requireNonNull(pdus).length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_str += smsm[i].getMessageBody();

                String sender = smsm[i].getOriginatingAddress().substring(3);
                Intent smsIntent = new Intent("otp");
                if (sender.equals("FINSUR")) {
                    smsIntent.putExtra("message", sms_str);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }
            }
        }
    }
}
