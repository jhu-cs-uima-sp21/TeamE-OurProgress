package com.example.bismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    public ArrayList<Request> requests;
    private static Context cntx;

    public RequestAdapter(ArrayList<Request> requests, Context cntx) {
        this.requests = requests;
        this.cntx = cntx;
       //this.activity = (RequestFrag) activity;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request, parent, false);
        TextView name = view.findViewById(R.id.member_name);
        TextView station = view.findViewById(R.id.station);
        return new ViewHolder(view, name, station);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.setData(request);
    }


    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        requests.add(request);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return requests.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView station;
        private Request req;

        public ViewHolder(View view, TextView name, TextView station) {
            super(view);
            this.name = name;
            this.station = station;

            ImageButton remove_request_btn = (ImageButton) view.findViewById(R.id.remove_request_btn);
            remove_request_btn.setOnClickListener((View.OnClickListener) btnview  -> {
                btnview.startAnimation(MainActivity.buttonClick);
                Toast toast = Toast.makeText(btnview.getContext(),
                        requests.get(getAdapterPosition()).getSenderID()
                                + " has been removed from team", Toast.LENGTH_SHORT);
                toast.show();
                //try {
                //    activity.checkToRemoveTeamMember(getAdapterPosition(), member);
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}
            });
        }

        public void setData(Request request) {
            station.setText(request.getSenderID());
            req = request;
        }
    }

}
