import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static ExpressionTree tree;
    private static String expressao;

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        System.out.println("Bem-vindo à nossa calculadora!");

        // Variável para armazenar opção
        int opcao = 0;

        // Exibir menu
        while (opcao != 5) {
            // Exibir menu
            opcao = exibirMenu(leitor);

            // Verificação de opção válida
            while (opcao < 1 || opcao > 5) {
                 System.out.println("Digite uma opção válida");
                opcao = exibirMenu(leitor);
            }

            switch (opcao) {
                case 1:
                    System.out.println("Digite a expressão aritmética (notação infixa): ");
                    leitor.nextLine(); // Limpa buffer
                    expressao = leitor.nextLine();

                    String erro = validarExpressao(expressao);
                    if (erro != null) {
                        System.out.println("Erro: " + erro);
                    } else {
                        System.out.println("Expressão válida.");
                    }
                    break;
                case 2:
                    if (expressao != null && !expressao.isEmpty()) {
                        tree = criarArvore(expressao);
                        System.out.println("Árvore criada com sucesso!");
                    } else {
                        System.out.println("Forneça a expressão primeiro (Opção 1)");
                    }
                    break;
                case 3:
                    if (tree != null) {
                        System.out.println("Exibindo a árvore em ordem:");
                        tree.inOrder(tree.getRoot());
                        System.out.println();
                        System.out.println("Exibindo a árvore pré-ordem:");
                        tree.preOrder(tree.getRoot());
                        System.out.println();
                        System.out.println("Exibindo a árvore pós-ordem:");
                        tree.postOrder(tree.getRoot());
                        System.out.println();
                    } else {
                        System.out.println("Árvore não foi criada.");
                    }
                    break;
                case 4:
                    if (tree != null) {
                        float resultado = tree.evaluate();
                        System.out.println("Resultado da expressão: " + resultado);
                    } else {
                        System.out.println("Árvore não foi criada.");
                    }
                    break;
                case 5:
                    System.out.println("Obrigado por usar nossa calculadora.");
                    leitor.close();
                    break;
            }
        }
    }

    // Função para exibir o menu e receber opção
    public static int exibirMenu(Scanner leitor) {
        System.out.println("Escolha uma das opções digitando o número da opção:");
        System.out.println("1. Entrada da expressão aritmética na notação infixa.");
        System.out.println("2. Criação da árvore binária de expressão aritmética.");
        System.out.println("3. Exibição da árvore binária de expressão aritmética.");
        System.out.println("4. Cálculo da expressão (realizando o percurso da árvore).");
        System.out.println("5. Encerramento do programa.");
        System.out.print("Sua opção: ");
        return leitor.nextInt();
    }

    // Função de validação da expressão
    public static String validarExpressao(String expressao) {
        // Remover espaços da expressão para facilitar a validação
        expressao = expressao.replaceAll("\\s", "");

        // Verifica se a expressão contém caracteres inválidos
        if (!expressao.matches("[0-9()+\\-*/.]+")) {
            return "A expressão contém operadores inválidos.";
        }

        // Verificação de balanceamento de parênteses
        int parenCount = 0;
        for (char c : expressao.toCharArray()) {
            if (c == '(') parenCount++;
            if (c == ')') parenCount--;
            if (parenCount < 0) return "Há mais parênteses de fechamento do que de abertura.";
        }
        if (parenCount != 0) {
            return "Parênteses não estão balanceados.";
        }

        // Verificação de operadores binários sem operadores suficientes
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            // Se for um operador, deve haver algo antes e depois
            if ("+-*/".indexOf(c) >= 0) {
                if (i == 0 || i == expressao.length() - 1) {
                    return "O operador " + c + " é binário, mas falta um operando.";
                }

                char antes = expressao.charAt(i - 1);
                char depois = expressao.charAt(i + 1);

                // Operador não pode estar precedido ou seguido de outro operador
                if ("+-*/".indexOf(antes) >= 0 || "+-*/".indexOf(depois) >= 0) {
                    return "O operador " + c + " é binário, mas falta um operando.";
                }
            }
        }

        // Se passou por todas as verificações, a expressão é válida
        return null;
    }

    // Método para criar a árvore de expressão
    public static ExpressionTree criarArvore(String expressao) {
        Stack<Node> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            // Se for um número, lê o número completo
            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expressao.length() && (Character.isDigit(expressao.charAt(i)) || expressao.charAt(i) == '.')) {
                    num.append(expressao.charAt(i));
                    i++;
                }
                i--; // Decrementa para ajustar o índice
                operandStack.push(new OperandNode(Float.parseFloat(num.toString())));
            }
            // Se for um operador
            else if ("+-*/".indexOf(c) >= 0) {
                while (!operatorStack.isEmpty() && prioridade(c) <= prioridade(operatorStack.peek())) {
                    char operador = operatorStack.pop();
                    Node right = operandStack.pop();
                    Node left = operandStack.pop();
                    operandStack.push(new OperatorNode(operador, left, right));
                }
                operatorStack.push(c);
            }
            // Se for parênteses
            else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (operatorStack.peek() != '(') {
                    char operador = operatorStack.pop();
                    Node right = operandStack.pop();
                    Node left = operandStack.pop();
                    operandStack.push(new OperatorNode(operador, left, right));
                }
                operatorStack.pop(); // Remove o '('
            }
        }

        while (!operatorStack.isEmpty()) {
            char operador = operatorStack.pop();
            Node right = operandStack.pop();
            Node left = operandStack.pop();
            operandStack.push(new OperatorNode(operador, left, right));
        }

        return new ExpressionTree(operandStack.pop());
    }

    // Método para definir a prioridade dos operadores
    private static int prioridade(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}
