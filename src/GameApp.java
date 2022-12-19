import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static javafx.scene.paint.Color.*;

public class GameApp extends Application {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    Point2D windowSize = new Point2D(WINDOW_WIDTH, WINDOW_HEIGHT);
    Set<KeyCode> keysDown = new HashSet<>();
    static List<Cloud> clouds;
    static List<Pond> ponds;
    static Helipad helipad;
    static Helicopter heli;
    boolean gameStarted;
    static boolean boundingBoxShown;
    Scene scene;
    int key(KeyCode k){
        return keysDown.contains(k) ? 1 : 0;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {

        gameStarted = true;
        Group gameGroup = new Group();
        int shiftX = 40;
        int shiftY = 75;
        int helipadWidth = 110;

        Image backgroundImage = new Image("DesertBackground.png");
        ImageView backgroundObj = new ImageView();
        backgroundObj.setImage(backgroundImage);

        int helipadRandomX =
                helipadWidth + (int) (Math.random()* WINDOW_WIDTH - helipadWidth);
        int helipadRandomY = 60+(int) (Math.random()*190) + 500;
        helipad = new Helipad( new Point2D(helipadRandomX,
                helipadRandomY));
        heli = new Helicopter(helipad.p);

        int numClouds = 5;
        clouds = new LinkedList<>();

        int numPonds = 3;
        ponds = new LinkedList<>();

        stage.setTitle("Rain Maker");

        gameGroup.getChildren().addAll(backgroundObj, helipad.helipadGroup,
                heli.heliGroup);

        for(int i = 0; i < numPonds; i++){
            int pondRandomX = shiftX +(int) (Math.random()* WINDOW_WIDTH);
            int pondRandomY = shiftY + (int) (Math.random()* 250);
            Pond pond1 = new Pond( new Point2D(pondRandomX, pondRandomY));
            ponds.add(pond1);
            gameGroup.getChildren().addAll(pond1.pondGroup);
        }

        for(int i = 0; i < numClouds; i ++ ){
            int cloudRandomX = shiftX +(int) (Math.random()* WINDOW_WIDTH);
            int cloudRandomY = shiftY + (int) (Math.random()* 250);
            Cloud newCloud = new Cloud( new Point2D(cloudRandomX,
                    cloudRandomY));
            clouds.add(newCloud);
            gameGroup.getChildren().addAll(newCloud.cloudGroup);
            newCloud.cloudGroup.toFront();
        }

        scene = new Scene(gameGroup, windowSize.getX(), windowSize.getY());
        stage.setScene(scene);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event){
                keysDown.add(event.getCode());

                if(event.getCode() == KeyCode.B) {
                    if (GameApp.boundingBoxShown == false)
                        GameApp.boundingBoxShown = true;
                    else
                        GameApp.boundingBoxShown = false;
                }
                if(keysDown.contains(KeyCode.R)) {
                    gameReset();
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event){
                keysDown.remove(event.getCode());
            }
        });

