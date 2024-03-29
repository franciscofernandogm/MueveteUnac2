package com.example.mueveteunac2.viewUser.ui.view.viewLine.viewRoute;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.mueveteunac2.LoginActivity;
import com.example.mueveteunac2.R;
import com.example.mueveteunac2.viewUser.data.model.Route;
import com.example.mueveteunac2.viewUser.data.model.Stop;
import com.example.mueveteunac2.viewUser.domain.MoveRouteMapInfoUseCase;
import com.example.mueveteunac2.viewUser.ui.view.UserActivity;
import com.example.mueveteunac2.viewUser.ui.viewModel.RouteViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class RouteActivity extends AppCompatActivity implements MoveRouteMapInfoUseCase {

    private CircleImageView image;
    private ImageButton back;
    private String lineId,firstTurnId,secondTurnId;
    private RouteViewModel routeViewModel;
    private TextView twRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();

        image= findViewById(R.id.profile_image);

        if (currentUser!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Glide
                        .with(this)
                        .load(currentUser.getPhotoUrl().toString())
                        .placeholder(R.drawable.image_photo)
                        .into(image);
            }
        }

        back= findViewById(R.id.back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(RouteActivity.this, UserActivity.class);
            startActivity(intent);
        });

        twRoute=findViewById(R.id.twRoute);

        lineId= getIntent().getExtras().getString("lineId");
        firstTurnId= getIntent().getExtras().getString("firstTurnId");
        secondTurnId= getIntent().getExtras().getString("secondTurnId");

        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        routeViewModel.getRouteFromFirestore(lineId,firstTurnId);
        routeViewModel.getLiveDatafromFireStore().observe(this, new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                String routeSelected= "Turno "+route.getTurn()+" - "+route.getRouteSchedule();
                twRoute.setText(routeSelected);
            }
        });

        Fragment map=new RouteMapFragment();
        Fragment info=new RouteInfoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,map).commit();

        Bundle turns=new Bundle();
        turns.putString("firstTurnId",firstTurnId);
        turns.putString("secondTurnId",secondTurnId);
        info.setArguments(turns);
        getSupportFragmentManager().beginTransaction().replace(R.id.mostrarparaderos,info).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Notificar a los fragmentos sobre el cambio de orientación
        for (Fragment routeInfoFragment : getSupportFragmentManager().getFragments()) {
            if (routeInfoFragment instanceof RouteInfoFragment) {
                ((RouteInfoFragment) routeInfoFragment).onParentConfigurationChanged();
            }
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.inicio, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                // Respond to the action bar's Up/Home button
                case R.id.CerrarSesion:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent1);
                    break;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public void moveMapInfo(Stop stop) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        RouteMapFragment routeMapFragment=(RouteMapFragment) fragmentManager.findFragmentById(R.id.contenedor);
        RouteInfoFragment routeInfoFragment=(RouteInfoFragment) fragmentManager.findFragmentById(R.id.mostrarparaderos);

        routeMapFragment.moveMap(stop);
        routeInfoFragment.moveFragment();
    }
}