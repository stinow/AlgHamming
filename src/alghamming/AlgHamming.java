package alghamming;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlgHamming {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        
        System.out.print("Insira a mensagem: ");
        String mensagem = input.nextLine();
        mensagem = new StringBuilder(mensagem).reverse().toString();
        char[] tempArrayMsg = mensagem.toCharArray();
        int[] arrayMsg = new int[tempArrayMsg.length];
        
        int j = 0;
        for(int i = 0; i < tempArrayMsg.length; i++){
            if(tempArrayMsg[i] == '1'){
                arrayMsg[j] = 1;
                j++;
            }else if(tempArrayMsg[i] == '0'){
                arrayMsg[j] = 0;
                j++;
            }
        }
        
        int P = calcRedundancia(arrayMsg);
        int[] arrayMsgR = adicionaParidade(arrayMsg, P);
        System.out.print("\nMensagem a enviar: ");
        for(int i = 0; i < arrayMsgR.length; i++) {
            System.out.print(arrayMsgR[arrayMsgR.length-i-1]);
        }
        System.out.println();
        
        System.out.print("Enviando mensagem");
        int ct = 4;
        do{
            try {
                TimeUnit.SECONDS.sleep(1);
                if(ct != 1)
                    System.out.print(".");
                ct--;
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }while(ct > 0);
        System.out.println("");
        
        int[] arrayMsgEnv = geraErro(arrayMsgR);
        System.out.print("\n Mensagem enviada: ");
        for(int i = 0; i < arrayMsgEnv.length; i++) {
            System.out.print(arrayMsgEnv[arrayMsgEnv.length-i-1]);
        }
        System.out.println();
        
        receptor(arrayMsgEnv, P);
    }
    
    private static int calcRedundancia(int[] msgArray){
        int redund = 0;
        
        while (Math.pow(2, redund) < (msgArray.length + redund + 1)){
            redund++;
        }
        
        System.out.println("Bits de paridade: " + redund);
        
        return redund;
    } //fim calcRedundancia()
    
    private static int [] adicionaParidade(int[] msgArray, int P){
        int arrayMsgR[] = new int[msgArray.length + P];
        int j = 0;
        
        for(int i = 0; i < P; i++){
            arrayMsgR[((int) Math.pow(2, i))-1] = 9;
        }
        
        for(int i = 0; i < arrayMsgR.length; i++){
            if(!(arrayMsgR[i] == 9)){
                arrayMsgR[i] = msgArray[j];
                j++;
            }
        }
        
        for(int i = 0; i < P; i++){
            arrayMsgR[((int) Math.pow(2, i)) - 1] = getParidade(arrayMsgR, i);
        }
        
        return arrayMsgR;
        
    } //fim adicionaParidade()
    
    private static int getParidade(int[] arrayMsgR, int P){
        int paridade = 0;
        
        for(int i = 0 ; i < arrayMsgR.length ; i++) {
            if(arrayMsgR[i] != 9) {
                int k = i + 1;
                String s = Integer.toBinaryString(k);

                int x = ((Integer.parseInt(s))/((int) Math.pow(10, P)))%10;
                if(x == 1) {
                    if(arrayMsgR[i] == 1) {
                        paridade = (paridade+1)%2;
                    }
                }
            }
        }
        
        return paridade;
    }
    
    private static int[] geraErro(int[] msgArray){
        Random r = new Random();
        int i = r.nextInt(msgArray.length + 1);
        
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
    
    private static void receptor(int[] arrayMsg, int P){
        
        // This is the receiver code. It receives a Hamming code in array 'a'.
        // We also require the number of parity bits added to the original data.
        // Now it must detect the error and correct it, if any.

        int power;
        // We shall use the value stored in 'power' to find the correct bits to check for parity.

        int parity[] = new int[P];
        // 'parity' array will store the values of the parity checks.

        String syndrome = new String();
        // 'syndrome' string will be used to store the integer value of error location.

        for(power=0 ; power < P ; power++) {
        // We need to check the parities, the same no of times as the no of parity bits added.

            for(int i=0 ; i < arrayMsg.length ; i++) {
                // Extracting the bit from 2^(power):

                int k = i+1;
                String s = Integer.toBinaryString(k);
                int bit = ((Integer.parseInt(s))/((int) Math.pow(10, power)))%10;
                if(bit == 1) {
                    if(arrayMsg[i] == 1) {
                        parity[power] = (parity[power]+1)%2;
                    }
                }
            }
            syndrome = parity[power] + syndrome;
        }
        // This gives us the parity check equation values.
        // Using these values, we will now check if there is a single bit error and then correct it.

        int error_location = Integer.parseInt(syndrome, 2);
        if(error_location != 0) {
            System.out.println("Error is at location " + error_location + ".");
            arrayMsg[error_location-1] = (arrayMsg[error_location-1]+1)%2;
            System.out.println("Corrected code is:");
            for(int i=0 ; i < arrayMsg.length ; i++) {
                System.out.print(arrayMsg[arrayMsg.length-i-1]);
            }
            System.out.println();
        }
        else {
            System.out.println("There is no error in the received data.");
        }

        // Finally, we shall extract the original data from the received (and corrected) code:
        System.out.println("Original data sent was:");
        power = P-1;
        for(int i=arrayMsg.length ; i > 0 ; i--) {
            if(Math.pow(2, power) != i) {
                System.out.print(arrayMsg[i-1]);
            }
            else {
                power--;
            }
        }
        System.out.println();

    }
}
