package com.nuvalence.RA;

import java.util.Optional;

public class NRectangle {

    // point with minimum values of x and y coordinates of all the points of the rectangle
    private NPoint bottomLeft;

    private NPoint topRight;

    // represents delta on the x axis between left and right set of points of the rectangle
    private int width;

    // represents delta on the y axis between top and bottom set of points of the rectangle
    private int height;

    public NRectangle(int bottomLeftX, int bottomLeftY, int width, int height) throws IllegalArgumentException {

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Height and / or width of the rectangle cannot be 0 or negative.");
        }

        this.bottomLeft = new NPoint();
        this.bottomLeft.setX((long)bottomLeftX);
        this.bottomLeft.setY((long)bottomLeftY);

        this.topRight = new NPoint();
        // the two values below can overflow, using long for that
        this.topRight.setX((long)bottomLeftX + (long)width);
        this.topRight.setY((long)bottomLeftY + (long)height);

        this.height = height;
        this.width = width;
    }

    public NPoint getBottomLeft() {
        return bottomLeft;
    }

    public NPoint getTopRight() {
        return topRight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Optional<NRectangle> doesIntersectWith(NRectangle other) {

        // rule out contains case first
        if (this.doesContain(other) || other.doesContain(this)) {
            return Optional.ofNullable(null);
        }

        // for x axis, check if there is a gap between max left x value and min right x value
        long xMaxLeft = Math.max(this.bottomLeft.getX(), other.bottomLeft.getX());
        long xMinRight = Math.min(this.topRight.getX(), other.topRight.getX());

        // for y axis, check if there is a gap between max bottom y value and min top y value
        long yMaxBottom = Math.max(this.bottomLeft.getY(), other.bottomLeft.getY());
        long yMinTop = Math.min(this.topRight.getY(), other.topRight.getY());

        if (xMaxLeft < xMinRight && yMaxBottom < yMinTop) {
            // rectangles intersect. The intersection is a rectangle as well

            // none of the 4 values below can overflow int, safe to downcast
            NRectangle intersection = new NRectangle((int)xMaxLeft, (int)yMaxBottom,
                    (int)(xMinRight - xMaxLeft),
                    (int)(yMinTop - yMaxBottom));
            return Optional.ofNullable(intersection);
        }

        return Optional.ofNullable(null);
    }

    public boolean doesContain(NRectangle other) {
        // check if "other" rectangle is fully contained on the x axis
        if (this.bottomLeft.getX() < other.bottomLeft.getX() &&
                this.bottomLeft.getX() < other.topRight.getX() &&
                this.topRight.getX() > other.bottomLeft.getX() &&
                this.topRight.getX() > other.topRight.getX()) {
            // if so, check the same condition on the y axis
            if (this.bottomLeft.getY() < other.bottomLeft.getY() &&
                    this.bottomLeft.getY() < other.topRight.getY() &&
                    this.topRight.getY() > other.bottomLeft.getY() &&
                    this.topRight.getY() > other.topRight.getY()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdjacentTo(NRectangle other) {

        // if rectangles are adjacent, they are not intersecting or one does not contain another
        if (this.doesContain(other) || !this.doesIntersectWith(other).isEmpty()) {
            return false;
        }

        // each of the 4 sides of this rectangle can only be adjacent with 1 side of the other rectangle, respectively

        // adjacent on along Y axis on the left
        if (this.getBottomLeft().getX() == other.getTopRight().getX() ||
                this.getTopRight().getX() == other.getBottomLeft().getX()) {
            long maxLowerY = Math.max(this.getBottomLeft().getY(), other.getBottomLeft().getY());
            long minUpperY = Math.min(this.getTopRight().getY(), other.getTopRight().getY());

            if (maxLowerY <= minUpperY) {
                // rectangles are adjacent along y axis
                return true;
            }
        }

        // adjacent on along Y axis on the right
        if (this.getBottomLeft().getY() == other.getTopRight().getY() ||
                this.getTopRight().getY() == other.getBottomLeft().getY()) {
            long maxLeftX = Math.max(this.getBottomLeft().getX(), other.getBottomLeft().getX());
            long minRightX = Math.min(this.getTopRight().getX(), other.getTopRight().getX());

            if (maxLeftX <= minRightX) {
                // rectangles are adjacent along X axis
                return true;
            }
        }

        return false;
    }
}

