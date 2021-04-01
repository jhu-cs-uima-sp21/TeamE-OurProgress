package com.example.bismapp.ui.modifyTeams;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.bismapp.MainActivity;
import com.example.bismapp.R;
import com.example.bismapp.TeamMember;
import com.example.bismapp.TeamMemberAdapter;

public class TeamInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_info, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.activity_list_view, teamMemberNames());

        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.enterAssociateName);

        textView.setAdapter(adapter);

        return view;
    }

    private String[] teamMemberNames() {
        String[] names = new String[MainActivity.teamMembers.size()];
        for(int i = 0; i < names.length; i++) {
          names[i] = MainActivity.teamMembers.get(i).getName();
        }
        return names;
    }
}