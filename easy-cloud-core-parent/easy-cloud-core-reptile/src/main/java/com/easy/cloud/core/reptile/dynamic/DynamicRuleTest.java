package com.easy.cloud.core.reptile.dynamic;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easy.cloud.core.reptile.common.constant.EcReptileConstant;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.dynamic.DynamicGecco;
import com.geccocrawler.gecco.request.HttpGetRequest;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * 本demo是一个在线修改抓取规则的例子，DyncmicGecco支持规则类的重新加载，不需要重启应用
 * 
 * @author huchengyi
 *
 */
@Component
public class DynamicRuleTest {

	public static void main(String[] args) throws Exception {
		new DynamicRuleTest().init();
		// makeclass("HelloController", "hello", "name", "");
	}

//	@Autowired
	private GeccoEngine geccoEngine;

//	@PostConstruct
	public void init() {
		// 初始化爬虫引擎，此时由于没有初始请求，爬虫引擎会阻塞初始队列，直到获取到初始请求
		// 初始化爬虫引擎，此时由于没有初始请求，爬虫引擎会阻塞初始队列，直到获取到初始请求
		// GeccoEngine ge =
		// GeccoEngine.create("com.easy.cloud.core.reptile.pipeline")
		// .interval(2000)
		// .loop(true)
		// .thread(1)
		// .engineStart();

		// 定义爬取规则
		Class<?> rule = DynamicGecco.html()
				.gecco(new String[]{EcReptileConstant.MATCH_URL_DETAIL, EcReptileConstant.MATCH_URL_INDEX},"ecPipelineTest")
				.stringField("menpai").csspath("div#goods-detail.goods-detail div.tab-cont-item.role div.left.w323 div.role-show span.fn-other-menpai").text(false).build()
				.stringField("roleName").csspath("div#goods-detail.goods-detail div.tab-cont-item.role div.right.w222.fn-clearfix div.box2.h422 div.row2 span.span.fn-mr20").text(false).build()
				.stringField("service").csspath("div.goods-info ul.info-list li p.server-info.J-message").attr("title").build()
				.stringField("price").csspath("div.goods-info ul.info-list li p span.ui-money-color.ui-money-size")
				.text().build().loadClass();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// geccoEngine.getScheduler().into(new
		// HttpGetRequest("http://tl.cyg.changyou.com/goods/char_detail?serial_num=20180522001288380"));
		// 注册规则
		geccoEngine.register(rule);
		// 定义爬取规则
		 System.out.println("修改规则");
		 Class<?> newRule = null;
		 try {
		 // 开始更新规则
			 geccoEngine.beginUpdateRule();
		 // 修改规则
		 newRule = DynamicGecco.html(rule.getName()).gecco("https://github.com/xtuhcy/gecco", "consolePipeline")
		 // .intField("fork").csspath(".pagehead-actions
		 // li:nth-child(3) .social-count").text().build()
		 .removeField("star").loadClass();
		 // 注册新规则
		 geccoEngine.register(newRule);
		 } catch (Exception ex) {
		 ex.printStackTrace();
		 } finally {
		 // 规则更新完毕
			 geccoEngine.endUpdateRule();
		 }
		//
		// Thread.sleep(5000);
		//
		// System.out.println("下线规则");
		// try {
		// // 开始更新规则
		// ge.beginUpdateRule();
		// // 下线之前的规则（也支持不下线规则，直接修改）
		// ge.unregister(newRule);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// } finally {
		// // 规则更新完毕
		// ge.endUpdateRule();
		// }
	}

	public static void makeclass(String className, String methodName, String fieldName, String interfaceCode)
			throws NotFoundException, CannotCompileException, IOException {
		ClassPool pool = ClassPool.getDefault();
		CtClass ct = pool.makeClass("com.test.bean.Emp");
		// 创建属性
		CtField e1 = CtField.make("public int no;", ct);
		CtField e2 = CtField.make("public String name;", ct);
		ct.addField(e1);
		ct.addField(e2);
		CtMethod m1 = CtMethod.make("public int getNo(){return no;}", ct);
		CtMethod m2 = CtMethod.make("public void setNo(int no){return this.no = no;}", ct);
		ct.addMethod(m1);
		ct.addMethod(m2);
		// 添加构造器
		CtConstructor constructor = new CtConstructor(new CtClass[] { CtClass.intType, pool.get("java.lang.String") },
				ct);// 构造器的参数
		constructor.setBody("{this.no = no; this.name=name;}");// 构造器的方法体
		ct.addConstructor(constructor);// 如果不添加构造器 则会生成一个空的构造器
		ct.writeFile("E:/JavaFile");// 将构造好的类写出来
		System.out.println("类生成功");

	}
}
