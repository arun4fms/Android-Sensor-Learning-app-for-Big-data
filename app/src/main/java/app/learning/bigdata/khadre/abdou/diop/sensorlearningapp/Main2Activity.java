package app.learning.bigdata.khadre.abdou.diop.sensorlearningapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author  Abdou Khadre DIOP
 * @since 22/02/2017
 * This activity get the accelerators values end sent it to a server
 */
public class Main2Activity extends AppCompatActivity implements SensorEventListener{

    //the timer object
    private CountDownTimer countDownTimer;

    //time remaining text
    private TextView timer;

    //sensor manager
    private SensorManager sensorManager;

    //sensor object
    private Sensor accelerator;

    //x velue of the moment
    private TextView xValue;

    //y value of the moment
    private TextView yValue;

    //z value of the moment
    private TextView zValue;

    // adress of the server
    private String adress;

    // port of application
    private String port;

    // user who send data
    private String user;

    // kind of movement
    private String movement;

    // all movement values
    private AllValues allValues;

    // json string sendable
    private String requestBody;

    // number of accelerometter values
    private int nbValues;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle bundle = getIntent().getExtras();

        // getting the values sended by the first activity
        adress=bundle.getString("adress");
        port=bundle.getString("port");
        user=bundle.getString("user");
        movement=bundle.getString("movement");


        timer=(TextView)findViewById(R.id.timer);
        xValue=(TextView)findViewById(R.id.x);
        yValue=(TextView)findViewById(R.id.y);
        zValue=(TextView)findViewById(R.id.z);

        // istantiate sensor manager, sensor and listen
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerator=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerator ,SensorManager.SENSOR_DELAY_FASTEST);

        allValues=new AllValues(user,movement);
        nbValues=0;
        Toast toast=Toast.makeText(this,"adress = "+adress+" user = "+user+" mov = "+movement+" port = "+port,Toast.LENGTH_LONG);
        toast.show();

        //the timer for 10 seconds
        countDownTimer=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("seconds remaining : "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                // calling the finish method when timer is finish
                finish();
            }
        }.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xValue.setText("x = "+event.values[0]);
        yValue.setText("y = "+event.values[1]);
        zValue.setText("z = "+event.values[2]);
        allValues.addValues(new AcceleratorValues(event.values[0],event.values[1],event.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
        Toast toast=Toast.makeText(this,allValues.getListAcceleratorValues().size()+" values Acquired ",Toast.LENGTH_LONG);
        toast.show();

        // transform AllValue object into a json string
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestBody = ow.writeValueAsString(allValues);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,  "http://"+adress+":"+port+"/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast toast=Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG);
                    toast.show();
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Log.i("data",new String(requestBody.getBytes("utf-8"), StandardCharsets.UTF_8));
                        }
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JsonProcessingException e2){
            e2.printStackTrace();
        }
    }



}
