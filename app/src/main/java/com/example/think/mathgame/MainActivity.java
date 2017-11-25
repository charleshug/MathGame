package com.example.think.mathgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.os.Vibrator;


public class MainActivity extends AppCompatActivity {

    TextView messageText;
    Button refreshButtonView;
    TextView variable1View;
    EditText answerView;
    Button answerButtonView;
    Boolean [] checkBoxBoolean = {false,false,false,false,false,false,false,false,false,false,};
    Vibrator vibratorThing;

    String[] exclusionsNames;

    String [] mathFunctionNames = {"Addition","Subtraction","Multiplication","Division"};
    char [] mathSymbols = {'+','-','x','÷'};
    int mathFunction = 0;
    int variable1;
    int variable2;
    int correctAnswer;
    int userAnswer = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GUI references
        messageText = (TextView) findViewById(R.id.messageText);
        refreshButtonView = (Button) findViewById(R.id.refreshButtonView);
        variable1View = (TextView) findViewById(R.id.variable1View);
        answerView = (EditText) findViewById(R.id.answerView);
        answerButtonView = (Button) findViewById(R.id.answerButtonView);

        //assign string values to the array, but allow the array to be defined before onCreate
        exclusionsNames = new String [10];
        for(int i =0; i<10; i++){
            exclusionsNames[i] = Integer.toString(i);
        }

        //set listeners
        refreshButtonView.setOnClickListener(refresh_OnClickListener);
        answerButtonView.setOnClickListener(checkAnswer_OnClickListener);

        //instantiate vibrator service
        vibratorThing = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //to auto-show keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //start the game
        generateNumbers2();

    }

    //generate random number that is not excluded
    private int generateNum(){
        int temp;
        //create random int and check if it's an excluded number
        while(true) {

            temp = (int) (Math.random() * 10);
            //if random number is NOT excluded, return number to caller
            if (!checkBoxBoolean[temp]) {
                return temp;
            }
        }
    }

    private void buzz(int i){
        vibratorThing.vibrate(i);
    }

    private boolean setMathFunction(int i){
        //ensure parameter is valid 0-3
        //default to addition
        if(i > 3 || i < 0){
            mathFunction = 0;
            return false;
        }
        mathFunction = i;
        return true;
    }

    private String buildMathExpression(){
        String temp = "";
        temp = Integer.toString(variable1) + " ";
        temp += mathSymbols[mathFunction] + " ";
        temp += Integer.toString(variable2);
        return temp;
    }


    private void generateNumbers2(){

        //Generate random numbers
        variable1 = generateNum();
        variable2 = generateNum();

        if (mathFunction==0) {
            correctAnswer = variable1 + variable2;
        }
        //for subtraction
        else if(mathFunction==1) {
            correctAnswer = variable1 - variable2;
        }
        //for multiplication
        else if(mathFunction==2) {
            correctAnswer = variable1 * variable2;
        }
        //for division
        else {
            correctAnswer = variable1 / variable2;
        }

        //Update GUI
        messageText.setText("Enter guess:");
        variable1View.setText(buildMathExpression());
        answerView.getText().clear();
    }

    private boolean setUserAnswer(){
        String temp = answerView.getText().toString();
        try {userAnswer = Integer.parseInt(temp);}
        catch(NumberFormatException e) {return false;}
        return true;
    }

    private void checkAnswer(){
        //pull valid user answer from the EditText
        if(setUserAnswer()) {
            //compare user answer to the correct answer
            if (correctAnswer == userAnswer) {
                generateNumbers2();
                buzz(100);
                return;
            }
            buzz(500);
            messageText.setText("Try Again");
            answerView.getText().clear();
        }
    }

    View.OnClickListener refresh_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            generateNumbers2();
        }
    };

    View.OnClickListener checkAnswer_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkAnswer();
        }
    };

}
