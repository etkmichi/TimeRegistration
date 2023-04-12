package MFLibrary.MFSafety;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import MFNetwork.MFData.MFObjects.MFObject;

public class MFSafetyCodings extends MFObject {
    public static byte[] getSha256Hash(byte[] data){
        byte[] values;
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            values=digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            p_debugStaticPrint(e.getMessage());
            values=new byte[1];
            values[0]=0;
        }

        p_debugStaticPrint("MFCreatePasswordDialog - getHash(String data) Create hash: \ndata="+data+"\nhash="+values);
        return values;
    }

    /**
     * Opens the given pwFile and checks if it contains the given hash. If file doesn't exit, it will return false.
     * @param pwFile
     * @param hash
     * @return true if file contains hash. false if file is null, cant be read, doesn't exist or doesn't contain the hash.
     */
    public static boolean checkFileForHash(File pwFile,byte[] hash){
        if(pwFile==null){
            return false;
        }

        if(!pwFile.canRead()){
            return false;
        }

        if(!pwFile.exists()){
            return false;
        }

        byte[] readBuffer=new byte[hash.length];
        //TODO ordentlich machen!!!
        try {
            FileInputStream fileStream=new FileInputStream(pwFile);
            int read;
            int offset=0;
            while(true){
                read=fileStream.read();
                if(read==-1)
                    return false;
                readBuffer[offset]=(byte)read;

                if(readBuffer[offset]==hash[offset]){
                    offset++;
                    while(offset<readBuffer.length){
                        read=fileStream.read();
                        if(read<0){
                            return false;
                        }
                        readBuffer[offset]=(byte)read;
                        if(readBuffer[offset]!=hash[offset]) {/*It must be another value!*/
                            if(readBuffer[offset]==hash[0])/*It could be the start value!*/
                                offset=1;
                            break;
                        }
                        if(offset==hash.length-1){
                            return true;
                        }
                        offset++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a password file and stores the sha256 hash in it.
     * @param filePath
     * @param fileName
     * @param password
     * @return
     */
    public static boolean createPasswordFile(String filePath,String fileName,String password){
        File pFile=new File(filePath,fileName);
        File dir=new File(filePath);
        boolean dirCreated=dir.mkdirs()||dir.exists();

        if(!dirCreated){
            p_debugStaticPrint("Couldn't create directory!");
            return false;
        }

        if(!pFile.exists()){
            try {
                pFile.createNewFile();
                FileOutputStream out = new FileOutputStream(pFile,true);
                int count=0;
                byte[] hash=MFSafetyCodings.getSha256Hash(password.getBytes());
                out.write(hash);
            } catch (Exception e) {
                p_debugStaticPrint("MFSafetyPWDialog - writeHashToPWFile(String hash,String title): "+e.getMessage());
                return false;
            }
        }

        return true;
    }



    public static boolean writeHashToFile(File pwFile,String password){
        boolean dirCreated=pwFile.mkdirs();
        if(!dirCreated){
            p_debugStaticPrint("MFSafetyCodings writeHashToFile(File pwFile,String password) - Couldn't create directory!");
            return false;
        }

        if(!pwFile.exists()){
            try {
                pwFile.createNewFile();
                FileOutputStream out =new FileOutputStream(pwFile,true);
                out.write(getSha256Hash(password.getBytes()));
            } catch (Exception e) {
                p_debugStaticPrint("MFSafetyCodings - writeHashToPWFile(String hash,String title): "+e.getMessage());
                return false;
            }
        }
        return true;
    }
}
