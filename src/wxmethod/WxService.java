package wxmethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ifp.wechat.util.WeixinUtil;

public class WxService {
	/*根据输入的json格式字符串申请创建菜单 并返回是否成功*/
	public boolean createMenu(String jsonMenu) {
		String access_token = WeixinUtil.getAccessToken("wx7011496372902790", "22448b7ad7edf143d027144f378e2fe6").getToken();
		String result =  WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token="+access_token, jsonMenu);
		JSONObject t = JSON.parseObject(result);
		if(t.get("errcode").equals(0)){
			return true;
		}else{
			return false;
		}
	} 
	/*根据输入的标签名创建用户标签 tagname为标签名 并返回是否成功*/
	public boolean createTag(String tagname) {
		JSONObject jsonTab = new JSONObject();
		JSONObject tag = new JSONObject();
		tag.put("name",tagname);
		jsonTab.put("tag",tag);
		System.out.println(jsonTab.toString());
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/create?access_token="+access_token, jsonTab.toString());
		JSONObject t = JSON.parseObject(result);
		if(t.get("errcode").equals(0)){
			return true;
		}else{
			return false;
		}
	}
	/*设置用户的标签  tagid为标签id学生为109　老师为111 并返回是否成功*/
	public boolean setUserTag(String useropenid,int tagid) {
		JSONObject userSetTag = new JSONObject();
		String[] userList = {useropenid};
		userSetTag.put("openid_list",userList );
		userSetTag.put("tagid", tagid);
		System.out.println(userSetTag.toString());
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token="+access_token, userSetTag.toString());
		JSONObject t = JSON.parseObject(result);
		if(t.get("errcode").equals(0)){
			return true;
		}else{
			return false;
		}
	}
	/*删除对应标签下的对用用户 因为用户可以用过多个标签 避免冲突  并返回是否成功*/
	public boolean deletUserTag(String useropenid,int tagid) {
		JSONObject userSetTag = new JSONObject(true);
		String[] userList = { useropenid};
		userSetTag.put("openid_list",userList );
		userSetTag.put("tagid", tagid);
		System.out.println(userSetTag.toString());
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token="+access_token, userSetTag.toString());
		JSONObject t = JSON.parseObject(result);
		if(t.get("errcode").equals(0)){
			return true;
		}else{
			return false;
		}
	}
	/*发送服务信息 第一个参数为接收者id 第二个参数为news的标题 第三个参数为news的内容 url为跳转的url地址 并返回是否成功*/
	public boolean sendCustomerServiceInfo(String openid,String title,String content,String url) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject map1 = new JSONObject(true);
		map1.put("touser",openid);
		map1.put("msgtype","news");
		JSONObject map2 = new JSONObject(true);
		map2.put("title",title);
		map2.put("description",content);
		map2.put("url", url);
		map2.put("picurl","");
		LinkedHashMap<String, Object> root1 = JSON.parseObject(map2.toString(),new TypeReference<LinkedHashMap<String, Object>>(){});//关键的地方，转化为有序map
		LinkedHashMap<String, Object> root = JSON.parseObject(map1.toString(),new TypeReference<LinkedHashMap<String, Object>>(){});//关键的地方，转化为有序map
		JSONObject map3 = new JSONObject(true);
		list.add(root1);
		map3.put("articles",list);
		root.put("news",map3);
		System.out.println(JSON.toJSONString(root));
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token,JSON.toJSONString(root));
		JSONObject t = JSON.parseObject(result);
		if(t.get("errcode").equals(0)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 发送模板信息 返回true为成功       用户id url(可为空 为空没有详细)first内容（可为空） teacher为老师名字  classname为课程 content为具体信息 并返回是否成功
	 * message为模板消息
	 * */
	public boolean sendTemplateInfo(String openid,String url,String first,String teacher,String classname,String content){
		JSONObject message = new JSONObject(true);
		message.put("touser",openid);
		message.put("url",url);
		message.put("template_id","V8U6dGAN8h_z6plh2vSEtjrK15DrUmolyUWbqHsn7lQ");
		JSONObject data1 = new JSONObject(true);
		JSONObject key = new JSONObject(true);
		key.put("value",first);
		key.put("color", "#000000");
		data1.put("first",key);
		key = new JSONObject(true);
		key.put("value", teacher);
		key.put("color", "#000000");
		data1.put("key1",key);
		key = new JSONObject(true);
		key.put("value", classname);
		key.put("color", "#000000");
		data1.put("key2",key);
		key = new JSONObject(true);
		key.put("value", content);
		key.put("color", "#000000");
		data1.put("key3",key);
		LinkedHashMap<String, Object> roottest = JSON.parseObject(message.toString(),new TypeReference<LinkedHashMap<String, Object>>(){});//关键的地方，转化为有序map
		roottest.put("data",data1);
		System.out.println(JSON.toJSONString(roottest));
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token,JSON.toJSONString(roottest));
		System.out.println(result);
		JSONObject t = JSON.parseObject(result);
		if(t.get("errcode").equals(0)){
			return true;
		}else{
			return false;
		}
	}
}
