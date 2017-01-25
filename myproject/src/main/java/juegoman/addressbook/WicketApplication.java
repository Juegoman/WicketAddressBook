package juegoman.addressbook;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class WicketApplication extends WebApplication
{
        //define the Jedis Pool for connecting to the redis Database. To get a
        // Jedis instance just call WicketApplication.JedisPool.getResource()
        public static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        //namespace prefix for all redis keys.
        public static final String KEYPREFIX = "WICKETADDRESSBOOK|||";
        
	@Override
	public Class<? extends WebPage> getHomePage() {
            return HomePage.class;
	}
        
	@Override
	public void init() {
            super.init();
	}
        
        //attempt to prevent memory leaks.
        @Override
        public void onDestroy() {
            jedisPool.destroy();
        }
}
