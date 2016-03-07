package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;

    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private static final String TAG = "QuizActivity";
    //Key-value pair to save Question mCurrentIndex across rotation.
    private static final String KEY_INDEX ="index";
    /*"Request code" is a user defined code that is sent to child activity
    and is received back by the parent.*/
    private static final int REQUEST_CODE_CHEAT = 0;

    /*Creating an array of Questions.*/
    private final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asias, true),
    };

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResID();
        mQuestionTextView.setText(question);
    }

    /*When user presses True or False, will check user's answer against the current
    * question and display the appropriate Toast message to user*/
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResID;

        if (mIsCheater){
            messageResID = R.string.judgment_toast;
        } else{
            if(userPressedTrue == answerIsTrue)
                messageResID = R.string.correct_toast;
            else
                messageResID = R.string.incorrect_toast;
        }
        //Toast.makeText(Context context, int resID, int duration).show()
        Toast.makeText(this, messageResID, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Called from: onCreate(Bundle)");

        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity

                /*public Intent(Context packageContext, Class<?> cls)
                packageContext = tells ActivityManager which application package activity class is found in
                cls = specifies activity class ActivityManager should start*/

                //public static Intent newIntent(Context packageContext, boolean answerIsTrue)
                //public void startActivity(Intent intent)
                //Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                //Or startActivity(new Intent((QuizActivity.this, CheatActivity.class));

                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

                //public static Intent newIntent(Context packageContext, boolean answerIsTrue)
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);

                /*public void startActivity(Intent intent)
                startActivity() replaced by startActivityForResult() which is used when
                you want to hear back from the child activity
                startActivityForResult(Intent intent, int requestCode)*/
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        updateQuestion();
    }

    @Override
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data)
    * The parameters are the original requestCode from QuizActivity, and the resultCode
    * and data are passed into setResult() from CheatActivity
    * From CheatActivity, when the user presses the BACK button to return to QuizActivity,
    * the ActivityManager will call onActivityResult*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasShownAnswer(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "from onSaveInstanceState");
        //saveInstanceState.putInt(String key, int value);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "called from onStart()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "called from onPause()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "called from onResume()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "called from onStop()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "called from onDestroy()");
    }

}
