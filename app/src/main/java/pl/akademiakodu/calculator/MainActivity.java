package pl.akademiakodu.calculator;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
    Button plus, minus, multiply, divide, sqrt;
    Button equals, clear;
    TextView result;

    StringBuilder numberOne;
    StringBuilder numberTwo;

    String dzialanie;
    ProgressBar progress;

    List<String> numbers =Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    List<String> dzialania = Arrays.asList("+", "-", "*", "/", "sqrt");

    private double resultNumber;
    private boolean showResult;
    private boolean isCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberOne = new StringBuilder();
        numberTwo = new StringBuilder();

        dzialanie = "";
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setIndeterminate(true);
        progress.setVisibility(View.GONE);

        b0 = (Button) findViewById(R.id.zero);
        b0.setOnClickListener(this);
        b1 = (Button) findViewById(R.id.one);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.two);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.three);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.four);
        b4.setOnClickListener(this);
        b5 = (Button) findViewById(R.id.five);
        b5.setOnClickListener(this);
        b6 = (Button) findViewById(R.id.six);
        b6.setOnClickListener(this);
        b7 = (Button) findViewById(R.id.seven);
        b7.setOnClickListener(this);
        b8 = (Button) findViewById(R.id.eight);
        b8.setOnClickListener(this);
        b9 = (Button) findViewById(R.id.nine);
        b9.setOnClickListener(this);

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(this);
        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(this);
        multiply = (Button) findViewById(R.id.multiply);
        multiply.setOnClickListener(this);
        divide = (Button) findViewById(R.id.divide);
        divide.setOnClickListener(this);
        sqrt = (Button) findViewById(R.id.sqrt);
        sqrt.setOnClickListener(this);

        equals = (Button) findViewById(R.id.equals);
        equals.setOnClickListener(this);
        clear = (Button) findViewById(R.id.cllear);
        clear.setOnClickListener(this);

        result = (TextView) findViewById(R.id.result);

    }

    private void addNumber(String number){
        if (dzialanie == null | dzialanie.equals("")){
            numberOne.append(number);
        } else{
            numberTwo.append(number);
        }
        showOperation();
    }

    private boolean check(){
        if (numberOne.toString().equals("")){
            Toast.makeText(this, "Uzupełnij pierwszą liczbę - nawet jeśli tylko pierwiastkujesz!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dzialanie.equals("")){
            Toast.makeText(this, "Uzupełnij znak działania!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numberTwo.toString().equals("")){
            Toast.makeText(this, "Uzupełnij drugą liczbę!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dzialanie.equals("/")) {
            if(numberTwo.toString().equals("0")){
                Toast.makeText(this, "Nie wolno dzielić przez ZERO!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean calculate (){

        double number1 = Double.valueOf(numberOne.toString());
        double number2 = Double.valueOf(numberTwo.toString());
        int operationResult = 0;
        switch (dzialanie){
            case "/":
                if (number2==0){
                    Toast.makeText(this, "Nie wolno dzielić przez ZERO!", Toast.LENGTH_SHORT).show();
                    return false;
                }else{
                    resultNumber = number1/ number2;
                } break;
            case "*": resultNumber = number1 * number2; break;
            case "+": resultNumber = number1 + number2; break;
            case "-": resultNumber = number1 - number2; break;
            case "sqrt":
                if (number2<0){
                    Toast.makeText(this, "Pierwiastek z liczb ujemnych nie istnieje!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    resultNumber = Math.sqrt(number2); break;
                }
        }
        return true;
    }

    private void showOperationResult(double number){
        result.setText(showOperation()+"="+ number);
        showResult = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showResult){
                    clean();
                }
            }
        }, 8000);

    }

    private void clean (){
        numberOne = new StringBuilder();
        numberTwo = new StringBuilder();
        dzialanie = "";
        result.setText("Brak");
    }

    private String showOperation(){
        if (numberOne.toString().equals("") | dzialanie.equals("sqrt")){
            result.setText(dzialanie+" "+numberTwo.toString());
            return dzialanie + " " +numberTwo.toString();
        } else {
            result.setText(numberOne.toString()+" "+dzialanie+" "+numberTwo.toString());
            return numberOne.toString()+" "+dzialanie+" "+numberTwo.toString();
        }
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        if (numbers.contains(b.getText())) {
            addNumber(b.getText().toString());
        } else if (dzialania.contains(b.getText())) {
            dzialanie = b.getText().toString();
            showOperation();
            if (showResult){
                showResult=false;
                clean();
            }
        } else{
            if (view.getId() == equals.getId()){
                    new CalTask().execute();
            } else if(view.getId() == clear.getId()){
                    clean();
            }
        }

    }

    private class CalTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            // ta metoda wykonuje się w innym wątku - wątek poboczny
            // należy pamiętać, że edycję widżetów, layoutu itd (komponenty graficzne) możemy wykonywać tylko w wątku głównym!!!!!!!
            if (isCorrect){
                calculate();
            }
            for (int i=0; i<=3; i++){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            progress.setVisibility(View.GONE);
            showOperationResult(resultNumber);
        }

        @Override
        protected void onPreExecute (){
            isCorrect = check();
            progress.setVisibility(View.VISIBLE);
        }

    }
}
