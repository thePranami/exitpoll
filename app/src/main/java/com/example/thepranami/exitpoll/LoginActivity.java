package com.example.thepranami.exitpoll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText VoterIdEditText , PasswordEditText;
    Button LoginButton, RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VoterIdEditText=findViewById(R.id.voter_id);
        PasswordEditText=findViewById(R.id.password);
        LoginButton=findViewById(R.id.login_button);
        RegisterButton=findViewById(R.id.register_button);

        sharedPreferences = getSharedPreferences(AppData.Share_pref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        RegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(LoginActivity.this, VoterRegister.class);
              startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String VoterId = VoterIdEditText.getText().toString();
                String Password = PasswordEditText.getText().toString();

                new VoterLoginTask().execute(VoterId , Password);
            }
        });

    }
    public class VoterLoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String RES = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
               // HttpPost httpPost = new HttpPost("http://192.168.43.125/exit_poll/voter_login.php");
                HttpPost httpPost = new HttpPost(AppData.BASE_URL+"voter_login.php");

                ArrayList<NameValuePair> DataList = new ArrayList<>();

                DataList.add(new BasicNameValuePair("voter_id", strings[0]));
                DataList.add(new BasicNameValuePair("password", strings[1]));

                httpPost.setEntity(new UrlEncodedFormEntity(DataList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                RES = EntityUtils.toString(httpEntity);
            }
            catch (Exception e) {
                RES = e.toString();
            }
            return RES;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

            if (s.equals("1")) {
                //Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                builder.setMessage("Login successfull");
                builder.setCancelable(false);

                builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("LOGIN_STATUS", true);
                        editor.putString("VOTER_ID", VoterIdEditText.getText().toString());
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });

            }
            else if (s.equals("0")) {

                builder.setMessage("Invalid voter id or password");
                builder.setCancelable(false);

                builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

            }
            builder.show();
        }
    }
}