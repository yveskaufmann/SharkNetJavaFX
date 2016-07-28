package net.sharksystem.sharknet.api;

import org.javatuples.Pair;
import net.sharkfw.security.key.SharkKeyGenerator;
import net.sharkfw.security.key.SharkKeyPairAlgorithm;
import net.sharkfw.system.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.Date;

/**
 * A helper class which helps to create and manage keyPairs
 * for the dummy profiles.
 * <p>
 * <b>NOTE:</b> this class is only intended for dummy data creation,
 * production use is not recommend.
 *
 * @author Yves Kaufmann
 * @since 16.07.2016
 */
public class DummyKeyPairHelper {

	private static final long AVG_MILLI_SECONDS_PER_MONTH = 1000L * 60L * 60L * 24L * 30L;

	/**
	 * Generates a dummy public and private key.
	 * <p>
	 *
	 * @return a pair with a public key and a private key.
     */
	public static Pair<PublicKey, PrivateKey> generateNewKeyPair() {
		// the key size can be low its only a demo key security doesn't matters here
		final SharkKeyGenerator keyGenerator = new SharkKeyGenerator(SharkKeyPairAlgorithm.RSA, 512);
		return new Pair<>(keyGenerator.getPublicKey(), keyGenerator.getPrivateKey());
	}

	/**
	 * Generate a new public key for the specified contact.
	 *
	 * @param contact
     */
	public static void createNewKeyForContact(ImplContact contact) {
		final Pair<PublicKey, PrivateKey> keyPair = generateNewKeyPair();
		final String fingerprint = createFingerPrint(keyPair.getValue0());
		final Timestamp expirationDate = new Timestamp(new Date().getTime() + AVG_MILLI_SECONDS_PER_MONTH * 6);

		contact.publicKeyFingerPrint = fingerprint;
		contact.keyExpiration = expirationDate;
		if (contact.publickey == null || contact.publickey.equals("")) {
			String key = Base64.encodeBytes(keyPair.getValue0().getEncoded());
			StringBuilder newKey = new StringBuilder();
			newKey.append("-----BEGIN PUBLIC KEY-----");
			newKey.append("\n");
			int keyLen = key.length();
			for (int i = 0; i < keyLen; i += 72) {
				int endIndex = (i + 72);
				if (endIndex > keyLen) endIndex = keyLen;
				newKey.append(key.substring(i, endIndex));
				newKey.append("\n");
			}
			newKey.append("-----END-PUBLIC KEY-------");
			contact.publickey = newKey.toString();
		}
	}

	/**
	 * Generates the fingerprint of a specified public key.
	 * A fingerprint is a sequence of octets printed as hexadecimal
	 * with lowercase letters and separated by colons.
	 * <p>
	 * The fingerprint prevents a user from verifying two entire public
	 * keys, which is a difficult task for humans. The same task can be
	 * be done by verifying two fingerprints.
	 *
	 * @param publicKey the public key to convert to a fingerprint
	 *
	 * @return the fingerprint representation of the specified public key
	 *
	 * @see <a href="https://tools.ietf.org/html/rfc4716#section-4">rfc4716 - The Secure Shell (SSH) Public Key File Format</a>
     */
	public static String createFingerPrint(PublicKey publicKey) {
		String digestAlgorithm = "MD5";
		StringBuilder stringBuilder = new StringBuilder();
		try {

			final MessageDigest messageDigest = MessageDigest.getInstance(digestAlgorithm);
			final byte[] hashBytes = messageDigest.digest(publicKey.getEncoded());

			for (byte b : hashBytes) {
				if (stringBuilder.length() > 0) {
					stringBuilder.append(":");
				}

				byte b0 = (byte) ((byte) b >> 4 & 0xF);
				byte b1 = (byte) (b & 0xF);

				stringBuilder.append(Character.forDigit(b0, 16));
				stringBuilder.append(Character.forDigit(b1, 16));

			}
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Failed to create fingerprint: " + digestAlgorithm + " isn't a supported MessageDigest algorithm");
		}
		return stringBuilder.toString();
	}

}
