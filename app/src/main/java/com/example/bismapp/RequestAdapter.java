package com.example.bismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bismapp.ui.modifyTeams.TeamMRFragment;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    public ArrayList<Request> requests;
    private RequestFrag activity;

    public RequestAdapter(ArrayList<Request> requests, Context cntx) {
        this.requests = requests;
       //this.activity = (RequestFrag) activity;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request, parent, false);
        TextView name = view.findViewById(R.id.member_name);
        TextView id = view.findViewById(R.id.member_id);
        return new ViewHolder(view, name, id);
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
        private TextView id;
        private Request req;

        public ViewHolder(View view, TextView name, TextView id) {
            super(view);
            this.name = name;
            this.id = id;

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
            id.setText(request.getSenderID());
            req = request;
        }
    }
}
