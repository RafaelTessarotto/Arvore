// Nó que armazena um operador (+, -, *, /)
class OperatorNode extends Node{
  private Node left, right;
  private char operator;

  public OperatorNode(char operator, Node left, Node right) {
      this.operator = operator;
      this.left = left;
      this.right = right;
  }

  public Node getLeft(){
    return left;
  }

  public Node getRight(){
    return right;
  }

  @Override
  public float visit(){
    switch (this.operator){
      case '+':
        return left.visit() + right.visit();
      case '-':
        return left.visit() - right.visit();
      case '*':
        return left.visit() * right.visit();
      case '/':
        if (right.visit() == 0) {
          throw new ArithmeticException("Divisão por zero: Você explodiu o universo! X(");
        }
        return left.visit() / right.visit();
      default:
        return Float.NaN; // Caso de erro
    }
  }

  @Override
  public String toString() {
    return String.valueOf(operator); // Retorna o operador como string
  }
}
