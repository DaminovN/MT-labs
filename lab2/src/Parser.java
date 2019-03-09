import java.io.InputStream;
import java.security.cert.TrustAnchor;
import java.text.ParseException;

public class Parser {
    private LexicalAnalyzer lex;

    private Tree S() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in S and token" + lex.curVal());
        Tree curRes;
        Tree aRes;
        Tree sPrimeRes;
        switch (curToken) {
            case NOT:
            case VAL:
            case LPAREN:
                aRes = B();
                String op = lex.curVal();
                sPrimeRes = SPrime();
                if (sPrimeRes != null) {
                    curRes = new Tree(op, aRes, sPrimeRes);
                    while (true) {
                        op = lex.curVal();
                        sPrimeRes = SPrime();
                        if (sPrimeRes == null)
                            return curRes;
                        else
                            curRes = new Tree(op, curRes, sPrimeRes);
                    }
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
            case XOR:
                lex.nextToken();
                return B();
            default:
                return null;
        }
    }
    private Tree A() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in A and token" + lex.curVal());
        Tree bRes;
        Tree aPrimeRes;
        Tree curRes;
        switch (curToken) {
            case NOT:
            case VAL:
            case LPAREN:
                bRes = B();
                aPrimeRes = APrime();
                if (aPrimeRes != null) {
                    curRes = new Tree("xor", bRes, aPrimeRes);
                    while (true) {
                        aPrimeRes = APrime();
                        if (aPrimeRes == null)
                            return curRes;
                        else
                            curRes = new Tree("xor", curRes, aPrimeRes);
                    }
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
        switch (curToken) {
            case XOR:
                lex.nextToken();
                return B();
            default:
                return null;
        }
    }
    private Tree B() throws ParseException {
        LexicalAnalyzer.Token curToken = lex.curToken();
//        System.out.println("in B and token" + lex.curVal());
        Tree cRes;
        Tree bPrimeRes;
        Tree curRes;
        switch (curToken) {
            case NOT:
            case VAL:
            case LPAREN:
                cRes = C();
                bPrimeRes = BPrime();
                if (bPrimeRes != null) {
                    curRes = new Tree("and", cRes, bPrimeRes);
                    while (true) {
                        bPrimeRes = BPrime();
                        if (bPrimeRes == null)
                            return curRes;
                        else
                            curRes = new Tree("and", curRes, bPrimeRes);
                    }
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
        switch (curToken) {
            case AND:
                lex.nextToken();
                return C();
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
