package com.tns.espapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tns.espapp.AppConstraint;
import com.tns.espapp.R;
import com.tns.espapp.Utility.AppSingleton;
import com.tns.espapp.Utility.SharedPreferenceUtils;
import com.tns.espapp.Utility.Utility;
import com.tns.espapp.database.DatabaseHandler;
import com.tns.espapp.database.SettingData;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LockScreenActivity extends AppCompatActivity {
    private EditText employeePasswordEditText;
    private String empPassword;
    private SharedPreferenceUtils sharedPreferences;
    private ImageView verifyBtn;
    private TextView tnsMpinTextView;
    private TextView descriptionTextView;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        db = new DatabaseHandler(this);

        getLayoutsId();
        registerOnClickListener();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void getLayoutsId() {
        employeePasswordEditText = (EditText) findViewById(R.id.employeePasswordEditText);
        tnsMpinTextView = (TextView) findViewById(R.id.tnsMpinTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        verifyBtn = (ImageView) findViewById(R.id.verifyBtn);
        setFontFamily();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
private void setFontFamily()
{
    Typeface face = Typeface.createFromAsset(getAssets(),
            "arial.ttf");

    tnsMpinTextView.setTypeface(face);
    descriptionTextView.setTypeface(face);

}

    private boolean isMandatoryField() {
        employeePasswordEditText.setError(null);
        empPassword = employeePasswordEditText.getText().toString();

        if (TextUtils.isEmpty(empPassword)) {
            employeePasswordEditText.setError(getString(R.string.employeeIdError));
            return false;
        }
        return true;


    }

    private void registerOnClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.verifyBtn:
                        if (Utility.isNetworkAvailable(getApplicationContext())) {
                            if (isMandatoryField()) {
                                compareEmployeeId();
                            }
                        } else {
                            Utility.displayMessage(getApplicationContext(), "Network is not available");
                        }

                }


            }
        };
        verifyBtn.setOnClickListener(clickListener);
    }
    private void compareEmployeeId() {
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(getApplicationContext());
        String storedEmpId = sharedPreferences.getString(AppConstraint.EMPID);
        createSettingJsonRequest(storedEmpId, empPassword);




    }

    private JSONObject getUserSettingJsonObject(String empId, String empPassword) {

        JSONObject jsonObject = new JSONObject();
        String IMEINumber = Utility.getIMEINumber(this);

        try {


            jsonObject.put("DatabaseName", "TNS_HR");
            jsonObject.put("ServerName", "bkp-server");
            jsonObject.put("UserId", "sanjay");
            jsonObject.put("Password", "tnssoft");
            jsonObject.put("spName", "USP_GetDeviceSetting");
            jsonObject.put("loginID", empId);
            jsonObject.put("loginPassword", empPassword);
            jsonObject.put("Deviceid", IMEINumber);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private void createSettingJsonRequest(String empId, String empPassword) {
        Log.d("createSettJsonRequest", getUserSettingJsonObject(empId, empPassword).toString());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConstraint.VERIFYLOGINURL, getUserSettingJsonObject(empId, empPassword),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseSettingResponse(response);
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }

        });


        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, null);
    }

    private void parseSettingResponse(JSONObject response) {
        Log.d("printSettingResponse", response.toString());
        try {

            int gpsenabled_int;
            int status = response.getInt("status");
            String workingStatus = response.getString("EmpStatus");
            int GpSInterval  = response.getInt("GpSInterval");
            boolean GPSEnabled  = response.getBoolean("GPSEnabled");
            String GPSSpeed  = response.getString("GPSSpeed");



            if(GPSEnabled){

                gpsenabled_int = 1;
            }else{

                gpsenabled_int = 0;
            }

            if(status==0)
            {
                Utility.displayMessage(this,"Your record is not find in DataBase");
                employeePasswordEditText.setText("");
                employeePasswordEditText.setError(getString(R.string.check_employeePasswordError));
                return;
            }
            else
            {
                if(workingStatus.equalsIgnoreCase("NO"))
                {
                    Utility.displayMessage(this,"You are not Currently Working in Tns");
                    return;
                }

                db.deleteGPSsettingData();
                db.insertGPSSettingData(new SettingData(gpsenabled_int,GpSInterval,Integer.parseInt(GPSSpeed),"1"));
                Intent intent = new Intent(LockScreenActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}


