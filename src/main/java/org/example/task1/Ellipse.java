package org.example.task1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
