import com.yang.study.ServiceSharding8016Application;
import com.yang.study.dao.AccountDAO;
import com.yang.study.entity.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceSharding8016Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestClass {

    @Autowired
    private AccountDAO accountDAO;

    @Test
    // sharding有提供基于XA的事务，等待加入
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            Account account = new Account();
            account.setUser_id(i + random.nextInt(100) + 1L);
            account.setMoney(1.1 + i);
//            if (i == 18) {
//                int n = 1 / 0;
//            }
            accountDAO.addDefault(account);
        }

    }

}
