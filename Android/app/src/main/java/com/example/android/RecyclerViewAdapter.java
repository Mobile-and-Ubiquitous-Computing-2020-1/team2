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

    private ArrayList<MemberDTO> memberDTOs = new ArrayList();

    public RecyclerViewAdapter(){
        memberDTOs.add(new MemberDTO(R.drawable.testimage, "김지수", "100"));
        memberDTOs.add(new MemberDTO(R.drawable.testimage2, "유홍규", "50"));
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
        ((RowCell)holder).name.setText(memberDTOs.get(position).name);
        ((RowCell)holder).score.setText(memberDTOs.get(position).score);
    }

    @Override
    public int getItemCount() {
        return memberDTOs.size();
    }

    private class RowCell extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView name;
        TextView score;

        public RowCell(View view) {
            super(view);
            circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
            name = (TextView)view.findViewById(R.id.user_name);
            score = (TextView)view.findViewById(R.id.ranking_score);
        }
    }
}
