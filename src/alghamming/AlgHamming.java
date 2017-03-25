package alghamming;

public class AlgHamming {

    public static void main(String[] args) {
        System.out.print("Insira a mensagem binária: ");
        String msg = new java.util.Scanner(System.in).nextLine();
        
        Emissor ems = new Emissor(msg);
        ems.printaMsg();
        
        
        
    }
    
}
