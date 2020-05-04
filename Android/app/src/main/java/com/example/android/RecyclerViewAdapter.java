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
        memberDTOs.add(new MemberDTO(R.drawable.testimage, 1, "김지수", "100"));
        memberDTOs.add(new MemberDTO(R.drawable.testimage2, 2, "누군가1", "50"));
        memberDTOs.add(new MemberDTO(R.drawable.testimage3, 3, "누군자2", "30"));
        memberDTOs.add(new MemberDTO(R.drawable.testimage4, 4, "누군가3", "10"));

        // delete above and just call readUser();
        // According to sorted users by score, put the data into memberDTOs sequentially
        // Current user shown first and then Top 5 user shown
    }
    /***
     * Assume that this is function which bring data from database
    private void readUser(){

        this code should be implemented as iteration form to get all user's data
     mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    memberDTOs.add(new MemberDTO(....)); // need to change MemberDTO's member variables
                } else {
                    Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

     ***/
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
