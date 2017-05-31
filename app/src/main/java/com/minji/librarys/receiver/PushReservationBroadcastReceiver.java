package com.minji.librarys.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.uitls.SharedPreferencesUtil;

/**
 * Created by user on 2016/8/31.
 */
public class PushReservationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                SharedPreferencesUtil.saveStirng(context, StringsFiled.CLIENTID,cid);
                // TODO:处理cid返回
                System.out.println("clientid==============================================" + cid);


                break;
            case PushConsts.GET_MSG_DATA:

                String taskid = bundle.getString("taskid");
                System.out.println("taskid==============================================" + taskid);
                String messageid = bundle.getString("messageid");
                System.out.println("messageid==============================================" + messageid);

                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    // TODO:接收处理透传（payload）数据
                    System.out.println("payload==============================================" + data);
                }

                break;
            default:
                break;
        }

    }
}
