package ru.ifmo.md.lesson4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//Sergey Budkov 2536

public class MainActivity extends Activity {

    TextView tv;
    String expr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        tv = (TextView) findViewById(R.id.textView);
        setText(expr);
    }


    void setText(String s){
        tv.setText(expr);
    }

    public void one(View V){
        expr+="1";
        setText(expr);
    }

    public void two(View V){
        expr+="2";
        setText(expr);
    }

    public void three(View V){
        expr+="3";
        setText(expr);
    }

    public void four(View V){
        expr+="4";
        setText(expr);
    }

    public void five(View V){
        expr+="5";
        setText(expr);
    }

    public void six(View V){
        expr+="6";
        setText(expr);
    }

    public void seven(View V){
        expr+="7";
        setText(expr);
    }

    public void eight(View V){
        expr+="8";
        setText(expr);
    }

    public void nine(View V){
        expr+="9";
        setText(expr);
    }

    public void zero(View V){
        expr+="0";
        setText(expr);
    }

    public void operatorAdd(View V){
        expr+="+";
        setText(expr);
    }

    public void operatorDiv(View V){
        expr+="/";
        setText(expr);
    }

    public void operatorMul(View V){
        expr+="*";
        setText(expr);
    }

    public void operatorSub(View V){
        expr+="-";
        setText(expr);
    }

    public void leftBracket(View V){
        expr+="(";
        setText(expr);
    }

    public void rightBracket(View V){
        expr+=")";
        setText(expr);
    }

    public void delete(View V){
        String expr1 = "";
        for(int i = 0;i < expr.length()-1;i++){
            expr1+=expr.charAt(i);
        }
        expr = expr1;
        setText(expr);
    }

    public void point(View V){
        expr+=".";
        setText(expr);
    }

    public void clean(View V){
        expr = "";
        setText(expr);
    }



    public void startCalculate(View v){
        double result;
        try {
            result = CalculationEngineFactory.defaultEngine().calculate(expr);
            String res = String.valueOf(result);
            tv.setText(res);
            expr = "";
        } catch (CalculationException e){
            Toast.makeText(this, "Invalid input",
                    Toast.LENGTH_LONG).show();
        }
    }



}
