package org.example.task1;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Canvas myCanvas;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button circleButton;
    @FXML
    private Button ellipseButton;
    @FXML
    private Button squareButton;
    @FXML
    private Label infoLabel;

    private Shape lastCreatedShape = null;
    private String selectedShapeType = null;
    private GraphicsContext gr;
    private List<Shape> Shapes = new ArrayList<>();
    private Shape selectedShape = null;
    private boolean isResizing = false;
    private boolean isDragging = false;
    private double resizeStartX, resizeStartY;
    private double originalWidth, originalHeight;
    private double startX, startY;
    private Color currentFillColor = Color.RED;
    private Color currentStrokeColor = Color.BLACK;

    @FXML
    private void initialize() {
        gr = myCanvas.getGraphicsContext2D();
        clearCanvas();

        rectangleButton.setOnAction(e -> selectShapeType("rectangle"));
        circleButton.setOnAction(e -> selectShapeType("circle"));
        ellipseButton.setOnAction(e -> selectShapeType("ellipse"));
        squareButton.setOnAction(e -> selectShapeType("square"));

        myCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        myCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        myCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);
    }

    @FXML
    private void clearCanvas() {
        gr.setFill(Color.WHITE);
        gr.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        Shapes.clear();
        selectedShape = null;
        lastCreatedShape = null;
        updateInfoLabel();
    }

    private void selectShapeType(String shapeType) {
        selectedShapeType = shapeType;
        if (selectedShape != null) {
            selectedShape.setSelected(false);
            selectedShape = null;
        }
        System.out.println("Selected shape type: " + shapeType);
    }

    private void updateInfoLabel() {
        if (lastCreatedShape != null) {
            String shapeInfo = String.format("Последняя созданная фигура: %s",
                    lastCreatedShape.getShapeType());
            infoLabel.setText(shapeInfo);
        } else {
            infoLabel.setText("Фигуры еще не созданы");
        }
    }

    private void handleMousePressed(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (event.getButton() == MouseButton.PRIMARY) {
            Shape clickedShape = findShapeAt(x, y);

            if (selectedShape != null && selectedShape.isOnResizeHandle(x, y)) {
                System.out.println("Resize handle clicked");
                isResizing = true;
                resizeStartX = x;
                resizeStartY = y;
                originalWidth = selectedShape.getWidth();
                originalHeight = selectedShape.getHeight();
            }
            else if (clickedShape != null) {
                System.out.println("Shape clicked: " + clickedShape);
                if (selectedShape != null && selectedShape != clickedShape) {
                    selectedShape.setSelected(false);
                }
                selectedShape = clickedShape;
                selectedShape.setSelected(true);
                selectedShapeType = null;

                isDragging = true;
                selectedShape.startDrag(x, y);
            }
            else if (selectedShapeType != null) {
                System.out.println("Creating new shape at: " + x + ", " + y);
                createNewShape(x, y);
            }
            else {
                if (selectedShape != null) {
                    selectedShape.setSelected(false);
                    selectedShape = null;
                    System.out.println("Deselected shape");
                }
            }
            redrawAllShapes();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isResizing && selectedShape != null) {
            double deltaX = event.getX() - resizeStartX;
            double deltaY = event.getY() - resizeStartY;
            selectedShape.resize(originalWidth + deltaX, originalHeight + deltaY);
            redrawAllShapes();
        } else if (isDragging && selectedShape != null) {
            selectedShape.updateDrag(event.getX(), event.getY());
            redrawAllShapes();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (isDragging && selectedShape != null) {
            selectedShape.endDrag();
        }
        isResizing = false;
        isDragging = false;
    }

    private Shape findShapeAt(double x, double y) {
        for (int i = Shapes.size() - 1; i >= 0; i--) {
            Shape shape = Shapes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    private void createNewShape(double x, double y) {
        Shape newShape = null;

        switch (selectedShapeType) {
            case "rectangle":
                newShape = new Rectangle(Color.RED, x, y, 80, 60);
                break;
            case "circle":
                newShape = new Circle(Color.BLUE, x, y, 40);
                break;
            case "ellipse":
                newShape = new Ellipse(Color.ORANGE, x, y, 60, 40);
                break;
            case "square":
                newShape = new Square(Color.GREEN, x, y, 70);
                break;
        }

        if (newShape != null) {
            Shapes.add(newShape);
            lastCreatedShape = newShape;
            if (selectedShape != null) {
                selectedShape.setSelected(false);
            }
            selectedShape = newShape;
            selectedShape.setSelected(true);

            updateInfoLabel();
        }
    }

    private void redrawAllShapes() {
        gr.setFill(Color.WHITE);
        gr.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        for (Shape shape : Shapes) {
            shape.draw(gr);
        }
    }

    @FXML
    private void setFillRed() {
        currentFillColor = Color.RED;
        updateSelectedShapeColors();
    }

    @FXML
    private void setFillBlue() {
        currentFillColor = Color.BLUE;
        updateSelectedShapeColors();
    }

    @FXML
    private void setFillGreen() {
        currentFillColor = Color.GREEN;
        updateSelectedShapeColors();
    }

    @FXML
    private void setFillYellow() {
        currentFillColor = Color.YELLOW;
        updateSelectedShapeColors();
    }


    @FXML
    private void setStrokeBlack() {
        currentStrokeColor = Color.BLACK;
        updateSelectedShapeColors();
    }

    @FXML
    private void setStrokeWhite() {
        currentStrokeColor = Color.WHITE;
        updateSelectedShapeColors();
    }

    @FXML
    private void setStrokeRed() {
        currentStrokeColor = Color.RED;
        updateSelectedShapeColors();
    }

    @FXML
    private void setStrokeBlue() {
        currentStrokeColor = Color.BLUE;
        updateSelectedShapeColors();
    }


    private void updateSelectedShapeColors() {
        if (selectedShape != null) {
            selectedShape.setFillColor(currentFillColor);
            selectedShape.setStrokeColor(currentStrokeColor);
            redrawAllShapes();
        }
    }

}