package com.github.sa.core.accesstoken;

/**
 * <p>根据账户ID加密的方式计算出访问令牌；根据访问令牌解密出账户ID
 * 
 * @author wh
 * @lastModified 2016-6-7 15:49:19
 */
public interface AccessTokenGenerator {

	/**
	 * 对accountId进行加密, 生成访问令牌
	 * 
	 * @param accountId
	 * @return
	 */
    String encode(String accountId);
    
    /**
     * 解密出userId
     * 
     * @param encryptStr
     * @return
     */
    String decode(String accessToken);
    
}
