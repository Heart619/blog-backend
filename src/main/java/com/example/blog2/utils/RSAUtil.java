package com.example.blog2.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密
 * 非对称加密，有公钥和私钥之分，公钥用于数据加密，私钥用于数据解密。加密结果可逆
 * 公钥一般提供给外部进行使用，私钥需要放置在服务器端保证安全性。
 * 特点：加密安全性很高，但是加密速度较慢
 *
 * @author mxp
 */
public class RSAUtil {

	private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAogfO/bveLnn0Tb7dnxIVXj25KjTmVFBllP0zqYY3FwNVKQ03iKI7kYd01sbnlRHLVcu74nmk41Ux7mHPQx5nghyktLiyovsbwBkqpWNnfmT9DNq89R6Lw9Mrw/L0pJDc9ACGkksGDyFvQjEt/3+h/phMjEkgHU2kA+F0ZCCgZgVOsKQXwL452+NpLH4rekwfXRJw0GCaZeyMPkNYbItsHvCD1CCPYeCJQw9sfYX/TKeDO2LGZKH5xCi672SqAeu36fpUsw4LXAosTln9Xwpvd2+JT3+GOzKhv05j45YtY96L4/zho0FGn7YSPAd+o3JGMg6B0Cb1unnp6PMKwEBMewIDAQAB";

	private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCiB879u94uefRNvt2fEhVePbkqNOZUUGWU/TOphjcXA1UpDTeIojuRh3TWxueVEctVy7vieaTjVTHuYc9DHmeCHKS0uLKi+xvAGSqlY2d+ZP0M2rz1HovD0yvD8vSkkNz0AIaSSwYPIW9CMS3/f6H+mEyMSSAdTaQD4XRkIKBmBU6wpBfAvjnb42ksfit6TB9dEnDQYJpl7Iw+Q1hsi2we8IPUII9h4IlDD2x9hf9Mp4M7YsZkofnEKLrvZKoB67fp+lSzDgtcCixOWf1fCm93b4lPf4Y7MqG/TmPjli1j3ovj/OGjQUafthI8B36jckYyDoHQJvW6eeno8wrAQEx7AgMBAAECggEAZYobL2tCK0IF1Yrc+8irV2m61jZceU4AusOKUpxnfaZ7AcTknl18kxCeKdJwYjzKfjO0xJKSwFwtdEQbTOaK20Dz/sUYPYa7t0CONyL6Qn0rD5ksfTftQoTdkmh444Bc1xRVQOx/GZBMVuDXAJyNei0bG3bgBz0Woc/xzqWbnxKDaqpS4d+LmdjSss4Q3HQ6L2uh0b3RaUyQ5HPSGTzoo8eA3iRAvxYW7dtgf2LoDztZ2Uv9HCL8v0/MnvVnRbOPPI5BwEr2dItGL5uj2BTrjvpCcd352WUlu4mMjdADywGDgyAn4GaPI9+G6rdplwVyO5nmVoMPjtd50Ul2mVm+AQKBgQD7hkcWd5thFlFYi0gUpUIs3hheaqKTm+mK4+RQxlGTg695fFs93L6Way98x9UiG86Oa2pCaMo0TZTdsSVqV8x/dCob+ElBcr5hM6zJhE3iuCH9mEw2DxDAFmyut5OqO/M3f4f1EWMuntgNQ7pI73A4UdPEvbhq4Kxb7RSn+QdmgQKBgQCk6eAn1+makU7quKsjscOoMp7b7DIrfv0FKVLPK1y0CbGvELExItqT4tfn3RBZO1kUmRTpaRx+ZvQhkULv9Y07U6iDiaicFmOf87t0iFDKm1aQCmyfoB1ZXxWXwUFlWsZGo0pB5lAj7mAQswxLcSsmBFtJ/bD2rKFbUK7TqFDM+wKBgQCYNwncSePpXGU8LYYgoGYs2vdqZCQhKlCqcgHg9DO4DxBpd92L0YQxaYpifFi6fJODUvQKXnpILs8xXpwjACFM3JMDPD2w8uzNzET76yprUz2Wx80PRgzAApIhJg2iDwfAhVhU8gHQ2+YqGNuqeeSy4KpEvP/XBkKomxBOGwHkgQKBgCpVv616yAVd+BxZe9WV9Nxg4lcis15nIx0IFrIIN6wgMPT4HAS5JdOBEFv9bAz8J0oaYjvpN99bHqDfYYeoSbFJMKFT3Wz0cm7FawHnXJYmenpPssLnn6Epv6lNezRBsVTA5nc0YK5Yq2CeFjHnw2PnCmhcL+mjN2jxtW+wbc2xAoGBAMBpqCHH3nyH+ckyPIao/gKkA/PFb4ur2e9JfhjEfpne80zwD5p6Md1Hm01j7vMu/uM+LhOq/J6w76eb1DRu4nzCngTS23/RErXZwvMCqkMe/GqM3gEvmqJqDX10r3RY86NM9tNSiX5ntgW+pyBLOxMJHwGG2+5xJ8nu7uGnl5K6";

	public static String getPublicKeyStr(){
		return PUBLIC_KEY;
	}

	public static RSAPublicKey getPublicKey() throws Exception {
		byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
		return (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
	}

	public static RSAPrivateKey getPrivateKey() throws Exception {
		byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
		return (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
	}

	public static RSAKey generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
		String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
		return new RSAKey(privateKey, privateKeyString, publicKey, publicKeyString);
	}

	public static String encrypt(String source) throws Exception {
		byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
		RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(1, rsaPublicKey);
		return Base64.encodeBase64String(cipher.doFinal(source.getBytes(StandardCharsets.UTF_8)));
	}

	public static Cipher getCipher() throws Exception {
		byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(2, rsaPrivateKey);
		return cipher;
	}

	public static String decrypt(String text) throws Exception {
		Cipher cipher = getCipher();
		byte[] inputByte = Base64.decodeBase64(text.getBytes(StandardCharsets.UTF_8));
		return new String(cipher.doFinal(inputByte));
	}

	public static class RSAKey {
		  private RSAPrivateKey privateKey;
		  private String privateKeyString;
		  private RSAPublicKey publicKey;
		  public String publicKeyString;

		  public RSAKey(RSAPrivateKey privateKey, String privateKeyString, RSAPublicKey publicKey, String publicKeyString) {
		    this.privateKey = privateKey;
		    this.privateKeyString = privateKeyString;
		    this.publicKey = publicKey;
		    this.publicKeyString = publicKeyString;
		  }

		  public RSAPrivateKey getPrivateKey() {
		    return this.privateKey;
		  }

		  public void setPrivateKey(RSAPrivateKey privateKey) {
		    this.privateKey = privateKey;
		  }

		  public String getPrivateKeyString() {
		    return this.privateKeyString;
		  }

		  public void setPrivateKeyString(String privateKeyString) {
		    this.privateKeyString = privateKeyString;
		  }

		  public RSAPublicKey getPublicKey() {
		    return this.publicKey;
		  }

		  public void setPublicKey(RSAPublicKey publicKey) {
		    this.publicKey = publicKey;
		  }

		  public String getPublicKeyString() {
		    return this.publicKeyString;
		  }

		  public void setPublicKeyString(String publicKeyString) {
		    this.publicKeyString = publicKeyString;
		  }
		}
}
