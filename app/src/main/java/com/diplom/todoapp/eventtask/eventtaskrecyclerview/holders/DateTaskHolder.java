package com.diplom.todoapp.eventtask.eventtaskrecyclerview.holders;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diplom.todoapp.R;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.adapters.SubtasksAdapter;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.AbstractTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.DateTask;
import com.diplom.todoapp.eventtask.eventtaskrecyclerview.models.Subtask;
import com.diplom.todoapp.eventtask.listeners.OnSetSuccsessListener;
import com.diplom.todoapp.utils.PriorityUtil;
import com.diplom.todoapp.databinding.ItemDateTaskBinding;
import com.diplom.todoapp.eventtask.listeners.OnRemoveListener;
import com.diplom.todoapp.eventtask.listeners.TaskListener;
import com.diplom.todoapp.utils.SuccsessFlagUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DateTaskHolder extends AbstractTaskHolder {
    ItemDateTaskBinding binding;
    boolean show;
    public DateTaskHolder(@NonNull ItemDateTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        binding.recyclerView.setAdapter(new SubtasksAdapter(null, false));
        show = false;

    }
    private void initShowSubtasksButton(DateTask dateTask){
        binding.showSubtasksView.setOnClickListener(v -> {
            if(!show){
                binding.showStateImage.setImageResource(R.drawable.baseline_expand_more_24);
                ((SubtasksAdapter)binding.recyclerView.getAdapter()).setSubtasks(dateTask.subtasks);
                binding.recyclerView.getAdapter().notifyDataSetChanged();
                binding.recyclerView.setVisibility(View.VISIBLE);
                show = true;
            } else {
                binding.showStateImage.setImageResource(R.drawable.baseline_expand_less_24);
                ((SubtasksAdapter)binding.recyclerView.getAdapter()).setSubtasks(null);
                binding.recyclerView.setVisibility(View.INVISIBLE);
                binding.recyclerView.getAdapter().notifyDataSetChanged();
                show = false;
            }
        });
    }
    @Override
    public void bind(AbstractTask abstractTask,
                     TaskListener listener,
                     OnRemoveListener removeListener,
                     OnSetSuccsessListener onSetSuccsessListener){
        if(!(abstractTask instanceof DateTask)) throw new IllegalArgumentException("wrong type of argument in date task holder");
        DateTask dateTask = (DateTask) abstractTask;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yy;HH:mm");
        String[] formatedDateStart = formatDate.format(dateTask.dateStart).split(";");
        String[] formatedDateEnd = formatDate.format(dateTask.dateEnd).split(";");
        binding.title.setText(dateTask.title);
        if(formatedDateStart[0].equals(formatedDateEnd[0])) {
            binding.date.setText(formatedDateStart[0]);
        } else {
            binding.date.setText(formatedDateStart[0] + "-" + formatedDateEnd[0]);
        }
        if(dateTask.allDayFlag == true){
            binding.dateStart.setText("");
            binding.defis.setText("All      \nDay");
            binding.dateEnd.setText("");
        } else {
            binding.dateStart.setText(formatedDateStart[1]);
            binding.defis.setText("-");
            binding.dateEnd.setText(formatedDateEnd[1]);
        }
        binding.place.setText(dateTask.place);
        binding.describe.setText(dateTask.describe);
        binding.category.setText(dateTask.category);


        int borderResource = PriorityUtil.getPriorityBorderResource(PriorityUtil.getPriorityEnum(dateTask.priority));
        int statusResource = SuccsessFlagUtil.getBackgroundResourceFromSuccsessFlagString(dateTask.succsessFlag);
        String succsessIndicatorText = SuccsessFlagUtil.getSuccsessStringFromString(dateTask.succsessFlag);
        binding.leftSide.setBackgroundResource(borderResource);
        binding.succsessIndicator.setBackgroundResource(statusResource);
        binding.succsessIndicator.setText(succsessIndicatorText);
        Log.d("DateTask", dateTask.id);
        binding.getRoot().setOnClickListener(v -> listener.taskNavigation(dateTask));
        if(!dateTask.subtasks.isEmpty()) {
            initShowSubtasksButton(dateTask);
        } else {
            binding.subtaskLayout.setVisibility(View.GONE);
        }
        binding.getRoot().setOnLongClickListener(v -> {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setMessage("What do you want?")
                    .setNeutralButton("Cancel", ((dialog, which) ->{} ))
                    .setPositiveButton("Set Task", (dialog, id) -> onSetSuccsessListener.set(abstractTask))
                    .setNegativeButton("Delete Task", (dialog, id) -> removeListener.remove(dateTask.id));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }
}
