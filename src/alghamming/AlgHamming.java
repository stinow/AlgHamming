package alghamming;

import java.util.Random;

public class AlgHamming {

    public static void main(String[] args) {
        System.out.print("Insira a mensagem binária: ");
        String msg = new java.util.Scanner(System.in).nextLine();
        
        Emissor ems = new Emissor(msg);
        ems.printaMsg();
        
        for(int i : geraErro(ems.getMensagem())){
            System.out.print(i);
        }
        
    }
    
    private static int[] geraErro(int[] msgArray){
        Random r = new Random();
        int i = r.nextInt(msgArray.length + 1);
        System.out.println("RANDOM: " + i);
        
        if(i == msgArray.length){
            System.out.println("SEM ERRO");
            return msgArray;
        }else{
            if(msgArray[i] == 1){
                msgArray[i] = 0;
            }else{
                msgArray[i] = 1;
            }
            return msgArray;
        }
    }   
            
}
