package mik.voice.siri.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import mik.voice.siri.R;
import mik.voice.siri.global;

public class calculator extends Fragment implements View.OnClickListener {
    View view;

    private double valueOne = Double.NaN;
    private double valueTwo;
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';

    private char CURRENT_ACTION;
    DecimalFormat decimalFormat = new DecimalFormat("#.##########");
    static Button one;
    static Button two;
    static Button three;
    static Button four;
    static Button five;
    static Button six;
    static Button seven;
    static Button eight;
    static Button nine;
    static Button zero;
    static Button add;
    static Button min;
    static Button mul;
    static Button div;
    static Button is;
    static Button Clear;
    static Button Back;
    static Button dot;
    EditText calculate;
    TextView Result;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calculator, container, false);
        calculate = view.findViewById(R.id.calculate);
        Result = view.findViewById(R.id.result);
        one = view.findViewById(R.id.btn_1);
        two = view.findViewById(R.id.btn_2);
        three = view.findViewById(R.id.btn_3);
        four = view.findViewById(R.id.btn_4);
        five = view.findViewById(R.id.btn_5);
        six = view.findViewById(R.id.btn_6);
        seven = view.findViewById(R.id.btn_7);
        eight = view.findViewById(R.id.btn_8);
        nine = view.findViewById(R.id.btn_9);
        zero = view.findViewById(R.id.btn_0);
        add = view.findViewById(R.id.btn_add);
        min = view.findViewById(R.id.btn_min);
        mul = view.findViewById(R.id.btn_mul);
        div = view.findViewById(R.id.btn_div);
        is = view.findViewById(R.id.btn_is);
        Clear = view.findViewById(R.id.btn_clear);
        Back = view.findViewById(R.id.btn_back);
        dot = view.findViewById(R.id.btn_dot);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        add.setOnClickListener(this);
        min.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        is.setOnClickListener(this);
        Clear.setOnClickListener(this);
        Back.setOnClickListener(this);
        dot.setOnClickListener(this);


        return view;
    }
    private void computeCalculation() {
        if(!Double.isNaN(valueOne)) {
            valueTwo = Double.parseDouble(calculate.getText().toString());
            calculate.setText(null);

            if(CURRENT_ACTION == ADDITION)
                valueOne = this.valueOne + valueTwo;
            else if(CURRENT_ACTION == SUBTRACTION)
                valueOne = this.valueOne - valueTwo;
            else if(CURRENT_ACTION == MULTIPLICATION)
                valueOne = this.valueOne * valueTwo;
            else if(CURRENT_ACTION == DIVISION)
                valueOne = this.valueOne / valueTwo;
        }
        else {
            try {
                valueOne = Double.parseDouble(calculate.getText().toString());
            }
            catch (Exception e){}
        }
    }
    public static void speech_calculate(String input){
        switch (input){
            case "0":
                zero.callOnClick();
                break;
            case "1":
                one.callOnClick();
                break;
            case "2":
                two.callOnClick();
                break;
            case "3":
                three.callOnClick();
                break;
            case "4":
                four.callOnClick();
                break;
            case "5":
                five.callOnClick();
                break;
            case "6":
                six.callOnClick();
                break;
            case "7":
                seven.callOnClick();
                break;
            case "8":
                eight.callOnClick();
                break;
            case "9":
                nine.callOnClick();
                break;
            case "add":
                add.callOnClick();
                break;
            case "sub":
                min.callOnClick();
                break;
            case "mul":
                mul.callOnClick();
                break;
            case "div":
                div.callOnClick();
                break;
            case "dot":
                dot.callOnClick();
                break;
            case "is":
                is.callOnClick();
                break;
            case "clear":
                Clear.callOnClick();
                break;
            case "back":
                Back.callOnClick();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_0:
                calculate.setText(String.format("%s0", calculate.getText().toString()));
                break;
            case R.id.btn_1:
                calculate.setText(String.format("%s1", calculate.getText().toString()));
                break;
            case R.id.btn_2:
                calculate.setText(String.format("%s2", calculate.getText().toString()));
                break;
            case R.id.btn_3:
                calculate.setText(String.format("%s3", calculate.getText().toString()));
                break;
            case R.id.btn_4:
                calculate.setText(String.format("%s4", calculate.getText().toString()));
                break;
            case R.id.btn_5:
                calculate.setText(String.format("%s5", calculate.getText().toString()));
                break;
            case R.id.btn_6:
                calculate.setText(String.format("%s6", calculate.getText().toString()));
                break;
            case R.id.btn_7:
                calculate.setText(String.format("%s7", calculate.getText().toString()));
                break;
            case R.id.btn_8:
                calculate.setText(String.format("%s8", calculate.getText().toString()));
                break;
            case R.id.btn_9:
                calculate.setText(String.format("%s9", calculate.getText().toString()));
                break;
            case R.id.btn_is:
                computeCalculation();
               Result.setText(Result.getText().toString() +
                        decimalFormat.format(valueTwo) + " = " + decimalFormat.format(valueOne));
                valueOne = Double.NaN;
                CURRENT_ACTION = '0';
                break;
            case R.id.btn_mul:
                computeCalculation();
                CURRENT_ACTION = MULTIPLICATION;
                Result.setText(decimalFormat.format(valueOne) + "*");
                calculate.setText(null);
                break;
            case R.id.btn_div:
                computeCalculation();
                CURRENT_ACTION = DIVISION;
                Result.setText(decimalFormat.format(valueOne) + "/");
                calculate.setText(null);
                break;
            case R.id.btn_add:
                computeCalculation();
                CURRENT_ACTION = ADDITION;
                Result.setText(decimalFormat.format(valueOne) + "+");
                calculate.setText(null);
                break;
            case R.id.btn_min:
                computeCalculation();
                CURRENT_ACTION = SUBTRACTION;
                Result.setText(decimalFormat.format(valueOne) + "-");
                calculate.setText(null);
                break;
            case R.id.btn_clear:
                Result.setText(null);
                calculate.setText(null);
                break;
            case R.id.btn_back:
                if(calculate.getText().toString().length()>0) {
                    calculate.setText(calculate.getText().toString().substring(0, calculate.getText().toString().length() - 1));
                }
                break;
            case R.id.btn_dot:
                calculate.setText(String.format("%s.", calculate.getText().toString()));
                break;
        }
    }
}
