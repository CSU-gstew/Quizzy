package com.example.project_02.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02.QuizInfo;
import com.example.project_02.R;

import java.util.ArrayList;
import java.util.List;

public class QuizInfoAdapter extends RecyclerView.Adapter<QuizInfoAdapter.QuizInfoViewHolder> {

    private final List<QuizInfo> items = new ArrayList<>();

    public void setItems(List<QuizInfo> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_quiz_info, parent, false);
        return new QuizInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizInfoViewHolder holder, int position) {
        QuizInfo info = items.get(position);
        holder.quizNameValue.setText(info.getQuizName());
        holder.passedValue.setText(String.valueOf(info.getQuestionsPassed()));
        holder.failedValue.setText(String.valueOf(info.getQuestionsFailed()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class QuizInfoViewHolder extends RecyclerView.ViewHolder {
        TextView quizNameValue;
        TextView passedValue;
        TextView failedValue;

        QuizInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            quizNameValue = itemView.findViewById(R.id.valueQuizName);
            passedValue = itemView.findViewById(R.id.valuePassed);
            failedValue = itemView.findViewById(R.id.valueFailed);
        }
    }
}