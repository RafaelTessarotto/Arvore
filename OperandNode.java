// Nó que armazena um operando (número)
class OperandNode extends Node{
  private float value;

  public OperandNode(float value){
    this.value = value;
  }

  @Override
  public float visit(){
    return this.value; // Retorna o valor do operando
  }
  
  @Override
  public String toString() {
    return String.valueOf(value); // Retorna valor numerico como string
  }
}