import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.Random;
import java.util.Scanner;

public class Tester {
    Tester() { }
    public static void randomTestWithSize(int sz) throws ParseException {
        System.out.println("Test size: " + sz);
        String testStr = "x";
        Tree testTree = new Tree("x");
        Random rand = new Random();
        for (int i = 0; i < sz; i++) {
            int opt = rand.nextInt(4);
            char var = (char) ('a' + rand.nextInt(26));
            if (opt == 0) {
                testStr = "(not" + testStr + ")";
                testTree = new Tree("not", testTree);
            } else {
                String oper = "";
                int swap = rand.nextInt(2);
                switch (opt) {
                    case 1:
                        oper = "or";
                        break;
                    case 2:
                        oper = "xor";
                        break;
                    case 3:
                        oper = "and";
                        break;
                }
                if (swap == 0) {
                    testStr = "(" + testStr + " " + oper + " " + var + ")";
                    testTree = new Tree(oper, testTree, new Tree(Character.toString(var)));
                } else {
                    testStr = "(" + var + " " + oper + " " + testStr + ")";
                    testTree = new Tree(oper, new Tree(Character.toString(var)), testTree);
                }
            }
        }
        Parser p = new Parser();
        Tree res = p.parse(new ByteArrayInputStream(testStr.getBytes()));
        if (!res.equals(testTree)) {
            throw new ParseException("WA in test " + testStr, 0);
        }
        System.out.println("OK.");
    }
    public static void priorityTest() throws ParseException {
        System.out.println("===============>PRIORITY TEST<===============");
        String test = "not (not a and b and (c or d xor e))";
        Parser p = new Parser();
        Tree res;
        res = p.parse(new ByteArrayInputStream(test.getBytes()));
        Tree.printTree(res);
        System.out.println("Please launch visualizer and tell whether OK or WA");
        Scanner in = new Scanner(System.in);
        String reply = in.nextLine();
        if (reply.equals("WA")) {
            throw new ParseException("WA in test " + test, 0);
        }
        System.out.println("Priority Test OK.");
    }
    public static void correctTest(String test) throws ParseException {
        System.out.println("Correctness check for: "+ test);
        Parser p = new Parser();
        Tree res;
        res = p.parse(new ByteArrayInputStream(test.getBytes()));
        Tree.printTree(res);
        System.out.println("Please launch visualizer and tell whether OK or WA");
        Scanner in = new Scanner(System.in);
        String reply = in.nextLine();
        if (reply.equals("WA")) {
            throw new ParseException("WA in test " + test, 0);
        }
        System.out.println("OK.");
    }
    public static void incorrectTest(String test) {
        System.out.println("Start incorrect test: " + test + " <===============");
        Parser p = new Parser();
        try {
            p.parse(new ByteArrayInputStream(test.getBytes()));
        } catch (ParseException e) {
            System.out.print("Caught Error, OK : ");
            System.out.println(e.getMessage());
        }
        System.out.println("Finish incorrect test ===============>");
    }
    public static void test() throws ParseException {
        for (int i = 0; i < 100; i++) {
            randomTestWithSize(new Random().nextInt(100));
        }
        priorityTest();
        correctTest("a   and      b");
        correctTest("not not a");
        incorrectTest("(a and b))");
        incorrectTest("an and b");
        incorrectTest("and and b");
        incorrectTest("a and  ");
    }
}