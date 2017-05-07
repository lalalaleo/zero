import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import method.MyService;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;











import com.ifp.wechat.constant.ConstantWeChat;
import com.ifp.wechat.entity.AccessToken;
import com.ifp.wechat.entity.message.resp.Article;
import com.ifp.wechat.entity.message.resp.NewsMessage;
import com.ifp.wechat.entity.message.resp.TextMessage;
import com.ifp.wechat.service.MessageService;
import com.ifp.wechat.service.UserGroupService;
import com.ifp.wechat.util.MessageUtil;
import com.ifp.wechat.util.WeixinUtil;
import com.sun.glass.ui.Robot;

/*
 *  "access_token": "lhkVEbBTXtug8c5JD3hptDvZ7Xf5YsJHN7pc-z1dZQ36Q_abUyTEGOfqZo50ctIdtZwL6A6Emf0dIxhEFiA1Wrnv5p-DSXkCIva0U1jbyJP4IksGTQI043hoyD5egMSYFHKeAHAACF", 
    "expires_in": 7200
 */
public class WechatService {

	public static Logger log = Logger.getLogger(WechatService.class);

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return String
	 */
	public static String processWebchatRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 消息类型
			String msgType = requestMap.get("MsgType");
			/*-建立text和news类型的对象-*/
			TextMessage textMessage = (TextMessage) MessageService
					.bulidBaseMessage(requestMap,
							ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
			NewsMessage newsMessage = (NewsMessage) MessageService
					.bulidBaseMessage(requestMap,
							ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
			String respContent = "";
			/*-创建多新闻信息list-*/
			List<Article> articleList = new ArrayList<Article>();
			/*-多文本信息list-*/
			List<TextMessage> textList = new ArrayList<TextMessage>();
			/*接收到文本信息*/
			if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_TEXT)) {
				// 接收用户发送的文本消息内容
				String content = requestMap.get("Content");

				/*-单新闻-*/
				/*-当输入的信息为tt-*/
				if ("tt".equals(content)) {
					System.out.println("testopenid:"+requestMap.get("openid"));
					Article article = new Article();
					/*-设置标签-*/
					article.setTitle("我是一条单图文消息");
					/*设置描述信息*/
					article.setDescription("我是描述信息");
					/*设置这个新闻图片的url*/
					article.setPicUrl("http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg");
					/*设置这个新闻指向的url地址*/
					article.setUrl("https://www.baidu.com/");
					/*-添加到新闻list中-*/
					articleList.add(article);
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					// 将图文消息对象转换成xml字符串                              图文信息容器 和要转换的类型        
					respMessage = MessageService.bulidSendMessage(newsMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					// 多图文消息
				}else if ("3".equals(content)) {
					Article article1 = new Article();
					article1.setTitle("我是一条多图文消息");
					article1.setDescription("");
					article1.setPicUrl("http://www.isic.cn/viewResourcesAction//logo/20130913/2013091314543416032.jpg");
					article1.setUrl("http://tuposky.iteye.com/blog/2008583");

					Article article2 = new Article();
					article2.setTitle("微信公众平台开发教程Java版（二）接口配置 ");
					article2.setDescription("");
					article2.setPicUrl("http://www.isic.cn/viewResourcesAction//logo/20131021/2013102111243367254.jpg");
					article2.setUrl("http://tuposky.iteye.com/blog/2008655");

					Article article3 = new Article();
					article3.setTitle("微信公众平台开发教程Java版(三) 消息接收和发送");
					article3.setDescription("");
					article3.setPicUrl("http://www.isic.cn/viewResourcesAction//logo/20131021/2013102111291287031.jpg");
					article3.setUrl("http://tuposky.iteye.com/blog/2017429");

					articleList.add(article1);
					articleList.add(article2);
					articleList.add(article3);
					newsMessage.setArticleCount(articleList.size());
					newsMessage.setArticles(articleList);
					respMessage = MessageService.bulidSendMessage(newsMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					/*-发送文本信息给用户-*/
				}else if ("z".equals(content)) {
					TextMessage t1 = new TextMessage();
					/*设置发送方 公众号ID*/
					t1.setFromUserName(requestMap.get("ToUserName"));
					/*设置接收方 用户*/
					t1.setToUserName(requestMap.get("FromUserName"));
					Date date = new Date();
					long time = date.getTime();
					/*-设置发送的时间-*/
					t1.setCreateTime(time);
					/*设置内容*/
					t1.setContent("zeta");
					/*设置发送的类型*/
					t1.setMsgType("text");
					respMessage = MessageService.bulidSendMessage(t1,ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
					/*-更简单的发送文本-*/
				}else if("zz".equals(content)){
					respContent = "更简单的方式实现！\n";
					/*创建字符流*/
					StringBuffer contentMsg = new StringBuffer();
					contentMsg.append("1\n");
					respContent += contentMsg.toString();
					textMessage.setContent(respContent);
					respMessage = MessageService.bulidSendMessage(textMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
				}
				/*声音信息处理*/	
			} else if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_VOICE)) {
				textMessage.setContent("您说的是：" + requestMap.get("Recognition"));
				respMessage = MessageService.bulidSendMessage(textMessage,
						ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
				/*- 事件处理-*/
			} else if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				/*-关注事件-*/
				if (eventType.equals(ConstantWeChat.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "关注测试！\n";
					/*-把文本内容放到了Content中具体为 <Content><![CDATA[内容]]></Content>-*/
					StringBuffer contentMsg = new StringBuffer();
					contentMsg.append("您还可以回复下列数字，体验相应服务").append("\n\n");
					contentMsg.append("1  我就是个测试的").append("\n");
					contentMsg.append("2  我啥都木有").append("\n");
					contentMsg.append("3  我是多图文").append("\n");
					/*-添加到回复中-*/
					respContent = respContent + contentMsg.toString();
					textMessage.setContent(respContent);
					respMessage = MessageService.bulidSendMessage(textMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
				} else if (eventType
						.equals(ConstantWeChat.EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息
					respContent = "取消关注";
					textMessage.setContent(respContent);
					respMessage = MessageService.bulidSendMessage(textMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
					/*自定义菜单时间*/
				} else if (eventType.equals(ConstantWeChat.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					/*-回复文本信息-*/
					if (eventKey.equals("b2")) {
						
						respContent = "11";
						textMessage.setContent(respContent);
						respMessage = MessageService.bulidSendMessage(textMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
						/*图文信息的形式*/
					} else if (eventKey.equals("b108")) {
						Article article = new Article();
						article.setTitle("我是一条单图文消息");
						article.setDescription("我是描述信息");
						article.setPicUrl("http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg");
						/*设置这个新闻指向的url地址*/
						article.setUrl("https://www.baidu.com/");
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					/*帮助按钮事件和教师学生功能按钮事件*/
					}else if (eventKey.equals("help") || eventKey.equals("teacherhelp") || eventKey.equals("studenthelp")) {
						
					}else if(eventKey.equals("tacher1")){
						
					}else if (eventKey.equals("teacher2")) {
						
					}else if (eventKey.equals("teacher3")) {
						
					}else if (eventKey.equals("student1")) {
						
					}else if (eventKey.equals("student1")) {
						
					}else if (eventKey.equals("student1")) {
					
					/*绑定*/
					}else if (eventKey.equals("bind")) {
						new MyService().setUserTag(requestMap.get("FromUserName"), 109);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(respMessage);
		return respMessage;
	}
}
