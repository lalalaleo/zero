package method;

import com.ifp.wechat.entity.menu.Button;
import com.ifp.wechat.util.WeixinUtil;

import modle.Matchrule;
import modle.MyTag;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MyService {
	/*根据输入的json格式字符串申请创建菜单 并返回菜单id或者错误信息*/
	public String createMenu(String jsonMenu) {
		//107:{"menuid":419897621} 0:{"menuid":419897636} 108：{"menuid":419897940}{"menuid":419897956}
		String access_token = WeixinUtil.getAccessToken("wx7011496372902790", "22448b7ad7edf143d027144f378e2fe6").getToken();
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token="+access_token, jsonMenu);
		return result;
	}
	/*根据输入的标签名创建用户标签*/
	public String createTag(String tagname) {
		JSONObject jsonTab = new JSONObject();
		MyTag tag1 = new MyTag();
		//Teachertest id:107 Studenttest id:108
		tag1.setName(tagname);
		jsonTab.put("tag",tag1);
		System.out.println(jsonTab.toString());
		String access_token = WeixinUtil.getAccessToken("wx7011496372902790", "22448b7ad7edf143d027144f378e2fe6").getToken();
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/create?access_token="+access_token, jsonTab.toString());
		return result;
	}
	/*设置用户的标签*/
	public String setUserTag(String useropenid,int tagid) {
		JSONObject userSetTag = new JSONObject();
		String[] userList = {useropenid};
		userSetTag.put("openid_list",userList );
		userSetTag.put("tagid", tagid);
		System.out.println(userSetTag.toString());
		String access_token = WeixinUtil.getAccessToken("wx7011496372902790", "22448b7ad7edf143d027144f378e2fe6").getToken();
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token="+access_token, userSetTag.toString());
		return result;
	}
	/*删除对应标签下的对用用户 因为用户可以用过多个标签 避免冲突*/
	public String deletUserTag(String useropenid,int tagid) {
		JSONObject userSetTag = new JSONObject();
		String[] userList = { useropenid};
		userSetTag.put("openid_list",userList );
		userSetTag.put("tagid", tagid);
		System.out.println(userSetTag.toString());
		String access_token = WeixinUtil.getAccessToken("wx7011496372902790", "22448b7ad7edf143d027144f378e2fe6").getToken();
		String result = WxHttpMethod.sendPost("https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token="+access_token, userSetTag.toString());
		return result;
	}

}
