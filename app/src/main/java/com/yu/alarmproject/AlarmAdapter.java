package com.yu.alarmproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    } //커스텀 리스너 인터페이스.  리사이클러뷰 외부에서 처리위해서.

    public interface OnButtonClickListener{
        void ondeleteClick(View view, int position);
    } //커스텀

    private OnItemClickListener mListener = null; //리스너 객체 참조를 저장

    private OnButtonClickListener bListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    } //어댑터의 외부에서 리스너 객체 참조를 어댑터에 전달하는 메서드

    public void setOnButtonClickListenr(OnButtonClickListener listener){
        this.bListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView;

        if(viewType == Constants.INTEGRATED_CONTENT)
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

        if(viewHolder instanceof IntegratedViewHolder){
            ((IntegratedViewHolder) viewHolder).setItem(item);
        }
        else if(viewHolder instanceof OnlyReadyViewHolder){
            ((OnlyReadyViewHolder) viewHolder).setItem(item);
        }
        else if(viewHolder instanceof OnlyGoOutViewHolder){
            ((OnlyGoOutViewHolder) viewHolder).setItem(item);
        }
        else{
            ((OnlySchedViewHolder) viewHolder).setItem(item);
        }
    }

    public void addItem(SchedAlarm item){
        items.add(item); //배열에 아이템객체 추가
    }

    public void setItems(ArrayList<SchedAlarm> items){
        this.items = items;
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

    public class IntegratedViewHolder extends RecyclerView.ViewHolder{
        private TextView label;
        private TextView schedTime;
        private Switch readyAlarmSwitch;
        private Switch goOutAlarmSwitch;
        private Button deleteButton;

        public IntegratedViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTime = itemView.findViewById(R.id.schedTime);
            readyAlarmSwitch = itemView.findViewById(R.id.readyAlarmSwitch);
            goOutAlarmSwitch = itemView.findViewById(R.id.goOutAlarmSwitch);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(bListener != null){
                            bListener.ondeleteClick(v,pos);
                        }
                    }
                }
            });

            //TODO: 스위치2개 클릭메서드->enabled,알람on/off 구현

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //아이템과 연결된 데이터를 넘겨준다 SubmitActivity로 이동
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos); //아이템 클릭 이벤트 핸들러 메서드에서 리스너 객체 메서드 호출
                        }
                    }
                }
            });
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTime.setText(Constants.timeFormat.format(item.getSchedTime().getTime()));//calendar ->String
            readyAlarmSwitch.setText(Constants.timeFormat.format(item.getReadyAlarmTime()));
            goOutAlarmSwitch.setText(Constants.timeFormat.format(item.getGoOutAlarmTime()));
        }
    }

    public class OnlyReadyViewHolder extends RecyclerView.ViewHolder{
        private TextView label;
        private TextView schedTime;
        private Switch readyAlarmSwitch;
        private Button deleteButton;

        public OnlyReadyViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTime = itemView.findViewById(R.id.schedTime);
            readyAlarmSwitch = itemView.findViewById(R.id.readyAlarmSwitch);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(bListener != null){
                            bListener.ondeleteClick(v,pos);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTime.setText(Constants.timeFormat.format(item.getSchedTime().getTime()));
            readyAlarmSwitch.setText(Constants.timeFormat.format(item.getReadyAlarmTime()));
        }
    }

    public class OnlyGoOutViewHolder extends RecyclerView.ViewHolder{
        private TextView label;
        private TextView schedTime;
        private Switch goOutAlarmSwitch;
        private Button deleteButton;

        public OnlyGoOutViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTime = itemView.findViewById(R.id.schedTime);
            goOutAlarmSwitch = itemView.findViewById(R.id.goOutAlarmSwitch);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(bListener != null){
                            bListener.ondeleteClick(v,pos);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTime.setText(Constants.timeFormat.format(item.getSchedTime().getTime()));
            goOutAlarmSwitch.setText(Constants.timeFormat.format(item.getGoOutAlarmTime()));
        }
    }

    public class OnlySchedViewHolder extends RecyclerView.ViewHolder{
        private TextView label;
        private Switch schedTimeSwitch;
        private Button deleteButton;

        public OnlySchedViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            schedTimeSwitch = itemView.findViewById(R.id.schedTimeSwitch);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(bListener != null){
                            bListener.ondeleteClick(v,pos);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }

        public void setItem(SchedAlarm item){
            label.setText(item.getLabel());
            schedTimeSwitch.setText(Constants.timeFormat.format(item.getSchedTime().getTime()));
        }
    }

}
