package cn.ambermoe.androidexperiment;

import java.util.Stack;

/**
 * Created by ASUS on 2017-03-04.
 */
//表达式求值 （来自数据结构 page95，添加小数点计算（支持带括号多则运算））
public class Expression {

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 16;
    /**
     * 将中缀表达式转换为后缀表达式
     * @param infix 中缀表达式
     * @return 后缀表达式
     */
    public static StringBuffer toPostfix(String infix) {
        //运算符斩
        Stack<String> stack = new Stack<>();
        //后缀表达式字符串
        StringBuffer postfix = new StringBuffer(infix.length() * 2);
        int i=0;
        while(i < infix.length()) {
            char ch = infix.charAt(i);
            switch (ch) {
                //+ - 运算符，将栈全部取出，除非栈空或运到左括号
                case '+':
                case '-':
                    while(!stack.isEmpty() && !stack.peek().equals("("))
                        postfix.append(stack.pop());
                    stack.push(ch+"");
                    i++;
                    break;
                // * / 时如果栈顶是 * / 则将其取出 把自身入栈
                case '*':
                case '/':
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/")))
                        postfix.append(stack.pop());
                    stack.push(ch+"");
                    i++;
                    break;
                // （ 直接入栈
                case '(':
                    stack.push(ch+"");
                    i++;
                    break;
                // 右括号 则直接pop() 直到遇到左括号
                case ')':
                    String out = stack.pop();
                    while(out != null && !out.equals("(")) {
                        postfix.append(out);
                        out = stack.pop();
                    }
                    i++;
                    break;
                //数字直接添加至后缀表达式infix
                default:
                    while (i<infix.length() && (ch >='0' && ch <= '9' || ch =='.')) {
                        postfix.append(ch);
                        i++;
                        if(i < infix.length()) {
                            ch = infix.charAt(i);
                        }
                    }
                    postfix.append(" ");
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix;
    }
    //后缀表达式 求值
    public static double toValue(StringBuffer postfix) {
        Stack<Double> stack = new Stack<>();
        double value = 0;
        for(int i=0;i<postfix.length();i++) {
            char ch = postfix.charAt(i);
            if((ch >= '0' && ch <= '9') || ch == '.') {
                String temp = "";
                while(ch !=' '){
                    temp += ch+"";
                    ch = postfix.charAt(++i);
                }
                stack.push(Double.parseDouble(temp));
            }
            else {
                if(ch != ' ') {

                    double y = stack.pop(),x;
                    //解决初始为负数时 计算出错问题
                    if(stack.isEmpty())
                        x = 0;
                    else
                        x = stack.pop();
                    switch(ch) {
                        //计算全部使用BigDecimal
                        //防止出现精度缺失 例如 0.2*0.2 != 0.04
                        case '+':
                            value = add(x,y);
                            break;
                        case '-':
                            value = sub(x,y);
                            break;
                        case '*':
                            value = mul(x,y);
                            break;
                        case '/':
                            value = div(x,y);
                            break;
                    }
                    stack.push(value);
                }
            }
        }
        //除不尽时保留6位小数
        return round(stack.pop(),6);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        return b.divide(one, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
