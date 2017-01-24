package juegoman.addressbook;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class WicketApplication extends WebApplication
{
        public static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        public static final String KEYPREFIX = "WICKETADDRESSBOOK|||";
        
	@Override
	public Class<? extends WebPage> getHomePage() {
            return HomePage.class;
	}
        
	@Override
	public void init() {
            super.init();
	}
        
        @Override
        public void onDestroy() {
            jedisPool.destroy();
        }
}
