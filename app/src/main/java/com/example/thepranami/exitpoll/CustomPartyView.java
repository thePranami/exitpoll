package com.example.thepranami.exitpoll;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomPartyView extends RecyclerView.Adapter<CustomPartyView.MyViewHolder> {
    ArrayList<PartyViewModel> partyViewModels;
    Context context;

    public CustomPartyView(ArrayList<PartyViewModel> partyViewModels, Context context) {
        this.partyViewModels = partyViewModels;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements RecyclerView.RecyclerListener{

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.party_name);
            imageView = itemView.findViewById(R.id.party_image);

           imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.party_list_container, new AddVoteFragment()).commit();
                }
            });
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            int ClickedPosition = holder.getAdapterPosition();
            PartyViewModel viewModel = partyViewModels.get(ClickedPosition);
            Toast.makeText(context, "Calling...", Toast.LENGTH_SHORT).show();
            viewModel.getId();
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.party_list_container, new AddVoteFragment()).commit();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_party_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String PartyName = partyViewModels.get(position).getPartyName();
        holder.textView.setText(PartyName);

        try {
            String ImageName = partyViewModels.get(position).getImageUrl();
            Picasso.with(context).load(AppData.BASE_URL_IMAGE+ImageName)
                    .into(holder.imageView);

        }
        catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return partyViewModels.size();
    }
}
