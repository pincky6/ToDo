package com.diplom.todoapp.eventtask;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import com.diplom.todoapp.R;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diplom.todoapp.databinding.FragmentEventTaskBinding;

public class EventTaskFragment extends Fragment {
    private FragmentEventTaskBinding binding = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTaskBinding.inflate(inflater);
        initMenus();
        initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }










    private void initMenus(){
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_settings){
                    Toast.makeText(getContext(), "smthj", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                if(id == R.id.add_new_event){
                                    Toast.makeText(getContext(), "Event", Toast.LENGTH_SHORT).show();
                                    return true;
                                } else if (id == R.id.add_new_task) {
                                    Toast.makeText(getContext(), "Task", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                return false;
                            }
                        });
                popupMenu.show();
            }
        });
    }
    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
