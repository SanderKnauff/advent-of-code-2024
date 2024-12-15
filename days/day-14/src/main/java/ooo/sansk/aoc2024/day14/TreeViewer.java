package ooo.sansk.aoc2024.day14;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TreeViewer extends Application {
    List<Day14.Robot> robots = List.of();
    @Nullable GraphicsContext graphicsContext = null;
    int secondsOffset = 66;
    Timer timer = new Timer();
    @Nullable Button prevButton;
    @Nullable Button nextButton;
    @Nullable Button startButton;
    @Nullable TextField counter;
    @Nullable Button stopButton;
    @Nullable TimerTask task;

    @Override
    public void start(Stage primaryStage) {
        final var parameters = getParameters().getRaw();

        final var robotsText = parameters.getFirst();
        final var width = Integer.parseInt(parameters.get(1));
        final var height = Integer.parseInt(parameters.get(2));

        if (robotsText == null) {
            return;
        }

        final var day14 = new Day14();
        robots = day14.readRobots(robotsText);

        primaryStage.setHeight(800);
        primaryStage.setWidth(800);

        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);
        root.setCenter(canvas);

        HBox controls = new HBox();
        prevButton = new Button("Previous");
        prevButton.setOnAction(event -> {
            secondsOffset--;
            render(graphicsContext, width, height);
        });
        startButton = new Button("Start");
        counter = new TextField("0");
        stopButton = new Button("Stop");
        nextButton = new Button("Next");
        nextButton.setOnAction(event -> {
            secondsOffset++;
            render(graphicsContext, width, height);
        });

        controls.getChildren().add(prevButton);
        controls.getChildren().add(startButton);

        counter.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (counter == null) {
                return;
            }
            if (event.getCode() != KeyCode.ENTER) {
                return;
            }
            secondsOffset = Integer.parseInt(counter.getText());
            render(graphicsContext, width, height);
        });

        startButton.setOnAction(event -> {
            task = new TimerTask() {
                @Override
                public void run() {
                    secondsOffset = 66 + (((secondsOffset / 101) + 1) * 101);
                    System.out.println(secondsOffset);
                    render(graphicsContext, width, height);
                }
            };
            timer.scheduleAtFixedRate(task, 0, 500);
        });
        controls.getChildren().add(counter);
        controls.getChildren().add(stopButton);
        controls.getChildren().add(nextButton);
        stopButton.setOnAction(event -> {
            if (task == null) {
                return;
            }
            task.cancel();
        });
        root.setBottom(controls);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void render(GraphicsContext graphicsContext, int width, int height) {
        if (graphicsContext == null) {
            return;
        }

        Platform.runLater(() -> {
            if (counter == null) {
                return;
            }
            counter.setText(String.valueOf(secondsOffset));
        });

        graphicsContext.clearRect(0, 0, width, height);

        for (var robot : robots) {
            final var position = robot.move(secondsOffset, width, height).position();
            graphicsContext.getPixelWriter().setColor(position.x(), position.y(), Color.BLACK);
        }
    }
}
