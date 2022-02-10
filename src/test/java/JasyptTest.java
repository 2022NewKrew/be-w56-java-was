import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

/**
 * Created by melodist
 * Date: 2022-02-10 010
 * Time: 오후 7:14
 */
public class JasyptTest {

    @Test
    public void stringEncryptTest(){
        // given
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        String password = "PASS";
        String plainString = "test";

        encryptor.setPassword(password);
        String encryptedString = encryptor.encrypt(plainString);
        System.out.println("encryptedString = " + encryptedString);

        // when
        String decryptedString = encryptor.decrypt(encryptedString);

        // then
        Assertions.assertThat(decryptedString).isEqualTo(plainString);
    }
}
