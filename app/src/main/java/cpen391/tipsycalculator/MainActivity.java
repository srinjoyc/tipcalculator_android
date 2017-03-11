package cpen391.tipsycalculator;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {

    TextView display = null;
    double displayedNumber = 0;
    boolean has_decimal = false;
    int decimal_places = 0;
    int overflow_counter = 0;
    TextView mtotal = null;
    double total = 0;

    TextView mtotalPerPerson = null;
    double totalPerPerson = 0;

    TextView mtipPercentage = null;
    SeekBar mtipPercentageSeek = null;
    double tipPercentage = 0;

    TextView mnumberOfPeople = null;
    SeekBar mnumberOfPeopleSeek = null;
    int people = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView) findViewById(R.id.display);

        mtipPercentageSeek = (SeekBar) findViewById(R.id.tipPercentageSeek);
        mtipPercentage = (TextView) findViewById(R.id.tipPercentage);

        mnumberOfPeopleSeek = (SeekBar) findViewById(R.id.numberOfPeopleSeek);
        mnumberOfPeople = (TextView) findViewById(R.id.numberOfPeople);

        mtotal = (TextView) findViewById(R.id.totalPrice);
        mtotalPerPerson = (TextView) findViewById(R.id.totalPricePerPerson);

        mnumberOfPeopleSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 1){
                    progress = 1;
                }
                people = progress;
                Log.v("calculate", "peopleprogress " + progress);
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() < 1){
                    mnumberOfPeopleSeek.setProgress(1);
                }
            }
        });
        mtipPercentageSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipPercentage = progress;
                Log.v("calculate", "tipprogress " + progress);
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void numberPressed(View v){
        int currentDigit= 0;
        double divisor = 1.0;
        boolean decimal_pressed = false;

        Button pushedButton = (Button) v;
        if(pushedButton.getId() == findViewById(R.id.decimal).getId()){
            decimal_pressed = true;
            has_decimal = true;
        } else {
            currentDigit = Integer.parseInt(pushedButton.getText().toString());
            if(!has_decimal) {
                overflow_counter++;
            }
            if(overflow_counter > 3){
                return;
            }
        }

        displayedNumber = Double.parseDouble(display.getText().toString());

        if(has_decimal && !decimal_pressed){
            decimal_places++;
            if(decimal_places  > 2){
                return;
            }
            for(int i=0; i < decimal_places; i++){
                divisor = divisor/10;
            }
            displayedNumber = displayedNumber  + currentDigit*divisor;
        }else if(!decimal_pressed) {
            displayedNumber = displayedNumber * 10 + currentDigit;
        }
        display.setText(String.format("%.2f", displayedNumber));
        calculate();
    }

    public void resetPressed(View v){

        displayedNumber = 0.0;
        decimal_places = 0;
        has_decimal = false;
        overflow_counter = 0;
        total = 0;
        totalPerPerson = 0;
        tipPercentage = 0;
        people = 1;
        renderDefaultValues();
    }

    public void calculate(){
        total = displayedNumber*(tipPercentage/100.0) + displayedNumber;
        totalPerPerson = total/people;
        renderValues();
    }
    public void renderValues(){

        mtipPercentage.setText(String.format("%.2f %%",tipPercentage));
        mnumberOfPeople.setText(String.valueOf(people));
        mtotal.setText(String.format("$ %.2f",total));
        mtotalPerPerson.setText(String.format("$ %.2f",totalPerPerson));
    }
    public void renderDefaultValues(){

        mnumberOfPeopleSeek.setProgress(1);
        mtipPercentageSeek.setProgress(0);
        display.setText("0");
        mtipPercentage.setText("");
        mnumberOfPeople.setText(String.valueOf(people));
        mtotal.setText("0");
        mtotalPerPerson.setText("0");
    }
}
