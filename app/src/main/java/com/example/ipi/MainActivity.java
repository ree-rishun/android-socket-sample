package com.example.ipi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {
    int flg = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //レイアウト設定
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);		// 縦に項目を並べる
        linearLayout.setGravity(Gravity.CENTER);				// センタリング


        //dp設定
        float scale = getResources().getDisplayMetrics().density;

        //テキスト表示
        TextView titleView = new TextView(this);
        titleView.setText("LED chica-chica");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);	// サイズ
        titleView.setTextColor(Color.rgb(0xcc, 0x00, 0x00));		// 色
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.setMargins(0, 0, 0, 0);


        TextView descriptionView = new TextView(this);
        descriptionView.setText("PLEASE ENTER YOUR Pi IP ADDRESS");
        descriptionView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);	// サイズ
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(0, 0, 0, (int)(10 * scale));

        // LayoutParamsの設定
        titleView.setLayoutParams(titleLayoutParams);
        descriptionView.setLayoutParams(textLayoutParams);

        // linerLayoutに追加
        linearLayout.addView(titleView);
        linearLayout.addView(descriptionView);

        //テキストフィールド作成
        final EditText editText = new EditText(this);

        String strHint = "0.0.0.0";   //初期値設定
        editText.setHint(strHint);

        // dp単位の横幅
        int editTextWidth = (int)(225 * scale);

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                editTextWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        editText.setLayoutParams(editTextParams);
        linearLayout.addView(editText);

        //ボタン作成
        final Button sendbutton = new Button(this);
        sendbutton.setText("OFF");
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(200, 200);
        buttonLayoutParams.setMargins(0, (int)(50 * scale),0, 0);
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.rgb(0x66,0x66,0x66), Color.rgb(0x66,0x66,0x66)});
        gradient.setCornerRadius(100);
        sendbutton.setLayoutParams(buttonLayoutParams);
        sendbutton.setBackground(gradient);
        sendbutton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);    // サイズ
        sendbutton.setTextColor(Color.rgb(0xff, 0xff, 0xff));    // 色

        linearLayout.addView(sendbutton);


        setContentView(linearLayout);
        linearLayout.setVisibility(View.VISIBLE);


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg++;
                if(flg % 2 == 0) {
                    sendbutton.setText("OFF");
                    GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.rgb(0x66,0x66,0x66), Color.rgb(0x66,0x66,0x66)});
                    gradient.setCornerRadius(100);
                    sendbutton.setBackground(gradient);
                }else{
                    sendbutton.setText("ON");
                    GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.rgb(0xff,0x66,0x00), Color.rgb(0xff,0x66,0x00)});
                    gradient.setCornerRadius(100);
                    sendbutton.setBackground(gradient);
                }

                Runnable sender = new Runnable() {
                    @Override
                    public void run() {
                        //IPアドレス
                        String address = editText.getText().toString();

                        //ポート番号
                        String strPort = "12345";
                        int port = Integer.parseInt(strPort);

                        Socket socket = null;

                        try {
                            socket = new Socket(address, port);
                            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

                            String sendTxt = "ON\0";
                            if(flg % 2 == 0) {
                                sendTxt = "OFF\0";
                            }
                            pw.println(sendTxt);


                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (socket != null) {
                            try {
                                socket.close();
                                socket = null;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Thread th = new Thread(sender);
                th.start();
            }
        });
    }
}
