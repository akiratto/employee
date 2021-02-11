package realm.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author owner
 */
public class Password {
    private SecureRandom random;
    private static final String CHARSET = "UTF-8";
    private static final String ENCRYPTION_ALGORITHM = "SHA-512";
    private BASE64Decoder decoder = new BASE64Decoder();
    private BASE64Encoder encoder = new BASE64Encoder();
    
    public byte[] getSalt(int length)
    {
        random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
    
    public byte[] hashWithSalt(String password, byte[] salt)
    {
        byte[] hash = null;
        try {
            byte[] bytesOfMessage = password.getBytes(CHARSET);
            MessageDigest messageDigest;
            messageDigest = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            messageDigest.reset();
            messageDigest.update(salt);
            messageDigest.update(bytesOfMessage);
            hash = messageDigest.digest();
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, "Encoding Problem", ex);
        }
        return hash;
    }
    
    public byte[] hashWithSlowsalt(String password, byte[] salt)
    {
        SecretKeyFactory factory;
        Key key = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt, 1000, 512);
            key = factory.generateSecret(keyspec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return key == null ? null : key.getEncoded();
    }
    
    public String base64FromBytes(byte[] bytes) {
        return encoder.encode(bytes);
    }
    
    public byte[] bytesFromBase64(String text)
    {
        byte[] textBytes = null;
        try {
            textBytes = decoder.decodeBuffer(text);
        } catch (IOException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, "Encoding failed!", ex);
        }
        return textBytes;
    }
}