        AnimationTimer loop = new AnimationTimer() {
            public void handle(long nano) {
                heli.update(key(KeyCode.UP), key(KeyCode.DOWN),
                        key(KeyCode.LEFT), key(KeyCode.RIGHT),
                        key(KeyCode.I), key(KeyCode.SPACE));
                heli.heliGroup.toFront();

                for(int i = 0; i < clouds.size(); i++) {
                    if (clouds.get(i).intersects(heli)) {
                    }

                    clouds.get(i).update();
                    if(clouds.get(i).cloudCircle.getLayoutX() > WINDOW_WIDTH - 100){
                        System.out.println("DELETED CLOUD");
                        gameGroup.getChildren().remove(clouds.get(i).cloudGroup);
                        clouds.remove(clouds.get(i));
                        i--;

                        int cloudRandomY = (int) (Math.random()* 250);
                        Cloud newCloud = new Cloud(new Point2D(0,
                                cloudRandomY));
                        clouds.add(newCloud);
                        gameGroup.getChildren().addAll(newCloud.cloudGroup);
                    }
                }
                    for(int j = 0; j < ponds.size(); j++) {
                        ponds.get(j).update();
                    }

                    if(heli.fuel == 0 && gameStarted == true){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        gameStarted = false;
                        alert.setContentText("You lost, play again?");
                        alert.show();
                    }

                    heli.heliBoundingBox.setVisible(boundingBoxShown);

                    helipad.helipadBoundingBox.setVisible(boundingBoxShown);

                    for(int k = 0; k < clouds.size(); k++) {
                        clouds.get(k).getBoundingBox().setVisible(boundingBoxShown);
                    }

                    for(int j = 0; j < ponds.size(); j++) {
                        ponds.get(j).getBoundingBox().setVisible(boundingBoxShown);
                    }
                }
        };
        loop.start();
        stage.show();


    }

    public void gameReset(){
        Group gameGroup = new Group();
        gameStarted = true;
        int shiftX = 40;
        int shiftY = 75;
        int numPonds = 3;
        int numClouds = 5;
        int helipadWidth = 110;

        int helipadRandomX =
                (int) (Math.random()*(WINDOW_WIDTH-helipadWidth) + 1);
        int helipadRandomY = 60+(int) (Math.random()*190 + 500);
        Helipad helipad = new Helipad( new Point2D(helipadRandomX,
                helipadRandomY));
        heli.heliReset(helipad.p);

        while(!clouds.isEmpty()){
            clouds.remove(0);}

        Image backgroundImage = new Image("DesertBackground.png");
        ImageView backgroundObj = new ImageView();
        backgroundObj.setImage(backgroundImage);

        gameGroup.getChildren().addAll(backgroundObj,
                 helipad.helipadGroup,
                heli.heliGroup);

        for(int i = 0; i < numPonds; i++){
            int pondRandomX = shiftX +(int) (Math.random()* WINDOW_WIDTH);
            int pondRandomY = shiftY + (int) (Math.random()* 250);
            Pond newPond = new Pond( new Point2D(pondRandomX, pondRandomY));
            ponds.add(newPond);
            gameGroup.getChildren().addAll(newPond.pondGroup);
        }

        for(int i = 0; i < numClouds; i ++ ){
            int cloudRandomX = shiftX +(int) (Math.random()* WINDOW_WIDTH);
            int cloudRandomY = shiftY + (int) (Math.random()* 250);
            Cloud newCloud = new Cloud( new Point2D(cloudRandomX,
                    cloudRandomY));
            clouds.add(newCloud);
            gameGroup.getChildren().addAll(newCloud.cloudGroup);
            newCloud.cloudGroup.toFront();
        }
        scene.setRoot(gameGroup);
    }
}

abstract class GameObject {

    Point2D pos;
    boolean isStarted;

    public GameObject( Point2D initialPos) {

        pos = initialPos;

        isStarted = true;
    }

    abstract Shape getBoundingBox();

    public boolean intersects(GameObject po) {
        return isStarted && po.isStarted &&
                !Shape.intersect(getBoundingBox(), po.getBoundingBox())
                        .getBoundsInLocal().isEmpty();
    }
}

class Cloud extends GameObject{

    Point2D p;
    Group cloudGroup;
    double cloudSpeed, cloudPercent;
    double cloudMaxSpeed = 0.5, cloudMinSpeed = 0.1, CloudMaxRadius = 50,
            CloudMinRadius = 20;
    double cloudRadius = (Math.random()*CloudMaxRadius + CloudMinRadius);
    int red = 255, green = 255, blue = 255;
    Text cloudPercentText = new Text();
    Circle cloudCircle;
    Rectangle cloudBoundingBox;

    public Cloud( Point2D p){
        super(p);
        cloudGroup = new Group();
        this.p = p;
        cloudPercent = 0;

        cloudSpeed = (Math.random()*cloudMaxSpeed + cloudMinSpeed);
        cloudCircle = new Circle(p.getX(), p.getY(), cloudRadius,
                Color.rgb(red , green ,blue));

        cloudPercentTextCreation();
        cloudBoundingBoxCreation();

        cloudGroup.getChildren().addAll( cloudCircle, cloudPercentText, cloudBoundingBox);
    }

