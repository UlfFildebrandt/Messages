package arch.datadisplay.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import arch.dataaggregate.IDataAggregate;
import arch.datadisplay.ui.internal.TableObject;

public class DispatcherThread extends Thread {
    
    private IDataAggregate dataAggregateService = null;
    private String in = "ANALYZE.IN";
    private String out = "ANALYZE.OUT";
    
    protected void setDataAggregateService(IDataAggregate da) {
    	dataAggregateService = da;
    }

    protected void unsetDataAggregateService(IDataAggregate da) {
    }
	
	@Override
	public synchronized void run() {
		String type = "identity";
		String header = "";
		String body = "";
    	
    	while(true) {
            Connection connection = null;
            // Create the connection.
            try {
    			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
    			connection = connectionFactory.createConnection();
    			
    			connection.start();

    	        // Create the session
    	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	       	Queue destination = session.createQueue(in);
    	        
    	        // Create the producer.
    	        MessageConsumer consumer = session.createConsumer(destination);
    	        
    	        Message message = consumer.receive();
    	        
    	        if ( message instanceof TextMessage ) {
    	        	TextMessage textmessage = (TextMessage)message;
    	        	StringTokenizer tokenizer = new StringTokenizer(textmessage.getText(), "\n");
    	        	type = tokenizer.nextToken();
    	        	header = tokenizer.nextToken();
    	        	
    	            List<List<String>> list1 = dataAggregateService.getData(type);
    	            StringBuilder buffer = new StringBuilder(200);
    	            TableObject analyzedTable = new TableObject(list1, header, buffer);
    	            renderTable(analyzedTable);
    	            body = buffer.toString();
    	        }
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			try {
    				connection.close();
    			} catch (JMSException e) {
    				e.printStackTrace();
    			}
    		}
    		
            // Create the connection.
            try {
    			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
    			connection = connectionFactory.createConnection();
    			
    			connection.start();

    	        // Create the session
    	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	       	Queue destination = session.createQueue(out);
    	        
    	        // Create the producer.
    	        MessageProducer producer = session.createProducer(destination);
    	        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

    	        TextMessage message = session.createTextMessage(type + "\n" + body);
    	        
                producer.send(message);
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			try {
    				connection.close();
    			} catch (JMSException e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}

	}

    private void renderTable(TableObject tableObject) {
        renderTableHeader(tableObject, tableObject.getTitle(), tableObject.getBuffer());

        for (int rowIdx = 1; rowIdx < tableObject.getRows(); rowIdx++) {
            renderTableRow(tableObject.getBuffer(), tableObject.getItem(rowIdx), rowIdx);
        }

        renderTableFooter(tableObject.getBuffer());
    }

    private void renderTableFooter(StringBuilder buffer) {
        buffer.append("</table>");
    }

    private void renderTableRow(StringBuilder buffer, List<String> list1, int i) {
        buffer.append("<tr><th scope=\"row\" class=\"spec\">" + list1.get(0) + "</th>");
        
        for(int j = 1; j < list1.size(); j++) {
        	buffer.append("<td>" + list1.get(j) + "</td>");
        }
        
        buffer.append("</tr>");
    }

    private void renderTableHeader(TableObject tableObject, String title, StringBuilder buffer) {
    	List<String> headers = tableObject.getItem(0);
    	
        buffer.append("<br><div class=title>" + title + "</div><br>");
        buffer.append("<table cellspacing=\"0\"><tr><th width=\"300px\" scope=\"col\" class=\"nobg\">" + headers.get(0) + "</th>");
        
        for(int i = 1; i < headers.size(); i++) {
        	buffer.append("<th width=\"200px\" scope=\"col\">" + headers.get(i) + "</th>");
        }
        buffer.append("</tr>");
    }
}
