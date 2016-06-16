package com.github.sa.core.accesstoken;

import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过DES算法对访问令牌进行加解密
 * 
 * @author wh
 * @lastModified 2016-2-3 16:27:32
 */
public class DesAccessTokenGenerator implements AccessTokenGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(DesAccessTokenGenerator.class);

    private static final String SEPARATOR = ":";
    
    private String desKeySpec;

    public void setDesKeySpec(String desKeySpec) {
		this.desKeySpec = desKeySpec;
	}

	/**
     * Token格式: 'accountId:时间戳', 通过DES算法加密, 再编码为BASE64
     */
    @Override
    public String encode(String accountId) {
        try {
            Long randomStr = new Date().getTime();
            Cipher encryptCipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sk = keyFactory.generateSecret(new DESKeySpec(desKeySpec.getBytes()));
            encryptCipher.init(Cipher.ENCRYPT_MODE, sk);
            return new String(Base64.encodeBase64(encryptCipher.doFinal((accountId + SEPARATOR + randomStr).getBytes())));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

    @Override
    public String decode(String accessToken) {
        try {
            Cipher decryptCipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sk = keyFactory.generateSecret(new DESKeySpec(desKeySpec.getBytes()));
            decryptCipher.init(Cipher.DECRYPT_MODE, sk);
            String decryptStr = new String(decryptCipher.doFinal(Base64.decodeBase64(accessToken.getBytes())));
            return decryptStr.split(SEPARATOR)[0];
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }

}