    public void cloudPercentTextCreation(){
        cloudPercentText.setText((int) cloudPercent + "%");
        cloudPercentText.setFill(BLUE);
        cloudPercentText.setFont(Font.font(20));
        cloudPercentText.setX(p.getX() - 10);
        cloudPercentText.setY(p.getY() + 10);
    }

    public void cloudBoundingBoxCreation(){
        cloudBoundingBox = new Rectangle(cloudRadius * 2, cloudRadius *2 ,
                TRANSPARENT);
        cloudBoundingBox.setStroke(RED);
        cloudBoundingBox.setY(p.getY() - cloudRadius);
        cloudBoundingBox.setX(p.getX() - cloudRadius);
    }

    public Shape getBoundingBox() {return cloudBoundingBox;}

    public void update(){
        cloudCircle.setLayoutX(cloudCircle.getLayoutX()+cloudSpeed);
        cloudBoundingBox.setX(cloudBoundingBox.getX()+cloudSpeed);
        cloudPercentText.setX(cloudPercentText.getX()+cloudSpeed);
        cloudPercentText.setText("" + (int) cloudPercent + "%");

        if(cloudPercent > 30){
            cloudPercent -= 0.01;
        }

        cloudCircle.setFill(color((255 - (int) cloudPercent)/255.0,
                (255 - (int) cloudPercent)/255.0,
                (255 - (int) cloudPercent)/255.0));

    }
}

class Pond extends GameObject{

    Point2D p;
    Group pondGroup;
    double pondPercent;
    int pondRadius = 40, centeringPondBoundingBox = 150;
    Text pondPercentText = new Text();
    Circle pondCircle;
    Rectangle pondBoundingBox;

    public Pond( Point2D p){
        super(p);
        pondGroup = new Group();
        this.p = p;
        pondPercent = (int) (Math.random()*25 + 10);
        pondCircle = new Circle(p.getX(), p.getY(), pondRadius, BLUE);

        pondPercentTextCreation();
        pondBoundingBoxCreation();

        pondGroup.getChildren().addAll( pondCircle, pondPercentText, pondBoundingBox);

    }

    public void pondPercentTextCreation(){

        pondPercentText.setText( (int) pondPercent + "%");
        pondPercentText.setFill(YELLOW);
        pondPercentText.setFont(Font.font(20));
        pondPercentText.setX(p.getX() - 10);
        pondPercentText.setY(p.getY() + 10);
    }

    public void pondBoundingBoxCreation(){
        pondBoundingBox = new Rectangle(pondRadius * 8, pondRadius * 8,
                TRANSPARENT);
        pondBoundingBox.setStroke(RED);
        pondBoundingBox.setY(p.getY() - centeringPondBoundingBox);
        pondBoundingBox.setX(p.getX() - centeringPondBoundingBox);
    }

    public Shape getBoundingBox() {return pondBoundingBox;}

    public void update(){

        pondPercentText.setText("" + (int) pondPercent + "%");

        for (int i = 0; i < GameApp.clouds.size(); i++) {
            if (GameApp.clouds.get(i).intersects(this)) {
                if (GameApp.clouds.get(i).cloudPercent > 30 && pondPercent < 100) {
                    pondPercent += .01;
                }
            }
        }
        pondCircle.setRadius(pondBoundingBox.getWidth()/2*pondPercent/150.0);
    }
}

class Helipad extends GameObject{
    Point2D p;
    Group helipadGroup;
    Text heliText = new Text();
    Rectangle helipadRect, helipadBoundingBox;
    Circle helipadCircle;

    public Helipad( Point2D p){
        super(p);
        helipadGroup = new Group();

        this.p = p;

        helipadTextCreation();
        helipadShapeCreation();
        helipadBoundingBoxCreation();

        helipadGroup.getChildren().addAll(helipadCircle,
                helipadRect, heliText);
    }

    public void  helipadTextCreation() {
        heliText.setText("H");
        heliText.setFill(Color.YELLOW);
        heliText.setFont(Font.font(100));
        heliText.setX(p.getX());
        heliText.setY(p.getY());
    }

