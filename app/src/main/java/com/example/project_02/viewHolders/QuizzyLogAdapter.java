package com.example.project_02.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.project_02.database.entities.QuizzyLog;

public class QuizzyLogAdapter extends ListAdapter<QuizzyLog, QuizzyLogViewHolder> {
    public QuizzyLogAdapter(@NonNull DiffUtil.ItemCallback<QuizzyLog> diffCallBack){
        super(diffCallBack);
    }

    @NonNull
    @Override
    public QuizzyLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return QuizzyLogViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzyLogViewHolder holder, int position) {
        QuizzyLog current = getItem(position);
        holder.bind(current.toString());
    }


    public static class QuizzyLogDiff extends DiffUtil.ItemCallback<QuizzyLog>{
        @Override
        public boolean areContentsTheSame(@NonNull QuizzyLog oldItem, @NonNull QuizzyLog newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(@NonNull QuizzyLog oldItem, @NonNull QuizzyLog newItem) {
            return oldItem == newItem;
        }
    }

}
