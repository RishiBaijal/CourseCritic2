package in.iiitd.pcsma.coursecritic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {


    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        addListenerOnRatingBar();
        addListenerOnButton();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> questionList = new ArrayList<String>();
        questionList.add("What is your attendance?");
        questionList.add("What is the amount of time per week that you put in?");
        questionList.add("What is your opinion of the instructor's ability to convey the key concepts? (Please be civil)");
        questionList.add("What is your opinion of the TA's ability to solve your doubts? (Please be civil)");
        questionList.add("How much did the course assignments help you in learning the course material?");
        questionList.add("How much did the course project help you in learning the course material?");
        questionList.add("Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, questionList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);




    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                String courseRating = String.valueOf(rating);
                System.out.println(";"+courseRating+";");
                if (courseRating.equals("1.0")) {
                    txtRatingValue.setText("God-awful.");
                }
                else if (courseRating.equals("2.0"))
                {
                    txtRatingValue.setText("Painfully average");
                }
                else if (courseRating.equals("3.0"))
                {
                    txtRatingValue.setText("Satisfactory");
                }
                else if (courseRating.equals("4.0"))
                {
                    txtRatingValue.setText("Will happily do it again.");
                }
                else
                {
                    txtRatingValue.setText("Awesometacular");
                }

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(ReviewActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("onItemSelected gets executed. ");
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        EditText visibleEditText = (EditText) findViewById(R.id.visibleEditText);
        EditText hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
        hiddenEditText.setVisibility(View.GONE);
        if (item.equalsIgnoreCase("Other"))
        {
            hiddenEditText.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_review);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//    }

}
