import java.math.BigDecimal;
import java.util.Objects;
import java.util.Stack;

public class Calculator {

	private Stack<Node> undoStack;

	private Stack<Node> redoStack;

	private Node node;

	public Calculator() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

	private static class Node {

		private BigDecimal operand;

		private BigDecimal operated;

		private char operator;

		private BigDecimal result;

		private boolean isUndo;

		public Node() {
			this.operand = BigDecimal.ZERO;
			this.operated = BigDecimal.ZERO;
			this.result = BigDecimal.ZERO;
			this.isUndo = false;
		}

	}

	public void addOperand(double operand) {
		addOperand(BigDecimal.valueOf(operand));
	}

	public void addOperand(long operand) {
		addOperand(BigDecimal.valueOf(operand));
	}

	public void addOperand(BigDecimal operand) {
		checkNode();
		if (node.operator == '\0' || node.isUndo) {
			node.operand = operand;
		} else {
			node.operated = operand;
		}
		if (node.isUndo) {
			undoStack.clear();
		}
	}

	public void add() {
		addOperator('+');
	}

	public void subtract() {
		addOperator('-');
	}

	public void multiply() {
		addOperator('*');
	}

	public void divide() {
		addOperator('/');
	}

	private void addOperator(char operator) {
		checkNode();
		if (node.operator != '\0' && !node.isUndo) {
			equal();
		}
		node.operator = operator;
		node.isUndo = false;
	}

	private void checkNode() {
		if (Objects.isNull(node)) {
			node = new Node();
		}
	}

	private void equal() {
		if (Objects.isNull(node)) {
			System.out.println("结果为: 0");
			return;
		}
		BigDecimal bigDecimal = execute(node.operand, node.operated, node.operator);
		node.result = bigDecimal;
		node.isUndo = false;
		undoStack.push(node);
		redoStack.clear();
		System.out.println(node.operand + " " + node.operator + " " + node.operated + ", 结果为: " + node.result);
		node = null;
		addOperand(bigDecimal);
	}

	private BigDecimal execute(BigDecimal operand, BigDecimal operated, char operator) {
		BigDecimal bigDecimal = BigDecimal.ZERO;
		switch (operator) {
			case '+':
				bigDecimal = operand.add(operated);
				break;
			case '-':
				bigDecimal = operand.subtract(operated);
				break;
			case '*':
				bigDecimal = operand.multiply(operated);
				break;
			case '/':
				bigDecimal = operand.divide(operated);
				break;
			default:
				bigDecimal = operand;
				break;
		}
		return bigDecimal;
	}

	public void undo() {
		if (undoStack.empty()) {
			System.out.println("已undo到起点");
			return;
		}
		node = undoStack.pop();
		redoStack.push(node);
		node.isUndo = true;
		System.out.println("undo操作: " + node.operator + " " + node.operated + ", 结果为: " + node.operand);
	}

	public void redo() {
		if (redoStack.empty()) {
			System.out.println("已redo到终点");
			return;
		}
		node = redoStack.pop();
		node.isUndo = false;
		undoStack.push(node);
		System.out.println("redo操作: " + node.operator + " " + node.operated + ", 结果为: " + node.result);
	}

	public static void main(String[] args) {
		Calculator calculator = new Calculator();
		calculator.addOperand(2);
		calculator.multiply();
		calculator.addOperand(6);
		calculator.add();
		calculator.equal();
		calculator.addOperand(2);
		calculator.subtract();
		calculator.addOperand(1);
		calculator.equal();
		calculator.undo();
		
		calculator.add();
		calculator.addOperand(2);
		calculator.equal();
		
		calculator.redo();
		calculator.undo();
		calculator.undo();
		calculator.undo();
		calculator.undo();
		calculator.redo();
		calculator.redo();
	}

}
