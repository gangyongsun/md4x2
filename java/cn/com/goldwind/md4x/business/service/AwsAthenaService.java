package cn.com.goldwind.md4x.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.athena.AmazonAthena;
import com.amazonaws.services.athena.AmazonAthenaClientBuilder;
import com.amazonaws.services.athena.model.CreateWorkGroupRequest;
import com.amazonaws.services.athena.model.CreateWorkGroupResult;
import com.amazonaws.services.athena.model.Datum;
import com.amazonaws.services.athena.model.GetQueryExecutionRequest;
import com.amazonaws.services.athena.model.GetQueryExecutionResult;
import com.amazonaws.services.athena.model.GetQueryResultsRequest;
import com.amazonaws.services.athena.model.GetQueryResultsResult;
import com.amazonaws.services.athena.model.QueryExecution;
import com.amazonaws.services.athena.model.QueryExecutionContext;
import com.amazonaws.services.athena.model.QueryExecutionState;
import com.amazonaws.services.athena.model.QueryExecutionStatus;
import com.amazonaws.services.athena.model.ResultConfiguration;
import com.amazonaws.services.athena.model.ResultSet;
import com.amazonaws.services.athena.model.Row;
import com.amazonaws.services.athena.model.StartQueryExecutionRequest;
import com.amazonaws.services.athena.model.StartQueryExecutionResult;
import com.amazonaws.services.athena.model.WorkGroupConfiguration;
import com.amazonaws.util.StringUtils;

/**
 * AWS Athena 服务封装
 * 
 * @author wangguiyu
 *
 */
@Service
public class AwsAthenaService implements InitializingBean{
	
	private AmazonAthena amazonAthena;
	
	@Value("${aws.credentialFlag}")
	private String credentialFlag;
	
	@Value("${aws.s3bucketName}")
	private String s3bucketName;
	
	@Value("${aws.iamRole}")
	private String awsIAMRole;

	@Value("${aws.ak}")
	private String accessKey;

	@Value("${aws.sk}")
	private String secretKey;
	
	@Value("${aws.region}")
	private String region;
	
	@Value("${aws.jobName}")
	private String jobName;
	
	@Value("${aws.s3bucketPrefix}")
	private String s3bucketPrefix;
	
	@Value("${aws.awsGlueDataCatalogPrefix}")
	private String awsGlueDataCatalogPrefix;
	
	@Value("${aws.workflowNamePrefix}")
	private String workflowNamePrefix;
	
	@Value("${aws.triggerNamePrefix}")
	private String triggerNamePrefix;
	
	@Value("${aws.crawlerNamePrefix}")
	private String crawlerNamePrefix;
	
