package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    //Constant string key to communicate w/ QuizActivity when it calls newIntent()
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.bignerdranch.android.geoquiz.answer_is_true";

    //Constant string key to communicate w/ QuizActivity if answer was shown or not.
    private static final String EXTRA_ANSWER_SHOWN =
            "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String CHEAT_TAG = "CheatActivity";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    //Key-value pair to save Cheat status across rotation.
    private static final String KEY_CHEAT = "cheat";
    private int mQuizAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        /*To retrieve value from extra, use the following:
        public boolean getBooleanExtra(String name, boolean defaultValue)*/
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        /*Alternatively, you could use:
        Intent intent = getIntent();
        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);*/

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                    mQuizAnswer = R.string.true_button;
                }
                else{
                    mAnswerTextView.setText(R.string.false_button);
                    mQuizAnswer = R.string.false_button;
                }
                setAnswerShownResult(true);
            }
        });

        //Check to see if there's a previous saved state. If so, update mAnswerTextView
        //to that state if CheatActivity is destroyed
        if(savedInstanceState != null){
            //savedInstanceState.getInt(String key, int defaultValue); This returns an
            //integer used by setText()
            mAnswerTextView.setText(savedInstanceState.getInt(KEY_CHEAT, 0));
            Log.d(CHEAT_TAG, "from if(savedInstanceState)");
            setAnswerShownResult(true);
        }
    }

    /*Sending back an intent of whether the answer was shown to QuizActivity*/
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();

        //public Intent putExtra(String name, boolean value)
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);

        /*public final void setResult(int resultCode, Intent data)
        resultCode = Activity.RESULT_OK or Activity.RESULT_CANCELED,*/
        setResult(Activity.RESULT_OK, data);
    }

    //Encapsulating an intent that is properly configured with the extras CheatActivity will need
    //Method is created in CheatActivity because CheatActivity is the one requesting the boolean
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        //public Intent(Context packageContext, Class<?> cls)
        Intent i = new Intent(packageContext, CheatActivity.class);

        //public Intent putExtra(String name, boolean value)
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    /*Method to help decode the extra so that it can be used by QuizActivity.
    * When user presses the Show Answer button, method will report back to QuizActivity
    * that user did in fact cheat.*/
    public static boolean wasShownAnswer(Intent result){
        //getBooleanExtra(String name, boolean value)
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    /*Method normally called by the system before onPause(), onStop() and onDestroy()
    * The Bundle object is a structure that maps string keys to values of
    * certain limited types*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(CHEAT_TAG, "called from onSavedInstanceState");
        //savedInstanceState.putInt(String key, int value);
        savedInstanceState.putInt(KEY_CHEAT, mQuizAnswer);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(CHEAT_TAG, "called from onStart()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(CHEAT_TAG, "called from onPause()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(CHEAT_TAG, "called from onResume()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(CHEAT_TAG, "called from onStop()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(CHEAT_TAG, "called from onDestroy()");
    }
}
