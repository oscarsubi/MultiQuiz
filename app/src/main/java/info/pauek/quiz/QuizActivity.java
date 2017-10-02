package info.pauek.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int ids_answers[] = {
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };

    //Shift + F6 para cambiar nombres de variables
    private int correct_answer; //Variable global
    private int actual_question; //Variable global
    private String[] all_questions; //(*)Variable global (es pot utilitzar a subprogrames)
    private boolean[] check_answers; //Variable global
    private int [] num_answer; //Variable global
    private TextView text_question; //Variable global
    private RadioGroup group; //Variable global
    private Button btn_next, btn_back; //Variable global

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Elements layout
        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup) findViewById(R.id.answer_group);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_back = (Button) findViewById(R.id.btn_back);
        //(*) all_ questions=g... Apretar Alt+Enter > Crees una variable field (global)
        all_questions = getResources().getStringArray(R.array.all_questions);
        //Array string per preguntes i respostes
        check_answers = new boolean[all_questions.length];
        //Array de booleans per saber quantes respostes estàn bé o malament
        num_answer = new int [all_questions.length];
        //Número de preguntes i respostes

        for (int i = 0; i < all_questions.length; i++){
            num_answer[i] = -1; //Cap resposta seleccionada
        }

        actual_question = 0;
        show_question();  // Refactor > Extract > Method... Para guardar parte en un subprograma



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_answer();

                if(actual_question < all_questions.length-1) { //Perque no deixi seguir si acaba
                    actual_question++;  // Pasar de pregunta
                    show_question();  // Mostrarla
                }
                else{
                    int correct=0, incorrect=0;
                    boolean b = true;

                    // for (boolean b : check_questions)
                    for (int i = 0; i < check_answers.length; i++) {
                        if(b == check_answers[i]) correct++;
                        else incorrect++;
                    }


                    //Mostrar en un solo String
                    String resultc = String.format("Respuestas correctas: %d", correct);
                    String resulti = String.format("Respuestas incorrectas: %d", incorrect);

                    Toast.makeText(QuizActivity.this, resultc, Toast.LENGTH_LONG).show();
                    Toast.makeText(QuizActivity.this, resulti, Toast.LENGTH_LONG).show();
                    //finish();  Per si volem que es surti del programa
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_answer();
                if(actual_question > 0){
                    actual_question--;
                    show_question();
                }
            }

        });
    }




    private void save_answer() {
        int id = group.getCheckedRadioButtonId();
        int answer = -1;

        for (int i = 0; i < ids_answers.length; i++) {
            if (ids_answers[i] == id) {
                answer = i;
            }
        }
        //El check answers guardara respostes correctes
        check_answers[actual_question] = (answer == correct_answer);  // == -> comparació
        num_answer[actual_question] = answer;
    }




    private void show_question() {

        String q = all_questions [actual_question];
        String [] parts = q.split(";");  //Separem Strings amb ;

        group.clearCheck();  //Elimina el botó marcat a la pregunta anterior

        text_question.setText(parts[0]); //Pregunta és la part 0 del array de strings

        for (int i = 0; i < ids_answers.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            String answer = parts[i + 1];

            if (answer.charAt(0) == '*') { //La resposta correcte te un * a la posició 0
                correct_answer = i;
                answer = answer.substring(1); // La resposta només es mostra a partir de posició 1
            }

            rb.setText(answer);
            if (num_answer[actual_question] == i) {
                rb.setChecked(true);
            }

        }

        if(actual_question == 0){
            btn_back.setVisibility(View.GONE);  //S'elimina el botó amb el GONE
        }
        else{
            btn_back.setVisibility(View.VISIBLE);  //Es mostra ek botó amb el VISIBLE
        }

        if(actual_question == all_questions.length-1){  //Cambi noms botons
            btn_next.setText(R.string.finish);
        }
        else{
            btn_next.setText(R.string.next);
        }

    }
}
