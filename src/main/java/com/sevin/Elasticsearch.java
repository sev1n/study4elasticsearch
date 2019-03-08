package com.sevin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 中文分词器：https://github.com/medcl/elasticsearch-analysis-ik
 * 拼音分词器：https://github.com/medcl/elasticsearch-analysis-pinyin
 * @author sevin
 *
 */
public class Elasticsearch {

	public static void main(String[] args) throws IOException {
		TransportClient client = getClient();
		
		System.out.println(createIndex1(client));
		//System.out.println(createIndex2(client));
		
		//List<String> names = Arrays.asList("凡人修仙传","平凡之路","人类简史", "伸冤人", "凡人歌", "修仙传", "仙履奇缘", "异人修仙传奇");
		//导入数据
		//System.out.println(addDocs(client, "standard", "test1", names));
		//System.out.println(addDocs(client, "ik", "test2", names));
		
		//System.out.println(createIndex1(client));
/*		A a1 = new A("sevin", 25);
		A a2 = new A("pangxueming", 27);
		A a3 = new A("wulupeng", 26);
		List<A> listA= new ArrayList<A>();
		listA.add(a1);
		listA.add(a2);
		listA.add(a3);
		System.out.println(addDocs(client, "myindex", "test", listA));*/
		
		//List<A> listA = queryAll(client, "my_index", "doc");
		//listA.forEach(System.out::println);
	}
	
	public static TransportClient getClient() throws UnknownHostException{
        
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        return client;
	}
	
	public static boolean createIndex1(TransportClient client) throws IOException{
        CreateIndexRequestBuilder cib = client.admin().indices().prepareCreate("myindex");
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                .startObject("_source").field("enabled", true).endObject()
                .startObject("_all").field("enabled", false).endObject()
                .startObject("properties")
                .startObject("name").field("type", "text").field("store", true).endObject()
                .startObject("age").field("type", "integer").field("store", true).endObject()
                .startObject("address").field("type", "keyword").field("store", true).endObject()//type=keyword适合wildcard查询
                .startObject("city").field("type", "text").field("fielddata", true).field("store", true).endObject()//fielddata=true将数据加载到内存，用于排序、聚合
                .endObject().endObject();
        
        cib.addMapping("test", mapping);
        return cib.get().isAcknowledged();
	}
	
	public static boolean createIndex2(TransportClient client) throws IOException{
        CreateIndexRequestBuilder cib = client.admin().indices().prepareCreate("ik");
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                .startObject("_source").field("enabled", true).endObject()
                .startObject("_all").field("enabled", false).endObject()
                .startObject("properties")
                .startObject("name").field("type", "text").field("store", true).field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").endObject()
                .endObject().endObject();
        
        cib.addMapping("test2", mapping);
        return cib.get().isAcknowledged();
	}
	
	private static String addDocs(TransportClient client, String indexName, String type, List<A> names){
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (A a : names) {
			bulkRequest.add(client.prepareIndex(indexName, type).setSource(JSON.toJSONString(a), XContentType.JSON));
		}
		BulkResponse rsp = bulkRequest.execute().actionGet();
		return rsp.status().toString();
	}
	
	private static String addDocs2(TransportClient client, String indexName, String type, List<Elasticsearch.A> listA){
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (A a : listA) {
			bulkRequest.add(client.prepareIndex(indexName, type).setSource(JSON.toJSONString(a), XContentType.JSON));
		}
		BulkResponse rsp = bulkRequest.execute().actionGet();
		return rsp.status().toString();
	}

	private static String getJsonStr(String name) {
		JSONObject job = new JSONObject();
		job.put("name", name);
		return job.toString();
	}
	
	private static List<A> queryAll(TransportClient client, String indexName, String type){
		SearchResponse rsp = client.prepareSearch(indexName).setTypes(type).setQuery(new MatchAllQueryBuilder()).get();
		SearchHits hits = rsp.getHits();
		return Arrays.stream(hits.getHits()).parallel().map(h -> JSONObject.parseObject(h.getSourceAsString(), Elasticsearch.A.class)).collect(Collectors.toList());
	}
	
	public static class A{
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
		public A(String name, Integer age) {
			super();
			this.name = name;
			this.age = age;
		}
		public A() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("A [name=");
			builder.append(name);
			builder.append(", age=");
			builder.append(age);
			builder.append("]");
			return builder.toString();
		}
	}
}
