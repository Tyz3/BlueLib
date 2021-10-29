package ru.kronos.bluelib.license;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class CryptoEngine {

	private static final String ALGRTM = "RSA";
	
//	private static final byte[] PUBLIC_KEY = new byte[] {48,-126,1,34,48,13,6,9,42,-122,72,-122,-9,13,1,1,1,5,0,3,-126,1,15,0,48,-126,1,10,2,-126,1,1,0,-127,-88,-60,-33,10,35,26,47,94,-83,0,56,65,72,-10,-18,-26,-112,-59,49,92,9,94,52,36,-41,47,-122,24,37,-51,0,100,-76,21,112,-79,-25,104,120,-64,75,-104,-124,67,10,10,50,56,90,12,88,-90,49,97,15,-51,-14,-117,-15,3,-45,27,61,-122,25,-109,47,-64,124,102,-90,-97,-11,44,41,-68,-78,-82,-79,-102,20,-91,118,99,-46,88,-19,-113,-44,-95,-69,-54,-100,108,-108,-85,56,-69,105,19,-128,-67,-6,126,-25,-100,1,57,-9,118,103,7,-95,15,102,92,-70,-18,-96,-104,24,-61,47,-90,-5,47,-116,-122,75,42,24,-76,56,-96,9,62,40,75,-102,103,2,31,-62,13,13,-116,-16,-86,82,108,2,28,-57,-61,-108,27,-84,124,-18,110,3,54,-109,117,67,-67,55,-112,91,-108,-5,-16,-34,123,74,71,-75,78,94,0,82,94,61,100,-10,-7,-86,-76,-73,30,75,-60,117,84,-66,78,71,-101,17,27,23,73,111,55,40,2,-32,25,58,-49,-7,-52,-89,61,126,-81,-47,19,15,115,113,-119,-23,125,98,43,-50,121,68,102,97,77,-80,20,-46,-120,-4,-27,16,-72,-26,-54,122,63,118,31,100,-85,39,5,26,24,-77,-38,-87,2,3,1,0,1};
	private static final String PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a028201010081a8c4df0a231a2f5ead00384148f6eee690c5315c095e3424d72f861825cd0064b41570b1e76878c04b9884430a0a32385a0c58a631610fcdf28bf103d31b3d8619932fc07c66a69ff52c29bcb2aeb19a14a57663d258ed8fd4a1bbca9c6c94ab38bb691380bdfa7ee79c0139f7766707a10f665cbaeea09818c32fa6fb2f8c864b2a18b438a0093e284b9a67021fc20d0d8cf0aa526c021cc7c3941bac7cee6e0336937543bd37905b94fbf0de7b4a47b54e5e00525e3d64f6f9aab4b71e4bc47554be4e479b111b17496f372802e0193acff9cca73d7eafd1130f737189e97d622bce794466614db014d288fce510b8e6ca7a3f761f64ab27051a18b3daa90203010001";
	private static PublicKey pubk;
	
	private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (pubk != null) return pubk;
		KeyFactory keyFactory = KeyFactory.getInstance(ALGRTM);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(CryptoUtil.hex2Byte(PUBLIC_KEY)); // CryptoUtil.hex2Byte(PUBLIC_KEY)
		
        return (pubk = keyFactory.generatePublic(publicKeySpec));
    }
	
	public static String decrypt(byte[] encryptedLicense) {
        byte[] dectyptedText = null;

        try {
            Cipher cipher = Cipher.getInstance(ALGRTM);
            cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
            dectyptedText = cipher.doFinal(encryptedLicense);
 
        } catch (Exception e) {
            LogEngine.debugMsg(LoggingLevel.ERROR, CryptoEngine.class.getSimpleName(), " | Ошибка при декодировании лицензии.");
            e.printStackTrace();
        }

        assert dectyptedText != null;
        return new String(dectyptedText);
    }
}