    public void helipadShapeCreation(){

        double helipadSize = 110, helipadStrokeSize = 3, helipadRadius = 50;

        helipadCircle = new Circle(p.getX()+35,
                p.getY()-35, helipadRadius, TRANSPARENT);
        helipadCircle.setStroke(Color.GRAY);
        helipadCircle.setStrokeWidth(helipadStrokeSize);

        helipadRect = new Rectangle(helipadSize, helipadSize,
                TRANSPARENT);

        helipadRect.setStroke(Color.GRAY);
        helipadRect.setStrokeWidth(helipadStrokeSize);
        helipadRect.setX(p.getX()-20);
        helipadRect.setY(p.getY()-90);

        helipadBoundingBox = new Rectangle(helipadRadius, helipadRadius,
                TRANSPARENT);

    }

    public void helipadBoundingBoxCreation(){
        helipadBoundingBox.setStroke(RED);
        helipadBoundingBox.setY(p.getY());
        helipadBoundingBox.setX(p.getX());
    }

    public Shape getBoundingBox() {return helipadBoundingBox;}

}

class Helicopter extends GameObject{

    ImageView heliBladesObj = new ImageView();
    ImageView heliBodyObj = new ImageView();
    Text fuelText = new Text();
    boolean helicopterStarted;
    double heliSpeed = 0, heliScale = 2, bladeSpeed, heliVolume = 0.1;
    int fuel, fuelDecreaseAmount = 100;
    Rectangle heliBoundingBox;
    Group heliGroup;

    public Helicopter( Point2D p){
        super(p);

        heliGroup = new Group();
        fuel = 250000;
        bladeSpeed  = 0;

        heliBoundingBox = new Rectangle(110, 110,
                TRANSPARENT);

        createHeliBody();
        heliBodyObj.setLayoutX(p.getX() - 70);
        heliBodyObj.setLayoutY(p.getY() - 150);

        createHeliBlades();
        createHeliFuelText();

        heliGroup.getChildren().addAll(heliBodyObj, heliBladesObj, fuelText,
                heliBoundingBox);
        helicopterStarted = false;
    }

    public void createHeliBody(){

        Image heliBodyImage = new Image("helicopter body.png");
        heliBodyObj.setImage(heliBodyImage);
        heliBodyObj.setFitWidth(heliBodyImage.getWidth()/heliScale);
        heliBodyObj.setFitHeight(heliBodyImage.getHeight()/heliScale);
    }

    public void createHeliBlades(){
        Image heliBladesImage = new Image("helicopter blades.png");
        heliBladesObj.setImage(heliBladesImage);
        heliBladesObj.setFitWidth(heliBladesImage.getWidth()/heliScale);
        heliBladesObj.setFitHeight(heliBladesImage.getHeight()/heliScale);
    }

    public void createHeliFuelText(){
        fuelText.setFill(Color.WHITE);
        fuelText.setFont(Font.font(20));
        fuelText.setText(fuel+"");
    }

