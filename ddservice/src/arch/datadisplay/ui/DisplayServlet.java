package arch.datadisplay.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import arch.dataaggregate.IDataAggregate;
import arch.datadisplay.ui.internal.TableObject;

public class DisplayServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 590808281763644925L;
    private static final String ANALYZED_DATA = "Analyzed Data";
    private static final String ORIGINAL_DATA = "Original Data";
    
    private IDataAggregate dataAggregateService = null;
    private static DispatcherThread t;
    
    static {
    	t = new DispatcherThread();
    	t.start();
    }
    
    protected void setDataAggregateService(IDataAggregate da) {
    	dataAggregateService = da;
    }

    protected void unsetDataAggregateService(IDataAggregate da) {
    	dataAggregateService = null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	StringBuilder buffer = new StringBuilder(200);
        PrintWriter writer = resp.getWriter();
        
        renderHeader(buffer);
        
        t.setDataAggregateService(dataAggregateService);
        
        triggerRendering(req.getParameter("type"), DisplayServlet.ANALYZED_DATA);
        triggerRendering("identity", DisplayServlet.ORIGINAL_DATA);
        
        String message1 = getResult();
    	StringTokenizer tokenizer1 = new StringTokenizer(message1, "\n");
    	String type1 = tokenizer1.nextToken();
    	String body1 = tokenizer1.nextToken();
    	
        String message2 = getResult();
    	StringTokenizer tokenizer2 = new StringTokenizer(message2, "\n");
    	String type2 = tokenizer2.nextToken();
    	String body2 = tokenizer2.nextToken();
    	
    	if ( "identity".equals(type2) ) {
    		buffer.append(body1);
    		buffer.append(body2);
    	} else {
    		buffer.append(body2);
    		buffer.append(body1);
    	}
        
        renderFooter(buffer);
        writer.write(buffer.toString());
    }
    
    private void renderHeader(StringBuilder buffer) {
    	buffer.append("<html><head>");
    	buffer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://localhost:1234/styles.css\"/>");
    	buffer.append("</head><body>");
    	buffer.append("<img src=annual-revenues.jpg/><br>");
    }
    
    private void renderFooter(StringBuilder buffer) {
        buffer.append("</body></html>");
    }
    
    private Destination destination;
    private String in = "ANALYZE.IN";
    
    private void triggerRendering(String type, String header) {
        Connection connection = null;
        // Create the connection.
        try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = connectionFactory.createConnection();
			
			connection.start();

	        // Create the session
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	       	destination = session.createQueue(in);
	        
	        // Create the producer.
	        MessageProducer producer = session.createProducer(destination);
	        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

	        TextMessage message = session.createTextMessage(type + "\n" + header);
	        
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
    
    private String out = "ANALYZE.OUT";
    
    private String getResult() {
    	String text = "";
    	
        Connection connection = null;
        // Create the connection.
        try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = connectionFactory.createConnection();
			
			connection.start();

	        // Create the session
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	       	Queue destination = session.createQueue(out);
	        
	        // Create the producer.
	        MessageConsumer consumer = session.createConsumer(destination);
	        
	        Message message = consumer.receive();
	        
	        if ( message instanceof TextMessage ) {
	        	TextMessage textmessage = (TextMessage)message;
	        	text = textmessage.getText(); 	
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
		return text;
    }
}
