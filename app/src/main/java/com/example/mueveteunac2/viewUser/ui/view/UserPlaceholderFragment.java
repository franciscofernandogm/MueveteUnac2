package com.example.mueveteunac2.viewUser.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mueveteunac2.databinding.FragmentUserBinding;
import com.example.mueveteunac2.viewUser.ui.viewModel.UserPageViewModel;
import com.example.mueveteunac2.viewUser.ui.view.viewLine.LineFragment;
import com.example.mueveteunac2.viewUser.ui.view.viewMyFavorite.MyFavoriteFragment;
import com.example.mueveteunac2.viewUser.ui.view.viewTrip.TripFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private UserPageViewModel pageViewModel;
    private FragmentUserBinding binding;

    public static Fragment newInstance(int index) {
        Fragment fragment = null;
        if (index==1){
            fragment=new LineFragment();
        }else if (index==2){
            fragment=new TripFragment();
        }else if (index==3){
            fragment=new MyFavoriteFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider((ViewModelStoreOwner) this,
                (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(UserPageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}