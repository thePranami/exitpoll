package com.example.thepranami.exitpoll;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class AddVoteFragment extends Fragment {

    Button addVoteButton;
    String Voter_id;
    Boolean LoginStatus;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    public AddVoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_add_vote, container, false);
        addVoteButton = (Button)fragmentView.findViewById(R.id.vote_button_id);
        sharedPreferences = getActivity().getSharedPreferences(AppData.Share_pref, Context.MODE_PRIVATE);
        Voter_id = sharedPreferences.getString("VOTER_ID", null);
        LoginStatus = sharedPreferences.getBoolean("LOGIN_STATUS", false);

        addVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Voter_id != null && LoginStatus){
                 new AddVoteTask().execute(Voter_id, "1");
                }

            }
        });

        return fragmentView;
    }
    public class AddVoteTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String RES = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(AppData.BASE_URL+"add_vote.php");
                ArrayList<NameValuePair> DataList = new ArrayList<>();
                DataList.add(new BasicNameValuePair("voter_id", strings[0]));
                DataList.add(new BasicNameValuePair("party_id", strings[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(DataList));
                HttpResponse httpResponse= httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                RES = EntityUtils.toString(httpEntity);
            }
            catch (Exception e){
                RES = e.toString();
            }
            return RES;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            if (s.equals("1")){
                builder.setMessage("Thanks for your valuable vote");
                builder.setCancelable(false);

                builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
            builder.show();
        }
    }

}
