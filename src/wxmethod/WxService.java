package wxmethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import wxmodel.WxTag;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ifp.wechat.util.WeixinUtil;

public class WxService {
	/*根据输入的json格式字符串申请创建菜单 并返回菜单id或者错误信息*/
	public String createMenu(String jsonMenu) {
		String access_token = WeixinUtil.getAccessToken("wx7011496372902790", "22448b7ad7edf143d027144f378e2fe6").getToken();
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token="+access_token, jsonMenu);
		return result;
	}
	/*根据输入的标签名创建用户标签 tagname为标签名*/
	public String createTag(String tagname) {
		JSONObject jsonTab = new JSONObject();
		WxTag tag1 = new WxTag();
		tag1.setName(tagname);
		jsonTab.put("tag",tag1);
		System.out.println(jsonTab.toString());
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/create?access_token="+access_token, jsonTab.toString());
		return result;
	}
	/*设置用户的标签  tagid为标签id学生为109　老师为111*/
	public String setUserTag(String useropenid,int tagid) {
		JSONObject userSetTag = new JSONObject();
		String[] userList = {useropenid};
		userSetTag.put("openid_list",userList );
		userSetTag.put("tagid", tagid);
		System.out.println(userSetTag.toString());
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token="+access_token, userSetTag.toString());
		return result;
	}
	/*删除对应标签下的对用用户 因为用户可以用过多个标签 避免冲突 */
	public String deletUserTag(String useropenid,int tagid) {
		JSONObject userSetTag = new JSONObject();
		String[] userList = { useropenid};
		userSetTag.put("openid_list",userList );
		userSetTag.put("tagid", tagid);
		System.out.println(userSetTag.toString());
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token="+access_token, userSetTag.toString());
		return result;
	}
	/*发送服务信息 第一个参数为接收者id 第二个参数为news的标题 第三个参数为news的内容 url为跳转的url地址*/
	public String SendCustomerServiceInfo(String openid,String title,String content,String url) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONObject map1 = new JSONObject(true);
		map1.put("touser",openid);
		map1.put("msgtype","news");
		JSONObject map2 = new JSONObject(true);
		map2.put("title",title);
		map2.put("description",content);
		map2.put("url", url);
		map2.put("picurl","");
		LinkedHashMap<String, Object> root1=JSON.parseObject(map2.toString(),new TypeReference<LinkedHashMap<String, Object>>(){});//关键的地方，转化为有序map
		LinkedHashMap<String, Object> root=JSON.parseObject(map1.toString(),new TypeReference<LinkedHashMap<String, Object>>(){});//关键的地方，转化为有序map
		JSONObject map3 = new JSONObject(true);
		list.add(root1);
		map3.put("articles",list);
		root.put("news",map3);
		System.out.println(JSON.toJSONString(root));
		String access_token = WxHttpMethod.getNAccessToken(new Date().getTime());
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token,JSON.toJSONString(root));
		return result;
	}
}
