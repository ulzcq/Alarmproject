package com.yu.alarmproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<SchedAlarm> items;//TODO: 여기 저장

    public AlarmAdapter(ArrayList<SchedAlarm> items)
    {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView;

        if(viewType == Constants.INTEGRATED_CONTENT)//준비o출발o xml
        {
            itemView = inflater.inflate(R.layout.integrated_content, viewGroup, false);
            return new IntegratedViewHolder(itemView);
        }
        else if(viewType == Constants.ONLY_READY_CONTENT)
        {
            itemView = inflater.inflate(R.layout.only_ready_content, viewGroup, false);
            return new OnlyReadyViewHolder(itemView);
        }
        else if(viewType == Constants.ONLY_GOOUT_CONTENT)
        {
            itemView = inflater.inflate(R.layout.only_go_out_content, viewGroup, false);
            return new OnlyGoOutViewHolder(itemView);
        }
        else
        {
            itemView = inflater.inflate(R.layout.only_sched_content, viewGroup, false);
            return new OnlySchedViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SchedAlarm item = getItem(position);

        if(viewHolder instanceof IntegratedViewHolder) ((IntegratedViewHolder) viewHolder).setItem(item);
        else if(viewHolder instanceof OnlyReadyViewHolder) ((OnlyReadyViewHolder) viewHolder).setItem(item);
        else if(viewHolder instanceof OnlyGoOutViewHolder) ((OnlyGoOutViewHolder) viewHolder).setItem(item);
        else ((OnlySchedViewHolder) viewHolder).setItem(item);
    }

    public void addItem(SchedAlarm item){
        items.add(item); //배열에 아이템객체 추가
    }

    public void setItems(ArrayList<SchedAlarm> items){
        this.items = items;
    }


    public void setItem(int position, SchedAlarm item){
        items.set(position,item);
    }

    public SchedAlarm getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    static class IntegratedViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        TextView schedTime;
        Switch readyAlarmSwitch;
        Switch goOutAlarmSwitch;

        public IntegratedViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTime = itemView.findViewById(R.id.schedTime);
            readyAlarmSwitch = itemView.findViewById(R.id.readyAlarmSwitch);
            goOutAlarmSwitch = itemView.findViewById(R.id.goOutAlarmSwitch);
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTime.setText(item.getSchedTime());
            readyAlarmSwitch.setText(item.getReadyAlarmTime());
            goOutAlarmSwitch.setText(item.getGoOutAlarmTime());
        }
    }

    static class OnlyReadyViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        TextView schedTime;
        Switch readyAlarmSwitch;

        public OnlyReadyViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTime = itemView.findViewById(R.id.schedTime);
            readyAlarmSwitch = itemView.findViewById(R.id.readyAlarmSwitch);
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTime.setText(item.getSchedTime());
            readyAlarmSwitch.setText(item.getReadyAlarmTime());
        }
    }

    static class OnlyGoOutViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        TextView schedTime;
        Switch goOutAlarmSwitch;

        public OnlyGoOutViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTime = itemView.findViewById(R.id.schedTime);
            goOutAlarmSwitch = itemView.findViewById(R.id.goOutAlarmSwitch);
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTime.setText(item.getSchedTime());
            goOutAlarmSwitch.setText(item.getGoOutAlarmTime());
        }
    }

    static class OnlySchedViewHolder extends RecyclerView.ViewHolder{
        TextView label;
        Switch schedTimeSwitch;

        public OnlySchedViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTimeSwitch = itemView.findViewById(R.id.schedTimeSwitch);
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTimeSwitch.setText(item.getSchedTime());
        }
    }

}
