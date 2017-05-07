import com.ifp.wechat.entity.menu.Button;
import com.ifp.wechat.entity.menu.Menu;
import com.ifp.wechat.entity.user.UserGroup;
import com.ifp.wechat.service.MenuService;
import com.ifp.wechat.service.UserGroupService;
import com.ifp.wechat.service.UserService;
import com.ifp.wechat.util.WeixinUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import method.MyService;
import modle.Matchrule;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Test {
	/*
	 * http://lalalaleo.com/wechat/wechat.do
	 * http://16wc499988.iok.la/wechatd2/wx
	 * -NenYw28wfDZ8GCoDDl6qUMH4UL3U15WNbaXNFwFzkUtEEMrjttSrBELrUTD_UcIGLMmSicCu_XfN8XEfdSVOj5h5DePKKGVIVMYo-yE2FNyw5VF-gHgipBO1N2Ia8LYJEDfACAPKJ
	 * appid:wx7011496372902790
	 * appsec:22448b7ad7edf143d027144f378e2fe6
	 */
	public static void main(String[] args) {
		/*自定义标签的json对象创建*/
		JSONObject custome = new JSONObject();
		JSONArray button = new JSONArray();
		JSONObject temp = new JSONObject();
		temp.put("type","click");
		temp.put("name", "帮助");
		temp.put("key", "teacherhelp");
		button.add(temp);
		JSONArray subbutton = new JSONArray();
		JSONArray subbuttons = new JSONArray();
		JSONObject temp2 = new JSONObject();
		temp2.put("name", "功能");
		JSONObject sub1 = new JSONObject();
		sub1.put("type", "click");
		sub1.put("name", "发送信息");
		sub1.put("key", "teachaer1");
		JSONObject sub2 = new JSONObject();
		sub2.put("type", "click");
		sub2.put("name", "历史信息");
		sub2.put("key", "teachaer1");
		JSONObject sub3 = new JSONObject();
		sub3.put("type", "click");
		sub3.put("name", "我的课程");
		sub3.put("key", "teachaer1");
		subbuttons.add(sub1);
		subbuttons.add(sub2);
		subbuttons.add(sub3);
		temp2.put("sub_button", subbuttons);
		button.add(temp2);
		custome.put("button", button);
		Matchrule mc = new Matchrule();
		mc.settag_id("110");
		JSONObject match = new JSONObject();
		JSONArray rule = new JSONArray();
		rule.add(mc);
		
		custome.put("matchrule", mc);;
		System.out.println(custome.toString());
//		System.out.println(new MyService().setUserTag("oHU1c08T4ty6lmSDWfuihMbhSa0M",109));
		//{"menuid":419911270}    {"menuid":419911302}
		//zerostudent id:109 zeroteacher id:110
//		System.out.println(new Myservice().createMenu(custome.toString()));
		
		//107:{"menuid":419897621} 0:{"menuid":419897636} 108：{"menuid":419897940}{"menuid":419897956}
	}
	/*
	 * {
    "button": [
        {
            "type": "click", 
            "name": "帮助", 
            "key": "studenthelp"
        }, 
        {
            "name": "功能", 
            "sub_button": [
                {
                    "type": "click", 
                    "name": "历史信息", 
                    "key": "student1"
                }, 
                {
                    "type": "click", 
                    "name": "我的教师", 
                    "key": "student2"
                }, 
                {
                    "type": "click", 
                    "name": "我的课表", 
                    "key": "studentg"
                }
            ]
        }
    ], 
    "matchrule": [
        {
            "tag_id": "109"
        }
    ]
}
	 * /
	 */
}
