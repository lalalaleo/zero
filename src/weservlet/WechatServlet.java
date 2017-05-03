package weservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ifp.wechat.service.SignService;

@Controller  
@RequestMapping("wechat")
public class WechatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@RequestMapping(method = { RequestMethod.GET })
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String echostr = request.getParameter("echostr");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (SignService.checkSignature(request)) {
				out.print(echostr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
			out = null;
		}
	}
	
	@RequestMapping(method = { RequestMethod.POST })
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");
		String respMessage = WechatController.processWebchatRequest(request);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(respMessage);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
			out = null;
		}
	}
}
