package ru.ifmo.md.lesson4;

import java.math.BigDecimal;

//Sergey Budkov 2536

public class DummyCalculateEngine implements CalculationEngine {

    String expression, operator;
    Expr answer;
    int pos;

    abstract class Operators implements Expr {
        protected Expr first;
        protected Expr second;

        protected Operators(Expr first, Expr second) {
            this.first = first;
            this.second = second;
        }

        public BigDecimal eval() throws Exception {
            return calc(first.eval(), second.eval());
        }

        protected abstract BigDecimal calc(BigDecimal first, BigDecimal second) throws Exception;
    }

    interface Expr {
        BigDecimal eval() throws Exception;
    }

    public class operatorAdd extends Operators {
        public operatorAdd(Expr first, Expr second) {
            super(first, second);
        }

        public BigDecimal calc(BigDecimal first, BigDecimal second) {
            return first.add(second);
        }
    }

    public class operatorSub extends Operators {
        public operatorSub(Expr first, Expr second) {
            super(first, second);
        }

        protected BigDecimal calc(BigDecimal first, BigDecimal second) {
            return first.subtract(second);
        }
    }

    public class operatorMul extends Operators {
        public operatorMul(Expr first, Expr second) {
            super(first, second);
        }

        protected BigDecimal calc(BigDecimal first, BigDecimal second) {
            return first.multiply(second);
        }
    }

    public class Division extends Operators {
        public Division(Expr first, Expr second) {
            super(first, second);
        }

        public BigDecimal calc(BigDecimal first, BigDecimal second) throws Exception {
            BigDecimal small = new BigDecimal("0.0000000001");
            if (second.abs().min(small) == second.abs())
                throw new Exception();
            return first.divide(second, BigDecimal.ROUND_HALF_EVEN)
                    .setScale(30, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    public class Const implements Expr {
        public final BigDecimal value;

        public Const(BigDecimal value) {
            this.value = value.setScale(30, BigDecimal.ROUND_HALF_EVEN);
        }

        public BigDecimal eval() {
            return value;
        }
    }

    public Expr Parser(String expression) throws Exception {
        this.expression = expression;
        pos = 0;
        operator = "";
        isValid();
        next();
        return AddDiv();
    }

    void isValid() throws Exception {
        int brNum = 0;
        if (expression.length() == 0)
            throw new Exception();
        if (expression.charAt(0) == '/' || expression.charAt(0) == '*')
            throw new Exception();
        if (expression.charAt(0) == '-' || expression.charAt(0) == '+')
            expression = "0" + expression;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == ')')
                brNum--;
            if (expression.charAt(i) == '(')
                brNum++;
            if (brNum < 0)
                throw new Exception();
        }
        if (brNum != 0)
            throw new Exception();
        if (Character.isDigit(expression.charAt(expression.length() - 1)) == false
                && expression.charAt(expression.length() - 1) != '.'
                && expression.charAt(expression.length() - 1) != ')')
            throw new Exception();
    }

    Expr MulDiv() throws Exception {
        Expr var = brackets();
        while (operator.equals("*") || operator.equals("/")) {
            String obr = operator;
            boolean negative = false;
            next();
            while (operator.equals("+") || operator.equals("-")) {
                if (operator.equals("-"))
                    negative = !negative;
                next();
            }
            if (operator.equals("*") || operator.equals("/") || operator.equals(")"))
                throw new Exception();
            if (negative == true) {
                operator = "-" + operator;
                answer = new Const(new BigDecimal(operator));
            }
            if (obr.equals("*")) {
                var = new operatorMul(var, brackets());
            }
            if (obr.equals("/")) {
                var = new Division(var, brackets());
            }
        }
        return var;
    }


    Expr AddDiv() throws Exception {
        Expr var = MulDiv();
        while (operator.equals("+") || operator.equals("-")) {
            boolean negative = false;
            while (operator.equals("+") || operator.equals("-")) {
                if (operator.equals("-"))
                    negative = !negative;
                next();
            }
            if (operator.equals("*") || operator.equals("/") || operator.equals(")"))
                throw new Exception();
            if (negative == false) {
                var = new operatorAdd(var, MulDiv());
            }
            if (negative == true) {
                var = new operatorSub(var, MulDiv());
            }
        }
        return var;

    }

    Expr brackets() throws Exception {
        Expr var;
        if (operator.equals("(")) {
            next();
            boolean negative = false;
            while (operator.equals("+") || operator.equals("-")) {
                if (operator.equals("-"))
                    negative = !negative;
                next();
            }
            if (operator.equals("*") || operator.equals("/"))
                throw new Exception();
            if (operator.equals(")"))
                throw new Exception();

            if (Character.isDigit(operator.charAt(0)) && negative == true)
                answer = new Const(new BigDecimal("-" + operator));
            if (Character.isDigit(operator.charAt(0)) && negative == false)
                answer = new Const(new BigDecimal("+" + operator));

            if (negative == true && operator.equals("("))
                var = new operatorSub(new Const(new BigDecimal("0")), AddDiv());
            else
                var = AddDiv();
            if (operator.equals(")")) {
                next();
                if (operator.equals("(") || Character.isDigit(operator.charAt(0)))
                    var = new operatorMul(var, AddDiv());
            }
        } else {
            var = answer;
            String prev = operator;
            next();
            if (Character.isDigit(prev.charAt(0)) && operator.equals("("))
                var = new operatorMul(var, AddDiv());

        }
        return var;
    }

    void next() throws Exception {
        operator = "";
        boolean morePoints = false;
        if (pos == expression.length()) {
            operator = "!";
            return;
        }
        if (expression.charAt(pos) == '(') operator = "(";
        if (expression.charAt(pos) == ')') operator = ")";
        if (expression.charAt(pos) == '+') operator = "+";
        if (expression.charAt(pos) == '-') operator = "-";
        if (expression.charAt(pos) == '*') operator = "*";
        if (expression.charAt(pos) == '/') operator = "/";
        if (operator.length() > 0) {
            pos++;
            return;
        }
        while (Character.isDigit(expression.charAt(pos)) || expression.charAt(pos) == '.') {
            if (expression.charAt(pos) == '.') {
                if (morePoints == true) {
                    throw new Exception();
                }
                morePoints = true;
            }
            operator += expression.charAt(pos);
            pos++;
            if (pos == expression.length()) break;
        }
        if (morePoints == true && operator.length() == 1)
            throw new Exception();
        if (operator.charAt(operator.length() - 1) == '.')
            operator = operator + "0";
        if (operator.charAt(0) == '.')
            operator = "0" + operator;
        answer = new Const(new BigDecimal(operator));
    }

    @Override
    public double calculate(String expression) throws CalculationException {
        String a;
        try {
            a = Parser(expression).eval().toString();
        } catch (Exception e) {
            throw new CalculationException();
        }
        return Double.parseDouble(a);
    }
}
