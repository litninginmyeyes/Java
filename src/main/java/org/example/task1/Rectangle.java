package org.example.task1;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
