package com.example.thepranami.exitpoll;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.os.Build.ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartyListFragment extends Fragment {
    RecyclerView PartyListRecyclerView;
    ProgressDialog progressDialog;

    public PartyListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_party_list, container, false);
        PartyListRecyclerView = fragmentView.findViewById(R.id.party_list);
        new FetchPartyListTask().execute();
        return fragmentView;
    }
    public class FetchPartyListTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String RES = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(AppData.BASE_URL+"fetch_party_list.php");

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                RES = EntityUtils.toString(httpEntity);

            } catch (Exception e) {
                RES = e.toString();
            }
            return RES;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            ArrayList<PartyViewModel> viewModels = new ArrayList<>();
              try {
                  JSONArray jsonArray = new JSONArray(s);
                  for (int i = 0; i < jsonArray.length(); i++) {
                      JSONObject TempJsonOb = jsonArray.getJSONObject(i);

                      String ID = TempJsonOb.getString("id");
                      String PARTY_NAME = TempJsonOb.getString("party_name");
                      String CANDIDATE_NAME = TempJsonOb.getString("candidate_name");
                      String PARTY_SYMBOL = TempJsonOb.getString("party_symbol");

                      viewModels.add(new PartyViewModel(ID, PARTY_NAME, CANDIDATE_NAME, PARTY_SYMBOL));
                  }
                      RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                      PartyListRecyclerView.setLayoutManager(layoutManager);
                      CustomPartyView customPartyView = new CustomPartyView(viewModels, getActivity());
                      PartyListRecyclerView.setAdapter(customPartyView);

                      RecyclerView.RecyclerListener recyclerListener = new RecyclerView.RecyclerListener() {
                          @Override
                          public void onViewRecycled(RecyclerView.ViewHolder holder) {
                              Toast.makeText(getActivity(), String.valueOf(holder.getPosition()), Toast.LENGTH_SHORT).show();
                          }
                      };
                  }
              catch (Exception e){

              }
           Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
        }
    }

}
