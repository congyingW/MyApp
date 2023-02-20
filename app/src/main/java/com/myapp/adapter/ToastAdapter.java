package com.myapp.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.R;

public class ToastAdapter extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastAdapter(Context context) {
        super(context);
    }
    private static ToastAdapter toastAdapter;
    private static void cancelToast(){
        if (toastAdapter!=null){
            toastAdapter.cancel();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * init toast
     * @param context: activity
     * @param charSequence: tips
     */
    public static void initToast(Context context, CharSequence charSequence){
        cancelToast();
        toastAdapter = new ToastAdapter(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = view.findViewById(R.id.toast_content);
        textView.setText(charSequence);
        toastAdapter.setView(view);
        toastAdapter.setGravity(Gravity.CENTER, 0, 0);
        toastAdapter.setDuration(Toast.LENGTH_SHORT);
        toastAdapter.show();
    }
}
