package com.example.poker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poker.Database.Player;
import com.example.poker.Database.PlayerDAO;
import com.example.poker.Database.PlayerDatabase;
import com.example.poker.game.Card;
import com.example.poker.game.Chip;
import com.example.poker.game.Hand;
import com.example.poker.game.HandComparator;
import com.example.poker.game.HandTestBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameActivity extends AppCompatActivity {
    private Button play;

    private Set<Integer> cardSet;
    private ArrayList<Card> dealtCards;
    private ArrayList<Card> playerCards;
    private ArrayList<Card> dealerCards;
    private Hand playerHand;
    private Hand dealerHand;
    private HandComparator handComparator;

    private int swapCount;
    private boolean hasSwapped;
    private int currentBet;
    private int playerChips;
    private int entryChips;


    private ArrayList<ImageView> cardImageViews;
    private ArrayList<ImageView> chipImageViews;
    private ImageView back;
    private ArrayList<Chip> chips;
    private TextView instructionOut;
    private TextView playerOut;
    private TextView dealerOut;
    private TextView betOut;
    private TextView chipsOut;
    private TextView winOut;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initActivityElements();

        // Read chips from menu activity through intent extra
        Intent intent = getIntent();
        entryChips = intent.getIntExtra("chips",0);
        playerChips = entryChips;
        initGame();



        back.setOnClickListener(v -> {
            super.onBackPressed();
        });

        play.setOnClickListener(v -> {
            String playText = (String) play.getText();
            if (playText.contains("Start")) {
                startGame();
                playerOut.setText(handComparator.getHand(playerHand));
            }
            else if (playText.equals("Swap")){
               // swap out selected cards
               for (Card card : playerCards) {
                   if (card.getState() == 2) {
                       card.updateCard(dealtCards.get(0));
                       dealtCards.remove(0);
                       updateImage(card.getImageView(), card.getImagePath());
                   }
                   // lock every card to only allow one swap
                   card.lockCard();
               }
                playerHand = new Hand(playerCards);
                play.setText("Place bet");
                hasSwapped = true;
                playerOut.setText(handComparator.getHand(playerHand));
                instructionOut.setText("Finish placing bet");
            }
            else if (playText.equals("Place bet")){
                if (currentBet == 0) {
                    showToast("You must bet at least one chip before placing bet!");
                }
                else {
                    showCards(dealerCards);
                    TextView dealerOut = findViewById(R.id.tv_dealerOut);
                    dealerOut.setText(handComparator.getHand(dealerHand));
                    playerOut.setText(handComparator.getHand(playerHand));
                    play.setText("Play again");
                    playerChips -= currentBet;
                    chipsOut.setText(playerChips + "");
                    instructionOut.setText("Round finished");

                    String winMessage = handComparator.getWinnerMessage(playerHand,dealerHand);
                    if (winMessage.contains("You")) {
                        int score = handComparator.getScoreMultiplier(playerHand) * currentBet;
                        playerChips += score;
                        chipsOut.setText(playerChips + "");
                        winMessage += " " + score + " Chips";
                    }
                    winOut.setText(winMessage);
                    PlayerDatabase db = Room.databaseBuilder(getApplicationContext(),
                            PlayerDatabase.class, "PlayerDatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                    PlayerDAO playerDAO = db.playerDAO();
                    Player player1 = playerDAO.getPlayer();
                    playerDAO.deletePlayer(player1);
                    player1.setChips(playerChips);
                    playerDAO.addPlayer(player1);
                }
            }
            else if (playText.startsWith("Play")) {
                hideCards(playerCards);
                hideCards(dealerCards);
                for (Chip c : chips) {
                    c.deselect();
                    updateImage(c.getImageView(), c.getDImagePath());
                }
                play.setText("Start");
                initGame();
            }
        });

        for (Chip chip : chips) {
            chip.getImageView().setOnClickListener( v -> {
                String playText = (String) play.getText();
                if (playText.equals("Place bet") || playText.equals("Swap")) {
                    int bet = chip.isSelected() ? currentBet + chip.getNumber()*-1 : currentBet + chip.getNumber();
                    if (bet <= playerChips) {
                        updateImage(chip.getImageView(), chip.getImagePath());
                        currentBet = bet;
                        betOut.setText(currentBet + "");
                    }
                    else {
                        showToast("Not enough chips!");
                    }
                }
            });
        }
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 125);
        toast.show();
    }

    private void startGame() {
        winOut.setText("Good luck!");
        play.setText("Place bet");
        instructionOut.setText("Place bet or select cards to swap");
        initCards();
        hideCards(dealerCards);
        playerHand = new Hand(playerCards);
        dealerHand = new Hand(dealerCards);
        handComparator = new HandComparator(playerHand, dealerHand);
    }

    private void initGame() {
        playerOut.setText("");
        dealerOut.setText("");
        instructionOut.setText("press start to begin");
        winOut.setText("");
        play.setText("Start");
        currentBet = 0;
        betOut.setText(currentBet + "");
        chipsOut.setText(playerChips + "");

        swapCount = 0;
        hasSwapped = false;
    }

    private void initCards() {
        // create 15 random distinct cards
        cardSet = new HashSet<>();
        while(cardSet.size() < 15)
            cardSet.add((int)(Math.random() * 52) + 1);

        // create a list of drawn cards
        dealtCards = new ArrayList<>();
        for(int i : cardSet)
            dealtCards.add(new Card(i));

        // create a list of player's & dealer's hands
        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        // set hand for debugging - also comment add lines below
//        playerCards = HandTestBuilder.getTwoPair(1,13, 7,0, 1);
//        dealerCards = HandTestBuilder.getTwoPair(1, 13, 8, 2, 3);


        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                playerCards.add(dealtCards.get(i));
                playerCards.get(i).setImageView(cardImageViews.get(i));
                dealtCards.remove(i);
            }
            else {
                dealerCards.add(dealtCards.get(i%5));
                dealerCards.get(i%5).setImageView(cardImageViews.get(i));
                dealtCards.remove(i%5);
            }
        }

        // watch players hand for swaps
        for (Card card : playerCards) {
            updateImage(card.getImageView(), card.getImagePath());
            card.getImageView().setOnClickListener(v -> {
                String playText = (String) play.getText();
                if (hasSwapped) {
                    showToast("You've already made a swap, place your bet!");
                }
                else {
                    switch(card.getState()) {
                        case 1:
                            updateImage(card.getImageView(),"swap_card2");
                            card.swapCard();
                            swapCount++;
                            play.setText("Swap");
                            break;
                        case 2:
                            updateImage(card.getImageView(), card.getImagePath());
                            card.unlockCard();
                            swapCount--;
                            if (swapCount == 0)
                                play.setText("Place bet");
                            break;
                    }
                }
            });
        }
    }

    private void showCards(ArrayList<Card> cards) {
        for (Card c : cards)
            updateImage(c.getImageView(), c.getImagePath());
    }

    private void hideCards(ArrayList<Card> cards) {
        for (Card c : cards)
            updateImage(c.getImageView(), c.getCoveredImagePath());
    }

    private void initActivityElements() {
        play = (Button)findViewById(R.id.btn_play);
        back = (ImageView)findViewById(R.id.iv_back);

        cardImageViews = new ArrayList<>();
        cardImageViews.add((ImageView)findViewById(R.id.iv_card1));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card2));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card3));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card4));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card5));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card6));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card7));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card8));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card9));
        cardImageViews.add((ImageView)findViewById(R.id.iv_card10));

        chipImageViews = new ArrayList<>();
        chipImageViews.add((ImageView)findViewById(R.id.iv_chip_5));
        chipImageViews.add((ImageView)findViewById(R.id.iv_chip_10));
        chipImageViews.add((ImageView)findViewById(R.id.iv_chip_25));

        chips = new ArrayList<>();
        chips.add(new Chip(5, "red_chip",(ImageView)findViewById(R.id.iv_chip_5)));
        chips.add(new Chip(10, "blue_chip",(ImageView)findViewById(R.id.iv_chip_10)));
        chips.add(new Chip(25, "green_chip",(ImageView)findViewById(R.id.iv_chip_25)));

        playerOut = (TextView)findViewById(R.id.tv_playerOut);
        dealerOut = (TextView)findViewById(R.id.tv_dealerOut);
        betOut = (TextView)findViewById(R.id.tv_betOut);
        chipsOut = (TextView)findViewById(R.id.tv_chipsOut);
        winOut = (TextView)findViewById(R.id.tv_winOut);
        instructionOut = (TextView)findViewById(R.id.tv_instructionOut);
    }

    private void updateImage(ImageView iv, String imagePath) {
        Resources res = getResources();
        int resID = res.getIdentifier(imagePath, "drawable", getPackageName());
        iv.setImageResource(resID);
    }
}