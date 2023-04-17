package com.example.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class MainActivity extends AppCompatActivity {

    private int numOne, numTwo;
    private int opOne, opTwo, opThree, opFour, operation, ans;
    private int totalQues = 0,totalAns = 0;
    boolean isCompleted = false;

    Random random;

    Button startBtn, op1, op2, op3, op4;
    ImageView brainTrainer;
    TextView tvQues, tvNoOfAns, tvTimer;
    private KonfettiView kv;

    public void settingVisibility() {
        startBtn.setVisibility(View.INVISIBLE);
        startBtn.setClickable(false);
        brainTrainer.setVisibility(View.INVISIBLE);
        tvTimer.setVisibility(View.VISIBLE);
        tvQues.setVisibility(View.VISIBLE);
        tvNoOfAns.setVisibility(View.VISIBLE);
        op1.setVisibility(View.VISIBLE);
        op2.setVisibility(View.VISIBLE);
        op3.setVisibility(View.VISIBLE);
        op4.setVisibility(View.VISIBLE);
    }

    public void initializations() {
        startBtn = findViewById(R.id.btnStart);
        brainTrainer = findViewById(R.id.ivBrainTrainer);
        tvNoOfAns = findViewById(R.id.tvNoOfAns);
        tvQues = findViewById(R.id.tvQuestion);
        tvTimer = findViewById(R.id.tvTimer);
        op1 = findViewById(R.id.btnOp1);
        op2 = findViewById(R.id.btnOp2);
        op3 = findViewById(R.id.btnOpt3);
        op4 = findViewById(R.id.btnOpt4);
        kv = findViewById(R.id.konfettiView);
        random = new Random();
    }

    public void calc() {
        String text;
        numOne = random.nextInt(20)+1;
        numTwo = random.nextInt(20)+1;
        operation = random.nextInt(4)+1;
        switch (operation) {
            case 1: ans = numOne + numTwo;
                    text = Integer.toString(numOne)+"+"+Integer.toString(numTwo);
                    break;
            case 2: ans = numOne - numTwo;
                    text = Integer.toString(numOne)+"-"+Integer.toString(numTwo);
                    break;
            case 3: ans = numOne * numTwo;
                    text = Integer.toString(numOne)+"*"+Integer.toString(numTwo);
                    break;
            case 4: ans = numOne / numTwo;
                    text = Integer.toString(numOne)+"/"+Integer.toString(numTwo);
                    break;
            default: ans = numOne % numTwo;
                    text = Integer.toString(numOne)+"%"+Integer.toString(numTwo);
        }
        tvQues.setText(text);
        ArrayList<Integer> options = new ArrayList<Integer>(Arrays.asList(ans, ans+2, ans-2, ans+1));
        Collections.shuffle(options);
        Log.i("ANSWER",Integer.toString(ans));

        op1.setText(Integer.toString(options.get(0)));
        op2.setText(Integer.toString(options.get(1)));
        op3.setText(Integer.toString(options.get(2)));
        op4.setText(Integer.toString(options.get(3)));



    }

    public void disableBtn (boolean bool) {
        op1.setClickable(bool);
        op2.setClickable(bool);
        op3.setClickable(bool);
        op4.setClickable(bool);
    }

    public void checkAnsSwitch(int idBtn,int ans) {
        int check = 0;
        switch (idBtn) {
            case R.id.btnOp1: check = Integer.parseInt(op1.getText().toString());
                break;
            case R.id.btnOp2: check = Integer.parseInt(op2.getText().toString());
                break;
            case R.id.btnOpt3: check = Integer.parseInt(op3.getText().toString());
                break;
            case R.id.btnOpt4: check = Integer.parseInt(op4.getText().toString());
                break;
            default: ;

        }
        if(check == ans) {
            Log.i("If","Correct Ans");
            totalAns++;
        }
        Log.i(Integer.toString(check),Integer.toString(ans));
        tvNoOfAns.setText(totalAns+"/"+totalQues);
    }

    public void checkAns(View view) {
        totalQues++;
        int idBtn = view.getId();
        checkAnsSwitch(idBtn, ans);
        calc();

    }

    public void yay() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        kv.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build()
        );
    }

    public void start(View view) {
        settingVisibility();
        if(isCompleted) {
            disableBtn(true);
            tvNoOfAns.setText("0/0");
        }
        new CountDownTimer (30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<10000)
                    tvTimer.setText("0:0"+String.valueOf(millisUntilFinished/1000));
                else
                    tvTimer.setText("0:"+String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                disableBtn(false);
                float per = (float) totalAns/totalQues;
                if(per>0.8){
                    Toast toast = Toast.makeText(MainActivity.this, "BRAVO ! You got "+(int)(per*100)+"%", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if(per>0.5) {
                    Toast toast = Toast.makeText(MainActivity.this, "You did fine ! You got "+(int)(per*100)+"%", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Eh You got " + (int) (per * 100) + "%", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                yay();
                playAgain();
            }
        }.start();
        calc();
        isCompleted = true;

    }

    public void playAgain() {
        startBtn.setText("TRY AGAIN");
        startBtn.setVisibility(View.VISIBLE);
        startBtn.setClickable(true);
        totalQues = 0;
        totalAns = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initializations();

       startBtn.setText("START");
    }
}