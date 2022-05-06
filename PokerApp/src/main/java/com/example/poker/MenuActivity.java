package com.example.poker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poker.Database.Player;
import com.example.poker.Database.PlayerDAO;
import com.example.poker.Database.PlayerDatabase;


public class MenuActivity extends AppCompatActivity {
    private Button action;
    private Button reset;
    private TextView chips;
    private Player player1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        
        action = (Button)findViewById(R.id.btn_action);
        reset = (Button)findViewById(R.id.btn_reset);

        PlayerDatabase db = Room.databaseBuilder(getApplicationContext(),
                PlayerDatabase.class, "PlayerDatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        PlayerDAO playerDAO = db.playerDAO();
        player1 = playerDAO.getPlayer();

        if (player1 == null) {
            player1 = new Player();
            player1.setId(1);
            player1.setName("player 1");
            player1.setChips(100);
            playerDAO.addPlayer(player1);
        }

        chips = (TextView)findViewById(R.id.tv_chipsOut_main);
        chips.setText(player1.chips + "");

        action.setOnClickListener(v -> {
            if (action.getText().equals("Play")){
                Intent gameIntent = new Intent(this, GameActivity.class);
                gameIntent.putExtra("chips", playerDAO.getPlayer().getChips());
                startActivity(gameIntent);
            }
        });

        Player finalPlayer = player1;
        reset.setOnClickListener(v -> {
            playerDAO.deletePlayer(finalPlayer);
            player1.setChips(100);
            playerDAO.addPlayer(player1);
            chips.setText(player1.chips + "");
            showToast("Progress reset, here's 100 chips");
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayerDatabase db = Room.databaseBuilder(getApplicationContext(),
                PlayerDatabase.class, "PlayerDatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        PlayerDAO playerDAO = db.playerDAO();
        player1 = playerDAO.getPlayer();
        chips.setText(player1.chips + "");
    }


    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -50);
        toast.show();
    }
}