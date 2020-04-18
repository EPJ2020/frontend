package com.example.lfg_source.main.user;

import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lfg_source.R;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.animation.DetectSwipeGestureListener;
import com.example.lfg_source.rest.RestClientPut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import co.lujun.androidtagview.TagContainerLayout;

public class UserSwipeFragment extends Fragment {
    private UserSwipeViewModel mViewModel;
    private GestureDetectorCompat gestureDetectorCompat = null;

    private TextView lastName;
    private TextView firstName;
    private TextView description;
    private Drawable drawable;
    private ProgressBar mProgress;
    private TagContainerLayout mTagContainerLayout;

    private List<User> usersToSwipe = new ArrayList<>();

    public static UserSwipeFragment newInstance() {
        return new UserSwipeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        gestureListener.setActivity(this);
        gestureDetectorCompat = new GestureDetectorCompat(
                Objects.requireNonNull(getActivity()).getParent(), gestureListener);
        View view = inflater.inflate(R.layout.user_swipe_fragment, container, false);
        getViewElements(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorCompat.onTouchEvent(event);
                return true;
            }
        });
        return view;
    }
    private void getViewElements(View view) {
        lastName = view.findViewById(R.id.name);
        firstName = view.findViewById(R.id.firstname);
        description = view.findViewById(R.id.description);
        mTagContainerLayout = (TagContainerLayout) view.findViewById(R.id.tagcontainerLayout);

        mProgress = (ProgressBar) view.findViewById(R.id.circularProgressbar);
        Resources res = getResources();
        drawable = res.getDrawable(R.drawable.circular);
    }

    private void setProgress(){
        mProgress.setProgress(60);
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);
    }

    private void setViewElements(String lastName, String firstName, String description, String[] tags){
        this.lastName.setText(lastName);
        this.firstName.setText(firstName);
        this.description.setText(description);
        mTagContainerLayout.setTags(tags);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserSwipeViewModel.class);
        final Observer<List<User>> userObserver = new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersToSwipe.addAll(users);
                showSuggestion();
            }
        };
        mViewModel.getUsers().observe(getViewLifecycleOwner(), userObserver);
        mViewModel.sendMessage();
    }

    public void setInterested(boolean value) {
        String text = "Match interested: " + value;
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        final String url = "http://152.96.56.38:8080/User/bool";
        RestClientPut task = new RestClientPut();
        task.setMessage(value);
        task.execute(url);
        usersToSwipe.remove(0);
    }

    public void showSuggestion(){
        if(usersToSwipe.size() < 3){
            mViewModel.sendMessage();
        }
        if(!usersToSwipe.isEmpty()){
            setViewElements(usersToSwipe.get(0).getLastName(),
                    usersToSwipe.get(0).getFirstName(),
                    usersToSwipe.get(0).getDescription(),
                    usersToSwipe.get(0).getTags());
            setProgress();
        }else {
            setViewElements("Zurzeit wurden leider keine Passenden Personen gefunden",
                    null,
                    null,
                    new String[0]);
        }
    }
}
