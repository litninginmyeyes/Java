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
    private List<Shape> shapes = new ArrayList<>();
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
        shapes.clear();
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
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
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
            shapes.add(newShape);
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

        for (Shape shape : shapes) {
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

    abstract class Shape {
        protected Color fillColor;      // Цвет заливки
        protected Color strokeColor;
        protected double x, y;
        protected double width, height;
        protected boolean isSelected = false;
        protected boolean isDragging = false;
        protected double dragStartX, dragStartY;

        abstract double area();
        abstract void draw(GraphicsContext gr);
        abstract boolean contains(double x, double y);
        abstract void resize(double newWidth, double newHeight);
        abstract String getShapeType();

        public Shape(Color color, double x, double y) {
            this.fillColor = color;
            this.strokeColor = Color.BLACK;
            this.x = x;
            this.y = y;
        }

        public void setColor(Color color) {
            this.fillColor = color;
        }

        public void setFillColor(Color fillColor) {
            this.fillColor = fillColor;
        }

        public void setStrokeColor(Color strokeColor) {
            this.strokeColor = strokeColor;
        }

        public Color getFillColor() {
            return fillColor;
        }

        public Color getStrokeColor() {
            return strokeColor;
        }

        public double getX() { return x; }
        public double getY() { return y; }
        public double getWidth() { return width; }
        public double getHeight() { return height; }

        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { this.isSelected = selected; }

        public void startDrag(double startX, double startY) {
            isDragging = true;
            dragStartX = startX - x;
            dragStartY = startY - y;
        }

        public void updateDrag(double currentX, double currentY) {
            if (isDragging) {
                x = currentX - dragStartX;
                y = currentY - dragStartY;
            }
        }

        public void endDrag() {
            isDragging = false;
        }

        public boolean isOnResizeHandle(double pointX, double pointY) {
            double handleSize = 15;
            return pointX >= x + width - handleSize &&
                    pointX <= x + width + handleSize &&
                    pointY >= y + height - handleSize &&
                    pointY <= y + height + handleSize;
        }
    }

    class Rectangle extends Shape {
        public Rectangle(Color fillColor, double x, double y, double width, double height) {
            super(fillColor, x, y);
            this.width = width;
            this.height = height;
        }

        @Override
        double area() {
            return width * height;
        }

        @Override
        void draw(GraphicsContext gr) {
            gr.setFill(fillColor);
            gr.fillRect(x, y, width, height);

            gr.setStroke(strokeColor);
            gr.setLineWidth(2);
            gr.strokeRect(x, y, width, height);

            if (isSelected) {
                drawResizeHandles(gr);
            }
        }

        @Override
        boolean contains(double pointX, double pointY) {
            return pointX >= x && pointX <= x + width &&
                    pointY >= y && pointY <= y + height;
        }

        @Override
        void resize(double newWidth, double newHeight) {
            this.width = Math.max(10, newWidth);
            this.height = Math.max(10, newHeight);
        }

        private void drawResizeHandles(GraphicsContext gr) {
            gr.setFill(Color.WHITE);
            gr.setStroke(Color.BLUE);
            gr.setLineWidth(1);
            double handleSize = 8;
            gr.fillRect(x + width - handleSize/2, y + height - handleSize/2, handleSize, handleSize);
            gr.strokeRect(x + width - handleSize/2, y + height - handleSize/2, handleSize, handleSize);
        }

        @Override
        public String getShapeType() {
            return "Прямоугольник";
        }

        @Override
        public String toString() {
            return "Rectangle color is " + fillColor + " and area is " + area();
        }
    }

    class Circle extends Shape {
        double radius;

        public Circle(Color fillColor, double x, double y, double radius) {
            super(fillColor, x, y);
            this.radius = radius;
            this.width = radius * 2;
            this.height = radius * 2;
            this.x = x - radius;
            this.y = y - radius;
        }

        @Override
        double area() {
            return Math.PI * Math.pow(radius, 2);
        }

        @Override
        void draw(GraphicsContext gr) {
            gr.setFill(fillColor);
            gr.fillOval(x, y, radius * 2, radius * 2);

            gr.setStroke(strokeColor);
            gr.setLineWidth(2);
            gr.strokeOval(x, y, radius * 2, radius * 2);

            if (isSelected) {
                drawResizeHandles(gr);
            }
        }

        @Override
        boolean contains(double pointX, double pointY) {
            double centerX = x + radius;
            double centerY = y + radius;
            double distance = Math.sqrt(Math.pow(pointX - centerX, 2) + Math.pow(pointY - centerY, 2));
            return distance <= radius;
        }

        @Override
        void resize(double newWidth, double newHeight) {
            this.radius = Math.max(5, Math.min(newWidth, newHeight) / 2);
            this.width = radius * 2;
            this.height = radius * 2;
        }

        private void drawResizeHandles(GraphicsContext gr) {
            gr.setFill(Color.WHITE);
            gr.setStroke(Color.BLUE);
            gr.setLineWidth(1);
            double handleSize = 8;

            double handleX = x + width - handleSize/2;
            double handleY = y + height - handleSize/2;

            gr.fillRect(handleX, handleY, handleSize, handleSize);
            gr.strokeRect(handleX, handleY, handleSize, handleSize);
        }

        @Override
        public String getShapeType() {
            return "Круг";
        }

        @Override
        public boolean isOnResizeHandle(double pointX, double pointY) {
            double handleSize = 15;
            double handleX = x + width - handleSize/2;
            double handleY = y + height - handleSize/2;

            boolean result = pointX >= handleX && pointX <= handleX + handleSize &&
                    pointY >= handleY && pointY <= handleY + handleSize;


            return result;
        }
    }

    class Ellipse extends Shape {
        double semiMajorAxis, semiMinorAxis;

        public Ellipse(Color fillColor, double x, double y, double semiMajorAxis, double semiMinorAxis) {
            super(fillColor, x, y);
            this.semiMajorAxis = semiMajorAxis;
            this.semiMinorAxis = semiMinorAxis;
            this.width = semiMajorAxis * 2;
            this.height = semiMinorAxis * 2;
            this.x = x - semiMajorAxis;
            this.y = y - semiMinorAxis;
        }

        @Override
        double area() {
            return Math.PI * semiMajorAxis * semiMinorAxis;
        }

        @Override
        void draw(GraphicsContext gr) {
            gr.setFill(fillColor);
            gr.fillOval(x, y, semiMajorAxis * 2, semiMinorAxis * 2);

            gr.setStroke(strokeColor);
            gr.setLineWidth(2);
            gr.strokeOval(x, y, semiMajorAxis * 2, semiMinorAxis * 2);

            if (isSelected) {
                drawResizeHandles(gr);
            }
        }

        @Override
        boolean contains(double pointX, double pointY) {
            double centerX = x + semiMajorAxis;
            double centerY = y + semiMinorAxis;
            double normalizedX = (pointX - centerX) / semiMajorAxis;
            double normalizedY = (pointY - centerY) / semiMinorAxis;
            return (normalizedX * normalizedX + normalizedY * normalizedY) <= 1;
        }

        @Override
        void resize(double newWidth, double newHeight) {
            this.semiMajorAxis = Math.max(5, newWidth / 2);
            this.semiMinorAxis = Math.max(5, newHeight / 2);
            this.width = semiMajorAxis * 2;
            this.height = semiMinorAxis * 2;
        }

        private void drawResizeHandles(GraphicsContext gr) {
            gr.setFill(Color.WHITE);
            gr.setStroke(Color.BLUE);
            gr.setLineWidth(1);
            double handleSize = 8;

            double handleX = x + width - handleSize/2;
            double handleY = y + height - handleSize/2;

            gr.fillRect(handleX, handleY, handleSize, handleSize);
            gr.strokeRect(handleX, handleY, handleSize, handleSize);
        }

        @Override
        public boolean isOnResizeHandle(double pointX, double pointY) {
            double handleSize = 15;
            double handleX = x + width - handleSize/2;
            double handleY = y + height - handleSize/2;

            boolean result = pointX >= handleX && pointX <= handleX + handleSize &&
                    pointY >= handleY && pointY <= handleY + handleSize;

            return result;
        }

        @Override
        public String getShapeType() {
            return "Эллипс";
        }

        @Override
        public String toString() {
            return "Ellipse color is " + fillColor + " and area is " + area();
        }
    }

    class Square extends Shape {
        double side;

        public Square(Color fillColor, double x, double y, double side) {
            super(fillColor, x, y);
            this.side = side;
            this.width = side;
            this.height = side;
        }

        @Override
        double area() {
            return Math.pow(side, 2);
        }

        @Override
        void draw(GraphicsContext gr) {
            gr.setFill(fillColor);
            gr.fillRect(x, y, side, side);

            gr.setStroke(strokeColor);
            gr.setLineWidth(2);
            gr.strokeRect(x, y, side, side);

            if (isSelected) {
                drawResizeHandles(gr);
            }
        }

        @Override
        boolean contains(double pointX, double pointY) {
            return pointX >= x && pointX <= x + side &&
                    pointY >= y && pointY <= y + side;
        }

        @Override
        void resize(double newWidth, double newHeight) {
            double newSize = Math.max(10, Math.min(newWidth, newHeight));
            this.side = newSize;
            this.width = newSize;
            this.height = newSize;
        }

        private void drawResizeHandles(GraphicsContext gr) {
            gr.setFill(Color.WHITE);
            gr.setStroke(Color.BLUE);
            gr.setLineWidth(1);
            double handleSize = 8;
            gr.fillRect(x + side - handleSize/2, y + side - handleSize/2, handleSize, handleSize);
            gr.strokeRect(x + side - handleSize/2, y + side - handleSize/2, handleSize, handleSize);
        }

        @Override
        public String getShapeType() {
            return "Квадрат";
        }

        @Override
        public String toString() {
            return "Square color is " + fillColor + " and area is " + area();
        }
    }
}