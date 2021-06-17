package com.julesg10.tetris;
import java.util.Arrays;
import java.util.Random;

enum TetrisShapeType
{
    LINE,
    SQUARE,
    L,
    Z,
    T,
    REVERSE_L,
    REVERSE_Z,
    NONE
}

enum TetrisClickActionType
{
    LEFT,
    RIGHT,
    ROTATE,
    NONE
}

class TetrisClickAction
{
    public static int SURFACE_HEIGHT = 0;
    public static int SURFACE_WIDTH = 0;

    public static TetrisClickActionType getAction(int section,Point pos)
    {
        if(pos.getY() >= SURFACE_HEIGHT - section)
        {
            int size = SURFACE_WIDTH/3;
            if(pos.getX() >= 0 && pos.getX() < size)
            {
                return TetrisClickActionType.LEFT;
            }else if(pos.getX() >= size && pos.getX() < size*2)
            {
                return TetrisClickActionType.ROTATE;
            }else  if(pos.getX() >= size*2 && pos.getX() <= SURFACE_WIDTH)
            {
                return TetrisClickActionType.RIGHT;
            }
        }
        return TetrisClickActionType.NONE;
    }
}

public class TetrisControl {

    private Point[] current_position = new Point[4];
    private TetrisShapeType current_type;
    private Random rand = new Random();

    private TetrisShapeType[][] pointsMatrix;
    public final int TETRIS_WIDTH = 10;
    public int TETRIS_HEIGHT = 20;

    private int tetrisScore = 0;
    public TetrisControl() {
    }

    public void Init() {
        this.pointsMatrix = new TetrisShapeType[TETRIS_WIDTH][TETRIS_HEIGHT];

        for (int x = 0; x < TETRIS_WIDTH; x++) {
            for (int y = 0; y < TETRIS_HEIGHT; y++) {
                this.pointsMatrix[x][y] = TetrisShapeType.NONE;
            }
        }

        this.generatePoints();
    }

    public int getScore() {
        return this.tetrisScore;
    }

    public int[] getTetrisShapeColor(TetrisShapeType type) {
        int[] argb = new int[4];
        argb[0] = 255;
        argb[1] = 0;
        argb[2] = 0;
        argb[3] = 0;

        switch (type) {
            case NONE:
                argb[1] = 60;
                argb[2] = 60;
                argb[3] = 60;
                break;
            case LINE:
                argb[1] = 0;
                argb[2] = 240;
                argb[3] = 240;
                break;
            case SQUARE:
                argb[1] = 240;
                argb[2] = 240;
                argb[3] = 0;
                break;
            case L:
                argb[1] = 0;
                argb[2] = 0;
                argb[3] = 240;
                break;
            case REVERSE_L:
                argb[1] = 240;
                argb[2] = 160;
                argb[3] = 0;
                break;
            case REVERSE_Z:
                argb[1] = 0;
                argb[2] = 240;
                argb[3] = 0;
                break;
            case Z:
                argb[1] = 240;
                argb[2] = 0;
                argb[3] = 0;
                break;
            case T:
                argb[1] = 160;
                argb[2] = 0;
                argb[3] = 240;
                break;
        }

        return argb;
    }

    public TetrisShapeType[][] getPointsMatrix() {
        return this.pointsMatrix;
    }

    public TetrisShapeType[][] getActivePointsMatrix() {
        TetrisShapeType[][] activematrix = new TetrisShapeType[this.TETRIS_WIDTH][];

        for (int i = 0; i < this.TETRIS_WIDTH; i++) {
            activematrix[i] = this.pointsMatrix[i].clone();
        }

        for (int i = 0; i < this.current_position.length; i++) {
            if (this.isOnMatrix(this.current_position[i])) {
                activematrix[(int) this.current_position[i].getX()][(int) this.current_position[i].getY()] = this.current_type;
            }
        }
        return activematrix;
    }

    public TetrisShapeType getActiveType() {
        return this.current_type;
    }

    public Point[] getActivePositions() {
        return this.current_position;
    }

    public void update() {
        this.moveDown();
    }

    public boolean setTile(Point p, TetrisShapeType value) {
        if (this.isOnMatrix(p)) {
            this.pointsMatrix[(int) p.getX()][(int) p.getY()] = value;
            return true;
        }

        return false;
    }

    public TetrisShapeType getTile(Point p) {
        if (this.isOnMatrix(p)) {
            return this.pointsMatrix[(int) p.getX()][(int) p.getY()];
        }
        return TetrisShapeType.NONE;
    }

