import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LexicalAnalyzer {
    public enum Token {
        AND, OR, XOR, NOT, LPAREN, RPAREN, VAL, END
    }
    private InputStream is;
    private int curChar;
    private int curPos;
    private String curString;
    private Token curToken;
    private boolean needToMiss = false;
    private static final int maxTokenLength = 3;
    private static Token getTokenAnalogue(String val, int pos) throws ParseException {
        if (val.length() == 0) {
            return Token.END;
        }
        if (tokenAnalogue.containsKey(val)) {
            return tokenAnalogue.get(val);
        }
        if (val.length() == 1)
            return Token.VAL;
        throw new ParseException("Unable to parse " + val, pos);
    }
    private static Map<String, Token> tokenAnalogue;
    static  {
        tokenAnalogue = new HashMap<>();
        tokenAnalogue.put("and", Token.AND);
        tokenAnalogue.put("or", Token.OR);
        tokenAnalogue.put("xor", Token.XOR);
        tokenAnalogue.put("not", Token.NOT);
        tokenAnalogue.put("(", Token.LPAREN);
        tokenAnalogue.put(")", Token.RPAREN);
    }
    public LexicalAnalyzer(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextData();
    }
    private void nextData() throws ParseException {
        int read = 0;
        StringBuilder curVal = new StringBuilder();
        while(read < maxTokenLength) {
            nextChar();
            if (read == 0 && isBlank(curChar)) {
                continue;
            }
            else if (isBlank(curChar))
                break;
            if (read != 0 && (tokenAnalogue.containsKey(String.valueOf((char)curChar)) || curChar == -1)) {
                needToMiss = true;
                break;
            }
            if (curChar != -1)
                curVal.append((char) curChar);
            else if (read == 0 && curChar == -1)
                break;
            if (tokenAnalogue.containsKey(String.valueOf((char)curChar))) {
                break;
            }
            read++;
        }
        curString = curVal.toString();
        curToken = getTokenAnalogue(curString, curPos);
    }
    private void nextChar() throws ParseException {
        if (needToMiss) {
            needToMiss = false;
            return;
        }
        curPos++;
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }
    private boolean isBlank(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }
    public void nextToken() throws ParseException {
        nextData();
    }
    public Token curToken() {
        return curToken;
    }
    public int curPos() {
        return curPos;
    }
    public String curVal() {
        return curString;
    }
}
