// Classe para a árvore binária
public class ExpressionTree{
  private Node root;

  public ExpressionTree(Node root){
    this.root = root;
  }

  // Pré-ordem (Root -> Left -> Right)
  public void preOrder(Node node){
    if (node == null) return;
    System.out.print(node.toString() + " ");
    if (node instanceof OperatorNode){
      preOrder(((OperatorNode) node).getLeft());
      preOrder(((OperatorNode) node).getRight());
    }
  }

  // Em ordem (Left-> Root -> Right)
  public void inOrder(Node node){
    if (node == null) return;
    if (node instanceof OperatorNode){
      inOrder(((OperatorNode) node).getLeft());
      System.out.print(node.toString() + " ");
      inOrder(((OperatorNode) node).getRight());
    } else{
      // Se for um nó operando (número), simplesmente exibe o valor
      System.out.print(node.toString() + " ");
    }
  }

  // Pós-ordem (Left -> Right -> Root)
  public void postOrder(Node node){
    if (node == null) return;
    if (node instanceof OperatorNode){
      postOrder(((OperatorNode) node).getLeft());
      postOrder(((OperatorNode) node).getRight());
    }
    System.out.print(node.toString() + " ");
  }

  // Avalia a expressão
  public float evaluate(){
    if (root != null){
      return root.visit();
    } else{
      return Float.NaN;
    }
  }

  // Getter para o nó raiz
  public Node getRoot(){
    return this.root;
  }
}