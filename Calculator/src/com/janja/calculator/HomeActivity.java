package com.janja.calculator;

import com.janja.calculator.expression.InfixBuilder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends ActionBarActivity implements OnClickListener {

    private TextView result;
    private Button zero;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button sum;
    private Button subtract;
    private Button multiply;
    private Button divide;
    private Button point;
    private Button back;
    private Button clear;
    private Button equal;

    private InfixBuilder infixBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        infixBuilder = new InfixBuilder();
        initialViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialViews() {
        result = (TextView) findViewById(R.id.result);
        zero = (Button) findViewById(R.id.zero);
        zero.setOnClickListener(this);
        one = (Button) findViewById(R.id.one);
        one.setOnClickListener(this);
        two = (Button) findViewById(R.id.two);
        two.setOnClickListener(this);
        three = (Button) findViewById(R.id.three);
        three.setOnClickListener(this);
        four = (Button) findViewById(R.id.four);
        four.setOnClickListener(this);
        five = (Button) findViewById(R.id.five);
        five.setOnClickListener(this);
        six = (Button) findViewById(R.id.six);
        six.setOnClickListener(this);
        seven = (Button) findViewById(R.id.seven);
        seven.setOnClickListener(this);
        eight = (Button) findViewById(R.id.eight);
        eight.setOnClickListener(this);
        nine = (Button) findViewById(R.id.nine);
        nine.setOnClickListener(this);
        sum = (Button) findViewById(R.id.sum);
        sum.setOnClickListener(this);
        subtract = (Button) findViewById(R.id.subtract);
        subtract.setOnClickListener(this);
        multiply = (Button) findViewById(R.id.multiply);
        multiply.setOnClickListener(this);
        divide = (Button) findViewById(R.id.divide);
        divide.setOnClickListener(this);
        point = (Button) findViewById(R.id.point);
        point.setOnClickListener(this);
        equal = (Button) findViewById(R.id.equal);
        equal.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == zero) {
            infixBuilder.addElement("0");
        } else if (view == one) {
            infixBuilder.addElement("1");
        } else if (view == two) {
            infixBuilder.addElement("2");
        } else if (view == three) {
            infixBuilder.addElement("3");
        } else if (view == four) {
            infixBuilder.addElement("4");
        } else if (view == five) {
            infixBuilder.addElement("5");
        } else if (view == six) {
            infixBuilder.addElement("6");
        } else if (view == seven) {
            infixBuilder.addElement("7");
        } else if (view == eight) {
            infixBuilder.addElement("8");
        } else if (view == nine) {
            infixBuilder.addElement("9");
        } else if (view == sum) {
            infixBuilder.addElement("+");
        } else if (view == subtract) {
            infixBuilder.addElement("-");
        } else if (view == multiply) {
            infixBuilder.addElement("*");
        } else if (view == divide) {
            infixBuilder.addElement("/");
        } else if (view == point) {
            infixBuilder.addElement(".");
        } else if (view == back) {
            infixBuilder.removeLast();
        } else if (view == clear) {
            infixBuilder.clear();
        } else if (view == equal) {
            String resultText = infixBuilder.getInfix().evaluate();
            infixBuilder.clear();
            infixBuilder.addElement(resultText);
            result.setText(resultText);
            return;
        }
        updateUI(infixBuilder.getTempInfix());
    }

    private void updateUI(String content) {
        result.setText(content);
    }
}
