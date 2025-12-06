package com.example.project_02.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02.R;

public class QuizzyLogViewHolder extends RecyclerView.ViewHolder {
    private final TextView quizzyLogViewItem;
    private QuizzyLogViewHolder(View quizzyLogView){
        super(quizzyLogView);
        quizzyLogViewItem = quizzyLogView.findViewById(R.id.logDisplayRecyclerView);
    }

    public void bind (String text){
        quizzyLogViewItem.setText(text);
    }

    static QuizzyLogViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_quiz_info, parent,false);
        return new QuizzyLogViewHolder(view);
    }
}
