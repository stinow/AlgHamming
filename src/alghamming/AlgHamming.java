package alghamming;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class AlgHamming {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Insira a mensagem: ");
        String mensagem = input.nextLine();
        mensagem = new StringBuilder(mensagem).reverse().toString();
        char[] tempArrayMsg = mensagem.toCharArray();
        //converte a mensagem em array de char para manipulá-la mais facilmente 
        int[] arrayMsg = new int[tempArrayMsg.length];

        //preenche o array novo nas posições corretas deixando em branco 
        //os espaços dos bits de paridade
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
        
        //calcula a quantidade de bits de paridade necessários
        int P = calcRedundancia(arrayMsg);
        //adiciona os bits de paridade nas posições 2^R
        int[] arrayMsgR = adicionaParidade(arrayMsg, P);
        System.out.print("Mensagem codificada: ");
        for(int i = 0; i < arrayMsgR.length; i++) {
            System.out.print(arrayMsgR[arrayMsgR.length-i-1]);
        }
        System.out.println();
        
        System.out.print("\nEnviando mensagem");
        int ct = 4;
        do{
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                if(ct != 1)
                    System.out.print(".");
                ct--;
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }while(ct > 0);
        System.out.println("");
        
        //inverte o valor de alguma posição aleatoriamente escolhida
        //pode também não conter erros
        int[] arrayMsgEnv = geraErro(arrayMsgR);
        System.out.print("\nMensagem enviada: ");
        for(int i = 0; i < arrayMsgEnv.length; i++) {
            System.out.print(arrayMsgEnv[arrayMsgEnv.length-i-1]);
        }
        System.out.println();
        
        //recepção da mensagem
        receptor(arrayMsgEnv, P);
    }//fim main()
    
    private static int calcRedundancia(int[] msgArray){
        int redund = 0;
        
        //de acordo com a fórmula  2^R >= m + R + 1
        while (Math.pow(2, redund) < (msgArray.length + redund + 1)){
            redund++;
        }
        
        System.out.println("\nBits de paridade: " + redund);
        
        return redund;
    } //fim calcRedundancia()
    
    private static int [] adicionaParidade(int[] msgArray, int P){
        int arrayMsgR[] = new int[msgArray.length + P];
        int j = 0;
        
        //adiciona 9 em todas as posições 2^R
        for(int i = 0; i < P; i++){
            arrayMsgR[((int) Math.pow(2, i))-1] = 9;
        }
        
        //no array final da mensagem (arrayMsgR) coloca os valores de input
        for(int i = 0; i < arrayMsgR.length; i++){
            if(!(arrayMsgR[i] == 9)){
                arrayMsgR[i] = msgArray[j];
                j++;
            }
        }
        
        //chama a função que realiza o cálculo do bit de paridade
        //em todas as posições 2^R existentes
        for(int i = 0; i < P; i++){
            arrayMsgR[((int) Math.pow(2, i)) - 1] = getParidade(arrayMsgR, i);
        }
        
        return arrayMsgR;
        
    } //fim adicionaParidade()
    
    private static int getParidade(int[] arrayMsgR, int P){
        int paridade = 0;
        
        for(int i = 0 ; i < arrayMsgR.length ; i++) {
            if(arrayMsgR[i] != 9) {
                //se na posição i não é um valor não setado
                //incrementa essa posição em 1 e converte pra binário
                int k = i + 1;
                String s = Integer.toBinaryString(k);
                
                //se o bit na posição 2^R é 1, confere o valor nessa posição
                //e calcula a paridade
                int x = ((Integer.parseInt(s))/((int) Math.pow(10, P)))%10;
                if(x == 1) {
                    if(arrayMsgR[i] == 1) {
                        //aqui sai 1 ou 0
                        paridade = (paridade+1)%2;
                    }
                }
            }
        }
        
        return paridade;
    }//fim getParidade()
    
    //método responsável por escolher uma posição aleatória para inverter
    //o valor, gerando assim o erro
    private static int[] geraErro(int[] msgArray){
        Random r = new Random();
        int i = r.nextInt(msgArray.length + 1);
        
        if(i == msgArray.length){
            return msgArray;
        }else{
            if(msgArray[i] == 1){
                msgArray[i] = 0;
            }else{
                msgArray[i] = 1;
            }
            return msgArray;
        }
    }//fim geraErro()
    
    private static void receptor(int[] arrayMsg, int P){

        //Variável para armazenar o valores corretos para checar a paridade
        int checar_P;
        
        //Array para armazenar os valores das paridades checadas
        int paridades[] = new int[P];

        //Sindrome é utilizada para armazenar o valor do local do erro
        String sindrome = new String();

        for(checar_P=0 ; checar_P < P ; checar_P++) {

            for(int i=0 ; i < arrayMsg.length ; i++) {
                
                int k = i+1;
                String s = Integer.toBinaryString(k);
                int bit = ((Integer.parseInt(s))/((int) Math.pow(10, checar_P)))%10;
                if(bit == 1) {
                    if(arrayMsg[i] == 1) {
                        paridades[checar_P] = (paridades[checar_P]+1)%2;
                    }
                }
            }
            sindrome = paridades[checar_P] + sindrome;
        }
        
        int error_location = Integer.parseInt(sindrome, 2);
        if(error_location != 0) {
            System.out.print("\nErro na posição: " + error_location);
            arrayMsg[error_location-1] = (arrayMsg[error_location-1]+1)%2;
            System.out.print("\nMensagem corrigida: ");
            for(int i=0 ; i < arrayMsg.length ; i++) {
                System.out.print(arrayMsg[arrayMsg.length-i-1]);
            }
            System.out.println();
        }
        else {
            System.out.println("Mensagem recebida sem erros!");
        }
        
        System.out.print("Mensagem original: ");
        checar_P = P-1;
        for(int i=arrayMsg.length ; i > 0 ; i--) {
            if(Math.pow(2, checar_P) != i) {
                System.out.print(arrayMsg[i-1]);
            }
            else {
                checar_P--;
            }
        }
        System.out.println();

    }//fim receptor()
}
