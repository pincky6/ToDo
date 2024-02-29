package com.diplom.todoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.diplom.todoapp.databinding.FragmentBottomBinding;
import com.diplom.todoapp.eventtask.TaskFragment;
import com.diplom.todoapp.notes.NotesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class BottomFragment extends Fragment {
    FragmentBottomBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContainerView fragmentContainerView =  binding.getRoot().findViewById(R.id.nav_host_fragment_bottom);
        ArrayList<Fragment> ar = (ArrayList<Fragment>) fragmentContainerView.getFragment().getParentFragment().getChildFragmentManager().getFragments();
        if(ar.size() != 0){
            for(Fragment fragment: ar) {
                if (fragment instanceof TaskFragment) {
                    ((TaskFragment) fragment).setBottomNavigationView(binding.navView);
                } else {
                    ((NotesFragment)fragment).setBottomNavigationView(binding.navView);
                }
            }
        }
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_bottom);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setOnItemSelectedListener (item -> {
            Fragment selectedFragment = null;
                if(item.getItemId() == R.id.navigation_notes) {
                    selectedFragment = new NotesFragment();
                    ((NotesFragment)selectedFragment).setBottomNavigationView(binding.navView);
                }
                if(item.getItemId() == R.id.navigation_events){
                    selectedFragment = new TaskFragment();
                    ((TaskFragment)selectedFragment).setBottomNavigationView(binding.navView);
                }
            if (selectedFragment != null) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_bottom, selectedFragment).commit();
                return true;
            }

            return false;
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void hideBottomNavigationPanel(){
        binding.navView.setVisibility(View.GONE);
    }
    public void showBottomNavigationPanel(){
        binding.navView.setVisibility(View.VISIBLE);
    }
}
