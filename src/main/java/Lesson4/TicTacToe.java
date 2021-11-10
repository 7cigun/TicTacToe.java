package Lesson4;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static final int MAX_SIZE = 25;
    private static final char FIELD_EMPTY = '•';
    private static final char FIELD_USER = 'X';
    private static final char FIELD_AI = '0';
    private static final String MAP_SPACE = "   ";
    private static final String MAP_ORIGIN = "♥";

    private static final Scanner in = new Scanner(System.in);
    private static final Random random = new Random();
    private static int mapSize;
    private static int winSize;
    private static int maxIndex;
    private static char[][] map;
    private static int lastRow;
    private static int lastColumn;
    private static int turnsCount;

    public static void runGame() {
        do {
            System.out.println("\n\nИгра начинается!");
            turnsCount = 0;
            mapSize = getMapSize();
            winSize = setWinSize(mapSize);
            setMaxIndex (mapSize);
            System.out.println("Для победы необходимо " + winSize + " X подряд.");
            map = new char[mapSize][mapSize];
            initEnv(mapSize);
            drawMap();
            playGame();
            System.out.println();
        } while (isContinueGame());
        endGame();
    }

    private static void playGame() {
        while (true) {
            humanTurn();
            drawMap();
            if (checkEnd(FIELD_USER)) {
                break;
            }

            aiTurn();
            drawMap();
            if (checkEnd(FIELD_AI)) {
                break;
            }
        }
    }

    public static void initEnv(int mapSize) {

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                map[i][j] = FIELD_EMPTY;
            }
        }
    }

    public static int getMapSize() {
        while (true) {
            System.out.print("Введите размер игрового поля: ");
            if (in.hasNextInt()) {
                int n = in.nextInt();
                if (n >= 3 && n < MAX_SIZE) {
                    return n;
                } else {
                    System.out.println("\nРазмер игрового поля может быть от 3 до " + MAX_SIZE);
                }
            } else {
                in.next();
                System.out.println("\nДопустимо только целое число!");
            }
        }
    }

    public static int setWinSize(int mapSize) {
        if (mapSize >= 3 && mapSize <= 5) {
            return 3;
        } else if (mapSize >= 6 && mapSize <= 10) {
            return 4;
        } else {
            return 5;
        }
    }



    public static void setMaxIndex (int mapSize) {
        maxIndex = mapSize - 1;
    }


    private static void drawMap() {
        printMapHeader();
        printMapBody();
    }

    private static void printMapHeader() {
        System.out.print(MAP_ORIGIN + MAP_SPACE);
        for (int i = 0; i < mapSize; i++) {
            printMapNumber(i);
        }
        System.out.println();
    }

    private static void printMapBody() {
        for (int i = 0; i < mapSize; i++) {
            printMapNumber(i);
            for (int j = 0; j < mapSize; j++) {
                System.out.print(map[i][j] + MAP_SPACE);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printMapNumber(int i) {
        if (i >= 9) {
            System.out.print(i + 1 + "  ");
        } else {
            System.out.print(i + 1 + "   ");
        }
    }

    private static void humanTurn() {
        System.out.println("ВАШ ХОД:");
        int rowNumber, columnNumber;

        while (true) {
            rowNumber = getValidNumberFromUser(1) - 1;
            columnNumber = getValidNumberFromUser(2) - 1;
            if (isCellFree(rowNumber, columnNumber)) {
                break;
            } else {
                System.out.println("\nВы выбрали занятую ячейку!");
            }
        }
        map[rowNumber][columnNumber] = FIELD_USER;
        lastRow = rowNumber;
        lastColumn = columnNumber;
        turnsCount++;
    }

    private static int getValidNumberFromUser(int coordinate) {
        while (true) {
            if (coordinate == 1) {
                System.out.print("Номер строки(1-" + mapSize + "): ");
            } else {
                System.out.print("Номер столбца(1-" + mapSize + "): ");
            }

            if (in.hasNextInt()) {
                int n = in.nextInt();
                if (isNumberValid(n)) {
                    return n;
                } else {
                    System.out.println("\nПроверьте значение координаты. Должно быть от 1 до " + mapSize);
                }
            } else {
                in.next();
                System.out.println("\nВвод допускает лишь целые числа!");
            }
        }
    }

    private static boolean isNumberValid(int n) {
        return n >= 1 && n <= mapSize;
    }

    private static boolean isCellFree(int rowNumber, int columnNumber) {
        return map[rowNumber][columnNumber] == FIELD_EMPTY;
    }

    private static void aiTurn() {
        System.out.println("ХОД КОМПЬЮТЕРА:");
        int rowNumber;
        int columnNumber;

        do {
            rowNumber = random.nextInt(mapSize);
            columnNumber = random.nextInt(mapSize);
        } while (!isCellFree(rowNumber, columnNumber));

        map[rowNumber][columnNumber] = FIELD_AI;
        lastRow = rowNumber;
        lastColumn = columnNumber;
        turnsCount++;
    }

    private static boolean checkEnd(char symbol) {
        if (checkWin(symbol)) {
            if (symbol == FIELD_USER) {
                System.out.println("\nПоздравляем с победой!");
            } else {
                System.out.println("\nПобеду одержал компьютер.");
            }
            return true;
        }

        if (checkDraw()) {
            System.out.println("\nНичья!");
            return true;
        }

        return false;
    }

    private static boolean checkDraw() {
        return turnsCount >= mapSize * mapSize;
    }

    private static boolean checkWin(char symbol) {
        return checkRow(symbol) || checkColumn(symbol) || checkUpDiag(symbol) || checkDownDiag(symbol);
    }



    private static boolean checkRow(char symbol) {
        int n = 0;
        for (int i = 0; i < mapSize; i++) {
            if (map[lastRow][i] == symbol) {
                n++;
                if (n == winSize) {
                    return true;
                }
            } else {
                n = 0;
            }
        }
        return false;
    }

    private static boolean checkColumn(char symbol) {
        int n = 0;
        for (int i = 0; i < mapSize; i++) {
            if (map[i][lastColumn] == symbol) {
                n++;
                if (n == winSize) {
                    return true;
                }
            } else {
                n = 0;
            }
        }
        return false;
    }

    private static boolean checkDownDiag(char symbol) {
        int n = 0;
        int startRowIndex = (lastRow > lastColumn) ? lastRow - lastColumn : 0;
        int startColumnIndex = (lastRow > lastColumn) ? 0 : lastColumn - lastRow;

        for (int i = 0; i < mapSize; i++) {
            int row = startRowIndex + i;
            int column = startColumnIndex + i;
            if (row <= maxIndex && column <= maxIndex) {
                if (map[row][column] == symbol) {
                    n++;
                    if (n == winSize) {
                        return true;
                    }
                } else {
                    n = 0;
                }
            }
        }
        return false;
    }

    private static boolean checkUpDiag(char symbol) {
        int n = 0;
        int startRowIndex = Math.min((lastRow + lastColumn), maxIndex);
        int startColumnIndex = (startRowIndex == maxIndex) ? lastRow + lastColumn - startRowIndex : 0;

        for (int i = 0; i < mapSize; i++) {
            int row = startRowIndex - i;
            int column = startColumnIndex + i;
            if (row >= 0 && column <= maxIndex) {
                if (map[row][column] == symbol) {
                    n++;
                    if (n == winSize) {
                        return true;
                    }
                } else {
                    n = 0;
                }
            }
        }
        return false;
    }

    private static boolean isContinueGame() {
        System.out.println("Хотите продолжить? y\\n");
        return switch (in.next()) {
            case "y", "yes", "+", "да", "конечно" -> true;
            default -> false;
        };
    }

    private static void endGame() {
        System.out.println("До новых встреч!");
    }

}

