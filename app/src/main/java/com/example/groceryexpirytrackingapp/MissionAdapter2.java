package com.example.groceryexpirytrackingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MissionAdapter2 extends RecyclerView.Adapter<MissionAdapter2.MyViewHolder> {
    private List<Mission> missions;
    Context context;
    String username;
    int isAdmin;

    public MissionAdapter2(List<Mission> missions, Context context,int isAdmin,String username) {
        this.missions = missions;
        this.context = context;
        this.isAdmin=isAdmin;
        this.username=username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.missionrow,parent,false);
        return new MissionAdapter2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(missions.get(position).getTitle());
        holder.status.setText(missions.get(position).getStatus());
        holder.points.setText(missions.get(position).getPoint());
        holder.assignto.setText(missions.get(position).getAssignTo());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin==1){
                Intent intent=new Intent(context, Admin_Mission_Validate.class);
                intent.putExtra("Title",missions.get(position).getTitle());
                intent.putExtra("Desc",missions.get(position).getDesc());
                intent.putExtra("Points",missions.get(position).getPoint());
                intent.putExtra("Status",missions.get(position).getStatus());
                intent.putExtra("Key",missions.get(position).getKey());
                intent.putExtra("People",missions.get(position).getAssignTo());
                context.startActivity(intent);
                } else if (isAdmin==0) {
                    Intent intent=new Intent(context, User_Accecpt_Mission.class);
                    intent.putExtra("Title",missions.get(position).getTitle());
                    intent.putExtra("Desc",missions.get(position).getDesc());
                    intent.putExtra("Points",missions.get(position).getPoint());
                    intent.putExtra("Key",missions.get(position).getKey());
                    intent.putExtra("Status",missions.get(position).getStatus());
                    intent.putExtra("CurrentStaff",missions.get(position).getAssignTo());
                    intent.putExtra("People",username);
                    context.startActivity(intent);

                }
                else {
                    Toast.makeText(context, "Please login into system", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, Login.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return missions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,status,points,assignto;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.mission_title1);
            status=itemView.findViewById(R.id.mission_status1);
            points=itemView.findViewById(R.id.mission_reward1);
            assignto=itemView.findViewById(R.id.mission_assignTo1);
            cardView=itemView.findViewById(R.id.missionCard);

        }
}


}
