package org.example.task1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
