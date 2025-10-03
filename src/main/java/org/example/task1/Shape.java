package org.example.task1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Shape {
    protected Color fillColor;
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
