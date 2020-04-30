package com.wzzzzor.billrecord.intercepter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

import com.wzzzzor.billrecord.base.DataTablePage;
import com.wzzzzor.billrecord.utils.ReflectUtil;

/**
 * 
 * 分页拦截器，用于拦截需要进行分页查询的操作，然后对其进行分页处理。 
 * @author wzzzzor
 */
@Component
@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	/**
	 * 数据库类型：oracle,mysql
	 */
	private String databaseType;

	/*
	 * 拦截后要执行的方法
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
		MappedStatement mappedStatement  = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
		BoundSql boundSql = delegate.getBoundSql();
		BoundSql tempBoundSql = null;
		//修改参数值
		SqlSource sqlSource = (SqlSource) ReflectUtil.getFieldValue(mappedStatement, "sqlSource");
		if(sqlSource != null && sqlSource instanceof DynamicSqlSource){
			SqlNode sqlNode = (SqlNode) ReflectUtil.getFieldValue(sqlSource, "rootSqlNode");
			tempBoundSql = getBoundSql(mappedStatement.getConfiguration(), boundSql.getParameterObject(),sqlNode);
		}
		if(tempBoundSql == null){
			tempBoundSql = boundSql;
		}
		Object obj = boundSql.getParameterObject();
		if (obj instanceof DataTablePage) {//是否需要分页
			DataTablePage page = (DataTablePage) obj;
			page.internalSet();
			if(page.getPageState()) {
				Connection connection = (Connection) invocation.getArgs()[0];
				String sql = tempBoundSql.getSql();
				this.setTotalRecord(page, mappedStatement, connection);
				String pageSql = this.getPageSql(page, sql);
				ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
			}
		}
		
		return invocation.proceed();
	}

	/**
	 * 拦截器对应的封装原始对象的方法
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 设置注册拦截器时设定的属性
	 */
	public void setProperties(Properties properties) {
		this.databaseType = properties.getProperty("databaseType");
		//this.databaseType = "oracle";
	}

	/**
	 * 根据page对象获取对应的分页查询Sql语句，这里只做了两种数据库类型，Mysql和Oracle 其它的数据库都 没有进行分页
	 * 
	 * @param page
	 *            分页对象
	 * @param sql
	 *            原sql语句
	 * @return
	 */
	private String getPageSql(DataTablePage page, String sql) {
		StringBuffer sqlBuffer = new StringBuffer(sql);
		if ("mysql".equalsIgnoreCase(databaseType)) {
			return getMysqlPageSql(page, sqlBuffer);
		} else if ("oracle".equalsIgnoreCase(databaseType)) {
			return getOraclePageSql(page, sqlBuffer);
		}
		return sqlBuffer.toString();
	}

	/**
	 * 获取Mysql数据库的分页查询语句
	 * 
	 * @param page
	 *            分页对象
	 * @param sqlBuffer
	 *            包含原sql语句的StringBuffer对象
	 * @return Mysql数据库分页语句
	 */
	private String getMysqlPageSql(DataTablePage page, StringBuffer sqlBuffer) {
		sqlBuffer.append(" limit ").append(page.getiDisplayStart()).append(",")
				.append(page.getiDisplayLength());
		return sqlBuffer.toString();
	}

	/**
	 * 获取Oracle数据库的分页查询语句
	 * @param page
	 *            分页对象
	 * @param sqlBuffer
	 *            包含原sql语句的StringBuffer对象
	 * @return Oracle数据库的分页查询语句
	 */
	private String getOraclePageSql(DataTablePage page, StringBuffer sqlBuffer) {
		int offset = page.getiDisplayStart() + 1;
		sqlBuffer.insert(0, "select u.*, rownum r from (")
				 .append(") u where rownum < ")
				 .append(offset + page.getiDisplayLength());
		sqlBuffer.insert(0, "select * from (").append(") where r >= ")
				 .append(offset);
		return sqlBuffer.toString();
	}

	/**
	 * 给当前的参数对象page设置总记录数
	 * 
	 * @param page
	 *            Mapper映射语句对应的参数对象
	 * @param mappedStatement
	 *            Mapper映射语句
	 * @param connection
	 *            当前的数据库连接
	 */
	private void setTotalRecord(DataTablePage page, MappedStatement mappedStatement,
			Connection connection) {
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		BoundSql countBoundSql = new BoundSql(
				mappedStatement.getConfiguration(), countSql,
				parameterMappings, page);
		ParameterHandler parameterHandler = new DefaultParameterHandler(
				mappedStatement, page, countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(countSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int totalRecord = rs.getInt(1);
				page.setiTotalDisplayRecords(totalRecord);
				page.setiTotalRecords(totalRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据原Sql语句获取对应的查询总记录数的Sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql) {
		/**
		 * 修正原因:查询字段含有子查询
		 */
		/*int index = sql.toLowerCase().indexOf("from");
		return "select count(*) " + sql.substring(index);*/
		
		return "select count(*) from (".concat(sql).concat(") T");
	}

	
	public static BoundSql getBoundSql(Configuration configuration,Object parameterObject,SqlNode sqlNode) {
	    DynamicContext context = new DynamicContext(configuration, parameterObject);
	
		sqlNode.apply(context);
		String countextSql=context.getSql();
	
	    
	    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
	    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
	    String sql=modifyLikeSql(countextSql, parameterObject);
	    SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
	    
	   
	    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
	    for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
	      boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
	    }
	    
	    return boundSql;
	  }
	
	public static String modifyLikeSql(String sql,Object parameterObject)
	{
		if(parameterObject instanceof DataTablePage){
			
		}else{
			return sql;			
		}
		if(!sql.toLowerCase().contains("like"))
			return sql;
		 //sql=" and OPER_REMARK LIKE '%' || #{operRemark} || '%'  \n " +"and OPER_U_NAME LIKE #{operUName} || '%' ";
		//原始表达式：\s\w+\sLIKE\s('%'\s\|{2})?\s*(#\{\w+\})\s*(\|{2}\s*'%')
		String reg="\\s\\w+\\s[Ll][Ii][Kk][Ee]\\s*('%'\\s*\\|{2}\\s*)?(#\\{[\\w\\.]+\\})(\\s*\\|{2}\\s*'%')?";//"order\\s+by\\s+.+"
		
		
		Pattern pattern = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		
		List<String> replaceEscape=new ArrayList<>();
		List<String> replaceFiled=new ArrayList<>();
		
		while(matcher.find()){
			replaceEscape.add(matcher.group());
			 int n = matcher.groupCount();  
             for (int i = 0; i <= n; i++)
             {  
                String  output = matcher.group(i);  
                if(2==i&&output!=null)
                {
                	replaceFiled.add(output.trim());
                }
             }  
	       }

		//sql = matcher.replaceAll(reg+" 1111");
		
		for(String s:replaceEscape)
		{
			sql=sql.replace(s, s+" ESCAPE '/' ");
		}
		//修改参数
		DataTablePage param=(DataTablePage)parameterObject;
		for(String s:replaceFiled)
		{
			String key=s.replace("#{", "").replace("}", "").replace("mapBean.", "").trim();
			Map<String,Object> mapBean = param.getMapBean();
			Object val =mapBean.get(key);
			if(val!=null &&val instanceof String&&(val.toString().contains("%")||val.toString().contains("_")))
			{
				val=val.toString().replaceAll("%", "/%").replaceAll("_", "/_");
				mapBean.put(key.toString(), val);
			}			
		}	
		return sql;   
	}

}
