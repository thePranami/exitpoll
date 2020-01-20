package com.example.thepranami.exitpoll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class VoterRegister extends AppCompatActivity {
    EditText VoterIdEditText, VoterNameEditText, VoterPasswordEditText, ConfirmPassEditText;
    Button SubmitButton, BackButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_register);
        VoterIdEditText=(EditText)findViewById(R.id.voterId);
        VoterNameEditText=(EditText)findViewById(R.id.voter_name);
        VoterPasswordEditText=(EditText)findViewById(R.id.voter_password);
        ConfirmPassEditText=(EditText)findViewById(R.id.confirm_password);
        SubmitButton=(Button)findViewById(R.id.submit_button);
        BackButton=(Button)findViewById(R.id.back_button);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VoterRegister.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String V_ID = VoterIdEditText.getText().toString();
             String V_NAME = VoterNameEditText.getText().toString();
             String V_PASSWORD = VoterPasswordEditText.getText().toString();
             String CONPASS = ConfirmPassEditText.getText().toString();

             if (V_ID.isEmpty() || V_NAME.isEmpty() || V_PASSWORD.isEmpty() || CONPASS.isEmpty()) {
                 Toast.makeText(VoterRegister.this, "Please fill all required field", Toast.LENGTH_SHORT).show();
             }

              else if (V_PASSWORD.equals(CONPASS)) {
                  new InserData().execute(V_ID, V_NAME, V_PASSWORD);
              }
              else {
                  Toast.makeText(VoterRegister.this, "Password are not matched", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }
    class InserData extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VoterRegister.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String RES = null;
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(AppData.BASE_URL+"voter_register.php");

                ArrayList<NameValuePair> arrayList = new ArrayList<>();
                arrayList.add(new BasicNameValuePair("voter_id", strings[0]));
                arrayList.add(new BasicNameValuePair("voter_name", strings[1]));
                arrayList.add(new BasicNameValuePair("password", strings[2]));

                httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                RES= EntityUtils.toString(httpEntity);
            }
            catch (Exception e)
            {
            RES=e.toString();
            }

            return RES;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            VoterIdEditText.setText("");
            VoterNameEditText.setText(null);
            VoterPasswordEditText.setText("");
            ConfirmPassEditText.setText("");
            Toast.makeText(VoterRegister.this, ""+s, Toast.LENGTH_SHORT).show();
        }
    }
}
