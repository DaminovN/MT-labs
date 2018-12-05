import java.io.InputStream;
import java.text.ParseException;

public class Parser {
    private LexicalAnalyzer lex;

    private Tree S() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in S and token" + lex.curVal());
        Tree aRes;
        Tree sPrimeRes;
        switch (curToken) {
            case NOT:
            case VAL:
            case LPAREN:
                aRes = A();
                sPrimeRes = SPrime();
                if (sPrimeRes != null) {
                    return new Tree("or", aRes, sPrimeRes);
                } else {
                    return aRes;
                }
            default:
                throw new ParseException("Parse Error: Unknown token in position " + lex.curPos(), lex.curPos());
        }
    }
    private Tree SPrime() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in SPrime and token" + lex.curVal());
        switch (curToken) {
            case OR:
                lex.nextToken();
                Tree aRes = A();
                Tree sPrimeRes = SPrime();
                if (sPrimeRes != null) {
                    return new Tree("or", aRes, sPrimeRes);
                } else {
                    return aRes;
                }
            default:
                return null;
        }
    }
    private Tree A() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in A and token" + lex.curVal());
        Tree bRes;
        Tree aPrimeRes;
        switch (curToken) {
            case NOT:
            case VAL:
            case LPAREN:
                bRes = B();
                aPrimeRes = APrime();
                if (aPrimeRes != null) {
                    return new Tree("xor", bRes, aPrimeRes);
                } else {
                    return bRes;
                }
            default:
                throw new ParseException("Parse Error: Unknown token in position " + lex.curPos(), lex.curPos());
        }
    }
    private Tree APrime() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in APrime and token" + lex.curVal());
        Tree bRes;
        Tree aPrimeRes;
        switch (curToken) {
            case XOR:
                lex.nextToken();
                bRes = B();
                aPrimeRes = APrime();
                if (aPrimeRes != null) {
                    return new Tree("xor", bRes, aPrimeRes);
                } else {
                    return bRes;
                }
            default:
                return null;
        }
    }
    private Tree B() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in B and token" + lex.curVal());
        Tree cRes;
        Tree bPrimeRes;
        switch (curToken) {
            case NOT:
            case VAL:
            case LPAREN:
                cRes = C();
                bPrimeRes = BPrime();
                if (bPrimeRes != null) {
                    return new Tree("and", cRes, bPrimeRes);
                } else {
                    return cRes;
                }
            default:
                throw new ParseException("Parse Error: Unknown token in position " + lex.curPos(), lex.curPos());
        }
    }
    private Tree BPrime() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in BPrime and token" + lex.curVal());
        Tree cRes;
        Tree bPrimeRes;
        switch (curToken) {
            case AND:
                lex.nextToken();
                cRes = C();
                bPrimeRes = BPrime();
                if (bPrimeRes != null) {
                    return new Tree("and", cRes, bPrimeRes);
                } else {
                    return cRes;
                }
            default:
                return null;
        }
    }
    private Tree C() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in C and token" + lex.curVal());
        switch (curToken) {
            case NOT:
                lex.nextToken();
                Tree cRes = C();
                return new Tree("not", cRes);
            case VAL:
            case LPAREN:
                return D();
            default:
                throw new ParseException("Parse Error: Unknown token in position " + lex.curPos(), lex.curPos());
        }
    }
    private Tree D() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in D and token" + lex.curVal());
        switch (curToken) {
            case LPAREN:
                lex.nextToken();
                Tree sRes = S();
                if (lex.curToken() != LexicalAnalyzer.Token.RPAREN) {
                    throw new ParseException("Parse Error: Expected ')' in position " + lex.curPos(), lex.curPos());
                }
                lex.nextToken();
                return sRes;
            case VAL:
                String res = lex.curVal();
                lex.nextToken();
                return new Tree(res);
            default:
                throw new ParseException("Parse Error: Unknown token in position " + lex.curPos(), lex.curPos());
        }
    }
    Tree parse(InputStream is) throws ParseException {
        lex = new LexicalAnalyzer(is);
        Tree res = S();
        if (lex.curToken() != LexicalAnalyzer.Token.END) {
            throw new ParseException("Parse Error: Unknown token in position " + lex.curPos(), lex.curPos());
        }
        return res;
    }
}
