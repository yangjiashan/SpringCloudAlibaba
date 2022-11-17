
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.yang.study.es.ElasticSearchApplication;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestClass {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DefaultDataSourceCreator dataSourceCreator;

    @Test
    public void printSource(){
        System.out.println(dataSource);

        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource("test_datasource", dataSource);

        System.out.println(ds.getCurrentDataSources().keySet());

        SearchRequest searchRequest = new SearchRequest("test_index2");
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
//        IndicesOptions.


    }

//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
//
//    @Test
//    public void testMultiMatch() throws IOException {
//        // 设置需要查询的索引
//        SearchRequest searchRequest = new SearchRequest("test_index2");
//
//        // 设置multiMatch
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("天安门", "content", "name");
//
//        // 设置属性best_fields
//        builder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
////        builder.tieBreaker(0.3F);
////        builder.operator(Operator.OR);
////        builder.minimumShouldMatch("50%");
////        builder.analyzer("ik_smart");
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(builder);
//
//        searchSourceBuilder.query(boolQueryBuilder);
//
//        // 设置查询
//        searchRequest.source(searchSourceBuilder);
//
//        // 查询
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(response);
//    }
//
//    @Test
//    public void testQueryString() throws IOException {
//        // 设置需要查询的索引
//        SearchRequest searchRequest = new SearchRequest("test_index2");
//
//        // 设置multiMatch
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery("天安门");
////        builder.f
//
//        // 设置属性best_fields
//        builder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
////        builder.tieBreaker(0.3F);
////        builder.operator(Operator.OR);
////        builder.minimumShouldMatch("50%");
////        builder.analyzer("ik_smart");
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(builder);
//
//        searchSourceBuilder.query(boolQueryBuilder);
//
//        // 设置查询
//        searchRequest.source(searchSourceBuilder);
//
//        // 查询
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(response);
//    }

}
