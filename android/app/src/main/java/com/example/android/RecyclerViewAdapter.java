package com.example.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Member;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<MemberDTO> memberDTOs = new ArrayList();

    public RecyclerViewAdapter(){


        // delete above and just call readUser();


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RowCell)holder).circleImageView.setImageResource(memberDTOs.get(position).image);
        ((RowCell)holder).rank.setText(Integer.toString(memberDTOs.get(position).ranking_num));
        ((RowCell)holder).name.setText(memberDTOs.get(position).name);
        ((RowCell)holder).score.setText(memberDTOs.get(position).score);
    }

    @Override
    public int getItemCount() {
        return memberDTOs.size();
    }

    private class RowCell extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView rank;
        TextView name;
        TextView score;

        public RowCell(View view) {
            super(view);
            circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
            rank = (TextView)view.findViewById(R.id.ranking_number);
            name = (TextView)view.findViewById(R.id.user_name);
            score = (TextView)view.findViewById(R.id.ranking_score);
        }
    }
}