    public void heliReset(Point2D p)
    {
        heliBodyObj.setLayoutX(p.getX() - 70);
        heliBodyObj.setLayoutY(p.getY() - 150);

        heliBladesObj.setLayoutX(heliBodyObj.getLayoutX()+100/heliScale);
        heliBladesObj.setLayoutY(heliBodyObj.getLayoutY()+100/heliScale);


        fuelText.setFill(Color.WHITE);
        fuelText.setFont(Font.font(20));
        fuelText.setText(fuel+"");

        helicopterStarted = false;
        fuel = 250000;
        bladeSpeed  = 0;
        heliStartSound();
    }
    public void update(int up, int down,
                       int left, int right, int ignitionKey,
                       int cloudSeeding){

        double minBladeSpeed = 5, maxBladeSpeed = 10, heliSpeedChange = 0.05;

        if(cloudSeeding == 1) {
            for (int i = 0; i < GameApp.clouds.size(); i++) {
                if (GameApp.clouds.get(i).intersects(this)) {
                    if(GameApp.clouds.get(i).cloudPercent < 100) {
                        GameApp.clouds.get(i).cloudPercent += 0.5;
                    }
                }
            }
        }

        double xDelta =
                heliSpeed * Math.cos(Math.toRadians(heliBodyObj.getRotate()-90));
        double yDelta =
                heliSpeed * Math.sin(Math.toRadians(heliBodyObj.getRotate()-90));
        heliBladesObj.setRotate(heliBladesObj.getRotate() + bladeSpeed);
        double maxSpeed = 2.5;

        if(heliSpeed > maxSpeed)
            heliSpeed = maxSpeed;

        if(heliSpeed < -maxSpeed)
            heliSpeed = -maxSpeed;

        if(helicopterStarted && fuel > 0) {
            if (up == 1 && bladeSpeed >= minBladeSpeed ) {
                heliSpeed += 0.1;
                heliBodyObj.setLayoutX(heliBodyObj.getLayoutX() + xDelta);
                heliBodyObj.setLayoutY(heliBodyObj.getLayoutY() + yDelta);
            } else if (down == 1 && bladeSpeed >= minBladeSpeed ) {
                heliSpeed -= 0.1;
                heliBodyObj.setLayoutX(heliBodyObj.getLayoutX() + xDelta);
                heliBodyObj.setLayoutY(heliBodyObj.getLayoutY() + yDelta);
            } else {
                if(heliSpeed > 0) {
                    heliSpeed -= heliSpeedChange;
                    heliBodyObj.setLayoutX(heliBodyObj.getLayoutX() + xDelta);
                    heliBodyObj.setLayoutY(heliBodyObj.getLayoutY() + yDelta);
                } else if(heliSpeed < 0) {
                    heliSpeed += heliSpeedChange;
                    heliBodyObj.setLayoutX(heliBodyObj.getLayoutX() + xDelta);
                    heliBodyObj.setLayoutY(heliBodyObj.getLayoutY() + yDelta);
                }
            } if (left == 1 && bladeSpeed >= minBladeSpeed) {
                heliBodyObj.setRotate(heliBodyObj.getRotate() - 2);
            } if (right == 1 && bladeSpeed >= minBladeSpeed) {
                heliBodyObj.setRotate(heliBodyObj.getRotate() + 2);
            } if(bladeSpeed < maxBladeSpeed) {
                bladeSpeed += 0.03;
            }
            fuel -= fuelDecreaseAmount;
        }

        if(helicopterStarted==false && bladeSpeed>0)
        {
            bladeSpeed -= 0.03;
            heliSpeed = 0;

            if(bladeSpeed < 0)
                bladeSpeed = 0;
        }

        if(ignitionKey == 1 && helicopterStarted == false && bladeSpeed < 9.5) {
            helicopterStarted = true;
            heliStartSound();
        } else if(ignitionKey == 1 && helicopterStarted == true
                && this.intersects(GameApp.helipad) && bladeSpeed >=10) {
            helicopterStarted = false;
            heliStartSound();
        }

        heliBladesObj.setLayoutX(heliBodyObj.getLayoutX()+100/heliScale);
        heliBladesObj.setLayoutY(heliBodyObj.getLayoutY()+100/heliScale);

        fuelText.setX(heliBladesObj.getLayoutX());
        fuelText.setY(heliBladesObj.getLayoutY()+ 150);
        fuelText.setX(heliBladesObj.getLayoutX()+ 25);
        fuelText.setText(fuel+"");

        heliBoundingBox.setX(heliBodyObj.getLayoutX()+100/heliScale);
        heliBoundingBox.setY(heliBodyObj.getLayoutY()+100/heliScale);
        heliBoundingBox.setStroke(RED);
    }

    public void heliStartSound()
    {
        AudioClip heliSound = new AudioClip(this.getClass().getResource(
                "heli_blade_sound.mp3").toString());
        if(helicopterStarted == true) {

            heliSound.setCycleCount(MediaPlayer.INDEFINITE);
            heliSound.play(heliVolume);
        }
        else if(helicopterStarted == false){
            heliSound.stop();
        }
    }

    public Shape getBoundingBox() {return heliBoundingBox;}

}

