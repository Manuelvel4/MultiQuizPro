package edu.upc.eseiaat.pma.myapplication;

import android.content.DialogInterface;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



public class MultiQuiPRO extends AppCompatActivity {
    public static final String CORRECT_ANSWER = "correct_answer";
    public static final String CORRENT_QUESTION = "corrent_question";
    public static final String ANSWER_CORRECT = "answer_correct";
    public static final String ANSWER = "answer";
    private String[] all_question;
    private TextView test_question;

    private int ids_answers[]={R.id.answer1, R.id.answer2,R.id.answer3, R.id.answer4};

    private int correct_answer;
    private int[]answer;
    private boolean[] answer_correct;
    private int corrent_question;


    private RadioGroup grup;
    private Button btn_next;
    private Button btn_previus;

    @Override
    protected void onStop() {
        Log.i("lifecycle","OnStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i("lifecycle","OnStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.i("lifecycle","OnDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.i("lifecycle","Onsave");
        super.onSaveInstanceState(outState); // objeto grande que contiene todos los datos que yo quiero preservar
        // guardo todo en mi mochila
        outState.putInt(CORRECT_ANSWER, correct_answer);
        outState.putInt(CORRENT_QUESTION, corrent_question);
        outState.putBooleanArray(ANSWER_CORRECT, answer_correct);
        outState.putIntArray(ANSWER,answer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("lifecycle","OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_qui_pro);

        test_question = (TextView)findViewById(R.id.test_question);
        grup = (RadioGroup) findViewById(R.id.radioGroup); // son mis referencias a objetos de la pantalla
        all_question = getResources().getStringArray(R.array.all_question);

        // lo hago aki porque ya se cuantas preguntas tengo





        // TODO; cuando clickan el voton tienen que pasar a la siguiente pagina
        // TODO con el boton

        btn_next = (Button) findViewById(R.id.btn_check);
        btn_previus = (Button) findViewById(R.id.bnt_previus); // declaro mi boton
        // declaro el otro boton

        if(savedInstanceState == null){Start_over();}
        else {  Bundle state = savedInstanceState;
            corrent_question = state.getInt(CORRENT_QUESTION);
            correct_answer = state.getInt(CORRECT_ANSWER);
            answer_correct = state.getBooleanArray(ANSWER_CORRECT);
            answer = state.getIntArray(ANSWER);
        }
        // siesk noe s la primera ves pues recuperare todo lo que tenia


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   /*
                if(answer == correct_answer){Toast.makeText(MultiQuizActivity.this,R.string.Correct, Toast.LENGTH_SHORT).show();}
                else {Toast.makeText(MultiQuizActivity.this,R.string.Mistake, Toast.LENGTH_SHORT).show();} */


                // quitamos el toasht

                CheckAnswer();
                // con esto de arriba guardo las que son correctas;

                if (corrent_question < all_question.length -1){
                    corrent_question ++;
                    ShowQuestion();}

                else {check_resolts();}



                /*for(int i= 0 ; i<answer_correct.length ; i++){

                    Log.i("CORRECION", String.format("respuesta %d: %b {%b}", i, answer[i], answer_correct[i]));



                } */
            }});
        btn_previus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAnswer();

                if(corrent_question > 0){corrent_question --;

                }
                ShowQuestion();


            }
        });


    }

    private void Start_over() {
        answer_correct = new  boolean[all_question.length];
        answer = new int[all_question.length];

        for (int i =0; i < answer.length; i++){
            answer[i] = -1;
            // no se lo que has respondido es la 0,1,????
        }
        corrent_question = 0;

    }

    private void check_resolts() {
        int correct = 0, incorrect =0, no_contestadas=0;
        for (int i=0; i <all_question.length;i++){
            if(answer_correct[i]) correct++;
            else if(answer[i] == -1) no_contestadas++;
            else incorrect++;
        }

        // TODO permitir traduccion de este texto
        String SMS =
                String.format("Correctas : %d \n Incorrectas: %d \n No  contestadas: %d\n ",  correct , incorrect, no_contestadas );

        AlertDialog.Builder builder = new AlertDialog.Builder(this); // este nos construye el cuadro de dialogo

        builder.setTitle(R.string.resultados);
        builder.setMessage(SMS);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        builder.setNegativeButton(R.string.star_over, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // borrar respuesas y volver a al pregunta 0

                Start_over();





            }
        });



        builder.create().show();

    }

    private void CheckAnswer() {
        int id = grup.getCheckedRadioButtonId();
        int ans = -1;
        for(int i = 0; i < ids_answers.length; i++){


            if (ids_answers[i]==id){ans =i;}}


        answer_correct [corrent_question] = (ans == correct_answer);
        answer [corrent_question] = ans;
    }

    private void ShowQuestion() {
        String q = all_question[corrent_question];
        String [] parts = q.split(";");
        test_question.setText(parts[0]);
        grup.clearCheck();

        for(int i=0 ; i <ids_answers.length ; i++){
            RadioButton ans1 = (RadioButton)findViewById(ids_answers[i]);
            String answers = parts[i+1];
            if (answers.charAt(0) == '*'){
                correct_answer = i;
                answers = answers.substring(1);
            }

            ans1.setText(answers);

            if(answer[corrent_question] == i){ans1.setChecked(true);}
        }

        if (corrent_question == 0) { btn_previus.setVisibility(View.GONE);
        }
        else {btn_previus.setVisibility(View.VISIBLE);}


        if (corrent_question == all_question.length -1){
            btn_next.setText(R.string.finish);}
        else {btn_next.setText(R.string.Next);}

    }
}
