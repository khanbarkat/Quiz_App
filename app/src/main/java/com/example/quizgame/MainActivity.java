package com.example.quizgame;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SoundManager soundManager;

    private int selectedOperation = ADDITION;  // Default to ADDITION

    private static final int ADDITION = 0;
    private static final int SUBTRACTION = 1;
    private static final int MULTIPLICATION = 2;
    private static final int DIVISION = 3;
    private static final int RANDOM = 4;

    private LinearLayout startButton;
    private LinearLayout subtractButton;
    private LinearLayout multiplyButton;
    private LinearLayout divisionButton;
    private LinearLayout randomButton;
    private LinearLayout gamelayout;
    private RelativeLayout dashbord;
    private RelativeLayout gameover;
    private TextView questionTextView;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;
    private Button exitButton;
    private Button gotohome;
    private TextView finalscoretv;
    private TextView timerTextView;
    private ProgressBar progressBar;
    private Button questionNumberTextView;
    private int currentQuestionNumber = 1;

    private Random random = new Random();
    private int score = 0;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;
    private CountDownTimer timer;

    private TextView scoreTextView;
    private int correctColor;
    private int incorrectColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout exitLayout = findViewById(R.id.exit);
        Button exitButton = findViewById(R.id.exitbutton);
        Button gotohome = findViewById(R.id.gotohome);

        View.OnClickListener exitClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Exit")
                        .setMessage("Are you sure you want to exit the app?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        exitLayout.setOnClickListener(exitClickListener);
        exitButton.setOnClickListener(exitClickListener);


        soundManager = new SoundManager(this);

        correctColor = ContextCompat.getColor(this, R.color.correctColor);
        incorrectColor = ContextCompat.getColor(this, R.color.incorrectColor);

        startButton = findViewById(R.id.addition_layout);
        subtractButton = findViewById(R.id.subtract_button);
        multiplyButton = findViewById(R.id.multiply_button);
        divisionButton = findViewById(R.id.division_button);
        randomButton = findViewById(R.id.random_button);
        gamelayout = findViewById(R.id.gamelayout);
        dashbord = findViewById(R.id.dashboard);
        gameover = findViewById(R.id.gameoverscreen);
        progressBar = findViewById(R.id.progressBar);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        finalscoretv = findViewById(R.id.final_score);
        questionNumberTextView = findViewById(R.id.questionNumberTextView);

        // Set visibility
        gamelayout.setVisibility(View.GONE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long durationInMillis = 10000;
                long intervalInMillis = 100;

                new CountDownTimer(durationInMillis, intervalInMillis) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int progress = (int) (millisUntilFinished * 100 / durationInMillis);
                        progressBar.setProgress(progress);

                        int secondsRemaining = (int) (millisUntilFinished / 1000);
                        timerTextView.setText(String.valueOf(secondsRemaining));
                    }

                    @Override
                    public void onFinish() {
                        timerTextView.setText("0");
                    }
                }.start();
            }
        });

        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        timerTextView = findViewById(R.id.timerTextView);
        progressBar = findViewById(R.id.progressBar);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(ADDITION);
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperation = SUBTRACTION;
                startGame(SUBTRACTION);
            }
        });

        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperation = MULTIPLICATION;
                startGame(MULTIPLICATION);
            }
        });

        divisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperation = DIVISION;
                startGame(DIVISION);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperation = RANDOM;
                startGame(RANDOM);
            }
        });
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionSelected(v);
            }
        });
        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionSelected(v);
            }
        });
        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionSelected(v);
            }
        });
        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionSelected(v);
            }
        });

        changeStatusBarColor("#673AB7"); // Replace with your desired color code

        gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameover.setVisibility(View.GONE);
                dashbord.setVisibility(View.VISIBLE);
            }
        });

        Button playagain = findViewById(R.id.play_again);

        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(selectedOperation);
                gameover.setVisibility(View.GONE);
            }
        });

    }
    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(android.graphics.Color.parseColor(color));
        }
    }


    private void startGame(int selectedOperation) {
        option1Button.setBackgroundResource(R.drawable.rounded_button_background);
        option2Button.setBackgroundResource(R.drawable.rounded_button_background);
        option3Button.setBackgroundResource(R.drawable.rounded_button_background);
        option4Button.setBackgroundResource(R.drawable.rounded_button_background);

        dashbord.setVisibility(View.GONE);
        gamelayout.setVisibility(View.VISIBLE);
        score = 0;
        currentQuestionIndex = 0;
        showNextQuestion(selectedOperation);
        score = 0;
        updateScoreDisplay();
    }

    private void showNextQuestion(int selectedOperation) {
        currentQuestion = generateRandomQuestion(selectedOperation);
        updateQuestionUI();
        startTimer();
        questionNumberTextView.setText("    Question " + currentQuestionNumber + "/50    ");
        currentQuestionNumber++;
    }

    private void updateQuestionUI() {
        questionTextView.setText(currentQuestion.getQuestion());
        List<String> options = currentQuestion.getOptions();
        option1Button.setText(options.get(0));
        option2Button.setText(options.get(1));
        option3Button.setText(options.get(2));
        option4Button.setText(options.get(3));
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        final long duration = 10000L; // Total duration of the timer in milliseconds
        final long interval = 50L; // Update interval in milliseconds

        timer = new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((duration - millisUntilFinished) * 100 / duration);
                progressBar.setProgress(progress);

                long timeLeft = millisUntilFinished / 1000;
                timerTextView.setText(String.valueOf(timeLeft));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                timerTextView.setText("Time's up!");
                endGame();
            }
        }.start();

        soundManager.stopSound();
        soundManager.playSound(R.raw.timer);
    }

    private Question generateRandomQuestion(int operation) {
        int num1 = random.nextInt(100);
        int num2;
        switch (operation) {
            case ADDITION:
            case SUBTRACTION:
            case MULTIPLICATION:
                num2 = random.nextInt(100) + 1; // Adjust the range as needed
                break;
            case DIVISION:
                num2 = generateRandomDivisor(num1);
                break;
            default:
                num2 = random.nextInt(100) + 1; // Adjust the range as needed for other cases
                break;
        }

        String question;
        int correctAnswer;

        switch (operation) {
            case ADDITION:
                question = num1 + " + " + num2 + "?";
                correctAnswer = num1 + num2;
                break;
            case SUBTRACTION:
                question = num1 + " - " + num2 + "?";
                correctAnswer = num1 - num2;
                break;
            case MULTIPLICATION:
                question = num1 + " * " + num2 + "?";
                correctAnswer = num1 * num2;
                break;
            case DIVISION:
                question = num1 + " / " + num2 + "?";
                correctAnswer = num1 / num2;
                break;
            case RANDOM:
                int randomOperation = random.nextInt(4);
                return generateRandomQuestion(randomOperation);
            default:
                question = num1 + " + " + num2 + "?";
                correctAnswer = num1 + num2;
                break;
        }

        List<String> options = generateOptions(correctAnswer);

        return new Question(question, options, options.indexOf(String.valueOf(correctAnswer)));
    }

    private int generateRandomDivisor(int dividend) {
        List<Integer> possibleDivisors = new ArrayList<>();
        for (int i = 1; i <= dividend; i++) {
            if (dividend % i == 0) {
                possibleDivisors.add(i);
            }
        }
        return possibleDivisors.get(random.nextInt(possibleDivisors.size()));
    }

    private List<String> generateOptions(int correctAnswer) {
        List<String> options = new ArrayList<>();
        options.add(String.valueOf(correctAnswer));

        while (options.size() < 4) {
            int wrongAnswer = correctAnswer + random.nextInt(20) - 10;
            if (wrongAnswer != correctAnswer && !options.contains(String.valueOf(wrongAnswer))) {
                options.add(String.valueOf(wrongAnswer));
            }
        }

        Collections.shuffle(options);
        return options;
    }
    private void endGame() {
        Toast.makeText(this, "Game Over! Your score: " + score, Toast.LENGTH_SHORT).show();
        finalscoretv.setText("Score " + score);

        soundManager.stopSound();
        soundManager.playSound(R.raw.gameover);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                gameover.setVisibility(View.VISIBLE);
                gamelayout.setVisibility(View.GONE);
                soundManager.stopSound();
            }
        }, 1000);

        if (timer != null) {
            timer.cancel();
        }
        currentQuestionNumber = 1;
    }

    public void onOptionSelected(View view) {
        int selectedOption;
        if (view.getId() == R.id.option1Button) {
            selectedOption = 0;
        } else if (view.getId() == R.id.option2Button) {
            selectedOption = 1;
        } else if (view.getId() == R.id.option3Button) {
            selectedOption = 2;
        } else if (view.getId() == R.id.option4Button) {
            selectedOption = 3;
        } else {
            selectedOption = -1;
        }


        if (selectedOption == currentQuestion.getCorrectOption()) {
            score += 10;  // Increase the score by 10
            updateScoreDisplay();
            view.setBackgroundColor(correctColor);
            soundManager.stopSound();
            soundManager.playSound(R.raw.correctanswer);
        } else {
            view.setBackgroundColor(incorrectColor);
            Toast.makeText(this, "Wrong! Game Over.", Toast.LENGTH_SHORT).show();
            endGame();
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setBackgroundResource(R.drawable.rounded_button_background);
                currentQuestionIndex++;
                showNextQuestion(selectedOperation);  // <-- Pass selectedOperation here
            }
        }, 1000);
    }
    private void updateScoreDisplay() {
        scoreTextView.setText("Score: " + score);
    }

}