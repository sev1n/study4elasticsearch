package com.sevin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sevin.Elasticsearch.A;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
/*    	App a = new App();
    	A a1 = new A();
    	a(a);
    	a(a1);*/
/*    	Person p1 = new Person("xieshiqi", 25);
    	String json = JSONObject.toJSONString(p1);
    	System.out.println(json);*/
/*    	Person p = JSONObject.parseObject("{\"age\":25,\"name\":\"xieshiqi\",\"tel\":18357877571}", Person.class);
		System.out.println(p.toString());
    	JSONObject query2 = new JSONObject();
		JSONObject priceObj = new JSONObject();
		JSONObject gtObj = new JSONObject();
		gtObj.put("gt", 0);
		priceObj.put("price", gtObj);
		query2.put("range", priceObj);
		System.out.println(query2);*/
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date time = c.getTime();
		System.out.println(time);
    }

    public static void a(Object o){
    	if (o instanceof App) {
    		System.out.println("类型符合App");
		}
    	
    	if (o instanceof A) {
			System.out.println("类型符合A");
		}
    }
    
    public static String test(){
    	int[] a = new int[]{1, 2, 3, 4};
    	int i = 0;
    	try{
    		i = a[2];
    		return "我是：" + i;
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally {
			i = 17;
		}
    	return "ok";
    }
    
   public static class Person{
    	private String name;
    	private Integer age;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Person [name=");
			builder.append(name);
			builder.append(", age=");
			builder.append(age);
			builder.append("]");
			return builder.toString();
		}
		public Person(String name, Integer age) {
			super();
			this.name = name;
			this.age = age;
		}
    }
}
