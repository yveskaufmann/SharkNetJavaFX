package net.sharksystem.sharknet.api;

/**
 * Created by timol on 16.05.2016.
 */

public class ImplContact implements Contact {

	String nickname;
	String uid;
	String PublicKey;

	public ImplContact(String nickname, String uid){
		this.nickname = nickname;
		this.uid = uid;
	}


	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public String getUID() {
		return uid;
	}

	@Override
	public String getPicture() {
		return null;
	}

	@Override
	public String getPublicKey() {
		return "-----BEGIN PUBLIC KEY-----\n" +
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0\n" +
			"FPqri0cb2JZfXJ/DgYSF6vUpwmJG8wVQZKjeGcjDOL5UlsuusFncCzWBQ7RKNUSesmQRMSGkVb1/\n" +
			"3j+skZ6UtW+5u09lHNsj6tQ51s1SPrCBkedbNf0Tp0GbMJDyR4e9T04ZZwIDAQAB\n" +
			"-----END PUBLIC KEY-----";
	}
}