	@Value("${aws.s3PathAthenaQueryResult}")
	private String s3PathAthenaQueryResult;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if ("true".equals(credentialFlag)) {
			if(!StringUtils.isNullOrEmpty(accessKey) && !StringUtils.isNullOrEmpty(secretKey)){
				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
				amazonAthena = AmazonAthenaClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
			}else {
				amazonAthena = AmazonAthenaClientBuilder.standard().withRegion(region).build();
			}
			
		} else {
			amazonAthena = AmazonAthenaClientBuilder.standard().withRegion(region).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		}
	}
	
	// 1.创建工作组
	//s3pathPrefix: test_md4x_result/md4x_aws_athena_result/
	public String createWorkGroup(String workGroupName,String s3pathPrefix) {
		String outputLocation = "s3://"+s3bucketName+"/"+s3pathPrefix;
		CreateWorkGroupRequest createWorkGroupRequest = new CreateWorkGroupRequest();
		WorkGroupConfiguration configuration = new WorkGroupConfiguration();
		ResultConfiguration resultConfiguration = new ResultConfiguration();
		resultConfiguration.setOutputLocation(outputLocation);
		configuration.setResultConfiguration(resultConfiguration);
		createWorkGroupRequest.setConfiguration(configuration);
		createWorkGroupRequest.setName(workGroupName);
		CreateWorkGroupResult greateWorkGroupResult = amazonAthena.createWorkGroup(createWorkGroupRequest);
		return greateWorkGroupResult.toString();
	}
	
	/**
	 * 
	 * @param datasetId      数据集id
	 * @param datasetScope   数据集使用范围
	 * @param datasetOwner   数据集属主
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String,String>> getQueryResult(String datasetId,String datasetScope,String datasetOwner) throws Exception {
		//String workflowName = workflowNamePrefix + datasetId;
		//String triggerName =  triggerNamePrefix +  datasetId;
		//String crawlerName =  crawlerNamePrefix +  datasetId;
		String crawlerDatabaseName = awsGlueDataCatalogPrefix+datasetScope+"_"+datasetOwner;
		String crawlerTablePrefix = awsGlueDataCatalogPrefix+datasetScope+"_"+datasetOwner+"_";
		//String crawlerAWSS3Target = "s3://"+s3bucketName+"/"+datasetScope+"/"+datasetOwner+"/"+datasetId;
		String crawlerTableName = crawlerTablePrefix + datasetId;
        String athenaQueryResult = "s3://" + s3bucketName+ "/"+ s3PathAthenaQueryResult;
		//1.开始查询
		String queryString = "select * from  " + crawlerTableName + " order by ts  limit 10";//select * from  md4x_private_31240_20200607120628566 limit 10
		StartQueryExecutionRequest startQueryExecutionRequest = new StartQueryExecutionRequest();
		startQueryExecutionRequest.setQueryString(queryString);
		//startQueryExecutionRequest.setWorkGroup("md4x");// ***工作组***  查询结果集存放路径 yaml中配置
		QueryExecutionContext queryExecutionContext = new QueryExecutionContext();
		queryExecutionContext.setDatabase(crawlerDatabaseName);
		queryExecutionContext.setCatalog("AwsDataCatalog");
		startQueryExecutionRequest.setQueryExecutionContext(queryExecutionContext);
		ResultConfiguration resultConfiguration = new ResultConfiguration();
		resultConfiguration.setOutputLocation(athenaQueryResult);
		startQueryExecutionRequest.setResultConfiguration(resultConfiguration);
		StartQueryExecutionResult startQueryExecutionResult = amazonAthena.startQueryExecution(startQueryExecutionRequest);
		
		List<Row> listrows = null;
		while (true) {
			GetQueryExecutionRequest getQueryExecutionRequest = new GetQueryExecutionRequest();
			getQueryExecutionRequest.setQueryExecutionId(startQueryExecutionResult.getQueryExecutionId());
			GetQueryExecutionResult getQueryExecutionResult = amazonAthena.getQueryExecution(getQueryExecutionRequest);
			QueryExecution gueryExecution = getQueryExecutionResult.getQueryExecution();
			QueryExecutionStatus queryExecutionStatus = gueryExecution.getStatus();
			if(queryExecutionStatus.getState().equals("FAILED")) {
				throw new Exception(queryExecutionStatus.getStateChangeReason());
			}
			if (QueryExecutionState.SUCCEEDED.name().equals(gueryExecution.getStatus().getState())) {
				// 2.获取查询结果
				GetQueryResultsRequest getQueryResultsRequest = new GetQueryResultsRequest();
				getQueryResultsRequest.setQueryExecutionId(startQueryExecutionResult.getQueryExecutionId());
				GetQueryResultsResult getQueryResultsResult = amazonAthena.getQueryResults(getQueryResultsRequest);
				ResultSet set = getQueryResultsResult.getResultSet();
				listrows = set.getRows();
				break;
			}
		}
		//----转换为 变量名:变量值 的map集合
		List<Map<String,String>> maps = new ArrayList<Map<String,String>>();
		List<Datum> datumsHead = listrows.get(0).getData();
		for(int i=1;i<listrows.size();i++) {
			Row row = listrows.get(i);
			Map<String,String> map = new HashMap<String,String>();
			List<Datum> datums = row.getData();
			for(int j=0;j<datums.size();j++) {
				map.put(datumsHead.get(j).getVarCharValue(), datums.get(j).getVarCharValue());
			}
			maps.add(map);
		}
		return maps;
	}
	
}
