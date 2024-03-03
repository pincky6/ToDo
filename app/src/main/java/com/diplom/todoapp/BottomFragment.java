package com.diplom.todoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.diplom.todoapp.databinding.FragmentBottomBinding;
import com.diplom.todoapp.eventtask.TaskFragment;
import com.diplom.todoapp.notes.NotesFragment;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

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
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_bottom);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                    if(destination.getId() == R.id.navigation_events || destination.getId() == R.id.navigation_notes){
                        binding.navView.setVisibility(View.VISIBLE);
                        return;
                    }
            binding.navView.setVisibility(View.GONE);
        });
//        binding.navView.setOnItemSelectedListener (item -> {
//            ArrayList<Fragment> fragments = (ArrayList<Fragment>) fragmentContainerView.getFragment().getParentFragment().getChildFragmentManager().getFragments();
//            Fragment selectedFragment = null;
//                if(item.getItemId() == R.id.navigation_notes) {
//                    ((TaskFragment)fragments.get(0)).hideFabButton();
//                    selectedFragment = new NotesFragment();
//                    ((NotesFragment)selectedFragment).setBottomNavigationView(binding.navView);
//                    navController.set(Navigation.findNavController(getActivity(), R.id.navigation_notes));
//                }
//            if(item.getItemId() == R.id.navigation_events){
//                    ((NotesFragment)fragments.get(0)).hideFabButton();
//                    selectedFragment = new TaskFragment();
//                    ((TaskFragment)selectedFragment).setBottomNavigationView(binding.navView);
//                    navController.set(Navigation.findNavController(getActivity(), R.id.navigation_events));
//                }
//            if (selectedFragment != null) {
//                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_bottom, selectedFragment).commit();
//                NavigationUI.setupWithNavController(binding.navView, navController.get());
//                return true;
//            }
//
//            return false;
//        });
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
