import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.awt.*;

public class GameApp extends Application {

    // -initial scene
    // -sets up all keyboard event handlers to invoke public methods in game
    // -sets up constants
    // -Game commands
    //  Left Arrow Changes the heading of the helicopter by 15 degrees to the left.
    //  Right Arrow Changes the heading of the helicopter by 15 degrees to the right.
    //  Up Arrow Increases the speed of the helicopter by 0.1.
    //  Down Arrow Decreases the speed of the helicopter by 0.1.
    //  ‘I’ Turns on the helicopter ignition.
    //  ‘b’ [optional] shows bounding boxes around objects.
    //  ‘r’ Reinitializes the game
    // - For each command in our game, we want to add a key listener in our
    //   GameApp class. Note in JavaFX these are constants and you do not need
    //   to define them in terms of any integer or character constants.

    private static final int WIDTH = 400;
    private static final int HEIGHT = 800;

    public class Game extends Pane {
        // -all rules implemented
        // -holds current state of game
        // -determines win/lose conditions
        //  + Lose condition run out of fuel, Win condition pond at 100% and
        //    fuel not 0
        // -instantiates and links the other Game Objects
        // -does not know anything about where user input comes from or how it
        // is generated
        // -Container for all game objects
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


        Pane canvas = new Pane();
        Scene scene = new Scene(canvas, WIDTH, HEIGHT, Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("Rain Maker");
        stage.show();

    }
}

abstract class GameObject{

    // -Contains methods and fields that manage common aspects of all game
    // objects
    // -Any state or behavior in this class should apply to all game object
    // this. For example, the helicopter can move, while a pond cannot
    // Consequently, you would not include anything regarding movement in
    // this class.


    public class Pond{

        // -This class represents a pong or lake in the Central Valley
        // -pond will be abstracted as just a blue circle placed at random
        // such that it does not intersect any other ground based objects
        // -When cloud reaches 30% pond will start to grow and the % will go up

    }

    public class Cloud{

        // -Represents cloud in Central Valley
        // -Cloud abstracted as a white cricle placed at random anywhere other
        //  than fully directly over helipad
        // -Cloud always starts at 0% and as the % goes up the color turns to
        // gray (fully saturated cloud would be the color rgb(155,155,155)
        // -At 30% the rainfall will start and pond will start to fill, the
        // clouds saturation drops 1%/second

    }

    public class Helipad{

        // -Represents the start and end location of the first game
        // -Helicopter takes off from helipad to move then lands to end game
        // -Helicopter is landed whenever it is contained within bounds of
        //  helipad and not moving
        // -Represented by a gray square with a gray circle centered over
        // square (there should be a gap between circle edge and square edge

    }

    public class Helicopter{

        // -Represents the main player character
        // -Is a small filled yellow circle with a line pointing towards the
        //  direction the helicopter is going
        // -As the heading changes the line must rotate to point the new
        // direction
        // -Size of helicopter should be derived by dimensions of the screen
        // -Display the current fuel below helicopter
        // -If location of the helipad changes no changes to the helicopter
        // code should have to be made
        // -Fuel is the integer value 25000 must be specified in the Game class
        // -Speed should increase and decrease similar to asteroids or actual
        // helicopter
        // -initial speed is set to 0 max speed is 10 and min is -2 (negative
        // speeds fly the helicopter backwards)

    }
}
