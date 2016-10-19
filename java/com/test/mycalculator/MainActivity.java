package com.test.mycalculator;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    int operator = 0, eql = 0;
    double ans=0;
    String text="",error="0",exp;
    Boolean point = false,calcView = true,mr=false;
    ArrayList<String> history;
    File readWriteHistory;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(calcView == true)
        {
            TextView display = (TextView) findViewById(R.id.display);
            exp = display.getText().toString();
        }
        outState.putString("DISPLAY_TEXT",exp);
        outState.putInt("OPERATOR",operator);
        outState.putInt("EQL",eql);
        outState.putDouble("ANS",ans);
        outState.putString("TEXT",text);
        outState.putString("ERROR",error);
        outState.putBoolean("POINT",point);
        outState.putBoolean("CALCULATOR_VIEW",calcView);
        outState.putBoolean("MR",mr);
        Log.d("test","onSaveInstateState called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        exp=savedInstanceState.getString("DISPLAY_TEXT");
        calcView=savedInstanceState.getBoolean("CALCULATOR_VIEW");
        if(calcView == true)
        {
            TextView display = (TextView) findViewById(R.id.display);
            display.setText(exp);
        }
        else
        {
            showHistory();
        }
        operator=savedInstanceState.getInt("OPERATOR");
        eql=savedInstanceState.getInt("EQL");
        ans=savedInstanceState.getDouble("ANS");
        text=savedInstanceState.getString("TEXT");
        error=savedInstanceState.getString("ERROR");
        point=savedInstanceState.getBoolean("POINT");
        mr=savedInstanceState.getBoolean("MR");
        Log.e("test","onRestoreInstateState called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        history = new ArrayList<String>();
        readWriteHistory = new File(this.getFilesDir(), "history");
        try {
            Scanner sc = new Scanner(readWriteHistory);

            while(sc.hasNextLine()) {
                String str = sc.nextLine();
                history.add(str);
            }
            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void show_display(String text) {
        TextView display = (TextView) findViewById(R.id.display);
        display.setText(text);
        display.setMovementMethod(new ScrollingMovementMethod());
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    public void btnNumEvent(View view) {
        final Button btnNum = (Button)view;
        if(eql==1 || mr==true){
            text = "";
            eql = 0;
            if(mr==true)
            {
                mr=false;
            }
            else
            {
                operator=0;
            }
        }

        text+=btnNum.getText().toString();
        show_display(text);
    }

    public void btnOperatorEvent(View view) {
        final Button btnOperator = (Button)view;
        if(mr==true)
        {
            mr=false;
        }
        String operatorSign = btnOperator.getText().toString();
        if(operator!=0 && eql==0){

            if(isDouble(text))
            {
                if(operator==1){
                    addHistory(ans+"+"+Double.parseDouble(text)+"="+(ans + Double.parseDouble(text)));
                    ans = ans + Double.parseDouble(text);
                }
                if(operator==2){
                    addHistory(ans+"-"+Double.parseDouble(text)+"="+(ans - Double.parseDouble(text)));
                    ans = ans - Double.parseDouble(text);
                }
                if(operator==3){
                    addHistory(ans+"*"+Double.parseDouble(text)+"="+(ans * Double.parseDouble(text)));
                    ans = ans * Double.parseDouble(text);
                }
                if(operator==4){
                    if (Double.parseDouble(text) == 0) {
                        error = "Error:Divided by Zero";
                        addHistory(ans + "/" + Double.parseDouble(text) + "=" + error);
                        ans = 0;
                    } else {
                        addHistory(ans + "/" + Double.parseDouble(text) + "=" + (ans / Double.parseDouble(text)));
                        ans = ans / Double.parseDouble(text);
                    }
                }
            }
        }
        else{
            if(eql==0)
            {
                if(!isDouble(text)) text = "0";
                ans = Double.parseDouble(text);
            }
            else{
                eql=0;
            }
        }
        if(error.equals("0"))
            show_display(String.valueOf(ans));
        else{
            show_display(error);
            error="0";
        }
        if(operatorSign.equals("+"))
        {
            operator = 1;
        }
        if(operatorSign.equals("-"))
        {
            operator = 2;
        }
        if(operatorSign.equals("*"))
        {
            operator = 3;
        }
        if(operatorSign.equals("/"))
        {
            operator = 4;
        }
        text = "";
    }

    public void btnEqualEvent(View view) {
        final Button btnEqual = (Button) view;
        if(operator!=0){
            if(isDouble(text))
            {
                if(operator==1){
                    addHistory(ans+"+"+Double.parseDouble(text)+"="+(ans + Double.parseDouble(text)));
                    ans = ans + Double.parseDouble(text);
                }
                if(operator==2){
                    addHistory(ans+"-"+Double.parseDouble(text)+"="+(ans - Double.parseDouble(text)));
                    ans = ans - Double.parseDouble(text);
                }
                if(operator==3){
                    addHistory(ans+"*"+Double.parseDouble(text)+"="+(ans * Double.parseDouble(text)));
                    ans = ans * Double.parseDouble(text);
                }
                if(operator==4){
                    if (Double.parseDouble(text) == 0) {
                        error = "Error:Divided by Zero";
                        addHistory(ans + "/" + Double.parseDouble(text) + "=" + error);
                        ans = 0;
                    } else {
                        addHistory(ans + "/" + Double.parseDouble(text) + "=" + (ans / Double.parseDouble(text)));
                        ans = ans / Double.parseDouble(text);
                    }
                }
            }
        }else
        {
            if(!isDouble(text)) text = "0";
            ans = Double.parseDouble(text);
            addHistory(ans+"="+ans);
        }
        eql = 1;
        if(error.equals("0"))
            show_display(String.valueOf(ans));
        else
        {
            show_display(error);
            error="0";
        }
    }
    public void btnPointEvent(View view) {
        final Button btnPoint = (Button) view;
        if(eql==1 || mr==true){
            text = "";
            eql = 0;
            if(mr == true)
            {
                mr = false;
            }
            else
            {
                operator=0;
            }
        }
        if(isDouble(text + '.')){
            text+= '.';
        }else if(text.length()==1 && text.charAt(0)=='-'){
            text+='.';
        }else if(text.length()==0) text = ".";
        show_display(text);
    }

    public void btnSignedEvent(View view) {
        final Button btnSigned = (Button) view;
        if(eql ==0 && mr== false)
        {
            if (text.length() > 0) {
                if(text.charAt(0)!='-') text = "-"+text;
                else
                    text = text.substring(1, text.length());
            }else{
                text = "-";
            }
        }
        else {
            text = "-";
            eql = 0;
            if (mr == true) {
                mr = false;
            }
            else
            {
                operator = 0;
            }
        }

        show_display(text);
    }

    public void btnDelEvent(View view) {
        final Button btnDel = (Button) view;
        if(eql==0 && mr == false)
        {
            if (text.length() > 0) {
                text = text.substring(0, text.length()-1);
            }
            show_display(text);
        }
    }
    public void btnClearEvent(View view) {
        final Button btnClear = (Button) view;
        operator = 0;
        ans = 0;
        text = "";
        eql=0;
        mr=false;
        show_display(text);
    }
    public void btnHistoryEvent(View view) {
        final Button btnHistory = (Button) view;
        TextView display = (TextView) findViewById(R.id.display);
        exp=display.getText().toString();
        showHistory();

    }
    public void showHistory()
    {
        ListView lv = new ListView(this);
        Collections.reverse(history);
        ArrayAdapter<String> v = new ArrayAdapter<String>(this, R.layout.my_list_item, history);
        lv.setAdapter(v);
        this.setContentView(lv);
        calcView = false;
    }
    void addHistory(String hist) {
        history.add(hist);
        try {
            FileWriter fw = new FileWriter(readWriteHistory, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(hist);
            bw.write("\r\n");
            bw.close();
            fw.close();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if(!calcView) {
            Collections.reverse(history);
            setContentView(R.layout.activity_main);
            calcView = true;
            TextView display = (TextView) findViewById(R.id.display);
            display.setText(exp);
        }
        else {
            super.onBackPressed();
        }
    }

    public void btnMplusEvent(View view) {
        final Button Mplus = (Button) view;
        double memory;
        TextView display = (TextView) findViewById(R.id.display);
        String value = display.getText().toString();
        if(value.equals(""))
        {
            value="0";
        }
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String temp = sharedPref.getString("memory", "");
        if(temp.equals(""))
        {
            temp="0";
        }
        memory=Double.parseDouble(value);
        memory += Double.parseDouble(temp);
        temp = String.valueOf(memory);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("memory", temp);
        editor.commit();
    }

    public void btnMminusEvent(View view) {
        final Button Mminus = (Button) view;
        double memory;
        TextView display = (TextView) findViewById(R.id.display);
        String value = display.getText().toString();
        if(value.equals(""))
        {
            value="0";
        }
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String temp = sharedPref.getString("memory", "");
        if(temp.equals(""))
        {
            temp="0";
        }
        memory=Double.parseDouble(temp);
        memory -= Double.parseDouble(value);
        temp = String.valueOf(memory);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("memory", temp);
        editor.commit();
    }

    public void btnMCEvent(View view) {
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("memory","");
        editor.commit();
    }

    public void btnMREvent(View view) {
        SharedPreferences sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String temp = sharedPref.getString("memory", "");
        if(temp.equals("")== false)
        {
            mr=true;
        }
        text=temp;
        show_display(text);
    }

}