    public boolean isOnMatrix(Point p) {
        if ((p.getX() >= 0 && p.getX() < this.TETRIS_WIDTH) && (p.getY() >= 0 && p.getY() < this.TETRIS_HEIGHT)) {
            return true;
        }
        return false;
    }


    private boolean moveX(boolean left) {
        int add = left ? -1 : 1;
        boolean valide = true;

        for (int i = 0; i < this.current_position.length; i++) {
            if(!(this.current_position[i].getX()+add < 0)  && !(this.current_position[i].getX()+add >= this.TETRIS_WIDTH))
            {
                Point tmp = new Point(this.current_position[i].getX()+add,this.current_position[i].getY());
                if(this.getTile(tmp) != TetrisShapeType.NONE)
                {
                    valide = false;
                    break;
                }
            }else{
                valide = false;
                break;
            }
        }

        if(valide)
        {
            for (int i = 0; i < this.current_position.length; i++) {
                this.current_position[i].setX(this.current_position[i].getX()+add);
            }

            return true;
        }else{
            return false;
        }
    }

    public boolean left() {
        return this.moveX(true);
    }

    public boolean right() {
        return this.moveX(false);
    }

    public boolean rotate() {
        return false;
    }

    private boolean moveDown() {
        boolean valide = true;

        for (int i = 0; i < this.current_position.length; i++) {
            if(!(this.current_position[i].getY()+1 >= this.TETRIS_HEIGHT))
            {
                Point tmp = new Point(this.current_position[i].getX(),this.current_position[i].getY()+1);
                if(this.getTile(tmp) != TetrisShapeType.NONE)
                {
                    valide = false;
                    break;
                }
            }else{
                valide = false;
                break;
            }
        }

        if(valide)
        {
            for (int i = 0; i < this.current_position.length; i++) {
                this.current_position[i].setY(this.current_position[i].getY()+1);
            }
            return true;
        }else{
            this.stopCurrent();
            return false;
        }
    }

    private void stopCurrent() {
        for (int i = 0; i < this.current_position.length; i++)
        {
            this.setTile(this.current_position[i],this.current_type);
        }


        //this.checkLines();

        this.generatePoints();
    }

    private void checkLines() {
        for (int i = 0; i < this.TETRIS_HEIGHT; i++)
        {
            boolean stop = false;
            for (int j = 0; j < this.TETRIS_WIDTH; j++)
            {
                if(this.getTile(new Point(i,j)) == TetrisShapeType.NONE)
                {
                    stop = true;
                    break;
                }
            }

            if(!stop)
            {
                this.clearLine(i);
                this.tetrisScore += 10 * i;
                i--;
            }
        }
    }

    private void clearLine(int y)
    {
        for(int i = y;i < 0; i--)
        {
            for(int j = 0;j<this.TETRIS_WIDTH;j++)
            {
                this.setTile(new Point(j,i),this.getTile(new Point(j,i+1)));
            }
        }
    }

    private void generatePoints() {
        int id = this.rand.nextInt(4);

        this.current_type = TetrisShapeType.values()[id];
        switch (TetrisShapeType.values()[id]) {
            case SQUARE:
                this.current_position = this.SQUARE_Points;
                break;
            case L:
                this.current_position = this.L_Points;
                break;
            case LINE:
                this.current_position = this.LINE_Points;
                break;
            case Z:
                this.current_position = this.Z_Points;
                break;
            case REVERSE_L:
                this.current_position = this.REVERSE_L_Points;
                break;
            case REVERSE_Z:
                this.current_position = this.REVERSE_Z_Points;
                break;
            case T:
                this.current_position = this.T_Points;
                break;
        }
    }

    private final Point[] SQUARE_Points = {new Point(3, -2), new Point(4, -2), new Point(3, -1), new Point(4, -1)};
    private final Point[] LINE_Points = {new Point(4, -1), new Point(4, -2), new Point(4, -3), new Point(4, -4)};
    private final Point[] Z_Points = {new Point(3, -2), new Point(4, -2), new Point(4, -1), new Point(5, -1)};
    private final Point[] T_Points = {new Point(3, -2), new Point(4, -2), new Point(5, -2), new Point(4, -1)};
    private final Point[] L_Points = {new Point(3, -2), new Point(3, -1), new Point(4, -1), new Point(5, -1)};

    private final Point[] REVERSE_L_Points = {new Point(3, -2), new Point(4, -2), new Point(3, -1), new Point(4, -1)};
    private final Point[] REVERSE_Z_Points = {new Point(3, -2), new Point(4, -2), new Point(3, -1), new Point(4, -1)};
}
