/**
 * CSC 221 - Final Project
 * Android App Development
 * by Klaudio Vito
 * 12/15/2015
 */

package com.example.klaudio.battleship_final;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Klaudio on 12/10/2015.
 */
public class MainActivity extends AppCompatActivity {
    static GridView gridView; //a grid to place the cells
    private static SecureRandom rnd = new SecureRandom(); //random number generator
    public static final int EMPTY = 0; //zero stands for an empty cell
    public static final int SHIP = 1;//one stands for a ship in a cell
    public static final int HIT = 2;//two stands for a hit in the grid
    public static final int MISS = 3;//three stands for a miss in the grid
    private static final int DIRECTION_RIGHT = 0; //identifies direction right with zero for ship placement
    private static final int DIRECTION_DOWN = 1;//identifies direction down with one for ship placement
    private static int SHIP_LENGTH; //defines the length of a ship
    private static int[] grid = new int[100]; // an array of integers representing the cells of the 10x10 grid
    int hits = 0; //set number of hits initially to zero
    int misses = 0; //set number of misses initially to zero
    double percentage = 0.0; //hit percentage initially is zero
    static Boolean hard = false; //game difficulty hard
    static Boolean medium = false;//game difficulty medium
    static Boolean normal = true;//game difficulty normal
    static int direction; //will specify if up or down
    static int shipSizeSum; //total sum of ship cells
    SoundPool Sound; //declare Sound object from SoundPool class
    MediaPlayer media; //declare media object from MediaPlayer class
    int hitSoundID; //integer returning sound id for hhit Sound
    int missSoundID;//integer returning sound id for miss Sound
    int winSoundID;//integer returning sound id for win Sound
    static boolean soundCheck = true; //boolean variable to check if sound switch is on or off


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button quitButton = (Button)findViewById(R.id.quitButton); //declare quitButton and initialize it from its ID
        final Button resetButton = (Button)findViewById(R.id.resetButton);//declare resetButton and initialize it from its ID
        final Button hintButton =(Button)findViewById(R.id.hintButton);//declare hintButton and initialize it from its ID
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);//declare Spinner and initialize it from its ID
        gridView = (GridView) findViewById(R.id.gridView); //assign gridview by its ID
        final ImageAdapter adpt = new ImageAdapter(this); //this is an adapter object form ImageAdapter class
        gridView.setAdapter(adpt); //display adpt in gridview
        int randCell = rnd.nextInt(100); //random number from 0 to 100 that represents a cell in the grid
        final TextView shotsLabel = (TextView) findViewById(R.id.shotsLabel); //label to display number of shots
        final TextView hitsLabel = (TextView) findViewById(R.id.hitsLabel);//label to display number of hits
        final TextView missLabel = (TextView) findViewById(R.id.missLabel);//label to display number of misses
        final TextView percLabel = (TextView) findViewById(R.id.percLabel);//label to display hit percentage
        List<String> dropDown = new ArrayList<String>(); //drop down menu items
        dropDown.add("Normal"); //add normal to the above list
        dropDown.add("Medium"); //add medium to the above list
        dropDown.add("Hard"); //add hard to the above list
        Sound = new SoundPool(1, AudioManager.STREAM_MUSIC,0); //initialize Sound object with its constructor
        media = MediaPlayer.create(this, R.raw.battleship); //initialize media with battleship background music
        hitSoundID = Sound.load(this,R.raw.hit,1); //initialize hit sound with hit coming from raw resource folder
        missSoundID = Sound.load(this,R.raw.miss,2);//initialize miss sound with miss coming from raw resource folder
        winSoundID = Sound.load(this,R.raw.win, 3);//initialize win sound with win coming from raw resource folder
        media.start(); //start background music
        media.setLooping(true); //keep looping till the thread ends
        Switch soundSwitch = (Switch) findViewById(R.id.soundSwitch); //initialize switch button from its ID
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dropDown); //declare dataAdapter for dropDown menu
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //initialize adapter
        spinner.setAdapter(dataAdapter); //add adapter to spinner
        initializeGrid(); //set all cells to 0(i.e EMPTY)
        placeAllShips(); //call this function to place random ships of random length on the grid

        //item selected listener for the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set boolean variables according to selection
                if (parent.getItemAtPosition(position) == "Normal") {
                    normal = true; //normal is selected
                    hard = false;
                    medium = false;
                    hintButton.setEnabled(true);
                } else if (parent.getItemAtPosition(position) == "Medium") {
                    medium = true; //medium is selected
                    normal = false;
                    hard = false;
                    if (misses + hits == 0) {
                        hintButton.setEnabled(true); //hint button is disabled if there is at least 1 shot
                    } else {
                        hintButton.setEnabled(false); //hint button is enabled otherwise
                    }
                } else if (parent.getItemAtPosition(position) == "Hard") {
                    hard = true; //hard is selected
                    medium = false;
                    normal = false;
                    hintButton.setEnabled(false); //no hint available in this level
                    for (int i = 0; i < 100; i++) {
                        if (grid[i] != HIT) {
                            gridView.getChildAt(i).setBackgroundResource(0); //remove all miss background from the cells
                        }
                    }
                }
            }
            //do nothing if nothing is selected
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //item clicked listener for the grid
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //if there are hints displayed, remove them
                for (int i = 0; i < 100; i++) {
                    if (grid[i] != HIT && grid[i] == SHIP) {
                        gridView.getChildAt(i).setBackgroundResource(0);
                    }
                }
                //in difficulty hard, the misses on the same cell should be counted
                if (hard == true) {
                    if (grid[position] == MISS || grid[position] == EMPTY) {
                        misses++;
                    }
                }
                //when player hits a ship
                if (grid[position] == SHIP) {
                    hits++; //increase number of hits
                    grid[position] = HIT; //set the cell to hit
                    gridView.getChildAt(position).setBackgroundColor(Color.RED);//color backgound red
                    if(soundCheck){
                        Sound.play(hitSoundID, 1, 1, 1, 0, 1); //if the switch is on, play the hit sound
                    }
                    else
                        Sound.pause(hitSoundID); //if the switch is off, pause the hit sound
                }
                //when player misses
                else if (grid[position] == EMPTY) {
                    misses++;//increase number of misses
                    grid[position] = MISS; //set cell to missed
                    if (hard == false) { //if the hard level is selected color the background blue signifying a miss
                        gridView.getChildAt(position).setBackgroundColor(Color.BLUE);
                    }
                    if(soundCheck){
                        Sound.play(missSoundID, 1, 1, 1, 0, 1); //if sound switch is on play the miss sound
                    }
                    else{
                        Sound.pause(missSoundID); //pause the music on switch off
                    }
                }
                //check if the player won
                if (hits == shipSizeSum) {
                    gridView.setEnabled(false); //disable the grid, you cannot play anymore after you sink all the ships
                    hintButton.setEnabled(false); //no hints after win
                    spinner.setEnabled(false); //no difficulty selecting after win
                    Toast.makeText(MainActivity.this, "YOU WIN", Toast.LENGTH_SHORT).show(); //display text that the player won
                    if(soundCheck){
                        Sound.play(winSoundID, 1, 1, 1, 0, 1); //if switch is on, play win sound
                    }
                    else{
                        Sound.pause(winSoundID); //pause controlled by switch
                    }
                    media.pause(); //pause the background sound
                }

                if ((hits + misses) != 0)
                    percentage = (double) hits / (hits + misses); //calculate percentage if there are more than 1 shots

                shotsLabel.setText(String.format("%s%d", "Shots: ", (hits + misses))); //display shots in the label
                hitsLabel.setText(String.format("%s%d", "Hits: ", hits));//display hits in the label
                missLabel.setText(String.format("%s%d", "Misses: ", misses));//display misses in the label
                percLabel.setText(String.format("%s%.2f%s", "Hit%: ", percentage * 100, "%"));//display hit percentage in the label
                //if the medium level of difficulty is selected
                if (medium == true) {
                    if (hits + misses != 0) {
                        hintButton.setEnabled(false); //disable the hint button if there is at least 1 shot
                    } else {
                        hintButton.setEnabled(true); //enable hint otherwise
                    }
                }
            }
        });
        //quit button click listener quits the app when it is clicked
        quitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
            }
        });

        //reset button clikc listener
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                media.start(); //start background music
                hintButton.setEnabled(true); //enable hint button (if it was disabled)
                spinner.setEnabled(true); //enable spinner
                gridView.setEnabled(true); //enable gridview clicking
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
                hits = 0; //reset the number of hits to zero
                misses = 0;//reset the number of misses to zero
                percentage = 0.0; //reset the hit percentage to zero
                shotsLabel.setText(String.format("%s%d", "Shots: ", (hits + misses))); //display new shots
                hitsLabel.setText(String.format("%s%d", "Hits: ", hits)); //display new hits
                missLabel.setText(String.format("%s%d", "Misses: ", misses)); //display new misses
                percLabel.setText(String.format("%s%.2f%s", "Hit%: ", percentage * 100, "%")); //display new percentage
                gridView = new GridView(MainActivity.this); //create a new grid view
                gridView = (GridView) findViewById(R.id.gridView); //choose it to be the gridview from the xml file
                gridView.setAdapter(new ImageAdapter(MainActivity.this)); //add a new image adapter to the new grid view
                initializeGrid();
                placeAllShips();
            }
        });

        //when hint is clicked
        hintButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                for(int i = 0; i < 100; i++){//scan through the grid
                    if(grid[i] == SHIP){
                        gridView.getChildAt(i).setBackgroundResource(R.drawable.sh); //if there's a ship display the ship image from the drawable folder
                        //gridView.getChildAt(i).setBackgroundColor(Color.GREEN); //set cell background color to green if there is a ship in it
                    }
                }
            }
        });

        //switch click listener
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    soundCheck = true; //set sound check variable to true
                    media.start(); //start background music
                    //Sound.play(hitSoundID, 1, 1, 1, 0, 1); //play hit sound
                    //Sound.play(missSoundID, 1, 1, 1, 0, 1);//play miss sound
                }else{
                    soundCheck = false; //set boolean to false
                    media.pause(); //pause background music
                    Sound.pause(hitSoundID); //pause hit sound
                    Sound.pause(missSoundID); //pause miss sound
                }
            }
        });
    }

    //grid is initialized  to all zeroes
    protected void initializeGrid(){
        for(int i = 0; i < 100; i++)
            grid[i] = EMPTY;
    }

    //method to place all 6 ships in the grid
    protected void placeAllShips(){
        int randCell; //declare random cell as an integer
        SecureRandom dir = new SecureRandom(); //random number generator
        shipSizeSum = 0; //initial sum of sizes is zero
        for(int m = 0; m < 6; m++){ //make the following 6 times, therefore 6 ships on the grid
            direction =  dir.nextInt(2); //get a random direction, either 0 or 1
            SHIP_LENGTH = 2 +  rnd.nextInt(3); //get a random ships size from 2 to 4
            shipSizeSum = shipSizeSum + SHIP_LENGTH;// calculate sum of grid cells containing a ship
            do { //go through the loop at least once
                randCell = rnd.nextInt(49); //choose a cell between 0 and 49
            }while(!checkOverlap(randCell, direction, SHIP_LENGTH)); //make sure there is no overlapping

            //if the random direction is right
            if(direction == DIRECTION_RIGHT){
                for(int i = randCell; i < randCell+SHIP_LENGTH; i++){
                    grid[i] = SHIP; //place a ship directly next to each other as many times as the ship length is
                }
            }

            //if the direction is down
            else if (direction == DIRECTION_DOWN){
                for(int i = randCell; i < randCell+(10*(SHIP_LENGTH-1))+SHIP_LENGTH; i+=10){
                    grid[i] = SHIP; //place a ship every 10 cells (since we have 10 cells per row) for as long as the ship length is
                }
            }
        }
    }

    //method to make sure there is no overlapping
    protected boolean checkOverlap(int cell, int dir, int length){
        //if the direction is right, check that the ships doesn't go beyond the screen and restart in a new row
        if(dir == DIRECTION_RIGHT){
            if(cell  >= 6 && cell <= 9) //if cell is between here, then it might go beyond the screen since biggest size is 4
                return false;
            else if(cell  >= 16 && cell <= 19)
                return false;
            else if(cell  >= 26 && cell <= 29)
                return false;
            else if(cell  >= 36 && cell <= 39)
                return false;
            else if(cell  >= 46 && cell <= 49)
                return false;
            for(int i = cell; i < cell+length; i++){
                if(grid[i] == SHIP){
                    return false;//if there's a ship from the cell up to ship length, then return overlap
                }
            }
        }
        //if direction is down
        else if(dir == DIRECTION_DOWN){
            for(int i = cell; i < 50+length; i+=10){
                if(grid[i] == SHIP){
                    return false;//check for a ship in the same column and 5 rows down
                }
            }
        }
        return true;
    }
}
