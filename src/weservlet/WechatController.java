package weservlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ifp.wechat.constant.ConstantWeChat;
import com.ifp.wechat.entity.message.resp.Article;
import com.ifp.wechat.entity.message.resp.NewsMessage;
import com.ifp.wechat.entity.message.resp.TextMessage;
import com.ifp.wechat.service.MessageService;
import com.ifp.wechat.util.MessageUtil;

import dao.ClazzDao;
import model.Clazz;

public class WechatController {
	public static Logger log = Logger.getLogger(WechatController.class); 

	public static String processWebchatRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			String msgType = requestMap.get("MsgType");
			TextMessage textMessage = (TextMessage) MessageService.bulidBaseMessage(requestMap, ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
			NewsMessage newsMessage = (NewsMessage) MessageService.bulidBaseMessage(requestMap, ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
			String respContent = "";
			List<Article> articleList = new ArrayList<Article>();
			List<TextMessage> textList = new ArrayList<TextMessage>();
			if(msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_TEXT)){
				String openid = requestMap.get("FromUserName");
				String content = requestMap.get("Content");
				ClazzDao clazzDao = new ClazzDao();
				List<Clazz> listc = clazzDao.listBySubName(openid, content);
				Article article = new Article();
				article.setTitle("您的课程查询信息");
				if(listc.size() == 0){
					article.setDescription("无此课程信息");
					article.setPicUrl("");
					article.setUrl("");
				}else{
					Clazz c = listc.get(0);
					article.setDescription("点击查看您的查询结果");
					article.setPicUrl("");
					article.setUrl("http://lalalaleo.com/wechat/class.html?openid="+openid+"&classid="+c.getClaId());
				}
				articleList.add(article);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageService.bulidSendMessage(newsMessage, ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
			}else if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_EVENT)) {
				String eventType = requestMap.get("Event");
				if (eventType.equals(ConstantWeChat.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "欢迎关注本微信号";
					StringBuffer contentMsg = new StringBuffer();
					contentMsg.append("按帮助跳出帮助选项").append("\n");
					contentMsg.append("绑定进行用户绑定").append("\n");
					contentMsg.append("绑定成功后界面会更改，因为微信延迟问题可能要几分钟时间才更新显示").append("\n");
					contentMsg.append("绑定成功后帮助信息也会变").append("\n");
					respContent = respContent + contentMsg.toString();
					textMessage.setContent(respContent);
					respMessage = MessageService.bulidSendMessage(textMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
				} else if (eventType
						.equals(ConstantWeChat.EVENT_TYPE_UNSUBSCRIBE)) {
					textMessage.setContent(respContent);
					respMessage = MessageService.bulidSendMessage(textMessage,
							ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
				} else if (eventType.equals(ConstantWeChat.EVENT_TYPE_CLICK)) {
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("11")) {
						respContent = "11";
						textMessage.setContent(respContent);
						respMessage = MessageService.bulidSendMessage(textMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
					}else if (eventKey.equals("help")) {
						respContent = "此为未注册用户帮助信息！\n";
						/*-把文本内容放到了Content中具体为 <Content><![CDATA[内容]]></Content>-*/
						StringBuffer contentMsg = new StringBuffer();
						contentMsg.append("点击绑定后作为学生或者教师，正常使用本微信号").append("\n");
						contentMsg.append("点击绑定后菜单未出现变更请耐心等待，微信有几分钟延迟").append("\n");
						contentMsg.append("搜索课程名查不到课程的原因：您并为认证为学生或者教师").append("\n");
						/*-添加到回复中-*/
						respContent = respContent + contentMsg.toString();
						textMessage.setContent(respContent);
						respMessage = MessageService.bulidSendMessage(textMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
					}else if(eventKey.equals("teacherhelp1")){
						respContent = "此为教师用户帮助信息！\n";
						/*-把文本内容放到了Content中具体为 <Content><![CDATA[内容]]></Content>-*/
						StringBuffer contentMsg = new StringBuffer();
						contentMsg.append("教师功能有以下三个功能选项").append("\n");
						contentMsg.append("1：发送信息，填写信息后自动推送给学生连接的通知").append("\n");
						contentMsg.append("2：历史信息，点击即可收到历史信息页面连接的通知").append("\n");
						contentMsg.append("3：我的课程，点击可收到执教的课程信息页面连接的通知").append("\n");
						contentMsg.append("直接输入课程名称搜索课程信息").append("\n");
						contentMsg.append("查看信息会收到一个信息，点击后进入相应页面").append("\n");
						contentMsg.append("搜索课程名查不到课程的话有2种原因：").append("\n");
						contentMsg.append("1：您输入的名称不完全").append("\n");
						contentMsg.append("2：并无此课程").append("\n");
						/*-添加到回复中-*/
						respContent = respContent + contentMsg.toString();
						textMessage.setContent(respContent);
						respMessage = MessageService.bulidSendMessage(textMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
					}else if(eventKey.equals("studenthelp")){
						respContent = "此为学生帮助信息！\n";
						/*-把文本内容放到了Content中具体为 <Content><![CDATA[内容]]></Content>-*/
						StringBuffer contentMsg = new StringBuffer();
						contentMsg.append("学生功能有以下三个功能选项").append("\n");
						contentMsg.append("1：历史信息，点击即可收到历史信息页面连接的通知").append("\n");
						contentMsg.append("2：我的教师： 收到有教师信息的页面连接的通知").append("\n");
						contentMsg.append("3：我的课表，收到有您课程信息的页面连接的通知").append("\n");
						contentMsg.append("学生可以直接输入课程名称搜索课程信息").append("\n");
						contentMsg.append("查看信息会收到一个信息，点击后进入相应页面").append("\n");
						contentMsg.append("搜索课程名查不到课程的话有2种原因：").append("\n");
						contentMsg.append("1：您输入的名称不完全").append("\n");
						contentMsg.append("2：并无此课程").append("\n");
						/*-添加到回复中-*/
						respContent = respContent + contentMsg.toString();
						textMessage.setContent(respContent);
						respMessage = MessageService.bulidSendMessage(textMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
					}else if(eventKey.equals("teachaerg1")){
						Article article = new Article();
						article.setTitle("发送消息");
						article.setDescription("点此进行消息发送");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/mTeacherMessage.html?openid="+requestMap.get("FromUserName"));
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					}else if (eventKey.equals("teachaerg2")) {
						Article article = new Article();
						article.setTitle("历史消息");
						article.setDescription("点此进行查看历史消息");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/messageHistory.html?openid="+requestMap.get("FromUserName")+"&classid=all");
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					}else if (eventKey.equals("teachaerg3")) {
						/*此处需要classid*/
						String classid = new String();
						Article article = new Article();
						article.setTitle("查看我的课程");
						article.setDescription("点此查看我的课程");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/schoolTimetable.html?openid="+requestMap.get("FromUserName"));
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					}else if (eventKey.equals("student1")) {
						String classid = new String();
						Article article = new Article();
						article.setTitle("查看历史信息");
						article.setDescription("点此查看我的历史信息");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/messageHistory.html?openid="+requestMap.get("FromUserName")+"&classid=all");
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					}else if (eventKey.equals("student2")) {
						String classid = new String();
						Article article = new Article();
						article.setTitle("查看我的教师");
						article.setDescription("点此查看我的教师");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/teacherList.html?openid="+requestMap.get("FromUserName"));
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					}else if (eventKey.equals("student3")) {
						String classid = new String();
						Article article = new Article();
						article.setTitle("查看我的课表");
						article.setDescription("点此查看我的课表");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/schoolTimetable.html?openid="+requestMap.get("FromUserName"));
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
					/*绑定 意添加openid*/
					}else if (eventKey.equals("bind")) {
						
						Article article = new Article();
						article.setTitle("绑定申请");
						article.setDescription("点此进行绑定");
						article.setPicUrl("");
						article.setUrl("http://lalalaleo.com/wechat/binding.html?openid="+requestMap.get("FromUserName"));
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageService.bulidSendMessage(newsMessage,
								ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
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
