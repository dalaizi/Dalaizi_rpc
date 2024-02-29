# 基于Netty、Zookeeper、Spring的轻量级RPC框架 

**基于Netty进行高性能网络通信，基于Spring进行对象管理以及动态代理的生成，基于Zookeeper实现服务器节点创建与监听，最终实现高性能、轻量级的分布式RPC框架。**


# 项目特点
- 实现**长连接**和**心跳检测机制**，解决短连接的频繁通信问题；
- 实现基于Netty的**异步非阻塞**通信方式，解决同步阻塞调用的性能瓶颈；
- 实现基于**JSON**的序列化，解决Java原生序列化性能瓶颈，实现远程过程调用；
- 基于**动态代理**进行请求处理；解决进程通信问题；解决**TCP/IP拆包粘包**问题；
- 利用**BeanPostProcessor**机制和**ApplicationListenr**机制实现客户端的自启动与基于注解的服务调用；
- 基于**ZooKeeper**实现服务注册中心，提供客户端服务发现功能、服务节点监听以及动态管理功能；
- 实现基于注解的零配置框架。

![Markdown](README/PZK3SP.png)

# Quick Start
### 服务端开发
- **在服务端的Service下添加你自己的Service,并加上@Service注解**
	
	<pre>
	@Service
	public class TestService {
		public void test(User user){
			System.out.println("调用了TestService.test");
		}
	}
	</pre>
	
- **生成1个服务接口并生成1个实现该接口的类**
	###### 接口如下
	<pre>
	public interface TestRemote {
		public Response testUser(User user);  
	}
	</pre>
	###### 实现类如下，为你的实现类添加@Remote注解，该类是你真正调用服务的地方，你可以生成自己想返回给客户端的任何形式的Response

	<pre> 
	@Remote
	public class TestRemoteImpl implements TestRemote{
		@Resource
		private TestService service;
		public Response testUser(User user){
			service.test(user);
			Response response = ResponseUtil.createSuccessResponse(user);
			return response;
		}
	}	
	</pre>


### 客户端开发
- **在客户端生成一个接口，该接口为你要调用的接口**
	<pre>
	public interface TestRemote {
		public Response testUser(User user);
	}
	</pre>

### 使用
- **在你要调用的地方生成接口形式的属性，为该属性添加@RemoteInvoke注解**
	<pre>
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(classes=RemoteInvokeTest.class)
	@ComponentScan("\\")
	public class RemoteInvokeTest {
		@RemoteInvoke
		public static TestRemote userremote;
		public static User user;
		@Test
		public void testSaveUser(){
			User user = new User();
			user.setId(1000);
			user.setName("张三");
			userremote.testUser(user);
		}
	}	
	</pre>






