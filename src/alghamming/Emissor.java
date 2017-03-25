package alghamming;

public class Emissor {
    
    private char[] tempArray;
    private int[] msgArray, arrayMsgR; //Mensagem binária dentro de um vetor
    private int R; //Qtnd de bits de redundância

    public Emissor(String msg){
        tempArray = msg.toCharArray();

        transformaInt(tempArray);
        
        calcRedundancia(msgArray);
        
        adicionaParidade(inverteArray(msgArray), R);
    }
    
    public void transformaInt(char[] tempArray){
        int j = 0;
        
        msgArray = new int[tempArray.length];
        
        for(int i = 0; i < tempArray.length; i++){
            if(tempArray[i] == '1'){
                msgArray[j] = 1;
                j++; 
            }else if(tempArray[i] == '0'){
                msgArray[j] = 0;
                j++;
            }
        }
        
    }
    
    public int calcRedundancia(int[] msgArray){

        int redund = 0, result = 1;
        double pot = 0;
        
        while(pot < result){
            result = msgArray.length + redund + 1;
            pot = Math.pow(2, redund);
            if(pot >= result){
                R = redund;
            }
            redund++;
        }
        
        System.out.println("Bits de paridade: " + R);
        
        return R;
    } //fim calcRedundancia()
    
    public int[] inverteArray(int[] msgArray){
        int[] arrayInvertido = new int[msgArray.length];
        
        int j = 0;
        
        for(int i = msgArray.length-1; i >= 0; i--){
            arrayInvertido[j] = msgArray[i];
            j++;
        }
        
        return arrayInvertido;
    } //fim inverteArray
    
    public void adicionaParidade(int[] msgArray, int qtParidade){
        arrayMsgR = new int[msgArray.length + qtParidade]; 
        int j = 0;
        
        for(int i = 0; i < R; i++){
            arrayMsgR[((int) Math.pow(2, i))-1] = 9;
        }
        
        for(int i = 0; i < arrayMsgR.length; i++){
            if(!(arrayMsgR[i] == 9)){
                arrayMsgR[i] = msgArray[j];
                j++;
            }
        }
        
        for(int i = 0; i < R; i++){
            arrayMsgR[((int) Math.pow(2, i)) - 1] = getParidade(arrayMsgR, i);
        }
        
    } //fim adicionaParidade()
    
    public int getParidade(int[] arrayMsgR, int pos){
        int paridade = 0;
        
        for(int i = 0 ; i < arrayMsgR.length ; i++) {
            if(arrayMsgR[i] != 9) {
                int k = i + 1;
                String s = Integer.toBinaryString(k);

                int x = ((Integer.parseInt(s))/((int) Math.pow(10, pos)))%10;
                if(x == 1) {
                    if(arrayMsgR[i] == 1) {
                        paridade = (paridade+1)%2;
                    }
                }
            }
        }
        
        return paridade;
    }
    
    public void printaMsg(){
        System.out.print("Mensagem enviada: ");
        for(int val : inverteArray(arrayMsgR)){
            System.out.print(val + "");
        }
        System.out.println("\n");
    }
    
    public int[] getMensagem(){
        return inverteArray(arrayMsgR);
    }
}
