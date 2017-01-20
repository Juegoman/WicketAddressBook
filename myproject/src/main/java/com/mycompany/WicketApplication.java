package com.mycompany;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.mycompany.Start#main(String[])
 */
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
