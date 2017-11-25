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
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //Other variables
    Map<String,Boolean> numbersMap;
    String [] allowedNumbersNames;
    Boolean [] allowedNumbersBoolean;
    String [] mathFunctionNames = {"Addition","Subtraction","Multiplication","Division"};
    char [] mathSymbols = {'+','-','x','÷'};
    int mathFunction = 0;
    int userAnswer = 0;
    int variable1;
    int variable2;
    int correctAnswer;
    Vibrator vibratorThing;


    //GUI variables
    TextView messageText;
    Button refreshButtonView;
    TextView variable1View;
    EditText answerView;
    Button answerButtonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize allowedNumbersBoolean[], true values are included
        allowedNumbersBoolean = new Boolean[10];
        for(int i = 0; i < allowedNumbersBoolean.length; i++){
            allowedNumbersBoolean[i] = true;
        }

        //instantiate array of the names of allowed numbers
        allowedNumbersNames = new String [10];
        for(int i =0; i<10; i++){
            allowedNumbersNames[i] = Integer.toString(i);
        }

        //initialize HashMap of included numbers, default all true
        numbersMap = new HashMap<String,Boolean>();
        for(String number : allowedNumbersNames){
            numbersMap.put(number,true);
        }

        //instantiate vibrator service
        vibratorThing = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        //GUI references
        messageText = (TextView) findViewById(R.id.messageText);
        refreshButtonView = (Button) findViewById(R.id.refreshButtonView);
        variable1View = (TextView) findViewById(R.id.variable1View);
        answerView = (EditText) findViewById(R.id.answerView);
        answerButtonView = (Button) findViewById(R.id.answerButtonView);

        //set listeners
        refreshButtonView.setOnClickListener(refresh_OnClickListener);
        answerButtonView.setOnClickListener(checkAnswer_OnClickListener);

        //to auto-show keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        //start the game
        resetNumbers();
    }

    public void resetNumbers(){
        //Generate allowable, random numbers
        variable1 = generateNum();
        variable2 = generateNum();

        //calculate the answer depending on the function
        //addition
        if (mathFunction==0) {
            correctAnswer = variable1 + variable2;
        }
        //subtraction
        else if(mathFunction==1) {
            correctAnswer = variable1 - variable2;
        }
        //multiplication
        else if(mathFunction==2) {
            correctAnswer = variable1 * variable2;
        }
        //division
        else {
            correctAnswer = variable1 / variable2;
        }

        //Update GUI
        messageText.setText("Enter guess:");
        variable1View.setText(buildMathExpression());
        answerView.getText().clear();
    }

    //generate random integer from 0 to 9
    public int generateNum() {
        //keeps generating random number until it is allowed
        while (true) {
            int temp = (int) (Math.random() * 10);
            if (checkNum(temp)) {
                return temp;
            }
        }
    }

    //check if number is allowed
    public boolean checkNum(int number) {

        return numbersMap.get(Integer.toString(number));
    }

    public  void buzz(int i){
        vibratorThing.vibrate(i);
    }


    //for GUI presentation
    public String buildMathExpression(){
        String temp = "";
        temp = Integer.toString(variable1) + " ";
        temp += mathSymbols[mathFunction] + " ";
        temp += Integer.toString(variable2);
        return temp;
    }


    //assign userAnswer variable from GUI
    public boolean checkUserAnswerIsValid(){
        String temp = answerView.getText().toString();
        try {
            userAnswer = Integer.parseInt(temp);
        }
        catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    //run when user presses Check Answer button
    public void checkAnswer(){
        //pull valid user answer from the EditText
        if(checkUserAnswerIsValid()) {
            //compare user answer to the correct answer
            if (correctAnswer == userAnswer) {
                messageText.setText("Correct");
                buzz(200);
                resetNumbers();
                messageText.setText("Enter guess:");
                return;
            }
            buzz(500);
            messageText.setText("Try Again");
            answerView.getText().clear();
        }
    }

//    //called by Menu option when user selects addition, subtraction...
//    public  boolean setMathFunction(int i){
//        //ensure parameter is valid 0-3
//        //default to addition
//        if(i > 3 || i < 0){
//            mathFunction = 0;
//            return false;
//        }
//        mathFunction = i;
//        return true;
//    }

    View.OnClickListener refresh_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            resetNumbers();
        }
    };

    View.OnClickListener checkAnswer_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkAnswer();
        }
    };

}
