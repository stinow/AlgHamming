package alghamming;

public class Receptor {
    
    public Receptor(int[] arrayMsgRecebida){
        confereParidade(inverteArray(arrayMsgRecebida));
    }
    
    public int[] inverteArray(int[] msgArray){
        int[] arrayInvertido = new int[msgArray.length];
        
        int j = 0;
        
        for(int i = msgArray.length-1; i >= 0; i--){
            arrayInvertido[j] = msgArray[i];
            j++;
        }
        
        return arrayInvertido;
    } //fim inverteArray
    
    public void confereParidade(int[] msgArray){
        
        for(int i = 0; i < msgArray.length; i++){
            msgArray[((int) Math.pow(2, i)) - 1] = 9;
            
        }
    }
    
}
