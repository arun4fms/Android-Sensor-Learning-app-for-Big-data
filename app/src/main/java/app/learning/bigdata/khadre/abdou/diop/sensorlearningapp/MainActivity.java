package app.learning.bigdata.khadre.abdou.diop.sensorlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.regex.Pattern;

/**
 * Created by Abdou Khadre DIOP on 22/02/2017.
 *
 * This activity contains the data entry interface
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //the button validate
    private Button validate;

    //adress entry field
    private EditText urlEditText;

    //user entry field
    private EditText userEditText;

    //port entry field
    private EditText portEdiText;

    //kind of data selector
    private Spinner chooseSpiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instantiate all objects
        setContentView(R.layout.activity_main);
        validate=(Button)findViewById(R.id.validate);
        urlEditText=(EditText)findViewById(R.id.editText2);
        userEditText=(EditText)findViewById(R.id.editText);
        portEdiText=(EditText)findViewById(R.id.port);
        chooseSpiner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chooseDataType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSpiner.setAdapter(adapter);
        validate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //when you click in the validate button
        if (v.getId() == R.id.validate) {

            //getting fields values
            String url=urlEditText.getText().toString();
            String user=userEditText.getText().toString();
            String port=portEdiText.getText().toString();
            String movementType=chooseSpiner.getSelectedItem().toString();

            //create the activity to get accelerator values
            Intent captureIntent=new Intent(this,Main2Activity.class);

            //send fields values to the second activity
            captureIntent.putExtra("adress",url);
            captureIntent.putExtra("port",port);
            captureIntent.putExtra("user",user);
            captureIntent.putExtra("movement",movementType);

            //start the second activity
            startActivity(captureIntent);
        }
    }

    private boolean checkRegularExpression(String pattern,String match){
        return Pattern.matches(pattern,match);
    }
}
