package org.example.task1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
