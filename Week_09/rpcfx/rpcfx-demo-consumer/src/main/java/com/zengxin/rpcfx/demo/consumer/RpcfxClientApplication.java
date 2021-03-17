package com.zengxin.rpcfx.demo.consumer;

import com.zengxin.rpcfx.api.Filter;
import com.zengxin.rpcfx.api.LoadBalancer;
import com.zengxin.rpcfx.api.Router;
import com.zengxin.rpcfx.api.RpcfxRequest;
import com.zengxin.rpcfx.client.CuratorClient;
import com.zengxin.rpcfx.client.Rpcfx;
import com.zengxin.rpcfx.demo.api.Order;
import com.zengxin.rpcfx.demo.api.OrderService;
import com.zengxin.rpcfx.demo.api.User;
import com.zengxin.rpcfx.demo.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) throws Exception {

		//注册
		UserService userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new RequestFilter());
		User user2 = userService2.findById(100);
		System.err.printf("find user name=%s, amount=%s " ,user2.getId(),user2.getName());
		OrderService orderService2 = Rpcfx.createFromRegistry(OrderService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new RequestFilter());
		Order order2 = orderService2.findOrderById(100);
		System.err.printf("find order name=%s, amount=%f%n",order2.getName(),order2.getAmount());
		CuratorFramework client = CuratorClient.getClient("localhost:2181");
		//模拟注册新服务
		client.create().withMode(CreateMode.EPHEMERAL).
				forPath( "/" + UserService.class.getName() + "/localhost_8080", "provider".getBytes());
		//第二次调用
		User user3 = userService2.findById(100);
	}

	private static class TagRouter implements Router {
		@Override
		public List<String> route(List<String> urls) {
			return urls;
		}
	}

	private static class RandomLoadBalancer implements LoadBalancer {
		@Override
		public String select(List<String> urls) {
			return urls.get(0);
		}
	}

	@Slf4j
	private static class RequestFilter implements Filter {
		@Override
		public boolean filter(RpcfxRequest request) {
			log.info("filter {} -> {}", this.getClass().getName(), request.toString());
			return true;
		}
	}
}



